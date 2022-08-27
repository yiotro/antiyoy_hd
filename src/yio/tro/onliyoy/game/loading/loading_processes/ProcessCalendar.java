package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.EventSetMoney;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.core_model.generators.AbstractLevelGenerator;
import yio.tro.onliyoy.game.core_model.generators.LevelGeneratorFactory;
import yio.tro.onliyoy.game.core_model.generators.LgParameters;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.CalendarData;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

import java.util.ArrayList;
import java.util.Random;

public class ProcessCalendar extends AbstractLoadingProcess{

    long seed;
    int year;
    int month;
    int day;
    Random predictableRandom;


    public ProcessCalendar(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        extractDateFromParameters();
        initPredictableRandom();
        initGameMode(GameMode.calendar);
        initLevelSize(LevelSize.tiny);
    }


    private void extractDateFromParameters() {
        year = (int) loadingParameters.get("year");
        month = (int) loadingParameters.get("month");
        day = (int) loadingParameters.get("day");
    }


    private void initPredictableRandom() {
        seed = year + month * 1000 + day * 100000;
        predictableRandom = new Random(seed);
    }


    @Override
    public void initGameRules() {
        initRules(RulesType.def);
    }


    @Override
    public void createBasicStuff() {
        initEntities();
        AbstractLevelGenerator generator = LevelGeneratorFactory.create(getViewableModel());
        LgParameters parameters = new LgParameters();
        parameters.setEntities(getViewableModel().entitiesManager.entities);
        parameters.setLoop(getLoopValue());
        parameters.setNodesValue(getNodesValue());
        parameters.setTreesDensity(getTreesDensity());
        parameters.setLandsDensity(getLandsDensity());
        parameters.setBalancerIntensity(0.5f);
        generator.setSeed(seed);
        int c = 10;
        while (c > 0) {
            c--;
            generator.generate(parameters);
            if (isGeneratedLevelGoodEnough()) break;
        }
    }


    private boolean isGeneratedLevelGoodEnough() {
        if (getViewableModel().hexes.size() < 10) return false;
        return true;
    }


    private int getLandsDensity() {
        float rValue = predictableRandom.nextFloat();
        if (rValue < 0.5f) {
            return 2;
        }
        if (rValue < 0.8f) {
            return 3;
        }
        return 4;
    }


    private double getTreesDensity() {
        return 0.1 + 0.1 * predictableRandom.nextFloat();
    }


    private float getNodesValue() {
        float rValue = predictableRandom.nextFloat();
        return 0.1f + 0.4f * rValue * rValue;
    }


    private boolean getLoopValue() {
        return predictableRandom.nextFloat() < 0.25;
    }


    private void initEntities() {
        EntitiesManager entitiesManager = getViewableModel().entitiesManager;
        ArrayList<PlayerEntity> tempList = new ArrayList<>();
        HColor playerColor = getPlayerColor();
        tempList.add(new PlayerEntity(entitiesManager, EntityType.human, playerColor));
        int numberOfEnemies = generateNumberOfEnemies();
        for (int i = 0; i < numberOfEnemies; i++) {
            HColor color = getColorForNewEntity(tempList);
            tempList.add(new PlayerEntity(entitiesManager, EntityType.ai_balancer, color));
        }
        entitiesManager.initialize(tempList);
    }


    private HColor getPlayerColor() {
        CveColorYio cveColorYio = (CveColorYio) loadingParameters.get("color");
        String string = "" + cveColorYio;
        HColor hColor;
        try {
            hColor = (HColor.valueOf(string));
        } catch (Exception e) {
            hColor = HColor.aqua;
        }
        return hColor;
    }


    private HColor getColorForNewEntity(ArrayList<PlayerEntity> tempList) {
        HColor[] values = Colors.def;
        while (true) {
            HColor randomColor = values[predictableRandom.nextInt(values.length)];
            if (randomColor == HColor.gray) continue;
            if (isColorUsed(tempList, randomColor)) continue;
            return randomColor;
        }
    }


    private boolean isColorUsed(ArrayList<PlayerEntity> tempList, HColor color) {
        for (PlayerEntity playerEntity : tempList) {
            if (playerEntity.color == color) return true;
        }
        return false;
    }


    private int generateNumberOfEnemies() {
        return 1 + predictableRandom.nextInt(4);
    }


    private ViewableModel getViewableModel() {
        return gameController.objectsLayer.viewableModel;
    }


    @Override
    public void createAdvancedStuff() {
        prepareStartingMoney();
        syncStartingProvinces();
        applyDateToCalendarData();
        getViewableModel().fogOfWarManager.setEnabled(predictableRandom.nextDouble() < 0.2);
    }


    private void applyDateToCalendarData() {
        CalendarData calendarData = getObjectsLayer().calendarData;
        calendarData.year = year;
        calendarData.month = month;
        calendarData.day = day;
        calendarData.loadingParameters = loadingParameters;
    }


    private void syncStartingProvinces() {
        CoreModel startingPosition = getObjectsLayer().historyManager.startingPosition;
        startingPosition.provincesManager.setBy(getViewableModel().provincesManager);
    }


    private void prepareStartingMoney() {
        EventsManager eventsManager = getViewableModel().eventsManager;
        for (Province province : getViewableModel().provincesManager.provinces) {
            EventSetMoney event = eventsManager.factory.createSetMoneyEvent(province, 10);
            eventsManager.applyEvent(event);
        }
    }
}
