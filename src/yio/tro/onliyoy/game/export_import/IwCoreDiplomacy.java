package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.*;

public class IwCoreDiplomacy extends AbstractImportWorker{

    CoreModel coreModel;


    public IwCoreDiplomacy(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "diplomacy";
    }


    @Override
    protected void apply() {
        DiplomacyManager diplomacyManager = coreModel.diplomacyManager;
        diplomacyManager.setEnabled(false);
        if (source.length() == 0) return;
        if (source.equals("off")) return;
        diplomacyManager.setEnabled(true);
        EntitiesManager entitiesManager = coreModel.entitiesManager;
        for (String token : source.split(",")) {
            String[] split = token.split(" ");
            if (split.length < 4) continue;
            RelationType relationType = RelationType.valueOf(split[0]);
            HColor color1 = HColor.valueOf(split[1]);
            HColor color2 = HColor.valueOf(split[2]);
            int lock = Integer.valueOf(split[3]);
            PlayerEntity entity1 = entitiesManager.getEntity(color1);
            if (entity1 == null) continue;
            PlayerEntity entity2 = entitiesManager.getEntity(color2);
            if (entity2 == null) continue;
            Relation relation = entity1.getRelation(entity2);
            if (relation == null) continue;
            relation.setType(relationType);
            relation.setLock(lock);
        }
    }
}
