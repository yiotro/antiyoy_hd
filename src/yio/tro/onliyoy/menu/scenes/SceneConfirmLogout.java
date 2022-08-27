package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneConfirmLogout extends AbstractConfirmationScene{

    @Override
    protected Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.normalProfilePanel.create();
            }
        };
    }


    @Override
    protected Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                SettingsManager.getInstance().autoLogin = false;
                SettingsManager.getInstance().saveValues();
                Scenes.onSignedOut.create();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        String prefix = languagesManager.getString("sign_out");
        return prefix + "?";
    }
}
