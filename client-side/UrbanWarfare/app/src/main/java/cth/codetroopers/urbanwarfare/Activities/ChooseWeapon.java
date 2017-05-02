package cth.codetroopers.urbanwarfare.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cth.codetroopers.urbanwarfare.ClientSide.ClientController;
import cth.codetroopers.urbanwarfare.ClientSide.WeaponSkeleton;
import cth.codetroopers.urbanwarfare.R;

public class ChooseWeapon extends AppCompatActivity {

    List<WeaponSkeleton> weapons= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_weapon);



        JSONArray array= new JSONArray();
        try {
            array =ClientController.playerInfo.getJSONArray("weapons");

            for (int i=0;i<array.length();i++){
                weapons.add(new WeaponSkeleton(array.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("len",String.valueOf(array.length()));

        ListAdapter listAdapter = new WeaponViewAdapter(this,weapons);

        ListView listView = (ListView) findViewById(R.id.weaponList);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WeaponSkeleton weapon = (WeaponSkeleton) adapterView.getItemAtPosition(i);

                ClientController.changeWeapon(weapon);

            }
        });

    }
}