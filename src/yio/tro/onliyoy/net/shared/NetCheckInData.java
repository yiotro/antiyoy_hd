package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetCheckInData implements Encodeable, ReusableYio {

    public String id;
    public String name;


    public NetCheckInData() {
        reset();
    }


    @Override
    public void reset() {
        id = "-";
        name = "-";
    }


    @Override
    public String encode() {
        return name + "/" + id;
    }

    public void decode(String source) {
        String[] split = source.split("/");
        if (split.length < 2) return;
        name = split[0];
        id = split[1];
    }
}
