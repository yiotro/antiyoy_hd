package yio.tro.onliyoy.stuff.human_imitation;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.ai.ExternalAiWorker;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventUnitMove;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.UndoItem;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.choose_game_mode.CgmElement;
import yio.tro.onliyoy.menu.elements.net.QmsElement;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NecAnalyzer;
import yio.tro.onliyoy.net.shared.NecException;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class HumanImitationWorker {

    private final NecAnalyzer necAnalyzer;
    YioGdxGame yioGdxGame;
    RepeatYio<HumanImitationWorker> repeatApply;
    HiwStep step;
    Random random;
    private ArrayList<AbstractEvent> events;


    public HumanImitationWorker(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        step = HiwStep.main_lobby;
        necAnalyzer = new NecAnalyzer(true);
        random = new Random();
        events = new ArrayList<>();
        checkToDisableSomeStuff();
        initRepeats();
    }


    private void checkToDisableSomeStuff() {
        if (!DebugFlags.humanImitation) return;
        DebugFlags.pcDebugLogin1 = false; // to prevent double login problems
    }


    private void initRepeats() {
        repeatApply = new RepeatYio<HumanImitationWorker>(this, 2) {
            @Override
            public void performAction() {
                parent.apply();
            }
        };
    }


    public void onMatchStarted() {
        events.clear();
    }


    public void move() {
        if (!DebugFlags.humanImitation) return;
        repeatApply.move();
    }


    private void apply() {
        checkToProcessUi();
        checkToMakeMove();
    }


    private void checkToProcessUi() {
        processUi(
                HiwStep.main_lobby,
                Scenes.mainLobby,
                Scenes.mainLobby.playButton,
                HiwStep.choose_game_mode
        );
        processUi(
                HiwStep.choose_game_mode,
                Scenes.chooseGameMode,
                Scenes.chooseGameMode.cgmElement,
                HiwStep.choose_quick_match
        );
        processUi(
                HiwStep.choose_quick_match,
                Scenes.chooseGameMode,
                Scenes.chooseGameMode.cgmElement,
                HiwStep.in_match
        );
        processUi(
                HiwStep.in_match,
                Scenes.netMatchResults,
                Scenes.netMatchResults.rvElement,
                HiwStep.choose_game_mode
        );
        processUi(
                HiwStep.in_match,
                Scenes.kickedFromServer,
                Scenes.kickedFromServer.rvElement,
                HiwStep.choose_game_mode
        );
    }


    private void checkToMakeMove() {
        if (!yioGdxGame.gameView.coversAllScreen()) return;
        ObjectsLayer objectsLayer = yioGdxGame.gameController.objectsLayer;
        ViewableModel viewableModel = objectsLayer.viewableModel;
        if (viewableModel.isSomethingMovingCurrently()) return;
        if (!viewableModel.entitiesManager.isHumanTurnCurrently()) return;
        doMakeMove(viewableModel);
    }


    private void doMakeMove(ViewableModel viewableModel) {
        if (events.size() == 0) {
            events = ExternalAiWorker.getInstance().apply(viewableModel);
            return;
        }
        if (checkToUndo()) return;
        AbstractEvent firstEvent = events.get(0);
        events.remove(firstEvent);
        viewableModel.humanControlsManager.applyHumanEvent(firstEvent);
        checkForNecException();
    }


    private boolean checkToUndo() {
        if (random.nextInt(20) != 0) return false;
        ObjectsLayer objectsLayer = yioGdxGame.gameController.objectsLayer;
        ArrayList<UndoItem> items = objectsLayer.undoManager.items;
        if (items.size() == 0) return false;
        AbstractEvent event = items.get(items.size() - 1).event;
        String backupLevelCode = "";
        if (event instanceof EventUnitMove) {
            backupLevelCode = exportCurrentLevelCode();
        }
        Scenes.mechanicsOverlay.onUndoButtonPressed();
        if (event instanceof EventUnitMove) {
            Hex hex = ((EventUnitMove) event).start;
            if (hex.isEmpty()) {
                getNetRoot().sendMessage(NmType.debug_text, "undo bug detected " + event + ", levelCode = " + backupLevelCode);
            }
        }
        return true;
    }


    private String exportCurrentLevelCode() {
        ObjectsLayer objectsLayer = yioGdxGame.gameController.objectsLayer;
        ExportParameters exportParameters = new ExportParameters();
        exportParameters.setCoreModel(objectsLayer.viewableModel);
        exportParameters.setInitialLevelSize(objectsLayer.gameController.sizeManager.initialLevelSize);
        return objectsLayer.exportManager.perform(exportParameters);
    }


    private void checkForNecException() {
        if (!DebugFlags.necAnalysis) return;
        ObjectsLayer objectsLayer = yioGdxGame.gameController.objectsLayer;
        ArrayList<UndoItem> items = objectsLayer.undoManager.items;
        if (items.size() == 0) return;
        UndoItem lastItem = items.get(items.size() - 1);
        String levelCode = lastItem.getLevelCode();
        try {
            necAnalyzer.apply(levelCode);
        } catch (NecException e) {
            doCloseSocket();
            SoundManager.playSound(SoundType.coin, true);
            yioGdxGame.onExceptionCaught(e);
        }
    }


    private void doCloseSocket() {
        try {
            getNetRoot().socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void processUi(HiwStep targetStep, SceneYio scene, InterfaceElement interfaceElement, HiwStep nextStep) {
        if (step != targetStep) return;
        if (!isSceneVisibleAndReady(scene)) return;
        switch (targetStep) {
            default:
                interfaceElement.pressArtificially(0);
                break;
            case choose_quick_match:
                if (YioGdxGame.random.nextDouble() < 0.5) {
                    Scenes.quickMatchSearching.create();
                } else {
                    Scenes.searchingForDuel.create();
                }
                break;
        }
        step = nextStep;
    }


    private boolean isSceneVisibleAndReady(SceneYio sceneYio) {
        if (sceneYio == null) return false;
        if (!sceneYio.isCurrentlyVisible()) return false;
        for (InterfaceElement element : sceneYio.getLocalElementsList()) {
            FactorYio factor = element.getFactor();
            if (factor.getValue() == 1) return true;
        }
        return false;
    }


    private NetRoot getNetRoot() {
        return yioGdxGame.netRoot;
    }
}
