package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.net.NetRoot;

import java.io.IOException;
import java.util.Scanner;

public class NetInputListener implements Runnable{

    NetRoot root;


    public NetInputListener(NetRoot root) {
        this.root = root;
    }


    @Override
    public void run() {
        try (Scanner scanner = new Scanner(root.socket.getInputStream())) {
            while (true) {
                if (!scanner.hasNextLine()) break;
                String inputValue = scanner.nextLine();
                if (YioGdxGame.platformType == PlatformType.pc || DebugFlags.showMessagesFromServerInConsole) {
                    System.out.println("-> " + getLimitedString(inputValue));
                }
                if (inputValue.equals("stop")) break;
                root.bufferManager.onMessageReceived(inputValue);
            }
            System.out.println("Disconnected from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getLimitedString(String source) {
        if (source.length() < 150) {
            return source;
        }
        return source.substring(0, 150) + "...";
    }
}
