package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class FowItem implements ReusableYio {

    PointYio pos;
    int coordinate1;
    int coordinate2;


    public FowItem() {
        pos = new PointYio();
        reset();
    }


    @Override
    public void reset() {
        pos.reset();
        coordinate1 = 0;
        coordinate2 = 0;
    }


    void setValues(PointYio pointYio, int c1, int c2) {
        pos.setBy(pointYio);
        coordinate1 = c1;
        coordinate2 = c2;
    }
}
