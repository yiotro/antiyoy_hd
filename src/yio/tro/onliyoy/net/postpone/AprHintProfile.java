package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetValues;

public class AprHintProfile extends AbstractPostponedReaction{

    public AprHintProfile(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        if (OneTimeInfo.getInstance().hintProfile) return false;
        if (root.userData.money == NetValues.DEFAULT_MONEY) return false;
        if (!Scenes.mainLobby.isCurrentlyVisible()) return false;
        if (Scenes.attraction.isCurrentlyVisible()) return false;
        if (Scenes.mainLobby.nicknameViewElement.getFactor().getValue() < 1) return false;
        return true;
    }


    @Override
    void apply() {
        Scenes.forefinger.create();
        Scenes.forefinger.forefinger.setTarget(Scenes.mainLobby.nicknameViewElement, "");
    }
}
