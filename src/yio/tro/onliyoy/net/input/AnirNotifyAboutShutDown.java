package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetTimeSynchronizer;

public class AnirNotifyAboutShutDown extends AbstractNetInputReaction{

    @Override
    public void apply() {
        long serverTime = Long.valueOf(value);
        long targetTime = NetTimeSynchronizer.getInstance().convertToClientTime(serverTime);
        if (Scenes.admin.isCurrentlyVisible()) {
            long deltaTime = targetTime - System.currentTimeMillis();
            int timeInFrames = Yio.convertMillisIntoFrames(deltaTime);
            String timeString = Yio.convertTimeToUnderstandableString(timeInFrames);
            Scenes.notification.show("Shut down in " + timeString);
            return;
        }
        Scenes.announceShutDown.create();
        Scenes.announceShutDown.setTargetTime(targetTime);
    }
}
