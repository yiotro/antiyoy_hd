package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetUserData;

public class AnirUpdateUserData extends AbstractNetInputReaction{

    @Override
    public void apply() {
        root.userData.decode(value);
        if (Scenes.mainLobby.isCurrentlyVisible()) {
            Scenes.mainLobby.updateNicknameView();
            Scenes.mainLobby.updateCoinsElpView();
        }
        if (Scenes.shop.isCurrentlyVisible()) {
            Scenes.shop.onUserDataUpdated();
        }
    }
}
