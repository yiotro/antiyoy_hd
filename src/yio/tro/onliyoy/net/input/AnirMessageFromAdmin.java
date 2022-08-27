package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirMessageFromAdmin extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (value.length() == 0) return;
        Scenes.notification.show(value);
        if (Scenes.announceShutDown.isCurrentlyVisible()) {
            Scenes.announceShutDown.destroy();
        }
    }
}
