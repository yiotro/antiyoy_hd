package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;

public class IwClientInit extends AbstractImportWorker{

    YioGdxGame yioGdxGame;
    LoadingType loadingType;


    public IwClientInit(YioGdxGame yioGdxGame, LoadingType loadingType) {
        this.yioGdxGame = yioGdxGame;
        this.loadingType = loadingType;
    }


    @Override
    protected String getDefaultSectionName() {
        return "client_init";
    }


    @Override
    protected void apply() {
        String[] split = source.split(",");
        LevelSize levelSize = LevelSize.valueOf(split[0]);
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.add("level_code", levelCode);
        loadingParameters.add("level_size", "" + levelSize);
        loadingParameters.add("ai_version_code", "" + Integer.valueOf(split[1])); // why not validate it here
        yioGdxGame.loadingManager.startInstantly(loadingType, loadingParameters);
    }
}
