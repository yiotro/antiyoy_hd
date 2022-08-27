package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class ProcessVerification extends AbstractLoadingProcess{

    public ProcessVerification(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.verification);
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
        (new IwCoreGraph(viewableModel)).perform(levelCode);
        (new IwCoreHexes(viewableModel)).perform(levelCode);
        loadAiVersionCodeFromParameters();
    }


    @Override
    public void createAdvancedStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        (new IwEditor(getObjectsLayer().editorManager)).perform(levelCode);
        (new IwCoreProvinces(viewableModel)).perform(levelCode);
        (new IwCoreDiplomacy(viewableModel)).perform(levelCode);
        (new IwCoreMailBasket(viewableModel)).perform(levelCode);
        (new IwCoreFogOfWar(viewableModel)).perform(levelCode);
        viewableModel.readinessManager.reset();
        TouchMode.tmVerification.updateItems();
    }
}
