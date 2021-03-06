package cth.codetroopers.pixelwarfare.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.net.URISyntaxException;

import cth.codetroopers.pixelwarfare.Model.ClientModel;
import cth.codetroopers.pixelwarfare.Views.ILoadingView;
import cth.codetroopers.pixelwarfare.Views.LoadingViewImp;

/**
 * Created by latiif on 5/7/17.
 */

public class LoadingController extends AppCompatActivity implements ILoadingView.LoadingViewListener{

    private ILoadingView loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup view = (ViewGroup) findViewById(android.R.id.content);
        loadingView= new LoadingViewImp((LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE ),view);

        loadingView.setListener(this);

        ClientModel.getInstance().subscribeLoadUpdate(loadingView);

        setContentView(loadingView.getRootView());

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            ClientModel.getInstance().commenceLogin();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinishedLoading() {
        startActivity(new Intent(this,MainController.class));
        if (!ClientModel.getInstance().signIn){
            startActivity(new Intent(this,AvatarSelectionController.class));
        }
        finish();
    }
}
