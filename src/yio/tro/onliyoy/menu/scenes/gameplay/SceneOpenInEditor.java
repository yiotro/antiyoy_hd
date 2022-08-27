package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.export_import.ExportManager;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.BackgroundYio;

public class SceneOpenInEditor extends AbstractLoadingSceneYio{

    String levelCode;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.red;
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        ExportParameters exportParameters = new ExportParameters();
        exportParameters.setInitialLevelSize(getGameController().sizeManager.initialLevelSize);
        exportParameters.setCoreModel(getViewableModel());
        exportParameters.setCameraCode(getGameController().cameraController.encode());
        levelCode = (new ExportManager()).perform(exportParameters);
    }


    @Override
    protected void applyAction() {
        (new IwClientInit(yioGdxGame, LoadingType.editor_import)).perform(levelCode);
    }
}
