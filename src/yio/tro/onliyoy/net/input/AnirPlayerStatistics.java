package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetPlayerStatisticsData;

public class AnirPlayerStatistics extends AbstractNetInputReaction{


    private NetPlayerStatisticsData netPlayerStatisticsData;


    public AnirPlayerStatistics() {
        netPlayerStatisticsData = new NetPlayerStatisticsData();
    }


    @Override
    public void apply() {
        if (!Scenes.normalProfilePanel.isCurrentlyVisible()) return;
        netPlayerStatisticsData.decode(value);
        Scenes.normalProfilePanel.onPlayerStatisticsReceived(netPlayerStatisticsData);
    }
}
