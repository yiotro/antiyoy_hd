package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirOnKickedFromCustomMatch extends AbstractNetInputReaction{

    @Override
    public void apply() {
        yioGdxGame.applyFullTransitionToUI();
        MenuSwitcher.getInstance().createChooseGameModeMenu();
        String string = "kicked";
        if (value.length() > 0) {
            String prefix = LanguagesManager.getInstance().getString("kicked");
            String reason = LanguagesManager.getInstance().getString(value);
            string = prefix + ": " + reason.toLowerCase();
        }
        Scenes.notification.show(string);
    }
}
