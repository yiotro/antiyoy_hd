package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.AbstractConfirmationScene;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmDeclineLevel extends AbstractConfirmationScene {


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
                yioGdxGame.applyFullTransitionToUI();
                netRoot.sendMessage(NmType.decline_user_level, "");
                Scenes.moderator.create();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        String name = netRoot.verificationInfo.name;
        String string = languagesManager.getString("confirm_decline_level");
        return string.replace("[levelName]", name);
    }

}
