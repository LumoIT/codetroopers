package cth.codetroopers.pixelwarfare.ClientSide;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


import cth.codetroopers.pixelwarfare.GameUtils.SkeletonFactory;
import cth.codetroopers.pixelwarfare.Model.IConnectivityLayer;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * This class serves as a two-way gate that takes care of communication between the android
 * implementation of the game and it's classes, and the remote server
 *
 * All communication with the server is performed via and by this class
 *
 * @author latiif
 *
 */

/*
Useful reads:

**Android Studio Documentation on JSONObject class:**
https://developer.android.com/reference/org/json/JSONObject.html

JSON is a very useful and quick technique to send and recieve data on serializable objects via any connection medium.

JSON is readable by humans, and supported by our Networking framework (SocketIO)

** SocketIO java implementation**
* https://github.com/socketio/socket.io-client-java
*
* Read the usage part, and keep in mind that SocketIO was designed for Javascript, but this implementation makes it possible to be used in Java.
*
* SocketIO has a built-in support for sending JSON objects, which is something that is excessively used when communicating with the remote server in this module

 */

public class ConnectivityLayer implements IConnectivityLayer {

    /**
     * The Id of player who's signed in to the server within the game
     * This variable is set when the sign-in process is successfully accomplished.
     */
    private String playerID;

    private boolean firstFetch=true;

    private ConnectivityListener mListener;

    @Override
    public void requestChangeAvatar(String newAvatar) {
        JSONObject object= new JSONObject();

        try{
            object.put("playerId",playerID);
            object.put("avatarId",newAvatar);
            socket.emit("change-avatar",object);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void setListener(ConnectivityListener listener) {
        mListener=listener;
    }

    /**
     * This method is called whenever the user request a change in their online/offline radar status. Whether the request is granted or not is handled on the server and NOT here.
     */
    @Override
    public  void requestChangeRadarStatus(boolean currentStatus){
        JSONObject object= new JSONObject();

        try {
            object.put("playerId", playerID);
            object.put("wantToGoOnline",!currentStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("change-radar-status",object);
    }



    /**
     * The client socket that is created for the sole purpose of sending and receiving events from/to the remote server.
     * This object is assigned on Init() method.
     *
     * @see ConnectivityLayer#Init()
     */
    private  Socket socket;


    /**
     * One-time run method that registers all the events that the client can listen to, every event is added manually in this method, and is called once inside the Init method
     *
     * @see  ConnectivityLayer#Init()
     */
    private  void addListeners(){
        /**
         * Reacts to a player-info event.
         * If the recieved data is that of the player, they are stored in the local variable playerInfo of the type JSONObject.
         *
         * @see ConnectivityLayer#playerInfo
         */
        socket.on("player-info", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject msg= (JSONObject) args[0];

                if (msg==null){
                    return;
                }
                try {
                    if (msg.get("id").equals(playerID)){
                        if (firstFetch){
                            firstFetch=false;
                            mListener.onDataFetched();
                        }
                        mListener.onPlayerDataRecieved(SkeletonFactory.getPlayer(msg));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        /**
         * When the list of the nearby players gets updated on the server this event is sent and it contains a list of the new players that can be seen by this player.
         *
         * The sent array is then converted into a list and stored in the opponents list
         *
         * @see ConnectivityLayer#opponents
         */
        socket.on("nearby-players-update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i("nearby", String.valueOf( args.length));

                mListener.onNearbyPlayersReceived(SkeletonFactory.getPlayers(args));
            }
        });


        socket.on("lootbox-update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                List<LatLng> lootboxes= new ArrayList<LatLng>();

                for (Object arg: args){
                    JSONObject object = (JSONObject) arg;
                    try {
                        lootboxes.add(new LatLng(object.getJSONObject("geoPos").getDouble("latitude"),object.getJSONObject("geoPos").getDouble("longitude")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mListener.onLootboxesUpdate(lootboxes);
            }
        });

        socket.on("shopitems-listed", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

               mListener.updateShop(SkeletonFactory.getShop(args));
            }
        });

        socket.on("server-side-exception", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mListener.onExceptionReceived(SkeletonFactory.getGameException((JSONObject) args[0]));
            }
        });
    }

    /**
     * Creates a client socket and connects to the remote server.
     *
     * It does also add listeners for the events in the addListeners method.
     *
     * @see ConnectivityLayer#addListeners()
     * @throws URISyntaxException
     *
     */
    @Override
    public void Init() throws URISyntaxException {
        /*
        This address the one the emulator uses to connect to localhost on the hosting machine
        If connecting to a remote server online, this URI address needs to be changed
         */
        socket = IO.socket("http://10.0.2.2:3000");


        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i("information","CONNECTED");
                /*
                Prompts the loadingActivity that connection is established
                 */
                mListener.onConnected();
            }
        });

        addListeners();
        /*
        If all goes right, goes ahead and connects
         */
        socket.connect();

    }


    @Override
    public void requestShopItems(){
        JSONObject object= new JSONObject();

        try {
            object.put("playerId",playerID);

            socket.emit("get-shopitems",object);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public  void changePosition(Location position){
        LatLng pos= new LatLng(position.getLatitude(),position.getLongitude());

        changePosition(pos);

    }


    @Override
    public  void changePosition(LatLng position){

        JSONObject object= new JSONObject();

        /*
        Filling in the data required by the event.
        Look at the specifications for this event in the code of the server
         */
        try {
            object.put("id",playerID);
            object.put("lat",position.latitude);
            object.put("lang",position.longitude);


            /*
            Sends the actual request as a JSON object
             */
            socket.emit("position-changed",object);

            Log.i("position-changed", String.valueOf(position.latitude)+" "+position.longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerID(String id) {
        playerID =id;
    }

    /**
     * This method send a sign-in request to the server from the player
     * @param id
     */
    @Override
    public  void signIn(final String id){
        JSONObject object= new JSONObject();

        try {
            object.put("id",id);

            socket.emit("signin",object);
            requestPlayerInformation(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        Prompts loadingActivity that signing in is ongoing
         */
        mListener.onSignedin();

    }


    @Override
    public  void signUp(final String id){
        JSONObject object= new JSONObject();

        try {
            object.put("id",id);

            socket.emit("signup",object);
            requestPlayerInformation(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        Prompts loadingActivity that signing in is ongoing
         */
       mListener.onSignedup();

    }

    @Override
    public  void changeWeapon(int weaponID){
        JSONObject object = new JSONObject();

        try {
            object.put("playerId",playerID);
            object.put("weaponId",weaponID);

            socket.emit("change-weapon",object);
            Log.i("request","request to change weapon sent");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Send an "attack" event to the server to handle it.
     * NOTE: No logic is processed here, all is done on the server.
     * This event can be sent even if the weapon is on cooldown, it's up to the server to perform the suitable action.
     *
     * @param otherPlayerId The id of the other player that the current player is going to attack
     */
    @Override
    public  void attack(final String otherPlayerId){

        JSONObject object= new JSONObject();

        try {
            object.put("id",playerID);
            object.put("oId",otherPlayerId);
            socket.emit("attack",object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Asks the server to fetch back a JSON object containing all the information about a specific player.
     *
     * Usage: Can be used to either fetch information on the current player, or when viewing other player's profile
     *
     * @param id
     */

    @Override
    public  void requestPlayerInformation(final String id){
        JSONObject object= new JSONObject();
        try {
            object.put("id",id);
            socket.emit("get-player-info",object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void consumeLootboxRequest(LatLng coord){
        JSONObject object = new JSONObject();

        try {
            object.put("id",playerID);
            object.put("geoPos",new JSONObject().put("latitude",coord.latitude).put("longitude",coord.longitude));
            socket.emit("consume-lootbox",object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestItemBuy(Integer itemId, String itemType){
        JSONObject object = new JSONObject();

        try{

            object.put("playerId",playerID);
            object.put("itemId",itemId);
            object.put("itemType",itemType);

            socket.emit("buy-item",object);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void requestItemSell(Integer itemId, String itemType){
        JSONObject object = new JSONObject();

        try{

            object.put("playerId",playerID);
            object.put("itemId",itemId);
            object.put("itemType",itemType);

            socket.emit("sell-item",object);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
