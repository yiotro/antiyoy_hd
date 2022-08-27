package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.general.LevelSize;

public class IwExtractLevelSize extends AbstractImportWorker{


    public LevelSize levelSize;


    @Override
    protected String getDefaultSectionName() {
        return "client_init";
    }


    @Override
    protected void apply() {
        String[] split = source.split(",");
        levelSize = LevelSize.valueOf(split[0]);
    }
}
