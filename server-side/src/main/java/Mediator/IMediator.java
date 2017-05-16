package Mediator;

import GameModel.Item.Item;
import GameModel.Lootbox.ILootbox;
import GameModel.GameUtils.GeoPos;
import GameModel.Player.IPlayer;
import GameModel.Player.Player;
import com.corundumstudio.socketio.SocketIOClient;

import java.util.List;

/**
 * Created by latiif on 4/30/17.
 */
public interface IMediator {
	void updateNearbyPlayers(IPlayer IPlayer);
	void updatePlayer(IPlayer IPlayer);

	void performAttack(String s1,String s2);
	void playerChangePos(String id, GeoPos pos);
	IPlayer getPlayerById(String id);

	void setWorld(IWorld world);
	void setServer(IServer server);

	void registerPlayer(String ID);

	void sendPlayerInfo(String playerId);

	void getShopItems(String id);

	void changeRadarStatus(String id,boolean wantedStatus);

	void consumeLootbox(String  playerId, GeoPos geoPos);

	void updateLootbox(IPlayer player, List<ILootbox> lootboxes);

	void changeWeapon(String  playerId,Integer weaponID);

	List<Item> getShopItems();

	void updatePlayerShopItems(IPlayer p,List<Item> list);

	void buyItem(String id,Integer itemID,String itemType);
	void sellItem(String id,Integer itemID, String itemType);

	void playerSignin(String id, SocketIOClient socketIOClient);

	void changeAvatar(String playerId, String avatarId);

	void sendPlayerSpecificException(Player p,Exception exception);
	void sendException(Exception exception);
}
