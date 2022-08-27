package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

import java.util.ArrayList;

public class ProcessTest extends AbstractLoadingProcess{

    public ProcessTest(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.custom);
        initLevelSize(LevelSize.normal);
    }


    @Override
    public void initGameRules() {
        initRules(RulesType.def);
    }


    @Override
    public void createBasicStuff() {
        createSingleHumanEntity();
    }


    private void createSingleHumanEntity() {
        ViewableModel viewableModel = getObjectsLayer().viewableModel;
        EntitiesManager entitiesManager = viewableModel.entitiesManager;
        PlayerEntity playerEntity = new PlayerEntity(entitiesManager, EntityType.human, HColor.green);
        ArrayList<PlayerEntity> list = new ArrayList<>();
        list.add(playerEntity);
        entitiesManager.initialize(list);
    }


    @Override
    public void createAdvancedStuff() {

    }
}
