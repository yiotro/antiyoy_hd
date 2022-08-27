package yio.tro.onliyoy.net;

import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class BbaItem implements ReusableYio {

    public BbaType type;
    public Object object;


    public BbaItem() {
        reset();
    }


    @Override
    public void reset() {
        type = null;
        object = null;
    }


    public void setType(BbaType type) {
        this.type = type;
    }


    public void setObject(Object object) {
        this.object = object;
    }
}
