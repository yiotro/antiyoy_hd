package yio.tro.onliyoy.net;

public class NetTimeSynchronizer {

    private static NetTimeSynchronizer instance;
    private long requestSendTime;
    public long deltaTime;


    public static void initialize() {
        instance = null;
    }


    public static NetTimeSynchronizer getInstance() {
        if (instance == null) {
            instance = new NetTimeSynchronizer();
        }
        return instance;
    }


    public void onRequestSent() {
        requestSendTime = System.currentTimeMillis();
    }


    public void onServerTimeReceived(long serverTime) {
        long currentTime = System.currentTimeMillis();
        long ping = currentTime - requestSendTime;
        long assumedCurrentServerTime = serverTime + ping / 2;
        deltaTime = assumedCurrentServerTime - currentTime;
    }


    public long convertToServerTime(long clientTime) {
        if (clientTime == 0) return 0;
        return clientTime + deltaTime;
    }


    public long convertToClientTime(long serverTime) {
        if (serverTime == 0) return 0;
        return serverTime - deltaTime;
    }
}
