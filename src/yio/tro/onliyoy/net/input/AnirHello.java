package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetTimeSynchronizer;
import yio.tro.onliyoy.net.shared.NetValues;
import yio.tro.onliyoy.net.shared.NmType;

public class AnirHello extends AbstractNetInputReaction{

    @Override
    public void apply() {
        Scenes.entry.addRveTextItem("connected_to_server");
        root.sendMessage(NmType.protocol, "" + NetValues.PROTOCOL);
        NetTimeSynchronizer.getInstance().onRequestSent();
        yioGdxGame.generalBackgroundManager.spawnParticles();
        Scenes.entry.addRveTextItem("checking_protocol");
    }
}
