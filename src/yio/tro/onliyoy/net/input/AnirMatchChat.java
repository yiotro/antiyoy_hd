package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirMatchChat extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.netOverlay.isCurrentlyVisible()) return;
        Scenes.netOverlay.onNetChatMessageReceived(value);
    }
}
