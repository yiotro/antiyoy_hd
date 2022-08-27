package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.EventApplyLetter;
import yio.tro.onliyoy.game.core_model.events.EventSendLetter;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.ArrayList;
import java.util.Random;

public abstract class DiplomaticAI {

    CoreModel coreModel;
    Random random;
    ArrayList<Letter> inboxList;
    ArrayList<HColor> adjacentColors;
    Appraiser appraiser;
    AiSmileysGenerator smileysGenerator;


    public DiplomaticAI(CoreModel coreModel) {
        this.coreModel = coreModel;
        random = new Random();
        if (DebugFlags.determinedRandom) {
            random = new Random(0);
        }
        inboxList = new ArrayList<>();
        adjacentColors = new ArrayList<>();
        appraiser = new Appraiser(coreModel);
        smileysGenerator = new AiSmileysGenerator();
    }


    public abstract void apply();


    protected boolean isAlive(PlayerEntity entity) {
        return coreModel.provincesManager.getProvince(entity.color) != null;
    }


    protected Condition getMoneyCondition(PlayerEntity executor, int value) {
        Condition condition = new Condition();
        condition.executorColor = executor.color;
        condition.type = ConditionType.give_money;
        condition.argMoney = value;
        return condition;
    }


    protected Condition getNotificationCondition(RelationType relationType) {
        Condition condition = new Condition();
        condition.executorColor = coreModel.entitiesManager.getCurrentColor();
        condition.type = ConditionType.notification;
        condition.argRelationType = relationType;
        return condition;
    }


    protected Condition getLandsCondition(ArrayList<Hex> hexes) {
        Condition condition = new Condition();
        condition.executorColor = hexes.get(0).color;
        condition.type = ConditionType.give_lands;
        condition.setArgHexes(hexes);
        return condition;
    }


    protected Condition getSimpleRelationsCondition(PlayerEntity recipientEntity, RelationType relationType, int lock) {
        Condition condition = new Condition();
        condition.executorColor = getCurrentEntity().color;
        condition.type = ConditionType.change_relation;
        condition.argRelationType = relationType;
        condition.argLock = lock;
        return condition;
    }


    protected Condition getAdvancedRelationsCondition(PlayerEntity recipientEntity, PlayerEntity targetEntity, RelationType relationType, int lock) {
        Condition condition = new Condition();
        condition.executorColor = recipientEntity.color;
        condition.type = ConditionType.change_relation;
        condition.argColor = targetEntity.color;
        condition.argRelationType = relationType;
        condition.argLock = lock;
        return condition;
    }


    protected void updateInboxList() {
        inboxList.clear();
        HColor currentColor = coreModel.entitiesManager.getCurrentColor();
        for (Letter letter : coreModel.lettersManager.mailBasket) {
            if (letter.recipientColor != currentColor) continue;
            inboxList.add(letter);
        }
    }


    protected Letter startLetter(PlayerEntity sender, PlayerEntity recipient) {
        Letter letter = new Letter();
        letter.setSenderColor(sender.color);
        letter.setRecipientColor(recipient.color);
        return letter;
    }


    protected void sendLetter(Letter letter) {
        EventsManager eventsManager = coreModel.eventsManager;
        EventSendLetter event = eventsManager.factory.createEventSendLetter(letter);
        eventsManager.applyEvent(event);
    }


    protected void agreeToLetter(Letter letter) {
        if (!coreModel.lettersManager.isValid(letter)) return;
        EventsManager eventsManager = coreModel.eventsManager;
        EventApplyLetter event = eventsManager.factory.createEventApplyLetter(letter.id);
        eventsManager.applyEvent(event);
    }


    void updateAdjacentColors() {
        adjacentColors.clear();
        PlayerEntity currentEntity = getCurrentEntity();
        for (Province province : coreModel.provincesManager.provinces) {
            if (province.getColor() != currentEntity.color) continue;
            for (Hex hex : province.getHexes()) {
                for (Hex adjacentHex : hex.adjacentHexes) {
                    if (adjacentHex.isNeutral()) continue;
                    if (adjacentHex.color == hex.color) continue;
                    if (adjacentColors.contains(adjacentHex.color)) continue;
                    adjacentColors.add(adjacentHex.color);
                }
            }
        }
    }


    private PlayerEntity getCurrentEntity() {
        return coreModel.entitiesManager.getCurrentEntity();
    }
}
