package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetUlCacheData;

import java.util.ArrayList;

public class AnirSearchUlResults extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.searchUserLevel.isCurrentlyVisible()) return;
        Scenes.searchUserLevel.onDataReceived(decodeValue());
    }


    private ArrayList<NetUlCacheData> decodeValue() {
        ArrayList<NetUlCacheData> list = new ArrayList<>();
        if (value.equals("-")) return list;
        for (String token : value.split("%")) {
            NetUlCacheData netUlCacheData = new NetUlCacheData();
            netUlCacheData.decode(token);
            list.add(netUlCacheData);
        }
        return list;
    }
}
