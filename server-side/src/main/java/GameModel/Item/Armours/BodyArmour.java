package GameModel.Item.Armours;

/**
 * Created by Hugo on 2017-05-04.
 */
public class BodyArmour implements IArmour {
    private final Integer id= 3;
    private final String name="Body IArmour";
    private final Integer value = 100;
    private final Integer cost = 400;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getCost() {
        return cost;
    }

    public String getItemType() {
        return "armour";
    }
}