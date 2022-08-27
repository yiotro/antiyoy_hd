package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class NetRecentIpsData implements ReusableYio, Encodeable {

    public ArrayList<String> list;
    private StringBuilder stringBuilder;


    public NetRecentIpsData() {
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
        if (list.size() == 0) return "-";
        stringBuilder.setLength(0);
        for (String s : list) {
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        list.clear();
        if (source == null) return;
        for (String token : source.split(" ")) {
            if (token.length() < 5) continue;
            list.add(token);
        }
    }


    public void addIp(String ip) {
        list.remove(ip);
        list.add(ip);
        if (list.size() > 5) {
            list.remove(0);
        }
    }
}
