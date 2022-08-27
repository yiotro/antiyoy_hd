package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public abstract class AbstractEvent implements ReusableYio {

    protected CoreModel coreModel;
    private boolean quick;
    private boolean reusable;
    public PlayerEntity author;


    public AbstractEvent() {
        coreModel = null;
        quick = false;
        reusable = false;
        author = null;
    }


    @Override
    public void reset() {
        if (!reusable) {
            System.out.println("AbstractEvent.reset: problem");
        }
        quick = false;
        author = null;
    }


    public abstract EventType getType();


    public abstract boolean isValid();


    public abstract void applyChange();


    public abstract void copyFrom(AbstractEvent srcEvent);


    protected abstract String getLocalEncodedInfo();


    public String encode() {
        return EventKeys.convertTypeToKey(getType()) + " " + getLocalEncodedInfo();
    }


    public boolean isQuick() {
        return quick;
    }


    public void setQuick(boolean quick) {
        this.quick = quick;
    }


    public boolean isNotable() {
        return !isQuick() && !reusable;
    }


    public boolean isReusable() {
        return reusable;
    }


    public void setReusable(boolean reusable) {
        this.reusable = reusable;
    }


    public void setCoreModel(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    protected String getInternalName() {
        if (coreModel.name.length() == 0) {
            return getClass().getSimpleName();
        }
        return getClass().getSimpleName() + "(" + coreModel.name + ")";
    }


    public void setAuthor(PlayerEntity author) {
        this.author = author;
    }


    public CoreModel getCoreModel() {
        return coreModel;
    }


    public boolean canBeUndone() {
        return Core.canBeUndone(getType());
    }


    @Override
    public String toString() {
        return "[" + getInternalName() + "]";
    }
}
