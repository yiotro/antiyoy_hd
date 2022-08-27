package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

import java.util.ArrayList;

public class ProcessEditorCreate extends AbstractLoadingProcess{

    public ProcessEditorCreate(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.editor);
        initLevelSize(getLevelSizeFromParameters());
    }


    @Override
    public void initGameRules() {
        initRules(getRulesTypeFromParameters());
    }


    @Override
    public void createBasicStuff() {
        createSingleHumanEntity();
        getObjectsLayer().viewableModel.clearGraph();
    }


    private void createSingleHumanEntity() {
        ViewableModel viewableModel = getObjectsLayer().viewableModel;
        EntitiesManager entitiesManager = viewableModel.entitiesManager;
        ArrayList<PlayerEntity> list = new ArrayList<>();
        list.add(new PlayerEntity(entitiesManager, EntityType.human, HColor.green));
        list.add(new PlayerEntity(entitiesManager, EntityType.ai_balancer, HColor.red));
        entitiesManager.initialize(list);
    }


    @Override
    public void createAdvancedStuff() {

    }
}
