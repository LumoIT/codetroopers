package com.cth.codetroopers.pixelwars.serverside.Item.Weapons;
import com.cth.codetroopers.pixelwars.serverside.GameUtils.Exceptions.FactoryException;

/**
 * Created by latiif and Hugo on 3/22/17.
 */
public class WeaponsFactory {

	public static Weapon createWeapon(Integer id) throws FactoryException {
		if (id.intValue()==WeaponsDirectory.PISTOL){
			return new Pistol();
		}

		if (id.intValue()==WeaponsDirectory.SNIPER){
			return new Sniper();
		}

		if (id.intValue()==WeaponsDirectory.ASSAULT_RIFLE){
			return new AssaultRifle();
		}
		if (id.intValue()==WeaponsDirectory.SHOTGUN){
			return new Shotgun();
		}

		if (id.intValue()==WeaponsDirectory.WHITEFLAG){
			return new WhiteFlag();
		}

		throw new FactoryException("Weapons with id:" + id+" cannot be found");
	}

}
