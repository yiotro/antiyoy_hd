package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.AlternativeUpdateWorker;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetTimeSynchronizer;

public class AnirProtocolIsValid extends AbstractNetInputReaction{

    @Override
    public void apply() {
        long time = Long.valueOf(value);
        NetTimeSynchronizer.getInstance().onServerTimeReceived(time);
        AlternativeUpdateWorker.getInstance().onProtocolSuccessfullyValidated();
        Scenes.entry.onProtocolIsValidMessageReceived();
    }
}
