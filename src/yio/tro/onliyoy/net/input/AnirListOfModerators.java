package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetSearchResultData;

public class AnirListOfModerators extends AbstractNetInputReaction{

    @Override
    public void apply() {
        NetSearchResultData netSearchResultData = new NetSearchResultData();
        netSearchResultData.decode(value);
        Scenes.editModerators.onDataReceived(netSearchResultData);
    }
}
