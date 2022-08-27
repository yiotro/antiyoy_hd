package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.HColor;

public class EventIndicateUndoLetter extends AbstractEvent{

    public HColor senderColor;
    public HColor recipientColor;


    public EventIndicateUndoLetter() {
        senderColor = null;
        recipientColor = null;
    }


    @Override
    public EventType getType() {
        return EventType.indicate_undo_letter;
    }


    @Override
    public boolean isValid() {
        if (senderColor == null) return false;
        if (recipientColor == null) return false;
        return true;
    }


    @Override
    public void applyChange() {
        // no change
        // this event is used only to notify viewable model
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventIndicateUndoLetter eventIndicateUndoLetter = (EventIndicateUndoLetter) srcEvent;
        setSenderColor(eventIndicateUndoLetter.senderColor);
        setRecipientColor(eventIndicateUndoLetter.recipientColor);
    }


    @Override
    protected String getLocalEncodedInfo() {
        return "-"; // this event shouldn't be saved
    }


    @Override
    public boolean isQuick() {
        return true; // always quick
    }


    public void setSenderColor(HColor senderColor) {
        this.senderColor = senderColor;
    }


    public void setRecipientColor(HColor recipientColor) {
        this.recipientColor = recipientColor;
    }
}
