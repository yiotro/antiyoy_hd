package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.core_model.ai.AiManager;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.loading.LoadingParameters;

public abstract class AbstractLoadingProcess {


    YioGdxGame yioGdxGame;
    GameController gameController;
    LoadingManager loadingManager;
    LoadingParameters loadingParameters;


    public AbstractLoadingProcess(LoadingManager loadingManager) {
        this.loadingManager = loadingManager;
        yioGdxGame = loadingManager.yioGdxGame;
        gameController = yioGdxGame.gameController;

        loadingParameters = null;
    }


    public abstract void prepare();


    public abstract void initGameRules();


    protected LevelSize getLevelSizeFromParameters() {
        return LevelSize.valueOf(loadingParameters.get("level_size").toString());
    }


    protected RulesType getRulesTypeFromParameters() {
        return RulesType.valueOf(loadingParameters.get("rules_type").toString());
    }


    protected boolean getFogOfWarFromParameters() {
        if (!loadingParameters.contains("fog_of_war")) return false;
        return Boolean.valueOf(loadingParameters.get("fog_of_war").toString());
    }


    protected void loadAiVersionCodeFromParameters() {
        AiManager aiManager = getObjectsLayer().aiManager;
        Object object = loadingParameters.get("ai_version_code");
        if (object == null) {
            aiManager.setVersionCode(-1);
            return;
        }
        aiManager.setVersionCode(Integer.valueOf(object.toString()));
    }


    protected String getLevelCodeFromParameters() {
        return (String) loadingParameters.get("level_code");
    }


    public abstract void createBasicStuff();


    public abstract void createAdvancedStuff();


    public void setLoadingParameters(LoadingParameters loadingParameters) {
        this.loadingParameters = loadingParameters;
    }


    public void initGameMode(GameMode gameMode) {
        gameController.setGameMode(gameMode);
    }


    public void initLevelSize(LevelSize levelSize) {
        gameController.sizeManager.initLevelSize(levelSize);
        gameController.cameraController.onLevelBoundsSet();
    }


    public void initRules(RulesType rulesType) {
        gameController.objectsLayer.viewableModel.setRules(rulesType, -1);
    }


    protected ObjectsLayer getObjectsLayer() {
        return getGameController().objectsLayer;
    }


    protected GameController getGameController() {
        return loadingManager.yioGdxGame.gameController;
    }
}
