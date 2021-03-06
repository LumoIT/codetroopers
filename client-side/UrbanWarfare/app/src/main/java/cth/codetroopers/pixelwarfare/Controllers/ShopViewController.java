package cth.codetroopers.pixelwarfare.Controllers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cth.codetroopers.pixelwarfare.Model.ClientModel;
import cth.codetroopers.pixelwarfare.Views.IShopView;
import cth.codetroopers.pixelwarfare.Views.ShopViewImp;

/**
 * Created by latiif on 5/10/17.
 */

public class ShopViewController extends AppCompatActivity implements IShopView.ShopViewListener {

    private IShopView shopView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup view = (ViewGroup) findViewById(android.R.id.content);
        shopView= new ShopViewImp((LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE ),view);

        shopView.setListener(this);
        shopView.setContext(this);

        ClientModel.getInstance().subscribeShopUpdate(shopView);
        ClientModel.getInstance().subscribePlayerUpdate(shopView);

        setContentView(shopView.getRootView());
        ClientModel.getInstance().requestUpdate();
    }

    @Override
    public void onArmourBuyListener(Integer armourID) {
        ClientModel.getInstance().buyItem(armourID,"armour");
    }

    @Override
    public void onArmourSellListener(Integer armourID) {
        ClientModel.getInstance().sellItem(armourID,"armour");
    }

    @Override
    public void onWeaponBuyListener(Integer weaponID) {
        ClientModel.getInstance().buyItem(weaponID,"weapon");
    }

    @Override
    public void onWeaponSellListener(Integer weaponID) {
        ClientModel.getInstance().sellItem(weaponID,"weapon");
    }
}
