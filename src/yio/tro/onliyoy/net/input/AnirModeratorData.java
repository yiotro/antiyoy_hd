package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetModeratorData;

public class AnirModeratorData extends AbstractNetInputReaction{


    private NetModeratorData netModeratorData;


    public AnirModeratorData() {
        netModeratorData = new NetModeratorData();
    }


    @Override
    public void apply() {
        if (!Scenes.moderator.isCurrentlyVisible()) return;
        netModeratorData.decode(value);
        Scenes.moderator.onModeratorDataReceived(netModeratorData);
    }
}
