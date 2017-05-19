package GameModel.Player;

import GameModel.GameUtils.Exceptions.*;
import GameModel.GameUtils.GeoPos;
import GameModel.Item.Armours.IArmour;
import GameModel.Item.Item;
import GameModel.Item.Weapons.IWeapon;
import GameModel.Player.Avatar.Avatar;
import GameModel.Ranking.Ranks;

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

	Integer getOfflineCooldown();

	void grantWeapon(IWeapon weapon);
	void grantArmour(IArmour armour);

	void setCanGoOffline(Boolean value);

	IWeapon getWeaponEquipped();
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
}
