package cth.codetroopers.pixelwarfare.Views;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import cth.codetroopers.pixelwarfare.GameUtils.ResourceDirectory;
import cth.codetroopers.pixelwarfare.Model.Skeletons.PlayerSkeleton;
import cth.codetroopers.pixelwarfare.R;

/**
 * Created by latiif on 3/30/17.
 * <p>
 * This class manages all the marker-placement requests onto the map.
 * It implements OnMapReadyCallback interface.
 * <p>
 * It's passed a GoogleMap object in its onMapReady(...) method
 */


/*
USEFUL READS:
THE GOOGLE MAPS API
https://developers.google.com/maps/documentation/android-api/map

DO READ THE WHOLE THING
 */
public class GoogleMapHandler implements IMapHandler {

    private GoogleMap map;
    private View mapFragment;
    private FragmentActivity context;

    private IMainView.MapListener mMapListener;

    public GoogleMapHandler(FragmentActivity context, IMainView.MapListener mapListener) {

        this.context = context;

        SupportMapFragment mapFragment = (SupportMapFragment) context.getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        this.mMapListener=mapListener;

    }

    /**
     * This method is called, when the map is up and running, so that we can modify it.
     * @param googleMap the instance of GoogleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        this.map = googleMap;


        //Set a fixed zoom value (Needs to be revised when we set the constants for the game)
        map.setMinZoomPreference(16);
        map.setMaxZoomPreference(16);
        map.getUiSettings().setAllGesturesEnabled(false);

        //We create the first instance of the playerMarker, a marker that will always exist throughout the lifetime of the app.
        playerMarker =
                map.addMarker(
                        new MarkerOptions()
                                .title("Player")
                                .position(new LatLng(0, 0))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar_jim)));

        //needs to be revised to decide the most visually appealing map type
       // map.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        playerCircle=  map.addCircle(
                new CircleOptions()
                        .center(new LatLng(0,0))
                        .radius(0)
                        .clickable(false)
                        .visible(true)
                        .fillColor(Color.argb(75,255,0,0)));


        //Are we gonna use dark theme?
        applyDarkStyle();


        /**
         * Here we set the context for the generator
         * @see OpponentIconGenerator#setContext(Context)
         */
        OpponentIconGenerator.setContext(this.context);



        //Ask our class AttackOpponentListener to listen to all click events on markers
        map.setOnMarkerClickListener(new AttackOpponentListener(mMapListener));


    }

    private AttackOpponentListener attackListener;

    private void applyDarkStyle() {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.map_style_dark));

        } catch (Resources.NotFoundException e) {

        }
    }

    /**
     * A list that contains the markers of all the other people/things that are not this oldplayer
     */
    private List<Marker> opponentsMarkers = new ArrayList<>();

    @Override
    public void setMapFragment(View view) {
        mapFragment=view;
        OpponentIconGenerator.setMapFragment(mapFragment);
    }

    /**
     * Here we take all the opponents in the ConnectivityLayer class and place them as markers on the map
     */
    @Override
    public void pinOpponents(final List<PlayerSkeleton> nearbyPlayers) {


        /*
        The following two lines are used to do all visual operations on the thread of the application UI thread and not on some other thread i.e. that of ConnectivityLayer.
         */
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                /*
                Iterating through the already existing markers, and taking them off the map
                 */
                for (Marker marker : opponentsMarkers) {
                    marker.remove();
                }
                //We clear the array from the removed markers
                opponentsMarkers.clear();

                /*
                For every JSON object we extract the data we want and pass it to the OpponentIconGenerator and place them on the map, and in the list opponentsMarkers
                 */
                for (PlayerSkeleton opponent : nearbyPlayers) {
                    Double lat = 0.0, lng = 0.0;
                    String op_id = "Secret";
                    Integer hp = 100;
                    BitmapDescriptor descriptor = BitmapDescriptorFactory.defaultMarker();


                    try {
                        descriptor = OpponentIconGenerator.getInstance().generateFromPlayer(opponent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    LatLng pos =opponent.getGeoPos();


                    Marker marker = map.addMarker(new MarkerOptions().position(pos).title(op_id)
                            .icon(descriptor));

                    //Since this marker is that of an opponent, it's assigned a JSON-object tag, that would be useful in AttackOpponentListener.
                    marker.setTag(opponent);

                    //We go ahead and place the marker on the map
                    opponentsMarkers.add(marker);
                }

            }
        });


    }

    private Marker playerMarker;
    private Circle playerCircle;

    /**
     * Same thing happens here as in pinOpponents() method, apart from the fact that we place a simple marker and not a one generated from OpponentIconGenerator.
     */
    @Override
    public void pinPlayer(final PlayerSkeleton player) {

        //final LatLng pos = ClientModel.getInstance().mPlayer.getGeoPos();
        final LatLng pos = player.getGeoPos();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                playerMarker.setPosition(pos);
                playerMarker.setIcon(BitmapDescriptorFactory.fromResource(ResourceDirectory.getAvatarImage(
                        player.getIsAlive()?player.getAvatar():"dead")
                ));

                playerCircle.setCenter(player.getGeoPos());
                playerCircle.setRadius(player.getWeaponEquipped().getRange());

                map.moveCamera(CameraUpdateFactory.newLatLng(pos));

            }
        });



    }

    private List<Marker> lootboxesMarkers = new ArrayList<>();
    @Override
    public void pinLootboxes(final List<LatLng> lootboxes) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (Marker marker: lootboxesMarkers){
                    marker.remove();
                }

                lootboxesMarkers.clear();

                for (LatLng latLng:lootboxes){
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.wooden_crate)));

                    lootboxesMarkers.add(marker);
                    marker.setTag(latLng);
                }
            }
        });
    }

    @Override
    public void setMapListener(IMainView.MapListener mapListener) {
        mMapListener=mapListener;
    }


}
