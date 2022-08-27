package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class IwCoreCurrentIds extends AbstractImportWorker{

    CoreModel coreModel;


    public IwCoreCurrentIds(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "core_current_ids";
    }


    @Override
    protected void apply() {
        String[] split = source.split(" ");
        coreModel.currentUnitId = Integer.valueOf(split[0]);

        // previously current province id was loading here
        // but it was overwritten in other places
        // so now it's loading in iw core provinces
//        coreModel.provincesManager.setCurrentId(Integer.valueOf(split[1]));
    }

}
