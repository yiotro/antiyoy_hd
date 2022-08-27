package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.AlternativeUpdateWorker;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirProtocol extends AbstractNetInputReaction{

    @Override
    public void apply() {
        switch (value) {
            default:
                System.out.println("AnirProtocol.apply: problem");
                break;
            case "wait for server to update":
                Scenes.entry.addRveTextItem("server_is_updating");
                Scenes.entry.onServerIsUpdating();
                break;
            case "go update":
                Scenes.entry.addRveTextItem("client_version_deprecated");
                Scenes.entry.onClientVersionDeprecated();
                AlternativeUpdateWorker.getInstance().onClientVersionOutOfDateMessageReceived();
                break;
        }
    }
}
