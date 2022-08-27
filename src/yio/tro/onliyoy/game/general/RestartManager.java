package yio.tro.onliyoy.game.general;

import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.events.HistoryManager;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.export_import.IwCoreDiplomacy;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class RestartManager {

    GameController gameController;


    public RestartManager(GameController gameController) {
        this.gameController = gameController;
    }


    public void apply() {
        if (checkForCalendar()) return;
        if (checkForCampaign()) return;

        ObjectsLayer objectsLayer = gameController.objectsLayer;
        HistoryManager historyManager = objectsLayer.historyManager;
        historyManager.clearAll();

        // prepare starting position for restart
        CoreModel startingPosition = historyManager.startingPosition;
        ViewableModel viewableModel = objectsLayer.viewableModel;
        String backupCode = doBackupStartingPosition();
        startingPosition.entitiesManager.setBy(viewableModel.entitiesManager);
        startingPosition.setRulesBy(viewableModel);
        startingPosition.diplomacyManager.setEnabled(viewableModel.diplomacyManager.enabled);
        if (viewableModel.diplomacyManager.enabled) {
            (new IwCoreDiplomacy(startingPosition)).perform(backupCode);
        }

        ExportParameters parameters = ExportParameters.getInstance();
        parameters.setInitialLevelSize(gameController.sizeManager.initialLevelSize);
        parameters.setCoreModel(startingPosition);
        parameters.setAiVersionCode(objectsLayer.aiManager.getUpdatedAiVersionCode());
        parameters.setHistoryManager(historyManager);

        String levelCode = objectsLayer.exportManager.perform(parameters);
        (new IwClientInit(gameController.yioGdxGame, getLoadingType())).perform(levelCode);
        checkToUpdateReadiness();
    }


    private String doBackupStartingPosition() {
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        HistoryManager historyManager = objectsLayer.historyManager;
        CoreModel startingPosition = historyManager.startingPosition;
        ExportParameters parameters = ExportParameters.getInstance();
        parameters.setCoreModel(startingPosition);
        return objectsLayer.exportManager.perform(parameters);
    }


    private void checkToUpdateReadiness() {
        if (gameController.gameMode != GameMode.training) return;
        gameController.objectsLayer.viewableModel.readinessManager.update();
    }


    private boolean checkForCampaign() {
        if (gameController.gameMode != GameMode.campaign) return false;
        CampaignManager instance = CampaignManager.getInstance();
        instance.launchCampaignLevel(instance.currentLevelIndex);
        return true;
    }


    private boolean checkForCalendar() {
        if (gameController.gameMode != GameMode.calendar) return false;
        LoadingManager loadingManager = gameController.yioGdxGame.loadingManager;
        LoadingParameters loadingParameters = gameController.objectsLayer.calendarData.loadingParameters;
        loadingManager.startInstantly(LoadingType.calendar, loadingParameters);
        return true;
    }


    private LoadingType getLoadingType() {
        switch (gameController.gameMode) {
            default:
                return LoadingType.training_import;
            case completion_check:
                return LoadingType.completion_check;
            case user_level:
                return LoadingType.user_level;
        }
    }
}
