package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetSearchResultData;

public class AnirSearchResult extends AbstractNetInputReaction{

    @Override
    public void apply() {
        NetSearchResultData netSearchResultData = new NetSearchResultData();
        netSearchResultData.decode(value);

        if (Scenes.searchForNewModerator.isCurrentlyVisible()) {
            Scenes.searchForNewModerator.onDataReceived(netSearchResultData);
        }

        if (Scenes.findUserByAdmin.isCurrentlyVisible()) {
            Scenes.findUserByAdmin.onDataReceived(netSearchResultData);
        }
    }
}
