package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetUlTransferData implements ReusableYio, Encodeable {

    public String name;
    public long creationTime;
    public String levelCode;
    public String id;


    public NetUlTransferData() {
        reset();
    }


    @Override
    public void reset() {
        name = "-";
        levelCode = "-";
        creationTime = 0;
        id = "-";
    }


    @Override
    public String encode() {
        return name + "/" + creationTime + "/" + levelCode + "/" + id;
    }


    public void decode(String source) {
        reset();
        if (source.length() < 5) return;
        String[] split = source.split("/");
        if (split.length < 3) return;
        name = split[0];
        creationTime = Long.valueOf(split[1]);
        levelCode = split[2];
        id = split[3];
    }
}
