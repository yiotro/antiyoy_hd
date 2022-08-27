package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirCustomMatchesList extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.customMatches.isCurrentlyVisible()) return;
        Scenes.customMatches.onCodeReceived(value);
    }
}
