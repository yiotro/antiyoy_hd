package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.campaign.CampaignLevels;
import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;

public class IwCampaignInit extends AbstractImportWorker{

    YioGdxGame yioGdxGame;
    int levelIndex;


    public IwCampaignInit(YioGdxGame yioGdxGame, int levelIndex) {
        this.yioGdxGame = yioGdxGame;
        this.levelIndex = levelIndex;
    }


    @Override
    protected String getDefaultSectionName() {
        return "client_init";
    }


    @Override
    protected void apply() {
        String[] split = source.split(",");
        LevelSize levelSize = LevelSize.valueOf(split[0]);
        if (levelIndex == -1) {
            // when loading from save slot
            String campaignSection = getSection(levelCode, "campaign");
            levelIndex = Integer.valueOf(campaignSection);
            CampaignManager.getInstance().currentLevelIndex = levelIndex;
        }
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.add("level_size", levelSize);
        loadingParameters.add("level_index", levelIndex);
        loadingParameters.add("level_code", levelCode);
        yioGdxGame.loadingManager.startInstantly(LoadingType.campaign, loadingParameters);
    }
}
