package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class ProcessTutorial extends AbstractLoadingProcess{

    public ProcessTutorial(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.tutorial);
        initLevelSize(LevelSize.tiny);
        checkToDisableConstructionPanel();
    }


    private void checkToDisableConstructionPanel() {
        SettingsManager instance = SettingsManager.getInstance();
        if (!instance.constructionPanel) return;
        instance.constructionPanel = false;
        instance.saveValues();
    }


    @Override
    public void initGameRules() {
        getObjectsLayer().viewableModel.setRules(RulesType.def, -1);
    }


    @Override
    public void createBasicStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        (new IwCamera(gameController)).perform(levelCode);
        (new IwCorePlayerEntities(viewableModel)).perform(levelCode);
        (new IwCoreGraph(viewableModel)).perform(levelCode);
        (new IwCoreHexes(viewableModel)).perform(levelCode);
    }


    @Override
    public void createAdvancedStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        (new IwCoreProvinces(viewableModel)).perform(levelCode);
        (new IwCoreDiplomacy(viewableModel)).perform(levelCode);
        (new IwCoreMailBasket(viewableModel)).perform(levelCode);
        // no fog of war in tutorial
    }
}
