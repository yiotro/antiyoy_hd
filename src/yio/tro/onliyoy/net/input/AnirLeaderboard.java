package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirLeaderboard extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.leaderboard.isCurrentlyVisible()) return;
        Scenes.leaderboard.onCodeReceived(value);
    }
}
