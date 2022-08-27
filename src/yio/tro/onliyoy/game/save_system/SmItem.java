package yio.tro.onliyoy.game.save_system;

import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class SmItem implements ReusableYio{

    public String key;
    public String name;
    public String levelCode;
    public SaveType type;


    @Override
    public void reset() {
        key = "";
        name = "";
        levelCode = "";
        type = null;
    }


    public boolean isNot(SaveType saveType) {
        return type != saveType;
    }
}
