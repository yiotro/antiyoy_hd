package yio.tro.onliyoy.game.loading.loading_processes;

import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingManager;

public class ProcessEmpty extends AbstractLoadingProcess{

    public ProcessEmpty(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.custom);
        initLevelSize(LevelSize.normal);
    }


    @Override
    public void initGameRules() {
        initRules(RulesType.def);
    }


    @Override
    public void createBasicStuff() {

    }


    @Override
    public void createAdvancedStuff() {

    }
}
