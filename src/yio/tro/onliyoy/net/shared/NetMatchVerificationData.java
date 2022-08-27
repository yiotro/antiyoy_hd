package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetMatchVerificationData implements ReusableYio, Encodeable {

    public NetMatchType matchType;


    public NetMatchVerificationData() {
        reset();
    }


    @Override
    public void reset() {
        matchType = null;
    }


    @Override
    public String encode() {
        return matchType + "";
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        try {
            matchType = NetMatchType.valueOf(source);
        } catch (IllegalArgumentException e) {
            reset();
        }
    }
}
