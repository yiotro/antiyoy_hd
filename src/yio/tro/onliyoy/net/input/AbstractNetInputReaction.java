package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.net.NetMessage;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NmType;

public abstract class AbstractNetInputReaction {


    public NetMessage message;
    protected NetRoot root;
    protected YioGdxGame yioGdxGame;
    protected GameController gameController;
    protected ObjectsLayer objectsLayer;
    protected ViewableModel viewableModel;
    protected String value;


    public abstract void apply();


    public void perform(NetRoot netRoot, NetMessage message) {
        updateReferences(netRoot, message);
        try {
            apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateReferences(NetRoot netRoot, NetMessage message) {
        root = netRoot;
        this.message = message;
        value = message.value;
        yioGdxGame = root.yioGdxGame;
        gameController = yioGdxGame.gameController;
        objectsLayer = gameController.objectsLayer;
        if (objectsLayer == null) return;
        viewableModel = objectsLayer.viewableModel;
    }

}
