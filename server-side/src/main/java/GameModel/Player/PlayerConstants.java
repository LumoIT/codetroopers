package GameModel.Player;



/**
 * Created by latiif on 4/1/17.
 */
public class PlayerConstants {

	public final static Double MAX_HEALTH = 100.0;
	final static Integer START_GOLD = 100;
	final static Integer START_EXP = 200;

	//Vision in meters
	final static Integer START_VISION = 250;


	//Offline cooldown in seconds
	public final static Integer START_COOLDOWN = 2;

	//Respawn cooldown
	public final static Integer RESPAWN_COOLDWON=5;

	public final static Double damageCaluculation(final Integer damage, final Integer armour){
		return (damage*100.0)/(100+armour);
	}

}
