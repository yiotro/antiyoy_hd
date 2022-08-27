package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.net.shared.NmbdItem;

public class SceneConfirmExitMatch extends AbstractConfirmationScene{

    @Override
    protected Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
            }
        };
    }


    @Override
    protected Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                netRoot.sendMessage(NmType.exit_match_battle, netRoot.currentMatchData.matchId);
                Scenes.mainLobby.create();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return languagesManager.getString("exit_match") + "?";
    }


    @Override
    protected int getCountDown() {
        if (isPlayerInSpectatorMode()) return 0;
        if (netRoot.isInLocalMode()) return 0;
        return super.getCountDown();
    }


    private boolean isPlayerInSpectatorMode() {
        ViewableModel viewableModel = getObjectsLayer().viewableModel;
        if (!viewableModel.isNetMatch()) return false;
        NmbdItem item = netRoot.currentMatchData.getItem(netRoot.userData.id);
        if (item == null) return true;
        return item.color == null;
    }
}
