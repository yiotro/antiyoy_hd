package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirNotifyFishReceived extends AbstractNetInputReaction{

    @Override
    public void apply() {
        String prefix = LanguagesManager.getInstance().getString("fish_received");
        Scenes.notification.show(prefix + ": +" + value);
    }
}
