package yio.tro.onliyoy.net;

import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.RepeatYio;

public class NetKickPreventionWorker {

    NetRoot netRoot;
    RepeatYio<NetKickPreventionWorker> repeatSendMessage;


    public NetKickPreventionWorker(NetRoot netRoot) {
        this.netRoot = netRoot;
        initRepeats();
    }


    private void initRepeats() {
        repeatSendMessage = new RepeatYio<NetKickPreventionWorker>(this, 3 * 60 * 60) {
            @Override
            public void performAction() {
                parent.sendMessageToServer();
            }
        };
    }


    void move() {
        if (netRoot.yioGdxGame.gamePaused) return;
        if (!netRoot.isSpectatorCurrently()) return;
//        repeatSendMessage.move();
    }


    void sendMessageToServer() {
        netRoot.sendMessage(NmType.i_am_spectating, "");
    }
}
