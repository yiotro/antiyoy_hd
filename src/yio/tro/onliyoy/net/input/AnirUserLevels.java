package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetUlCacheData;
import yio.tro.onliyoy.net.shared.NetUserLevelData;

import java.util.ArrayList;

public class AnirUserLevels extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.userLevels.isCurrentlyVisible()) return;
        ArrayList<NetUlCacheData> list = new ArrayList<>();
        for (String token : value.split("%")) {
            if (token.length() < 5) continue;
            NetUlCacheData netUserLevelData = new NetUlCacheData();
            netUserLevelData.decode(token);
            list.add(netUserLevelData);
        }
        Scenes.userLevels.onCodeReceived(list);
    }
}
