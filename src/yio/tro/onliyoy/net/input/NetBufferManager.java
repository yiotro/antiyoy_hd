package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.net.NetRoot;

import java.util.ArrayList;

public class NetBufferManager {

    NetRoot root;
    private ArrayList<String> buffer;
    private final Object lockBuffer;


    public NetBufferManager(NetRoot root) {
        this.root = root;
        lockBuffer = new Object();
        buffer = new ArrayList<>();
    }


    public void move() {
        // this method is called in main thread
        if (root.yioGdxGame.minimized) return;
        synchronized (lockBuffer) {
            while (buffer.size() > 0) {
                String string = buffer.get(0);
                buffer.remove(0);
                root.inputHandler.processMessage(string);
            }
        }
    }


    void onMessageReceived(String inputValue) {
        // this method is called in secondary thread
        synchronized (lockBuffer) {
            buffer.add(inputValue);
        }
    }

}
