package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetReportData implements ReusableYio, Encodeable {

    public String levelId;
    public String userId;
    public String message;


    public NetReportData() {
        reset();
    }


    @Override
    public void reset() {
        levelId = "-";
        userId = "-";
        message = "-";
    }


    @Override
    public String encode() {
        return levelId + "/" + userId + "/" + message;
    }


    public void decode(String source) {
        if (source.length() < 4) return;
        String[] split = source.split("/");
        if (split.length < 3) return;
        levelId = split[0];
        userId = split[1];
        message = split[2];
    }
}
