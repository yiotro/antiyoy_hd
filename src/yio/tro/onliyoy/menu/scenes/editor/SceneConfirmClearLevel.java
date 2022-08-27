package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.AbstractConfirmationScene;

public class SceneConfirmClearLevel extends AbstractConfirmationScene {

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
                getObjectsLayer().editorManager.onClearButtonPressed();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "confirm_clear_level";
    }
}
