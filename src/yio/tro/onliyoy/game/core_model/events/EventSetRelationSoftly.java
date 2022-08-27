package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.Relation;
import yio.tro.onliyoy.game.core_model.RelationType;

public class EventSetRelationSoftly extends AbstractEvent{

    public RelationType relationType;
    public HColor color1;
    public HColor color2;
    public int lock;


    public EventSetRelationSoftly() {
        relationType = null;
        color1 = null;
        color2 = null;
        lock = 0;
    }


    @Override
    public EventType getType() {
        return EventType.set_relation_softly;
    }


    @Override
    public boolean isValid() {
        if (relationType == null) return false;
        if (color1 == null) return false;
        if (color2 == null) return false;
        if (lock < 0) return false;
        PlayerEntity entity1 = coreModel.entitiesManager.getEntity(color1);
        if (entity1 == null) return false;
        PlayerEntity entity2 = coreModel.entitiesManager.getEntity(color2);
        if (entity2 == null) return false;
        return true;
    }


    @Override
    public void applyChange() {
        PlayerEntity entity1 = coreModel.entitiesManager.getEntity(color1);
        PlayerEntity entity2 = coreModel.entitiesManager.getEntity(color2);
        Relation relation = entity1.getRelation(entity2);
        relation.setType(relationType);
        relation.setLock(lock);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventSetRelationSoftly eventSetRelationSoftly = (EventSetRelationSoftly) srcEvent;
        setRelationType(eventSetRelationSoftly.relationType);
        setColor1(eventSetRelationSoftly.color1);
        setColor2(eventSetRelationSoftly.color2);
        setLock(eventSetRelationSoftly.lock);
    }


    @Override
    protected String getLocalEncodedInfo() {
        return relationType + " " + color1 + " " + color2 + " " + lock;
    }


    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }


    public void setColor1(HColor color1) {
        this.color1 = color1;
    }


    public void setColor2(HColor color2) {
        this.color2 = color2;
    }


    public void setLock(int lock) {
        this.lock = lock;
    }
}
