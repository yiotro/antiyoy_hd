package yio.tro.onliyoy.game.loading;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.loading.loading_processes.*;
import yio.tro.onliyoy.game.view.GameView;
import yio.tro.onliyoy.game.viewable_model.EventFlowAnalyzer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.stuff.TimeMeasureYio;
import yio.tro.onliyoy.stuff.human_imitation.HumanImitationWorker;

public class LoadingManager {

    public YioGdxGame yioGdxGame;
    GameController gameController;
    GameView gameView;
    int w, h;
    public boolean working;
    int currentStep;
    AbstractLoadingProcess currentProcess;
    private long startTime;


    public LoadingManager(GameController gameController) {
        this.gameController = gameController;
        yioGdxGame = gameController.yioGdxGame;
        gameView = yioGdxGame.gameView;
        w = gameController.w;
        h = gameController.h;
        working = false;
        currentProcess = null;
        currentStep = -1;
    }


    public void startLoading(LoadingType type, LoadingParameters loadingParameters) {
        if (!DebugFlags.testingModeEnabled) {
            Yio.safeSay("Loading level...");
        }
        working = true;
        currentStep = 0;
        currentProcess = null;
        startTime = System.currentTimeMillis();
        createProcess(type);
        currentProcess.setLoadingParameters(loadingParameters);
    }


    private void createProcess(LoadingType loadingType) {
        switch (loadingType) {
            default:
                currentProcess = new ProcessEmpty(this);
                break;
            case training_create:
                currentProcess = new ProcessTrainingCreate(this);
                break;
            case test_create:
                currentProcess = new ProcessTest(this);
                break;
            case editor_create:
                currentProcess = new ProcessEditorCreate(this);
                break;
            case training_import:
                currentProcess = new ProcessTrainingImport(this);
                break;
            case replay_open:
                currentProcess = new ProcessReplayOpen(this);
                break;
            case editor_import:
                currentProcess = new ProcessEditorImport(this);
                break;
            case net_match:
                currentProcess = new ProcessNetMatch(this);
                break;
            case completion_check:
                currentProcess = new ProcessCompletionCheck(this);
                break;
            case verification:
                currentProcess = new ProcessVerification(this);
                break;
            case user_level:
                currentProcess = new ProcessUserLevel(this);
                break;
            case report:
                currentProcess = new ProcessReport(this);
                break;
            case calendar:
                currentProcess = new ProcessCalendar(this);
                break;
            case tutorial:
                currentProcess = new ProcessTutorial(this);
                break;
            case campaign:
                currentProcess = new ProcessCampaign(this);
                break;
        }
    }


    public void move() {
        switch (currentStep) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                gameController.defaultValues();
                currentProcess.prepare();
                break;
            case 4:
                gameController.createCamera();
                gameController.createObjectsLayer();
                break;
            case 5:
                currentProcess.initGameRules();
                Scenes.provinceManagement.onRulesDefined();
                currentProcess.createBasicStuff();
                break;
            case 6:
                gameController.onBasicStuffCreated();
                break;
            case 7:
                gameView.onBasicStuffCreated();
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                currentProcess.createAdvancedStuff();
                gameController.onAdvancedStuffCreated();
                prepareMenu();
                endCreation();
                break;
        }

        checkToProfile();
        currentStep++;
    }


    private void checkToProfile() {
        if (!DebugFlags.profileLoading) return;
        System.out.println("Loading step " + currentStep + ": " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
    }


    private void endCreation() {
        yioGdxGame.setGamePaused(false);
        gameView.updateAnimationTexture();
        checkIfRulesAreSet();
        checkToClearInputEventsBuffer();
        checkToApplyDebugModifications();
        yioGdxGame.humanImitationWorker.onMatchStarted();
        working = false;
    }


    private void checkToApplyDebugModifications() {
        if (DebugFlags.compareClientVsServerEventLogs) {
            gameController.speedManager.setSpeed(4);
            DebugFlags.analyzeEventFlows = true;
            ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
            EventFlowAnalyzer.getInstance().onActualModelCreated(viewableModel);
        }
    }


    private void checkToClearInputEventsBuffer() {
        if (gameController.objectsLayer.viewableModel.isNetMatch()) return;
        PostponedReactionsManager.aprServerEvent.clearBuffer();
    }


    private void checkIfRulesAreSet() {
        if (gameController.objectsLayer.viewableModel.ruleset != null) return;
        System.out.println("Problem detected: rules were not set during loading process");
    }


    public void startInstantly(LoadingType type, LoadingParameters loadingParameters) {
        startLoading(type, loadingParameters);

        while (working) {
            move();
        }
    }


    private void prepareMenu() {
        MenuSwitcher.getInstance().createMenuOverlay();
        Scenes.mechanicsOverlay.onMatchStarted();
    }


}
