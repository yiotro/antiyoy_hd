package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetTimeSynchronizer;

public class AnirMatchStartTime extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Yio.isNumeric(value)) return;
        long serverTime = Long.valueOf(value);
        long clientTime = NetTimeSynchronizer.getInstance().convertToClientTime(serverTime);
        if (Scenes.waitMatchLaunching.isCurrentlyVisible() && value.equals("0")) {
            // match was cancelled at the last moment
            Scenes.waitMatchLaunching.destroy();
            Scenes.matchLobby.create();
            Scenes.matchLobby.setMatchStartTime(clientTime);
            return;
        }
        if (!Scenes.matchLobby.isCurrentlyVisible()) return;
        Scenes.matchLobby.setMatchStartTime(clientTime);
    }
}
