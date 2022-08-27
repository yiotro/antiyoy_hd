package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmDislikeLevel extends AbstractConfirmationScene{

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
                netRoot.sendMessage(NmType.dislike_level, netRoot.tempUlTransferData.id);
                Scenes.userLevels.create();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "confirm_dislike_level";
    }
}
