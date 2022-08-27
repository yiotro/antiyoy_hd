package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetMatchVerificationData;
import yio.tro.onliyoy.net.shared.NmType;

public class AnirVerifyReadyForMatch extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.matchLobby.isCurrentlyVisible() && !Scenes.waitMatchLaunching.isCurrentlyVisible()) {
            root.sendMessage(NmType.rfm_problem, "");
            return;
        }
        NetMatchVerificationData netMatchVerificationData = new NetMatchVerificationData();
        netMatchVerificationData.decode(value);
        if (Scenes.matchLobby.netMatchLobbyData.matchType != netMatchVerificationData.matchType) {
            root.sendMessage(NmType.rfm_problem, "");
            return;
        }
        Scenes.matchLobby.onVerificationMessageSent();
        root.sendMessage(NmType.rfm_ready, "");
    }
}
