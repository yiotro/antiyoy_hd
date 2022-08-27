package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetUserData;

public class AnirUserData extends AbstractNetInputReaction{

    @Override
    public void apply() {
        NetUserData netUserData = new NetUserData();
        netUserData.decode(value);
        System.out.println("AnirUserData.apply: no real reaction currently");
//        if (Scenes.mlUserInfo.isCurrentlyVisible()) {
//            Scenes.mlUserInfo.onDossierReceived(netUserData);
//        }
    }
}
