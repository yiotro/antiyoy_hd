package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;

public class IwCoreRules extends AbstractImportWorker{

    CoreModel coreModel;


    public IwCoreRules(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "rules";
    }


    @Override
    protected void apply() {
        String[] split = source.split(" ");
        if (split.length < 2) return;
        RulesType rulesType = RulesType.valueOf(split[0]);
        int versionCode = Integer.valueOf(split[1]);
        coreModel.setRules(rulesType, versionCode);
    }
}
