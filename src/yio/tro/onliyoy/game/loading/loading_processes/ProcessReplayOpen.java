package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.events.HistoryManager;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class ProcessReplayOpen extends AbstractLoadingProcess{

    public ProcessReplayOpen(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.replay);
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
        // important: 'core current' should not be imported when opening replay
        // 'core turn' also shouldn't be imported here
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        (new IwCamera(gameController)).perform(levelCode);
        (new IwCorePlayerEntities(viewableModel, true)).perform(levelCode);
        (new IwCoreGraph(viewableModel)).perform(levelCode);
        (new IwCoreHexes(viewableModel)).perform(levelCode, "starting_hexes");
    }


    @Override
    public void createAdvancedStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        HistoryManager historyManager = getObjectsLayer().historyManager;
        (new IwStartingHexes(historyManager)).perform(levelCode);
        (new IwCorePlayerEntities(historyManager.startingPosition)).perform(levelCode);
        (new IwStartingProvinces(historyManager.startingPosition)).perform(levelCode);
        (new IwEventsList(viewableModel, historyManager)).perform(levelCode);
        // no fog of war in replays
        getObjectsLayer().replayManager.onAdvancedStuffCreated();
    }
}
