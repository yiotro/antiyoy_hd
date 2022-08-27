package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventSetRelationSoftly;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.export_import.Encodeable;

public class DiplomacyManager implements IEventListener, Encodeable {

    CoreModel coreModel;
    public boolean enabled;
    StringBuilder stringBuilder;


    public DiplomacyManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        enabled = false;
        stringBuilder = new StringBuilder();
    }


    public void setBy(DiplomacyManager srcDiplomacyManager) {
        enabled = srcDiplomacyManager.enabled;
        if (!enabled) return;
        if (getEntitiesManager().entities == null) return;
        if (srcDiplomacyManager.getEntitiesManager().entities == null) return;
        for (PlayerEntity srcEntity : srcDiplomacyManager.getEntitiesManager().entities) {
            for (Relation srcRelation : srcEntity.relations) {
                if (srcRelation.entity1 != srcEntity) continue;
                Relation realRelation = findRelation(srcRelation.entity1.color, srcRelation.entity2.color);
                if (realRelation == null) continue;
                realRelation.setType(srcRelation.type);
                realRelation.setLock(srcRelation.lock);
            }
        }
    }


    private Relation findRelation(HColor color1, HColor color2) {
        PlayerEntity entity1 = getEntitiesManager().getEntity(color1);
        if (entity1 == null) return null;
        PlayerEntity entity2 = getEntitiesManager().getEntity(color2);
        if (entity2 == null) return null;
        return entity1.getRelation(entity2);
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            case turn_end:
                onTurnEnded();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 6;
    }


    private void onTurnEnded() {
        decreaseLocks();
        checkToApplyAlliances();
    }


    private void checkToApplyAlliances() {
        for (PlayerEntity entity : getEntitiesManager().entities) {
            for (Relation relation : entity.relations) {
                if (relation.entity1 != entity) continue;
                if (relation.type != RelationType.war) continue;
                applyAlliances(relation.entity1, relation.entity2, relation.lock);
                applyAlliances(relation.entity2, relation.entity1, relation.lock);
            }
        }
    }


    private void applyAlliances(PlayerEntity playerEntity, PlayerEntity enemyEntity, int lock) {
        for (Relation relation : playerEntity.relations) {
            if (relation.type != RelationType.alliance) continue;
            PlayerEntity allianceEntity = relation.getOpposite(playerEntity);
            Relation otherRelation = allianceEntity.getRelation(enemyEntity);
            if (otherRelation.type == RelationType.war) continue;
            if (otherRelation.isLocked()) continue;
            EventSetRelationSoftly event = coreModel.eventsRefrigerator.getEventSetRelationSoftly(
                    RelationType.war,
                    enemyEntity.color,
                    allianceEntity.color,
                    lock
            );
            coreModel.eventsManager.applyEvent(event);
        }
    }


    public boolean isAttackAllowed(PlayerEntity attacker, Hex hex) {
        if (!enabled) return true;
        if (attacker.color == hex.color) return true;
        if (hex.getProvince() == null) return true;
        PlayerEntity victim = coreModel.entitiesManager.getEntity(hex.color);
        if (victim == null) return true;
        return victim.getRelation(attacker).type == RelationType.war;
    }


    private void decreaseLocks() {
        PlayerEntity currentEntity = getEntitiesManager().getCurrentEntity();
        for (Relation relation : currentEntity.relations) {
            if (relation.entity1 != currentEntity) continue;
            if (relation.lock <= 0) continue;
            if (relation.lock == 999) continue;
            relation.lock--;
        }
    }


    private EntitiesManager getEntitiesManager() {
        return coreModel.entitiesManager;
    }


    public void resetRelations() {
        // I should be careful with this method
        for (PlayerEntity entity : getEntitiesManager().entities) {
            for (Relation relation : entity.relations) {
                relation.setType(RelationType.neutral);
                relation.setLock(0);
            }
        }
    }


    @Override
    public String encode() {
        if (!enabled) {
            return "off";
        }
        String code = encodeRelations();
        if (code.length() == 0) {
            code = "-";
        }
        return code;
    }


    private String encodeRelations() {
        stringBuilder.setLength(0);
        for (PlayerEntity entity : getEntitiesManager().entities) {
            for (Relation relation : entity.relations) {
                if (relation.entity1 != entity) continue;
                if (relation.type == RelationType.neutral) continue;
                stringBuilder.append(relation.encode()).append(",");
            }
        }
        return stringBuilder.toString();
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
