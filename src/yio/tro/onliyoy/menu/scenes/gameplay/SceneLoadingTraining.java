package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;

public class SceneLoadingTraining extends AbstractLoadingSceneYio{

    String levelCode;


    @Override
    protected void onAppear() {
        super.onAppear();
        levelCode = null;
    }


    @Override
    protected void applyAction() {
        (new IwClientInit(yioGdxGame, LoadingType.training_import)).perform(levelCode);
    }


    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }
}
