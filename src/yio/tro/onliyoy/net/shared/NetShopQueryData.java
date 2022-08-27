package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetShopQueryData implements ReusableYio, Encodeable {

    public NetSqProductType productType;
    public String key;
    public NetSqActionType actionType;


    @Override
    public void reset() {
        productType = null;
        key = "-";
        actionType = null;
    }


    @Override
    public String encode() {
        return productType + " " + key + " " + actionType;
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 5) return;
        String[] split = source.split(" ");
        if (split.length < 3) return;
        productType = NetSqProductType.valueOf(split[0]);
        key = split[1];
        actionType = NetSqActionType.valueOf(split[2]);
    }
}
