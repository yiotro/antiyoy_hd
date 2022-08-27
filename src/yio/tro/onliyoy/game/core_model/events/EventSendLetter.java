package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Condition;
import yio.tro.onliyoy.game.core_model.Letter;

public class EventSendLetter extends AbstractEvent{

    public Letter letter;


    public EventSendLetter() {
        letter = null;
    }


    @Override
    public EventType getType() {
        return EventType.send_letter;
    }


    @Override
    public boolean isValid() {
        if (letter == null) return false;
        if (letter.conditions.size() == 0) return false;
        return true;
    }


    @Override
    public void applyChange() {
        coreModel.lettersManager.addLetterToBasket(letter);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        Letter srcLetter = ((EventSendLetter) srcEvent).letter;
        if (letter != null) {
            letter.reset();
        } else {
            letter = new Letter();
        }
        letter.setSenderColor(srcLetter.senderColor);
        letter.setRecipientColor(srcLetter.recipientColor);
        letter.setId(srcLetter.id);
        for (Condition srcCondition : srcLetter.conditions) {
            Condition condition = new Condition();
            condition.setBy(coreModel, srcCondition);
            letter.addCondition(condition);
        }
    }


    @Override
    protected String getLocalEncodedInfo() {
        return letter.encode();
    }


    public void setLetter(Letter letter) {
        this.letter = letter;
    }


    @Override
    public String toString() {
        return "[EventSendLetter: " + letter + "]";
    }
}
