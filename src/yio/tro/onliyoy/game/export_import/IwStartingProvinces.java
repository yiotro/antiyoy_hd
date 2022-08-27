package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesBuilder;

public class IwStartingProvinces extends IwCoreProvinces{

    public IwStartingProvinces(CoreModel coreModel) {
        super(coreModel);
    }


    @Override
    protected String getDefaultSectionName() {
        return "starting_provinces";
    }


    @Override
    protected void apply() {
        // need to create empty provinces before restoring local info
        ProvincesBuilder builder = coreModel.provincesManager.builder;
        builder.doGrantPermission();
        builder.apply();
        super.apply();
    }
}
