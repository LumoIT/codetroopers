package cth.codetroopers.urbanwarfare.ClientSide;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by latiif on 5/2/17.
 */

public class WeaponSkeleton {

    private String name;
    private Integer damage;
    private Integer id;
    private Integer range;

    public String getName() {
        return name;
    }

    public Integer getDamage() {
        return damage;
    }

    public Integer getRange() {
        return range;
    }

    public Integer getId(){
        return id;
    }

    public WeaponSkeleton(JSONObject weapon){
        try {

            name=weapon.getString("name");
            damage=weapon.getInt("damage");
            range=weapon.getInt("range");
            id=weapon.getInt("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}