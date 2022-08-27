package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirMatchStatistics extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.netMatchResults.isCurrentlyVisible()) return;
        if (value.length() < 5) return;
        Scenes.netMatchResults.onStatisticsCodeReceived(value);
    }
}
