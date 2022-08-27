package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;
import java.util.Arrays;

public class NetModActionsData implements ReusableYio, Encodeable {

    public ArrayList<String> list;
    StringBuilder stringBuilder;


    public NetModActionsData() {
        list = new ArrayList<>();
        stringBuilder = new StringBuilder();
        reset();
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
        for (String string : list) {
            stringBuilder.append(string).append("/");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        reset();
        if (source.length() < 5) return;
        list.addAll(Arrays.asList(source.split("/")));
    }
}
