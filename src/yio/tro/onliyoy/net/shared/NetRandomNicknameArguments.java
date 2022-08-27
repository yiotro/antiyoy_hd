package yio.tro.onliyoy.net.shared;

import java.util.ArrayList;
import java.util.HashMap;

public class NetRandomNicknameArguments {


    public HashMap<String, String> groups;
    public ArrayList<String> masks;


    public NetRandomNicknameArguments() {
        initGroups();
        initMasks();
    }


    private HashMap<String, String> initGroups() {
        groups = new HashMap<>();
        groups.put("k", "r t p s d k b n m");
        groups.put("m", "rb kr tr br bn cl rt t p s d k b nm mn sh ch gh j");
        groups.put("a", "o a ay e oy o e a ye ee i");
        groups.put("o", "na oy no in sa ya yo io io ko eo eo ao ao ek");
        return groups;
    }


    private ArrayList<String> initMasks() {
        masks = new ArrayList<>();
        masks.add("kama");
        masks.add("kakako");
        masks.add("kamao");
        masks.add("kakkamo");
        masks.add("kakko");
        masks.add("amako");
        masks.add("akamo");
        masks.add("kamak");
        masks.add("kakako");
        masks.add("akako");
        masks.add("amako");
        return masks;
    }

}
