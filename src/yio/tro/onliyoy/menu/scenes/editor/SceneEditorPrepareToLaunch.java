package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.export_import.ExportManager;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.menu.scenes.gameplay.AbstractLoadingSceneYio;

public class SceneEditorPrepareToLaunch extends AbstractLoadingSceneYio{


    private String levelCode;


    @Override
    protected void onAppear() {
        super.onAppear();
        GameController gameController = yioGdxGame.gameController;
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        ExportManager exportManager = objectsLayer.exportManager;
        System.out.println("SceneEditorPrepareToLaunch.onAppear");
    }


    @Override
    protected void applyAction() {
        System.out.println("SceneEditorPrepareToLaunch.applyAction");
    }
}
