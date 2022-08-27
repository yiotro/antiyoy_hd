package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Hex;

public class EventSetReady extends AbstractEvent{

    public Hex hex;
    public boolean targetValue;


    public EventSetReady() {
        hex = null;
        targetValue = false;
    }


    @Override
    public EventType getType() {
        return EventType.set_ready;
    }


    @Override
    public boolean isValid() {
        if (hex == null) return false;
        if (!hex.hasUnit()) return false;
        return true;
    }


    @Override
    public void applyChange() {
        coreModel.readinessManager.setReady(hex, targetValue);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventSetReady eventSetReady = (EventSetReady) srcEvent;
        hex = eventSetReady.hex;
        targetValue = eventSetReady.targetValue;
    }


    public void setHex(Hex hex) {
        this.hex = hex;
    }


    public void setTargetValue(boolean targetValue) {
        this.targetValue = targetValue;
    }


    @Override
    protected String getLocalEncodedInfo() {
        return hex.coordinate1 + " " + hex.coordinate2 + " " + targetValue;
    }


    @Override
    public String toString() {
        return "[" +
                getInternalName() + ": " + hex + " " + targetValue +
                "]";
    }
}
