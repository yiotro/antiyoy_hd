package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.scenes.AbstractNetWaitScene;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;

import java.util.ArrayList;

public abstract class AbstractPostponedReaction {


    PostponedReactionsManager postponedReactionsManager;
    boolean active;
    protected NetRoot root;
    protected YioGdxGame yioGdxGame;
    protected GameController gameController;
    protected ObjectsLayer objectsLayer;
    protected ViewableModel viewableModel;
    protected ArrayList<String> inputBuffer;


    public AbstractPostponedReaction(PostponedReactionsManager postponedReactionsManager) {
        this.postponedReactionsManager = postponedReactionsManager;
        postponedReactionsManager.reactions.add(this);
        inputBuffer = new ArrayList<>();
        active = false;
    }


    abstract boolean isReady();


    abstract void apply();


    protected boolean areNetWaitScenesMovingCurrently() {
        for (AbstractNetWaitScene netWaitScene : yioGdxGame.netRoot.postponedReactionsManager.netWaitScenes) {
            if (netWaitScene.isSomethingMovingCurrently()) return true;
        }
        return false;
    }


    void move() {
        if (!active) return;
        if (!isReady()) return;
        active = false;
        updateReferences();
        apply();
    }


    public void launch() {
        active = true;
        updateReferences();
        onLaunched();
    }


    protected void onLaunched() {

    }


    public void suspend() {
        active = false;
    }


    private void updateReferences() {
        root = postponedReactionsManager.root;
        yioGdxGame = root.yioGdxGame;
        gameController = yioGdxGame.gameController;
        objectsLayer = gameController.objectsLayer;
        if (objectsLayer == null) return;
        viewableModel = objectsLayer.viewableModel;
    }


    public void addToBuffer(String string) {
        inputBuffer.add(string);
    }


    public void clearBuffer() {
        inputBuffer.clear();
    }

}
