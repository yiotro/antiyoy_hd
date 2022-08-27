package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class NetDeletionsHistoryData implements ReusableYio, Encodeable {

    public ArrayList<String> list;
    private StringBuilder stringBuilder;


    public NetDeletionsHistoryData() {
        list = new ArrayList<>();
        stringBuilder = new StringBuilder();
    }


    @Override
    public void reset() {
        list.clear();
        stringBuilder.setLength(0);
    }


    @Override
    public String encode() {
        stringBuilder.setLength(0);
        for (String s : list) {
            stringBuilder.append(s).append(",");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        list.clear();
        if (source == null) return;
        for (String token : source.split(",")) {
            if (token.length() < 5) continue;
            list.add(token);
        }
    }


    public void onModeratorDeletedLevel(String moderatorId, String levelId) {
        long time = System.currentTimeMillis();
        String string = moderatorId + " " + levelId + " " + time;
        list.add(string);
        while (list.size() > 200) {
            list.remove(0);
        }
    }
}
