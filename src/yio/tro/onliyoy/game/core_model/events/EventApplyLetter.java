package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Condition;
import yio.tro.onliyoy.game.core_model.ConditionType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Letter;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class EventApplyLetter extends AbstractEvent{

    public int id;


    public EventApplyLetter() {
        id = -1;
    }


    @Override
    public EventType getType() {
        return EventType.apply_letter;
    }


    @Override
    public boolean isValid() {
        if (id == -1) return false;
        Letter letter = coreModel.lettersManager.getLetter(id);
        if (letter == null) return false;
        if (coreModel.entitiesManager.getCurrentColor() != letter.recipientColor) return false;
        return coreModel.lettersManager.isValid(letter);
    }


    @Override
    public void applyChange() {
        Letter letter = coreModel.lettersManager.getLetter(id);
        for (Condition condition : letter.conditions) {
            if (condition.type == ConditionType.give_lands) continue;
            coreModel.lettersManager.applyCondition(condition);
        }
        for (Condition condition : letter.conditions) {
            if (condition.type != ConditionType.give_lands) continue;
            coreModel.lettersManager.applyCondition(condition);
        }
        coreModel.cityManager.doFixProvincesWithoutCities();
        coreModel.lettersManager.removeLetterFromBasket(id);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventApplyLetter eventApplyLetter = (EventApplyLetter) srcEvent;
        id = eventApplyLetter.id;
    }


    @Override
    protected String getLocalEncodedInfo() {
        return "" + id;
    }


    public void setId(int id) {
        this.id = id;
    }
}
