package yio.tro.onliyoy.menu.scenes.options;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.AbstractConfirmationScene;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneConfirmResetSettings extends AbstractConfirmationScene {

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
                SettingsManager.getInstance().resetValues();
                Scenes.settings.loadValues();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "confirm_reset_settings";
    }
}
