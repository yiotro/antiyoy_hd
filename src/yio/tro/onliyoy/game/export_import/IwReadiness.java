package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class IwReadiness extends AbstractImportWorker{

    CoreModel coreModel;


    public IwReadiness(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "ready";
    }


    @Override
    protected void apply() {
        coreModel.readinessManager.decode(source);
    }
}
