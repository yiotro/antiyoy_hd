package yio.tro.onliyoy.net;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.input.NetInputListener;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NetValues;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;

public class NetConnectionManager {

    NetRoot root;


    public NetConnectionManager(NetRoot root) {
        this.root = root;
    }


    public void doConnectToServer() {
        try {
            root.socket = new Socket(getServerAddress(), NetValues.PORT);
            NetInputListener inputListener = new NetInputListener(root);
            Executors.newSingleThreadExecutor().execute(inputListener);
            root.printWriter = new PrintWriter(root.socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("NetConnectionManager.doConnectToServer: unable to connect");
            Scenes.entry.addRveTextItem("server_not_responding");
            Scenes.entry.addRveTextItem("offline_mode_enabled");
            PostponedReactionsManager.aprOfflineMode.launch();
            root.yioGdxGame.generalBackgroundManager.spawnParticles();
        }
    }


    private String getServerAddress() {
        switch (NetValues.LOCATION_TYPE) {
            default:
            case local:
                return NetValues.LOCAL_VPS_ADDRESS;
            case test:
                return NetValues.TEST_VPS_ADDRESS;
            case production:
                return NetValues.PRODUCTION_VPS_ADDRESS;
        }
    }
}
