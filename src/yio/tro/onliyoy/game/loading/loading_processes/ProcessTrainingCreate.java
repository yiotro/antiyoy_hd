package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.EventSetMoney;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.core_model.generators.AbstractLevelGenerator;
import yio.tro.onliyoy.game.core_model.generators.LevelGeneratorFactory;
import yio.tro.onliyoy.game.core_model.generators.LgParameters;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class ProcessTrainingCreate extends AbstractLoadingProcess {

    public ProcessTrainingCreate(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.training);
        initLevelSize(getLevelSizeFromParameters());
    }


    @Override
    public void initGameRules() {
        initRules(getRulesTypeFromParameters());
    }


    @Override
    public void createBasicStuff() {
        initEntities();
        AbstractLevelGenerator generator = LevelGeneratorFactory.create(getViewableModel());
        LgParameters parameters = new LgParameters();
        parameters.setEntities(getViewableModel().entitiesManager.entities);
        LevelSize levelSize = getLevelSizeFromParameters();
        if (levelSize.ordinal() > LevelSize.big.ordinal()) {
            parameters.setBalancerEnabled(false); // too slow on large maps
        }
        generator.generate(parameters);
        checkToEnableDiplomacy();
    }


    private void initEntities() {
        String entitiesString = (String) loadingParameters.get("entities");
        getViewableModel().entitiesManager.initialize(entitiesString);
    }


    private ViewableModel getViewableModel() {
        return gameController.objectsLayer.viewableModel;
    }


    @Override
    public void createAdvancedStuff() {
        prepareStartingMoney();
        syncStartingProvinces();
        getViewableModel().fogOfWarManager.setEnabled(getFogOfWarFromParameters());
    }


    private void checkToEnableDiplomacy() {
        boolean diplomacy = Boolean.valueOf((String) loadingParameters.get("diplomacy"));
        if (!diplomacy) return;
        getViewableModel().diplomacyManager.setEnabled(true);
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
