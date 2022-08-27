package yio.tro.onliyoy.game.tests;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;

public abstract class AbstractTest {


    GameController gameController;
    long timeStart;
    private int quantity;


    public AbstractTest() {

    }


    public void perform(GameController gameController) {
        this.gameController = gameController;

        prepare();
        execute();
        if (isInstant()) {
            end();
        }
    }


    public void move() {

    }


    protected void end() {
        DebugFlags.testingModeEnabled = false;
        gameController.yioGdxGame.setCurrentTest(null);
    }


    public abstract boolean isInstant();


    protected void prepare() {
        DebugFlags.testingModeEnabled = true;
        timeStart = System.currentTimeMillis();
        gameController.yioGdxGame.setCurrentTest(this);
    }


    protected abstract String getName();


    protected abstract void execute();


    protected String getFinishTitle() {
        int time = Yio.convertMillisIntoFrames(System.currentTimeMillis() - timeStart);
        return getClass().getSimpleName() + " finished in " + Yio.convertTime(time);
    }


    protected void launchGame() {
        gameController.yioGdxGame.loadingManager.startInstantly(LoadingType.test_create, new LoadingParameters());
        gameController.yioGdxGame.setCurrentTest(this);
    }


    protected void updateGameViewTexture() {
        gameController.yioGdxGame.gameView.updateAnimationTexture();
    }


    public boolean isQuantityRequired() {
        return false;
    }


    public void onQuantityChosen(int quantity) {
        this.quantity = quantity;
    }


    protected int getChosenQuantity() {
        return quantity;
    }

}
