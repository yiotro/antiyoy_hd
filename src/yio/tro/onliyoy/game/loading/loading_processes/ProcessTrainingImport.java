package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.core_model.events.HistoryManager;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class ProcessTrainingImport extends AbstractLoadingProcess{

    public ProcessTrainingImport(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.training);
        initLevelSize(getLevelSizeFromParameters());
    }


    @Override
    public void initGameRules() {
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        String levelCode = getLevelCodeFromParameters();
        (new IwCoreRules(viewableModel)).perform(levelCode);
    }


    @Override
    public void createBasicStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        (new IwCamera(gameController)).perform(levelCode);
        (new IwCorePlayerEntities(viewableModel)).perform(levelCode);
        (new IwCoreCurrentIds(viewableModel)).perform(levelCode);
        (new IwCoreTurn(viewableModel)).perform(levelCode);
        (new IwCoreGraph(viewableModel)).perform(levelCode);
        (new IwCoreHexes(viewableModel)).perform(levelCode);
        loadAiVersionCodeFromParameters();
    }


    @Override
    public void createAdvancedStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        (new IwCoreProvinces(viewableModel)).perform(levelCode);
        (new IwCoreDiplomacy(viewableModel)).perform(levelCode);
        (new IwCoreMailBasket(viewableModel)).perform(levelCode);
        (new IwReadiness(viewableModel)).perform(levelCode);
        HistoryManager historyManager = getObjectsLayer().historyManager;
        (new IwStartingHexes(historyManager)).perform(levelCode);
        (new IwCorePlayerEntities(historyManager.startingPosition)).perform(levelCode);
        (new IwStartingProvinces(historyManager.startingPosition)).perform(levelCode);
        (new IwEventsList(viewableModel, historyManager)).perform(levelCode);
        (new IwPauseName(viewableModel)).perform(levelCode);
        (new IwCoreFogOfWar(viewableModel)).perform(levelCode);
    }
}
