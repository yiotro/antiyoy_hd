package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetAdminData;

public class AnirAdminInfo extends AbstractNetInputReaction{

    NetAdminData netAdminData;


    public AnirAdminInfo() {
        netAdminData = new NetAdminData();
    }


    @Override
    public void apply() {
        if (!Scenes.admin.isCurrentlyVisible()) return;
        netAdminData.decode(value);
        Scenes.admin.onAdminInfoReceived(netAdminData);
    }
}
