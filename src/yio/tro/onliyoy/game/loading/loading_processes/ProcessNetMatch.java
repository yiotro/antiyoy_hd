package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NetMatchBattleData;
import yio.tro.onliyoy.net.shared.NmbdItem;

public class ProcessNetMatch extends AbstractLoadingProcess {

    NetRoot netRoot;


    public ProcessNetMatch(LoadingManager loadingManager) {
        super(loadingManager);
        netRoot = yioGdxGame.netRoot;
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.net_match);
        initLevelSize(getLevelSizeFromParameters());
        netRoot.currentMatchData.decode((String) loadingParameters.get("battle_data"));
        yioGdxGame.rejoinWorker.setReady(true);
    }


    @Override
    public void initGameRules() {
        ViewableModel viewableModel = getViewableModel();
        String levelCode = getLevelCodeFromParameters();
        (new IwCoreRules(viewableModel)).perform(levelCode);
    }


    private ViewableModel getViewableModel() {
        return gameController.objectsLayer.viewableModel;
    }


    @Override
    public void createBasicStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = getViewableModel();
        initEntities(levelCode);
        (new IwCoreCurrentIds(viewableModel)).perform(levelCode);
        (new IwCoreTurn(viewableModel)).perform(levelCode);
        (new IwCoreGraph(viewableModel)).perform(levelCode);
        (new IwCoreHexes(viewableModel)).perform(levelCode);
        loadAiVersionCodeFromParameters();
    }


    private void initEntities(String levelCode) {
        ViewableModel viewableModel = getViewableModel();
        (new IwCorePlayerEntities(viewableModel)).perform(levelCode);
        turnAllEntitiesIntoNetEntities();
        turnOwnedEntityIntoHuman(viewableModel);
    }


    private void turnOwnedEntityIntoHuman(ViewableModel viewableModel) {
        HColor ownedColor = getOwnedColor();
        if (ownedColor == null) return;
        PlayerEntity ownedEntity = viewableModel.entitiesManager.getEntity(ownedColor);
        if (ownedEntity == null) return;
        ownedEntity.type = EntityType.human;
    }


    private HColor getOwnedColor() {
        String id = yioGdxGame.netRoot.userData.id;
        for (NmbdItem item : netRoot.currentMatchData.items) {
            if (!item.id.equals(id)) continue;
            return item.color;
        }
        return null;
    }


    private void turnAllEntitiesIntoNetEntities() {
        EntitiesManager entitiesManager = getViewableModel().entitiesManager;
        for (PlayerEntity entity : entitiesManager.entities) {
            entity.type = EntityType.net_entity;
        }
    }


    @Override
    public void createAdvancedStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = getViewableModel();
        (new IwCoreProvinces(viewableModel)).perform(levelCode);
        (new IwCoreDiplomacy(viewableModel)).perform(levelCode);
        (new IwCoreMailBasket(viewableModel)).perform(levelCode);
        (new IwReadiness(viewableModel)).perform(levelCode);
        (new IwCoreFogOfWar(viewableModel)).perform(levelCode);
        gameController.cameraController.flyUp(true);
        updateTurnEndTime();
    }


    private void updateTurnEndTime() {
        NetMatchBattleData currentMatchData = netRoot.currentMatchData;
        currentMatchData.turnEndTime = System.currentTimeMillis() + currentMatchData.turnSeconds * 1000;
    }
}
