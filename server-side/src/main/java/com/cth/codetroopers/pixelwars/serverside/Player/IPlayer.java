package com.cth.codetroopers.pixelwars.serverside.Player;


import com.cth.codetroopers.pixelwars.serverside.GameUtils.Exceptions.*;
import com.cth.codetroopers.pixelwars.serverside.GameUtils.GeoPos;
import com.cth.codetroopers.pixelwars.serverside.Item.Armours.IArmour;
import com.cth.codetroopers.pixelwars.serverside.Item.Item;
import com.cth.codetroopers.pixelwars.serverside.Item.Weapons.IWeapon;
import com.cth.codetroopers.pixelwars.serverside.Player.PlayerUtils.Avatar;
import com.cth.codetroopers.pixelwars.serverside.WorldPackage.Lootbox.ILootbox;
import com.cth.codetroopers.pixelwars.serverside.Ranking.Ranks;

import java.util.List;

/**
 * Created by latiif on 5/6/17.
 */
public interface IPlayer {
	void setOfflineCooldownStops(Long time);

	void switchWeapon(Integer weaponID);

	void grantGold(Integer amount);

	void sellItem(Item item, Integer refund) throws NoItemException;

	void buyItem(Item item) throws InsufficientException, DuplicateItemException;

	Double getHp();

	Ranks getRank();

	Boolean getIsAlive();

	Integer getArmour();


	void updatePos(GeoPos newPos);

	void getAttacked(Integer damage);

	void attackOtherPlayer(IPlayer otherPlayer) throws CooldownException,CombatException;

	void goOnline();

	void goOffline() throws CooldownException;

	Boolean isOnline();

	public Boolean getOnlineStatus();

		Integer getOfflineCooldown();

	void grantWeapon(IWeapon weapon);
	void grantArmour(IArmour armour);

	void setCanGoOffline(Boolean value);

	IWeapon getWeaponEquipped();

	List<IWeapon> getWeapons();

	List <IArmour> getArmours();

	Boolean getCanGoOffline();

	String getID();

	Integer getExp();

	GeoPos getGeoPos();

	Integer getVision();

	Integer getGold();

	Avatar getAvatar();

	void setAvatar(Avatar a);

	void setHp(Double hp);

	void setExp(Integer exp);

	void setIsAlive (boolean life);

	List<IPlayer> getPlayersNearby();

	void addNearbyPlayer(IPlayer IPlayer);

	void removeNearbyPlayer(IPlayer IPlayer);

	public List<ILootbox> getVisibleLootboxes();

	public void addVisibleLootbox(ILootbox lootbox);

	public void removeVisibleLootbox(ILootbox lootbox);
}
