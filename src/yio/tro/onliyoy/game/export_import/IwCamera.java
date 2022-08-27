package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.general.GameController;

public class IwCamera extends AbstractImportWorker{

    GameController gameController;


    public IwCamera(GameController gameController) {
        this.gameController = gameController;
    }


    @Override
    protected String getDefaultSectionName() {
        return "camera";
    }


    @Override
    protected void apply() {
        gameController.cameraController.loadFromString(source);
    }
}
