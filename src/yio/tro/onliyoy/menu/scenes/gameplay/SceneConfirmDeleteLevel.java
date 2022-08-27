package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.AbstractConfirmationScene;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmDeleteLevel extends AbstractConfirmationScene {

    @Override
    protected Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                yioGdxGame.applyFullTransitionToUI();
                Scenes.moderator.create();
            }
        };
    }


    @Override
    protected Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                yioGdxGame.applyFullTransitionToUI();
                netRoot.sendMessage(NmType.request_delete_level, netRoot.verificationInfo.levelId);
                Scenes.moderator.create();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "confirm_delete_level";
    }


    @Override
    protected int getCountDown() {
        return 15;
    }
}
