package yio.tro.onliyoy.game;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.ai.AiSmileysGenerator;
import yio.tro.onliyoy.game.core_model.ai.ExternalAiWorker;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesManager;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.export_import.legacy.LegacyImportWorker;
import yio.tro.onliyoy.game.general.*;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.game.tests.TestCrowdedTinyMap;
import yio.tro.onliyoy.game.tests.TestReproduceFreeze;
import yio.tro.onliyoy.game.tests.TestRealSimulation;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.tutorial.TutorialType;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.smileys.SmileysKeyboardElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NetRandomNicknameArguments;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.JumpEngineYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.name_generator.NameGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DebugActionsController {

    GameController gameController;
    private final YioGdxGame yioGdxGame;
    private final MenuControllerYio menuControllerYio;
    PointYio tempPoint;
    boolean flag;


    public DebugActionsController(GameController gameController) {
        this.gameController = gameController;
        yioGdxGame = gameController.yioGdxGame;
        menuControllerYio = yioGdxGame.menuControllerYio;
        tempPoint = new PointYio();
        flag = true;
    }


    public void debugActions() {
        doCaptureEverything();
    }


    private void doTestJumpEngine() {
        System.out.println();
        System.out.println("DebugActionsController.doTestJumpEngine");
        doMeasureJump(2, 0.22);
        doMeasureJump(1, 0.1);
        System.out.println("---");
        doMeasureJump(0.5, 0.25);
        doMeasureJump(1, 0.25);
        doMeasureJump(2, 0.25);
        System.out.println("---");
        doMeasureJump(1, 0.22);
        doMeasureJump(0.5, 0.1);
    }


    private void doMeasureJump(double speed, double impulse) {
        JumpEngineYio jumpEngineYio = new JumpEngineYio();
        jumpEngineYio.apply(speed, impulse);
        int c = 0;
        float maxValue = 0;
        while (jumpEngineYio.hasToMove()) {
            jumpEngineYio.move();
            maxValue = Math.max(maxValue, jumpEngineYio.getValue());
            c++;
        }
        System.out.println("DebugActionsController.doMeasureJump(" +
                speed + ", " + impulse +
                "): " + Yio.roundUp(maxValue, 2) + " x" + c);
    }


    private void doTextExternalAiWorker() {
        ArrayList<AbstractEvent> events = ExternalAiWorker.getInstance().apply(getViewableModel());
        System.out.println();
        System.out.println("DebugActionsController.debugActions");
        for (AbstractEvent event : events) {
            System.out.println("- " + event);
        }
    }


    private void doApplyChangeThatWillLeadToCorruptedReplay() {
        ViewableModel viewableModel = getViewableModel();
        Province selectedProvince = viewableModel.provinceSelectionManager.selectedProvince;
        EventsManager eventsManager = viewableModel.eventsManager;
        EventsRefrigerator refrigerator = viewableModel.eventsRefrigerator;
        Hex emptyHex = viewableModel.getHex(0, -1);
        Hex hexWithUnit = viewableModel.getHex(-1, 0);
        // give money
//        eventsManager.applyEvent(refrigerator.getSetMoneyEvent(selectedProvince, selectedProvince.getMoney() + 25));
        // spawn unit and make him ready
//        EventPieceAdd addPieceEvent = refrigerator.getAddPieceEvent(emptyHex, PieceType.baron);
//        addPieceEvent.setUnitId(999);
//        eventsManager.applyEvent(addPieceEvent);
//        eventsManager.applyEvent(refrigerator.getSetReadyEvent(emptyHex, true));
        // weaken unit
//        eventsManager.applyEvent(refrigerator.getDeletePieceEvent(hexWithUnit));
//        EventPieceAdd addPieceEvent = refrigerator.getAddPieceEvent(hexWithUnit, PieceType.peasant);
//        addPieceEvent.setUnitId(999);
//        eventsManager.applyEvent(addPieceEvent);
        // remove unit
//        eventsManager.applyEvent(refrigerator.getDeletePieceEvent(hexWithUnit));
    }


    private void doTestAiSmileyGenerator() {
        if (!Scenes.setupSmileysCondition.isCurrentlyVisible()) {
            Scenes.setupSmileysCondition.create();
        }
        SmileysKeyboardElement smileysKeyboardElement = Scenes.setupSmileysCondition.smileysKeyboardElement;
        AiSmileysGenerator aiSmileysGenerator = new AiSmileysGenerator();
        smileysKeyboardElement.viewField.setItems(aiSmileysGenerator.apply());
    }


    private void doRequestDebugActionOnServer() {
        getNetRoot().sendMessage(NmType.debug_action, "");
    }


    private void doSpamUndoAction() {
        if (!Scenes.mechanicsOverlay.isCurrentlyVisible()) return;
        System.out.println("DebugActionsController.doSpamUndoAction");
        for (int i = 0; i < 5; i++) {
            boolean success = getViewableModel().onUndoRequested();
            if (!success) return;
            if (getViewableModel().isNetMatch()) {
                getNetRoot().sendMessage(NmType.undo_last_action, "");
            }
        }
    }


    private void doRequestSelfKick() {
        getNetRoot().sendMessage(NmType.request_self_kick, "");
    }


    private void doCheckLegacyImportWorker() {
        System.out.println();
        System.out.println("DebugActionsController.doCheckLegacyImportWorker");
        LegacyImportWorker legacyImportWorker = new LegacyImportWorker();
        String levelCode = legacyImportWorker.apply(getLegacyOfflineLevelCode());
        System.out.println("levelCode = " + levelCode);
        (new IwClientInit(yioGdxGame, LoadingType.editor_import)).perform(levelCode);
    }


    private void doShowCameraPositionInConsole() {
        gameController.cameraController.showInConsole();
    }


    private void doCheckTutorial() {
        gameController.tutorialManager.launch(TutorialType.basics);
    }


    private void doCaptureEverything() {
        if (!gameController.yioGdxGame.gameView.coversAllScreen()) return;
        ViewableModel viewableModel = getViewableModel();
        for (Hex hex : viewableModel.hexes) {
            EventsManager eventsManager = viewableModel.eventsManager;
            HColor currentColor = viewableModel.entitiesManager.getCurrentColor();
            EventHexChangeColor event = eventsManager.factory.createChangeHexColorEvent(hex, currentColor);
            eventsManager.applyEvent(event);
        }
    }


    private void doCloseSocket() {
        System.out.println("Socket was closed manually");
        try {
            getNetRoot().socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void doCheckSplitSymbols() {
        String splitters = " ,<>:#/!@%&";
        // bad symbols: ^?|\$*()
        // & is used in net message
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < splitters.length(); i++) {
            String symbol = splitters.substring(i, i + 1);
            builder.setLength(0);
            for (int value = 0; value < 5; value++) {
                builder.append(value).append(symbol);
            }
            String string = builder.toString();
            System.out.println(symbol + " -> " + Arrays.toString(string.split(symbol)));
        }
    }


    private void doPerformRealSimulationTest() {
        (new TestRealSimulation()).perform(gameController);
    }


    private void doTreatNextEventAsInvalid() {
        DebugFlags.treatNextServerEventAsInvalid = true;
        Scenes.notification.show("Next event will be treated as invalid");
        System.out.println("Next event will be treated as invalid, causing sync in net match");
    }


    private void doGenerateSomeNicknames() {
        NetRandomNicknameArguments nrmArguments = new NetRandomNicknameArguments();
        NameGenerator nameGenerator = new NameGenerator();
        nameGenerator.setCapitalize(true);
        nameGenerator.setGroups(nrmArguments.groups);
        nameGenerator.setMasks(nrmArguments.masks);
        System.out.println();
        System.out.println("DebugActionsController.debugActions");
        for (int i = 0; i < 20; i++) {
            System.out.println("- " + nameGenerator.generate());
        }
        System.out.println();
    }


    private NetRoot getNetRoot() {
        return yioGdxGame.netRoot;
    }


    private void doShowMailBasketInConsole() {
        getViewableModel().lettersManager.showInConsole();
    }


    private void doShowDiplomacyInConsole() {
        System.out.println();
        System.out.println("DebugActionsController.doShowDiplomacyInConsole");
        if (!getViewableModel().diplomacyManager.enabled) {
            System.out.println("Diplomacy is disabled");
            return;
        }
        System.out.println("Enabled");
        for (PlayerEntity entity : getViewableModel().entitiesManager.entities) {
            System.out.println("- " + entity);
        }
    }


    private void doOpenMatchResultsScene() {
        MatchResults matchResults = new MatchResults();
        matchResults.winnerColor = HColor.green;
        matchResults.entityType = EntityType.human;
        matchResults.levelSize = LevelSize.normal;
        matchResults.rulesType = RulesType.def;
        Scenes.matchResults.create();
        Scenes.matchResults.setMatchResults(matchResults);
    }


    private void doShowProvincesInConsole(CoreModel coreModel) {
        System.out.println();
        System.out.println("Provinces in " + coreModel + ": ");
        System.out.println("currentId = " + coreModel.provincesManager.currentId);
        for (Province province : getViewableModel().provincesManager.provinces) {
            System.out.println("- " + province);
        }
    }


    private void loadSlotTwo() {
        SmItem item = gameController.savesManager.getItem("883187");
        (new IwClientInit(yioGdxGame, LoadingType.training_import)).perform(item.levelCode);
    }


    private void doImportReplay() {
        Scenes.debugTests.performImportReplay();
    }


    private void doCheckProvinces() {
        ProvincesManager provincesManager = getViewableModel().provincesManager;
        provincesManager.showProvincesInConsole();
//        getObjectsLayer().historyManager.startingPosition.provincesManager.showProvincesInConsole();
    }


    private void doShowPlayerEntitiesInConsole() {
        getViewableModel().entitiesManager.showInConsole();
    }


    private void doShowHistoryInConsole() {
        getObjectsLayer().historyManager.showInConsole();
    }


    private ViewableModel getViewableModel() {
        return getObjectsLayer().viewableModel;
    }


    private ObjectsLayer getObjectsLayer() {
        return gameController.objectsLayer;
    }


    private void doShowKeyboard() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("Write something");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                Scenes.notification.show("[" + input + "]");
            }
        });
    }


    private void doSwitchFastForward() {
        gameController.speedManager.onFastForwardButtonPressed();
    }


    private void doShowVisibleInterfaceElements() {
        menuControllerYio.showVisibleElementsInConsole();
    }


    public void updateReferences() {

    }


    public String getLegacyOfflineLevelCode() {
        return "antiyoy_level_code#level_size:4#general:4 1 11#map_name:146#editor_info:1 false false false #land:42 13 1 0,41 17 1 0,39 19 7 0,40 17 1 0,45 9 1 1,44 9 7 0,20 21 1 0,21 21 1 0,28 6 1 0,24 15 1 0,26 25 1 0,29 25 7 0,30 25 1 0,43 3 1 0,38 3 1 0,33 14 7 4,32 13 7 0,23 18 1 0,21 22 1 0,21 24 1 0,37 15 7 0,37 12 7 0,41 9 1 0,42 9 1 0,38 5 7 0,39 5 1 0,39 7 1 7,41 7 1 0,37 7 1 0,35 6 1 7,38 15 7 0,36 20 7 0,35 20 1 0,33 20 7 2,31 20 7 0,26 20 3 0,30 13 7 0,30 12 7 2,33 7 1 0,31 6 1 0,29 10 1 0,27 14 1 7,26 16 7 0,25 18 1 0,27 11 1 0,28 8 1 0,32 4 1 0,34 4 1 2,36 4 1 7,34 9 7 0,33 8 7 0,32 9 4 6,33 10 7 2,30 16 7 2,39 15 1 7,31 23 1 0,30 23 1 7,28 23 1 7,27 23 1 0,26 23 1 7,25 23 1 7,24 23 1 0,32 23 1 0,33 23 7 0,34 23 1 0,35 23 1 0,36 22 1 0,35 22 1 0,34 22 1 0,33 22 1 0,32 22 1 0,31 22 1 7,30 22 1 0,28 22 1 7,26 21 1 0,27 21 1 7,28 21 1 0,29 21 7 0,30 21 7 0,33 21 1 0,34 21 1 0,35 21 1 0,36 21 1 0,37 20 1 0,37 19 1 0,36 19 1 0,34 19 2 3,33 19 7 0,32 19 7 0,31 19 7 0,30 19 7 2,27 18 3 3,28 18 7 0,29 18 7 2,30 18 7 0,32 18 7 0,33 18 7 2,34 18 7 0,36 18 2 0,37 18 1 1,38 17 1 0,37 17 1 0,34 16 7 2,33 16 7 0,31 16 7 0,28 16 7 0,27 17 7 0,30 17 7 0,31 17 7 0,32 17 7 2,35 17 7 0,36 17 7 0,38 16 1 0,37 16 7 0,36 16 7 0,35 14 7 2,39 14 1 7,38 14 7 0,37 14 7 2,36 15 7 2,35 15 7 0,28 15 7 0,29 14 7 0,31 14 7 2,35 13 7 0,40 13 1 7,39 13 1 7,37 13 7 0,35 12 7 0,33 12 7 0,36 10 7 0,37 10 7 2,38 10 7 0,39 10 0 3,40 12 1 0,39 12 7 2,31 11 7 0,32 11 7 0,33 11 7 0,35 11 7 2,37 11 7 0,38 11 7 0,39 11 7 0,40 10 0 0,39 9 0 0,37 9 7 0,36 9 7 0,38 8 7 0,39 8 7 0,40 8 0 0,41 8 1 0,42 8 1 0,43 7 1 0,42 7 1 0,36 6 7 0,40 5 1 7,41 5 1 0,42 5 1 1,38 6 1 7,36 7 1 7,32 10 7 0,29 13 7 0,30 11 7 0,31 9 4 6,32 7 1 0,32 8 4 0,31 10 4 3,31 13 7 0,30 15 7 0,34 11 7 0,35 9 7 0,35 7 1 7,34 7 7 0,33 9 7 0,29 15 7 2,28 17 7 0,26 19 3 6,25 21 1 0,25 22 1 0,26 22 7 0,29 22 1 7,31 21 1 7,29 20 7 0,28 19 7 0,27 19 3 6,27 20 7 0,28 20 7 0,30 20 7 0,32 20 7 0,34 20 2 0,35 19 2 6,35 18 2 6,35 16 7 0,34 15 7 0,36 12 7 2,38 12 7 0,40 11 1 0,41 11 1 0,41 10 1 0,40 9 0 0,38 9 7 0,34 8 7 0,35 8 7 0,36 8 7 2,40 7 1 0,42 6 1 1,41 6 1 0,40 6 1 0,39 6 1 0,37 6 1 0,34 6 1 0,33 6 1 0,36 5 1 7,37 5 1 7,43 4 7 0,44 4 1 0,45 4 1 2,33 5 1 0,34 5 1 7,37 4 1 2,38 4 1 7,39 4 1 7,42 4 1 0,43 5 1 2,44 5 1 0,24 24 1 2,25 24 1 0,26 24 1 7,27 24 1 0,28 24 1 7,29 24 1 7,30 24 1 0,31 24 1 2,32 24 1 2,35 24 1 0,24 16 1 0,25 14 1 7,26 12 1 7,27 10 1 0,28 9 1 0,29 7 1 2,29 6 1 0,30 5 1 0,31 4 1 0,25 15 1 7,24 17 1 0,22 21 1 2,21 23 1 0,22 22 1 0,23 20 1 0,28 12 1 1,30 8 1 1,30 7 7 0,28 11 1 0,26 14 1 7,22 24 1 0,22 23 1 0,23 22 1 0,23 21 1 0,24 20 1 1,24 18 7 0,25 17 1 0,25 16 1 7,27 13 1 7,27 12 1 7,29 9 1 1,29 8 7 0,30 6 1 0,31 5 1 0,45 6 1 0,44 7 1 0,44 8 7 0,43 9 1 0,42 12 1 7,41 14 1 7,40 16 1 7,39 18 1 0,36 23 1 0,37 22 1 0,37 21 1 0,38 20 7 0,38 19 1 0,39 17 1 2,39 16 1 7,40 15 1 0,40 14 1 7,41 13 1 0,41 12 1 7,42 11 1 2,43 8 1 0,44 6 1 0,45 5 1 0,31 8 1 0,23 23 1 1,24 22 1 0,24 21 7 0,25 20 1 0,25 19 1 0,26 18 3 0,26 17 1 0,27 16 7 0,27 15 1 7,28 13 1 7,29 12 7 0,29 11 1 0,30 10 4 0,30 9 1 0,31 7 1 0,32 5 1 0,#units:42 13 1 false,41 17 4 false,40 17 1 false,20 21 4 false,21 21 3 false,24 15 1 false,26 25 1 false,30 25 2 false,43 3 2 false,38 3 3 false,23 18 1 false,21 22 2 false,21 24 3 false,41 9 4 false,42 9 2 false,28 8 2 false,32 4 4 false,27 23 1 false,34 23 1 false,35 23 3 false,36 22 3 false,35 22 3 false,30 22 1 false,26 21 2 false,33 21 3 false,35 21 1 false,37 17 4 false,40 12 1 false,42 8 1 false,42 7 1 false,41 5 4 false,40 7 4 false,41 6 2 false,40 6 1 false,39 6 1 false,37 6 2 false,33 6 3 false,42 4 2 false,27 24 1 false,30 24 1 false,35 24 1 false,24 16 1 false,27 10 2 false,28 9 2 false,30 5 3 false,24 17 1 false,21 23 2 false,25 17 2 false,31 5 1 false,45 6 1 false,44 7 2 false,43 9 2 false,39 18 1 false,36 23 1 false,37 21 1 false,38 19 2 false,40 15 1 false,41 13 1 false,43 8 1 false,44 6 2 false,31 8 1 false,25 20 2 false,30 9 1 false,#provinces:42@13@1@Декмаи@10,26@20@2@Нокрас@10,32@9@3@Торте@10,34@19@4@Арбойрарг@10,39@10@5@Бокбе@10,#relations:#coalitions:temporary#messages:#goal:def 0#real_money:#";
    }


    public String getLevelCode() {
        return "onliyoy_level_code#client_init:tiny,1#camera:0.49 0.8 1.0#core_init:420.0 672.0 29.4#hexes:-1 1 red city -1,-1 0 cyan spearman 0,0 -1 cyan city -1,2 0 lavender peasant 1,-1 2 gray,-2 2 gray,-2 1 red,-2 0 gray,-1 -1 cyan,0 -2 cyan farm -1,3 0 lavender,2 1 lavender,3 -1 gray,-2 3 gray,-3 3 gray,-3 2 gray,-3 1 gray,-3 0 gray,-2 -1 cyan farm -1,-1 -2 cyan farm -1,0 -3 gray,3 1 lavender city -1,-4 3 gray,#core_current_ids:2#player_entities:human>cyan>Rartoyn,human>red>Koski,ai_balancer>lavender>Tismosk,#provinces:3>-1<1<2<12<Koski,0<-1<1<48<Rartoyn,3<0<0<1<Tismosk,#ready:-1 0,#rules:def 1#turn:0 2#diplomacy:off#mail_basket:0,#starting_hexes:-1 1 red city -1,-1 0 gray,0 -1 cyan city -1,2 0 gray,-1 2 gray,-2 2 gray,-2 1 red,-2 0 gray,-1 -1 cyan spearman 0,0 -2 cyan farm -1,3 0 lavender,2 1 gray,3 -1 gray,-2 3 gray,-3 3 gray,-3 2 gray,-3 1 gray,-3 0 gray,-2 -1 cyan farm -1,-1 -2 cyan farm -1,0 -3 gray,3 1 lavender city -1,-4 3 gray,#events_list:te - cyan,te - red,pb 2 1 peasant 1 0,te - lavender,um -1 -1 -1 0 t,te - cyan,te - red,um 2 1 2 0 t,te - lavender,#starting_provinces:3>-1<1<2<10<Koski,0<-1<1<25<Rartoyn,3<0<0<10<Tismosk,#";
    }


}
