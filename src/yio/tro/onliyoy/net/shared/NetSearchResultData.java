package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NetSearchResultData implements ReusableYio, Encodeable {

    public HashMap<String, String> map;
    StringBuilder stringBuilder;


    public NetSearchResultData() {
        map = new LinkedHashMap<>();
        stringBuilder = new StringBuilder();
        reset();
    }


    @Override
    public void reset() {
        map.clear();
        stringBuilder.setLength(0);
    }


    public void addUser(String id, String name) {
        map.put(id, name);
    }


    @Override
    public String encode() {
        if (map.size() == 0) return "-";
        stringBuilder.setLength(0);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append("/").append(entry.getValue()).append(",");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        reset();
        if (source.length() < 2) return;
        for (String token : source.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split("/");
            if (split.length < 2) continue;
            addUser(split[0], split[1]);
        }
    }
}
