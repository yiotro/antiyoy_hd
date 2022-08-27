package yio.tro.onliyoy.net;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetMessage implements ReusableYio, Encodeable {

    public static final String SEPARATOR = "&";
    public NmType type;
    public String value;


    public NetMessage() {
        reset();
    }


    @Override
    public void reset() {
        type = null;
        value = "-";
    }


    @Override
    public String encode() {
        return type + SEPARATOR + value;
    }


    public void decode(String source) {
        reset();
        String[] split = source.split(SEPARATOR);
        if (split.length < 2) return;
        try {
            type = NmType.valueOf(split[0]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            type = null;
            return;
        }
        value = split[1];
    }


    @Override
    public String toString() {
        return "[Message: " + type + ", " + value + "]";
    }
}
