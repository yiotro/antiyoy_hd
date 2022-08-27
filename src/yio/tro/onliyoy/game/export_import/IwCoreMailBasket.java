package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class IwCoreMailBasket extends AbstractImportWorker {

    CoreModel coreModel;


    public IwCoreMailBasket(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "mail_basket";
    }


    @Override
    protected void apply() {
        coreModel.lettersManager.decode(source);
    }
}
