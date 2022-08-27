package yio.tro.onliyoy.menu.scenes.saves;

import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneSaveCampaign extends AbstractSavesManagementScene{

    @Override
    protected String getTitleKey() {
        String prefix = languagesManager.getString("save_slots");
        String campaign = languagesManager.getString("campaign").toLowerCase();
        return prefix + " (" + campaign + ")";
    }


    @Override
    protected boolean isInReadMode() {
        return false;
    }


    @Override
    protected SaveType getCurrentSaveType() {
        return SaveType.campaign;
    }


    @Override
    protected void onItemClicked(String key) {
        ObjectsLayer objectsLayer = getObjectsLayer();
        GameController gameController = getGameController();
        ExportParameters parameters = ExportParameters.getInstance();
        parameters.setCameraCode(gameController.cameraController.encode());
        parameters.setInitialLevelSize(gameController.sizeManager.initialLevelSize);
        parameters.setCoreModel(objectsLayer.viewableModel);
        parameters.setHistoryManager(objectsLayer.historyManager);
        parameters.setAiVersionCode(-1);
        parameters.setCampaignLevelIndex(CampaignManager.getInstance().currentLevelIndex);
        String exportedLevelCode = objectsLayer.exportManager.perform(parameters);

        if (key.equals("create")) {
            showKeyboardForNewSave(exportedLevelCode);
            return;
        }

        getSavesManager().rewriteLevelCode(key, exportedLevelCode);
        getCloseReaction().perform(menuControllerYio);
    }


    @Override
    protected Reaction getCloseReaction() {
        return getOpenSceneReaction(Scenes.campaignPauseMenu);
    }
}
