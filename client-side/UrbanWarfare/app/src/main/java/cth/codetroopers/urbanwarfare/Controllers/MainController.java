package cth.codetroopers.urbanwarfare.Controllers;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import cth.codetroopers.urbanwarfare.Activities.ChooseWeapon;
import cth.codetroopers.urbanwarfare.ClientSide.ConnectivityLayer;
import cth.codetroopers.urbanwarfare.GameUtils.LocationHandler;
import cth.codetroopers.urbanwarfare.Model.ClientModel;
import cth.codetroopers.urbanwarfare.Views.IMainView;
import cth.codetroopers.urbanwarfare.Views.MainViewImp;

/**
 * Created by latiif on 5/6/17.
 */

public class MainController extends AppCompatActivity implements IMainController{

    private IMainView mainView;
    private LocationHandler locationHandler;

    public static int CODE_CHANGEWEAPON=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup view = (ViewGroup) findViewById(android.R.id.content);

        mainView = new MainViewImp((LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE ),view);
        mainView.setContext(this);
        mainView.setMapListener(this);
        mainView.setListener(this);

        ClientModel.getInstance().subscribePlayerUpdate(mainView);
        ClientModel.getInstance().subscribeLootboxUpdate(mainView);
        ClientModel.getInstance().subscribeOpponentUpdate(mainView);

        setContentView(mainView.getRootView());

        locationHandler= new LocationHandler(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ClientModel.getInstance().requestUpdate();
    }

    @Override
    public void onRequestRadarStatusChange() {
        ClientModel.getInstance().requestRadarStatusChange();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CODE_CHANGEWEAPON){
            if (resultCode==RESULT_OK){
                ClientModel.getInstance().changeWeapon(data.getIntExtra("weapon-id",1));
            }
        }
    }

    @Override
    public void onChangeWeapon() {
        Intent i = new Intent(this, ChooseWeapon.class);
        startActivityForResult(i,CODE_CHANGEWEAPON);
    }

    @Override
    public void onStartShop() {
        Intent i = new Intent(this, ShopViewController.class);
        startActivity(i);
    }

    @Override
    public void onAttackPlayer(String oID) {
        ClientModel.getInstance().attack(oID);
    }

    @Override
    public void onConsumeLootbox(LatLng coord) {
        ClientModel.getInstance().consumeLootbox(coord);
    }


    @Override
    public void onLocationChanged(Location location) {
        ClientModel.getInstance().onMovementDetected(location);
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }


}
