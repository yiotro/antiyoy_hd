package yio.tro.onliyoy.game.general;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.ai.AiManager;
import yio.tro.onliyoy.game.core_model.events.EventTurnEnd;
import yio.tro.onliyoy.game.core_model.events.EventsFactory;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.core_model.events.HistoryManager;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.export_import.ExportManager;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.save_system.UserLevelsProgressManager;
import yio.tro.onliyoy.game.viewable_model.ReplayManager;
import yio.tro.onliyoy.game.viewable_model.StatisticsWorker;
import yio.tro.onliyoy.game.viewable_model.UndoManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NetValues;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.calendar.CalendarManager;

public class ObjectsLayer implements TouchableYio, AcceleratableYio {

    public GameController gameController;
    public ExportManager exportManager;
    public ViewableModel viewableModel;
    public HistoryManager historyManager;
    public ReplayManager replayManager;
    public UndoManager undoManager;
    public AiManager aiManager;
    RepeatYio<ObjectsLayer> repeatAI;
    public EditorManager editorManager;
    RepeatYio<ObjectsLayer> repeatCheckToEndMatch;
    RepeatYio<ObjectsLayer> repeatAutoSkip;
    public SyncManager syncManager;
    public TreeManager treeManager;
    RepeatYio<ObjectsLayer> repeatForceEndTurn;
    AutoEndTurnWorker autoEndTurnWorker;
    public CalendarData calendarData;


    public ObjectsLayer(GameController gameController) {
        this.gameController = gameController;

        exportManager = new ExportManager();
        viewableModel = new ViewableModel(this);
        historyManager = new HistoryManager(viewableModel);
        replayManager = new ReplayManager(this);
        undoManager = new UndoManager(viewableModel);
        aiManager = new AiManager(viewableModel, Difficulty.balancer);
        editorManager = new EditorManager(this);
        syncManager = new SyncManager(viewableModel);
        treeManager = new TreeManager(viewableModel);
        autoEndTurnWorker = new AutoEndTurnWorker(this);
        calendarData = new CalendarData();

        defaultValues();
        initRepeats();
    }


    private void initRepeats() {
        repeatAI = new RepeatYio<ObjectsLayer>(this, 4, 4) {
            @Override
            public void performAction() {
                parent.checkToMakeAiMove();
            }
        };
        repeatCheckToEndMatch = new RepeatYio<ObjectsLayer>(this, 30, 30) {
            @Override
            public void performAction() {
                if (!viewableModel.entitiesManager.isInAiOnlyMode()) return;
                parent.checkToEndMatch();
            }
        };
        repeatAutoSkip = new RepeatYio<ObjectsLayer>(this, 15, 15) {
            @Override
            public void performAction() {
                parent.checkForAutoSkip();
            }
        };
        repeatForceEndTurn = new RepeatYio<ObjectsLayer>(this, 5, 5) {
            @Override
            public void performAction() {
                parent.checkToForceEndTurn();
            }
        };
    }


    @Override
    public void moveActually() {
        viewableModel.move();
        replayManager.moveActually();
        repeatAI.move();
        editorManager.moveActually();
        repeatCheckToEndMatch.move();
        repeatAutoSkip.move();
        repeatForceEndTurn.move();
    }


    private void checkToForceEndTurn() {
        EntitiesManager entitiesManager = viewableModel.refModel.entitiesManager;
        if (!entitiesManager.getCurrentEntity().isHuman()) return;
        if (!viewableModel.isNetMatch()) return;
        NetRoot netRoot = getNetRoot();
        if (netRoot.currentMatchData.turnEndTime == 0) return;
        if (System.currentTimeMillis() < netRoot.currentMatchData.turnEndTime) return;
        EventsManager eventsManager = viewableModel.eventsManager;
        EventTurnEnd endTurnEvent = eventsManager.factory.createEndTurnEvent();
        HColor previousColor = viewableModel.entitiesManager.getCurrentColor();
        eventsManager.applyEvent(endTurnEvent);
        System.out.println("Forced turn end on client locally: " + previousColor + " -> " + viewableModel.entitiesManager.getCurrentColor());
    }


    private NetRoot getNetRoot() {
        return gameController.yioGdxGame.netRoot;
    }


    private void checkToMakeAiMove() {
        if (!gameController.doesCurrentGameModeAllowGameplay()) return;
        if (!gameController.yioGdxGame.gameView.coversAllScreen()) return;
        if (viewableModel.isSomethingMovingCurrently()) return;
        aiManager.move();
        EntitiesManager entitiesManager = viewableModel.entitiesManager;
        if (entitiesManager.isInAiOnlyMode() && !DebugFlags.aiPerTurnMovement) {
            while (viewableModel.turnsManager.turnIndex != 0 && aiManager.active) {
                aiManager.move();
            }
        }
        if (entitiesManager.isSingleplayerHumanMatch() && !DebugFlags.aiPerTurnMovement) {
            while (entitiesManager.getCurrentEntity().isArtificialIntelligence() && aiManager.active) {
                aiManager.move();
            }
        }
    }


    public void checkToEndMatch() {
        if (!gameController.doesCurrentGameModeAllowGameplay()) return;
        if (viewableModel.isNetMatch()) return;
        MatchResults matchResults = viewableModel.finishMatchManager.getMatchResults();
        if (matchResults == null) return;
        if (viewableModel.isSomethingMovingCurrently()) return;
        if (!viewableModel.isBufferEmpty()) return;
        if (gameController.yioGdxGame.gamePaused) return;
        historyManager.onMatchEnded();
        aiManager.onMatchEnded();
        (new StatisticsWorker(viewableModel, historyManager)).apply(matchResults.statisticsData, matchResults.winnerColor);
        matchResults.levelSize = gameController.sizeManager.initialLevelSize;
        matchResults.rulesType = viewableModel.ruleset.getRulesType();
        matchResults.gameMode = gameController.gameMode;
        gameController.yioGdxGame.applyFullTransitionToUI();
        gameController.scriptManager.onMatchEnded();
        checkToCopyCalendarData(matchResults);
        Scenes.matchResults.create();
        Scenes.matchResults.setMatchResults(matchResults);
        checkToSaveProgress(matchResults);
    }


    private void checkToCopyCalendarData(MatchResults matchResults) {
        if (matchResults.gameMode != GameMode.calendar) return;
        matchResults.year = calendarData.year;
        matchResults.month = calendarData.month;
        matchResults.day = calendarData.day;
    }


    private void checkToSaveProgress(MatchResults matchResults) {
        PlayerEntity winner = viewableModel.entitiesManager.getEntity(matchResults.winnerColor);
        if (!winner.isHuman()) return;
        switch (matchResults.gameMode) {
            default:
                break;
            case user_level:
                String id = getNetRoot().tempUlTransferData.id;
                UserLevelsProgressManager.getInstance().onCompleted(id);
                getNetRoot().sendMessage(NmType.on_user_level_completed, "");
                break;
            case calendar:
                CalendarManager.getInstance().onCalendarDayCompleted(matchResults);
                break;
            case campaign:
                CampaignManager instance = CampaignManager.getInstance();
                instance.onLevelCompleted(instance.currentLevelIndex);
                break;
            case tutorial:
                gameController.tutorialManager.onTutorialLevelCompleted();
                break;
        }
    }


    @Override
    public void moveVisually() {
        replayManager.moveVisually();
        editorManager.moveVisually();
        autoEndTurnWorker.move();
    }


    public void onPlayerRequestedToEndTurn() {
        if (!viewableModel.refModel.entitiesManager.getCurrentEntity().isHuman()) return;
        if (!viewableModel.entitiesManager.getCurrentEntity().isHuman()) return;
        EventsFactory factory = viewableModel.eventsManager.factory;
        EventTurnEnd endTurnEvent = factory.createEndTurnEvent();
        viewableModel.humanControlsManager.applyHumanEvent(endTurnEvent);
    }


    void checkForAutoSkip() {
        if (!gameController.doesCurrentGameModeAllowGameplay()) return;
        PlayerEntity currentEntity = viewableModel.entitiesManager.getCurrentEntity();
        if (!currentEntity.isHuman()) return;
        if (viewableModel.isNetMatch()) return; // should be handled on server side
        if (viewableModel.provincesManager.getProvince(currentEntity.color) != null) return;
        EventTurnEnd endTurnEvent = viewableModel.eventsManager.factory.createEndTurnEvent();
        viewableModel.eventsManager.applyEvent(endTurnEvent);
    }


    public void checkForAutosave() {
        if (!SettingsManager.getInstance().autosave) return;
        if (viewableModel.turnsManager.turnIndex != 0) return;
        if (!viewableModel.entitiesManager.isSingleplayerHumanMatch()) return;
        if (!isGameModeGoodForAutosave()) return;
        ExportParameters parameters = ExportParameters.getInstance();
        parameters.setCameraCode(gameController.cameraController.encode());
        parameters.setInitialLevelSize(gameController.sizeManager.initialLevelSize);
        parameters.setCoreModel(viewableModel);
        parameters.setHistoryManager(historyManager);
        parameters.setAiVersionCode(aiManager.getUpdatedAiVersionCode());
        String levelCode = exportManager.perform(parameters);
        gameController.savesManager.applyAutosave(levelCode);
    }


    private boolean isGameModeGoodForAutosave() {
        switch (gameController.gameMode) {
            default:
                return false;
            case custom:
            case training:
            case campaign:
                return true;
        }
    }


    public void move() {
        for (int speed = gameController.speedManager.getSpeed(); speed > 0; speed--) {
            moveActually();
        }
        moveVisually();
    }


    public void defaultValues() {
        viewableModel.buildGraph(gameController.sizeManager.position, getHexRadius());
        replayManager.defaultValues();
    }


    public float getHexRadius() {
        return NetValues.HEX_RADIUS * GraphicsYio.width;
    }


    public void onBasicStuffCreated() {
        viewableModel.onBasicStuffCreated();
        replayManager.onBasicStuffCreated();
        editorManager.onBasicStuffCreated();
    }


    public void onAdvancedStuffCreated() {
        if (shouldActivateTreeManager()) {
            viewableModel.eventsManager.addListener(treeManager);
        }
        viewableModel.onAdvancedStuffCreated();
    }


    private boolean shouldActivateTreeManager() {
        if (viewableModel.isNetMatch()) return false;
        if (gameController.gameMode == GameMode.replay) return false;
        return true;
    }


    public void onClick() {

    }


    @Override
    public boolean onTouchDown(PointYio touchPoint) {
        return false;
    }


    @Override
    public boolean onTouchDrag(PointYio touchPoint) {
        return false;
    }


    @Override
    public boolean onTouchUp(PointYio touchPoint) {
        return false;
    }


    public void onDestroy() {

    }


}
