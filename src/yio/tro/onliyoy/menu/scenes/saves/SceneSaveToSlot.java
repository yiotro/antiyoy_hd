package yio.tro.onliyoy.menu.scenes.saves;

import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneSaveToSlot extends AbstractSavesManagementScene{

    @Override
    protected String getTitleKey() {
        return "save_slots";
    }


    @Override
    protected boolean isInReadMode() {
        return false;
    }


    @Override
    protected SaveType getCurrentSaveType() {
        return SaveType.normal;
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
        parameters.setAiVersionCode(objectsLayer.aiManager.getUpdatedAiVersionCode());
        parameters.setPauseName(getViewableModel().pauseName);
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
        return getOpenSceneReaction(Scenes.defaultPauseMenu);
    }
}
