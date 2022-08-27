package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class IwCoreFogOfWar extends AbstractImportWorker{

    CoreModel coreModel;


    public IwCoreFogOfWar(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "fog";
    }


    @Override
    protected void apply() {
        coreModel.fogOfWarManager.setEnabled(Boolean.valueOf(source));
    }
}
