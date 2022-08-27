package yio.tro.onliyoy.game.tests;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.ai.AiManager;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.EventSetMoney;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.core_model.generators.AbstractLevelGenerator;
import yio.tro.onliyoy.game.core_model.generators.LevelGeneratorFactory;
import yio.tro.onliyoy.game.core_model.generators.LgParameters;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.ExportManager;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.general.SizeManager;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.TimeMeasureYio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TestSimulateMatches extends AbstractTest {


    private CoreModel coreModel;
    private AiManager aiManager;
    HashMap<HColor, Integer> mapResults;
    SizeManager sizeManager;
    LevelSize levelSize;
    private long startTime;


    public TestSimulateMatches() {
        mapResults = new HashMap<>();
        sizeManager = new SizeManager(GraphicsYio.width);
        resetResults();
    }


    private void resetResults() {
        for (HColor color : Colors.def) {
            mapResults.put(color, 0);
        }
    }


    @Override
    public boolean isInstant() {
        return true;
    }


    @Override
    protected String getName() {
        return "Simulate matches";
    }


    @Override
    protected void execute() {
        startTime = System.currentTimeMillis();
        resetResults();
        for (int i = 0; i <= getChosenQuantity(); i++) {
            checkToIndicateCurrentStepInConsole(i);
            HColor winnerColor = simulateSingleMatch();
            if (winnerColor == null) continue;
            mapResults.put(winnerColor, mapResults.get(winnerColor) + 1);
        }
        onTestFinished();
    }


    private void onTestFinished() {
        System.out.println();
        long timeInMillis = System.currentTimeMillis() - startTime;
        int timeInFrames = Yio.convertMillisIntoFrames(timeInMillis);
        System.out.println("Test '" + getName() + "' finished in " + Yio.convertTime(timeInFrames));
        SoundManager.playSound(SoundType.hold_to_march);
        showResultsInConsole();
    }


    private void checkToIndicateCurrentStepInConsole(int i) {
        if (i == 0) return;
        if (i % 100 != 0) return;
        if (i % 1000 == 0) {
            System.out.println("----- " + i + " -----");
            return;
        }
        System.out.println("step " + i);
    }


    private void showResultsInConsole() {
        System.out.println("Results");
        double smallestResult = getSmallestResult();
        for (PlayerEntity entity : coreModel.entitiesManager.entities) {
            HColor color = entity.color;
            int value = mapResults.get(color);
            double f = (double) value / smallestResult;
            f = Yio.roundUp(f, 2);
            System.out.println(color + ": " + value + " (" + f + ")");
        }
    }


    private int getSmallestResult() {
        int minValue = -1;
        for (PlayerEntity entity : coreModel.entitiesManager.entities) {
            HColor color = entity.color;
            int value = mapResults.get(color);
            if (minValue == -1 || value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }


    private HColor simulateSingleMatch() {
        levelSize = LevelSize.normal;
        initCoreModel(5);
        generateRandomLevel();
        coreModel.eventsManager.applyEvent(coreModel.eventsRefrigerator.getGraphCreatedEvent());
        prepareStartingMoney();
        coreModel.eventsManager.applyEvent(coreModel.eventsRefrigerator.getMatchStartedEvent());
        initAiManager();
        int counter = 0;
        while (true) {
            counter++;
            aiManager.move();
            if (counter % 100 != 0) continue;
            if (counter >= 10000) {
                return null; // match going for too long, probably AI got stuck
            }
            PlayerEntity winner = coreModel.finishMatchManager.getWinner();
            if (winner == null) continue;
            return winner.color;
        }
    }


    private void doExportLevelCodeToClipboard() {
        ExportManager exportManager = new ExportManager();
        ExportParameters exportParameters = new ExportParameters();
        exportParameters.setCoreModel(coreModel);
        exportParameters.setInitialLevelSize(levelSize);
        exportParameters.setAiVersionCode(-1);
        exportManager.performToClipboard(exportParameters);
        System.out.println("Level code was exported to clipboard");
    }


    private void prepareStartingMoney() {
        EventsManager eventsManager = coreModel.eventsManager;
        for (Province province : coreModel.provincesManager.provinces) {
            EventSetMoney event = eventsManager.factory.createSetMoneyEvent(province, 10);
            eventsManager.applyEvent(event);
        }
    }


    private void generateRandomLevel() {
        AbstractLevelGenerator generator = LevelGeneratorFactory.create(coreModel);
        LgParameters parameters = new LgParameters();
        parameters.setEntities(coreModel.entitiesManager.entities);
        generator.generate(parameters);
    }


    private void initAiManager() {
        aiManager = new AiManager(coreModel, Difficulty.balancer);
        aiManager.createAIs();
    }


    private void initCoreModel(int numberOfEntities) {
        if (numberOfEntities > Colors.def.length) {
            System.out.println("TestSimulateMatches.initCoreModel: too many entities");
        }
        coreModel = new CoreModel("test");
        sizeManager.initLevelSize(levelSize);
        coreModel.diplomacyManager.setEnabled(true);
        float hexRadius = 0.07f * GraphicsYio.width; // objects layer may not exist at this point
        coreModel.buildGraph(sizeManager.position, hexRadius);
        coreModel.setRules(RulesType.def, -1);
        EntitiesManager entitiesManager = coreModel.entitiesManager;
        PlayerEntity[] entities = new PlayerEntity[numberOfEntities];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = new PlayerEntity(entitiesManager, EntityType.ai_balancer, Colors.def[i]);
        }
        entitiesManager.initialize(new ArrayList<>(Arrays.asList(entities)));
    }


    @Override
    public boolean isQuantityRequired() {
        return true;
    }
}
