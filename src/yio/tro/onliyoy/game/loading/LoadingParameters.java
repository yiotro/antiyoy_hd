package yio.tro.onliyoy.game.loading;

import java.util.HashMap;

public class LoadingParameters {

    private HashMap<String, Object> map;


    public LoadingParameters() {
        map = new HashMap<>();
    }


    public Object get(String key) {
        if (!contains(key)) return null;
        return map.get(key);
    }


    public boolean contains(String key) {
        return map.containsKey(key);
    }


    public void add(String key, Object value) {
        map.put(key, value);
    }

}
