package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.core_model.events.HistoryManager;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class ProcessCampaign extends AbstractLoadingProcess{

    public ProcessCampaign(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.campaign);
        initLevelSize(getLevelSizeFromParameters());
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
        (new IwCoreCurrentIds(viewableModel)).perform(levelCode);
        (new IwCoreTurn(viewableModel)).perform(levelCode);
        (new IwCoreGraph(viewableModel)).perform(levelCode);
        (new IwCoreHexes(viewableModel)).perform(levelCode);
    }


    @Override
    public void createAdvancedStuff() {
        String levelCode = getLevelCodeFromParameters();
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        (new IwCoreProvinces(viewableModel)).perform(levelCode);
        (new IwReadiness(viewableModel)).perform(levelCode);
        HistoryManager historyManager = getObjectsLayer().historyManager;
        (new IwStartingHexes(historyManager)).perform(levelCode);
        (new IwCorePlayerEntities(historyManager.startingPosition)).perform(levelCode);
        (new IwStartingProvinces(historyManager.startingPosition)).perform(levelCode);
        (new IwEventsList(viewableModel, historyManager)).perform(levelCode);
        (new IwCoreFogOfWar(viewableModel)).perform(levelCode);
        applyCampaignDifficulty();
    }


    private void applyCampaignDifficulty() {
        int levelIndex = (int) loadingParameters.get("level_index");
        Difficulty difficulty = CampaignManager.getInstance().getDifficulty(levelIndex);
        getObjectsLayer().aiManager.setDifficulty(difficulty);
    }
}
