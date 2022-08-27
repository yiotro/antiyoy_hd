package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class IwCorePlayerEntities extends AbstractImportWorker {

    CoreModel coreModel;
    boolean deadByDefault;


    public IwCorePlayerEntities(CoreModel coreModel) {
        this.coreModel = coreModel;
        deadByDefault = false;
    }


    public IwCorePlayerEntities(CoreModel coreModel, boolean deadByDefault) {
        this.coreModel = coreModel;
        this.deadByDefault = deadByDefault;
    }


    @Override
    protected String getDefaultSectionName() {
        return "player_entities";
    }


    @Override
    protected void apply() {
        coreModel.entitiesManager.decode(source, deadByDefault);
    }
}
