package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Letter;

public class EventDeclineLetter extends AbstractEvent{

    public int id;


    public EventDeclineLetter() {
        id = -1;
    }


    @Override
    public EventType getType() {
        return EventType.decline_letter;
    }


    @Override
    public boolean isValid() {
        if (id == -1) return false;
        Letter letter = coreModel.lettersManager.getLetter(id);
        if (letter == null) return false;
        if (coreModel.entitiesManager.getCurrentColor() != letter.recipientColor) return false;
        return true;
    }


    @Override
    public void applyChange() {
        coreModel.lettersManager.removeLetterFromBasket(id);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventDeclineLetter eventDeclineLetter = (EventDeclineLetter) srcEvent;
        id = eventDeclineLetter.id;
    }


    @Override
    protected String getLocalEncodedInfo() {
        return "" + id;
    }


    public void setId(int id) {
        this.id = id;
    }
}
