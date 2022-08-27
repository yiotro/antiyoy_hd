package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetRepTransferData implements ReusableYio, Encodeable {

    public String name;
    public String levelCode;
    public String message;
    public String levelId;


    public NetRepTransferData() {
        reset();
    }


    @Override
    public void reset() {
        name = "-";
        levelCode = "-";
        message = "-";
        levelId = "-";
    }


    @Override
    public String encode() {
        return message + "/" + name + "/" + levelCode + "/" + levelId;
    }


    public void decode(String source) {
        if (source.length() < 5) return;
        String[] split = source.split("/");
        if (split.length < 3) return;
        message = split[0];
        name = split[1];
        levelCode = split[2];
        levelId = split[3];
    }
}
