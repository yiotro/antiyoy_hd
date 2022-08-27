package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class Relation implements ReusableYio, Encodeable {

    public RelationType type;
    public PlayerEntity entity1;
    public PlayerEntity entity2;
    public int lock;


    @Override
    public void reset() {
        type = null;
        entity1 = null;
        entity2 = null;
        lock = 0;
    }


    public boolean isLocked() {
        return lock > 0;
    }


    public boolean contains(PlayerEntity playerEntity) {
        return playerEntity == entity1 || playerEntity == entity2;
    }


    public PlayerEntity getOpposite(PlayerEntity playerEntity) {
        if (playerEntity == entity1) return entity2;
        if (playerEntity == entity2) return entity1;
        return null;
    }


    public void setType(RelationType type) {
        this.type = type;
    }


    public void setEntity1(PlayerEntity entity1) {
        this.entity1 = entity1;
    }


    public void setEntity2(PlayerEntity entity2) {
        this.entity2 = entity2;
    }


    public void setLock(int lock) {
        this.lock = lock;
    }


    @Override
    public String encode() {
        return type + " " + entity1.color + " " + entity2.color + " " + lock;
    }


    @Override
    public String toString() {
        if (lock == 0) {
            return "[" + Yio.getCapitalizedString("" + type) + "]";
        }
        return "[" +
                Yio.getCapitalizedString("" + type) +
                ", " + lock +
                "]";
    }
}
