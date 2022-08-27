package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetModActionsData;

public class AnirModActions extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.modActions.isCurrentlyVisible()) return;
        NetModActionsData netModActionsData = new NetModActionsData();
        netModActionsData.decode(value);
        Scenes.modActions.onDataReceived(netModActionsData);
    }
}
