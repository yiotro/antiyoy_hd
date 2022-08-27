package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class IwCoreTurn extends AbstractImportWorker{

    CoreModel coreModel;


    public IwCoreTurn(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "turn";
    }


    @Override
    protected void apply() {
        coreModel.turnsManager.decode(source);
    }
}
