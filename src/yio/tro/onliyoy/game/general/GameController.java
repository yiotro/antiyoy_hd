package yio.tro.onliyoy.game.general;

import com.badlogic.gdx.Gdx;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.DebugActionsController;
import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.tutorial.ScriptManager;
import yio.tro.onliyoy.game.tutorial.TutorialManager;
import yio.tro.onliyoy.game.viewable_model.EventFlowAnalyzer;
import yio.tro.onliyoy.menu.ClickDetector;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.TimeMeasureYio;

import java.util.ArrayList;

public class GameController {

    public YioGdxGame yioGdxGame;
    public int w, h;
    public int currentTouchCount;
    long currentTime;
    public PointYio touchDownPos, currentTouch;
    public DebugActionsController debugActionsController;
    public GameMode gameMode;
    public ObjectsLayer objectsLayer;
    ClickDetector clickDetector;
    public boolean backgroundVisible;
    public CameraController cameraController;
    public PointYio currentTouchConverted;
    public TouchMode touchMode;
    public SpeedManager speedManager;
    public ArrayList<TouchMode> dyingTms;
    public SavesManager savesManager;
    public SizeManager sizeManager;
    RepeatYio<GameController> repeatFixUi;
    public TutorialManager tutorialManager;
    public ScriptManager scriptManager;


    public GameController(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        touchDownPos = new PointYio();
        currentTouch = new PointYio();
        cameraController = new CameraController(this);
        debugActionsController = new DebugActionsController(this);
        clickDetector = new ClickDetector();
        gameMode = null;
        currentTouchConverted = new PointYio();
        speedManager = new SpeedManager(this);
        dyingTms = new ArrayList<>();
        savesManager = new SavesManager(this);
        sizeManager = new SizeManager(GraphicsYio.width);
        tutorialManager = new TutorialManager(this);
        scriptManager = new ScriptManager(this);

        TouchMode.createModes(this);
        touchMode = null;
        initRepeats();
    }


    private void initRepeats() {
        repeatFixUi = new RepeatYio<GameController>(this, 60) {
            @Override
            public void performAction() {
                parent.checkToFixUi();
            }
        };
    }


    public void move() {
        currentTime = System.currentTimeMillis();

        tutorialManager.move();
        cameraController.move();
        moveTouchMode();
        objectsLayer.move();
        repeatFixUi.move();
        scriptManager.move();
    }


    private void checkToFixUi() {
        // yes, this is not cool but minimizing app causes problems
        if (yioGdxGame.gamePaused) return;
        if (!yioGdxGame.gameView.coversAllScreen()) return;
        if (Scenes.gameOverlay.isCurrentlyVisible()) return;
        if (gameMode != GameMode.net_match) return;
        MenuSwitcher.getInstance().createMenuOverlay();
    }


    private void moveTouchMode() {
        if (touchMode != null) {
            touchMode.move();
        }

        for (int i = dyingTms.size() - 1; i >= 0; i--) {
            TouchMode dtm = dyingTms.get(i);
            dtm.move();
            if (dtm.isReadyToBeRemoved()) {
                dyingTms.remove(dtm);
            }
        }
    }


    public void defaultValues() {
        GameRules.defaultValues();
        cameraController.defaultValues();
        currentTouchCount = 0;
        touchDownPos.set(0, 0);
        speedManager.defaultValues();
        scriptManager.clear();
        setTouchMode(TouchMode.tmNone);
    }


    public boolean doesCurrentGameModeAllowGameplay() {
        switch (gameMode) {
            default:
                return true;
            case editor:
            case replay:
            case verification:
            case report:
                return false;
        }
    }


    public void syncMechanicsOverlayWithCurrentTurn() {
        if (!doesCurrentGameModeAllowGameplay()) return;
        if (yioGdxGame.gameView.appearFactor.isInDestroyState()) return;
        EntitiesManager entitiesManager = objectsLayer.viewableModel.entitiesManager;
        if (entitiesManager.isInAiOnlyMode()) return;
        if (entitiesManager.isHumanTurnCurrently()) {
            Scenes.mechanicsOverlay.create();
        } else {
            Scenes.mechanicsOverlay.destroy();
        }
    }


    public void createCamera() {
        yioGdxGame.gameView.createOrthoCam();
        cameraController.createCamera();
        yioGdxGame.gameView.updateCam();
    }


    public void createObjectsLayer() {
        if (objectsLayer != null) {
            objectsLayer.onDestroy();
        }
        objectsLayer = new ObjectsLayer(this);
    }


    public void debugActions() {
        debugActionsController.updateReferences();
        debugActionsController.debugActions();
    }


    public YioGdxGame getYioGdxGame() {
        return yioGdxGame;
    }


    public void touchDown(int screenX, int screenY) {
        currentTouchCount++;
        updateTouchPoints(screenX, screenY);
        touchDownPos.setBy(currentTouch);
        clickDetector.onTouchDown(currentTouch);

        if (objectsLayer.onTouchDown(currentTouch)) return;
        touchMode.touchDownReaction();
    }


    boolean touchedAsClick() {
        return clickDetector.isClicked();
    }


    public void updateTouchPoints(int screenX, int screenY) {
        currentTouch.x = screenX;
        currentTouch.y = screenY;

        if (cameraController.orthoCam != null) {
            currentTouchConverted.x = (screenX - 0.5f * w) * cameraController.orthoCam.zoom + cameraController.orthoCam.position.x;
            currentTouchConverted.y = (screenY - 0.5f * h) * cameraController.orthoCam.zoom + cameraController.orthoCam.position.y;
        }
    }


    public void touchUp(int screenX, int screenY, int pointer, int button) {
        currentTouchCount--;
        if (currentTouchCount < 0) {
            currentTouchCount = 0;
            return;
        }

        updateTouchPoints(screenX, screenY);
        clickDetector.onTouchUp(currentTouch);
        if (objectsLayer.onTouchUp(currentTouch)) return;
        checkForClick();
        touchMode.touchUpReaction();
    }


    private void checkForClick() {
        if (currentTouchCount != 0) return;
        if (!touchedAsClick()) return;
        onClick();
    }


    public void onMouseWheelScrolled(int amount) {
        if (touchMode != null) {
            if (touchMode.onMouseWheelScrolled(amount)) {
                return; // touch mode can catch mouse wheel scroll
            }
        }

        cameraController.onMouseWheelScrolled(amount);
    }


    public void setTouchMode(TouchMode touchMode) {
        if (this.touchMode == touchMode) return;

        TouchMode previousTouchMode = this.touchMode;
        this.touchMode = touchMode;
        onTmEnd(previousTouchMode);
        touchMode.onModeBegin();
        Scenes.gameOverlay.onTouchModeSet(touchMode);

        dyingTms.remove(touchMode);
    }


    private void onTmEnd(TouchMode previousTouchMode) {
        if (previousTouchMode == null) return;
        previousTouchMode.kill();
        previousTouchMode.onModeEnd();

        if (!dyingTms.contains(previousTouchMode)) {
            dyingTms.add(previousTouchMode);
        }
    }


    public void resetTouchMode() {
        if (DebugFlags.debugEnabled) {
            setTouchMode(TouchMode.tmDebug);
            return;
        }
        switch (gameMode) {
            default:
            case training:
            case custom:
                setTouchMode(TouchMode.tmDefault);
                break;
            case replay:
                setTouchMode(TouchMode.tmReplay);
                break;
            case editor:
                setTouchMode(TouchMode.tmEditor);
                break;
            case verification:
                setTouchMode(TouchMode.tmVerification);
                break;
            case report:
                setTouchMode(TouchMode.tmReport);
                break;
            case net_match:
                if (yioGdxGame.netRoot.isSpectatorCurrently()) {
                    setTouchMode(TouchMode.tmSpectator);
                    break;
                }
                setTouchMode(TouchMode.tmDefault);
                break;
        }
    }


    private void onClick() {
        if (touchMode.onClick()) {
            cameraController.forgetAboutLastTap();
            return;
        }
        objectsLayer.onClick();
    }


    public void touchDrag(int screenX, int screenY, int pointer) {
        updateTouchPoints(screenX, screenY);
        clickDetector.onTouchDrag(currentTouch);
        if (objectsLayer.onTouchDrag(currentTouch)) return;
        touchMode.touchDragReaction();
    }


    public void onBasicStuffCreated() {
        cameraController.onBasicStuffCreated();
        objectsLayer.onBasicStuffCreated();
        resetTouchMode();
    }


    public void onAdvancedStuffCreated() {
        objectsLayer.onAdvancedStuffCreated();
        EventFlowAnalyzer.getInstance().onActualModelCreated(objectsLayer.viewableModel);
    }


    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }


    public void onEscapedToPauseMenu() {
        if (gameMode == null) return; // no matches were started yet
        resetTouchMode();
        if (objectsLayer.viewableModel == null) return;
        objectsLayer.viewableModel.moveZoneViewer.hide();
    }


    public void onPause() {
        if (objectsLayer != null && objectsLayer.editorManager != null) {
            objectsLayer.editorManager.onAppPaused();
        }
    }


    public void onResume() {
        currentTouchCount = 0;
    }


    public void setBackgroundVisible(boolean backgroundVisible) {
        this.backgroundVisible = backgroundVisible;
    }


    public float getViewZoomLevel() {
        return cameraController.viewZoomLevel;
    }
}
