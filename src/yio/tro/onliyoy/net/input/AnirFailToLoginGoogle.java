package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirFailToLoginGoogle extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.entry.isCurrentlyVisible()) {
            Scenes.entry.create();
        }
        Scenes.entry.onFailToAuthorizeOnServer();
    }
}
