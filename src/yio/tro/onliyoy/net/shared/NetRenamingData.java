package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetRenamingData implements ReusableYio, Encodeable {

    public String targetClientId;
    public String currentName;
    public String desiredName;


    public NetRenamingData() {
        reset();
    }


    @Override
    public void reset() {
        targetClientId = "-";
        currentName = "-";
        desiredName = "-";
    }


    @Override
    public String encode() {
        return targetClientId + "/" + currentName + "/" + desiredName;
    }


    public void decode(String source) {
        String[] split = source.split("/");
        if (split.length < 3) return;
        targetClientId = split[0];
        currentName = split[1];
        desiredName = split[2];
    }
}
