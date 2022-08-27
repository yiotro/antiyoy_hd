package yio.tro.onliyoy.game.export_import.legacy;

import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class LiwItem implements ReusableYio {

    public int coordinate1;
    public int coordinate2;
    public PointYio position; // used in algorithms


    public LiwItem() {
        position = new PointYio();
    }


    @Override
    public void reset() {
        coordinate1 = 0;
        coordinate2 = 0;
        position.reset();
    }


    public void setCoordinates(int c1, int c2) {
        coordinate1 = c1;
        coordinate2 = c2;
    }
}
