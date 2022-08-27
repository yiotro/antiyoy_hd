package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class EventTurnEnd extends AbstractEvent{

    public long targetEndTime;
    public HColor currentColor;


    public EventTurnEnd() {
        targetEndTime = 0;
        currentColor = null;
    }


    @Override
    public EventType getType() {
        return EventType.turn_end;
    }


    @Override
    public boolean isValid() {
        return true;
    }


    @Override
    public void applyChange() {
        coreModel.turnsManager.doSwitchTurnIndex();
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventTurnEnd eventTurnEnd = (EventTurnEnd) srcEvent;
        currentColor = eventTurnEnd.currentColor;
        targetEndTime = eventTurnEnd.targetEndTime;
    }


    @Override
    protected String getLocalEncodedInfo() {
        return getTeTimeString() + " " + currentColor;
    }


    private String getTeTimeString() {
        if (targetEndTime > 0) return "" + targetEndTime;
        return "-";
    }


    public void setTargetEndTime(long targetEndTime) {
        this.targetEndTime = targetEndTime;
    }


    public void setCurrentColor(HColor currentColor) {
        this.currentColor = currentColor;
    }


    @Override
    public String toString() {
        String colorString = "";
        if (currentColor != null) {
            colorString = "" + currentColor;
        }
        String timeString = "";
        if (targetEndTime > 0) {
            timeString = targetEndTime + " ";
        }
        return "[" +
                getInternalName() + ": " + timeString + colorString +
                "]";
    }
}

