package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.export_import.Encodeable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LettersManager implements Encodeable, IEventListener {

    CoreModel coreModel;
    public ArrayList<Letter> mailBasket;
    StringBuilder stringBuilder;
    public int currentId;
    private HashMap<Hex, PieceType> tempMap;


    public LettersManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        mailBasket = new ArrayList<>();
        stringBuilder = new StringBuilder();
        currentId = 0;
        tempMap = new HashMap<>();
    }


    public void addLetterToBasket(Letter letter) {
        letter.setId(currentId);
        currentId++;
        mailBasket.add(letter);
    }


    public void removeLetterFromBasket(int id) {
        Letter letter = getLetter(id);
        if (letter == null) {
            System.out.println("LettersManager.removeLetterFromBasket: problem");
        }
        mailBasket.remove(letter);
    }


    public void setBy(LettersManager source) {
        decode(source.encode());
    }


    public boolean containsLettersToCurrentEntity() {
        HColor currentColor = coreModel.entitiesManager.getCurrentColor();
        for (Letter letter : mailBasket) {
            if (letter.recipientColor == currentColor) return true;
        }
        return false;
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case turn_end:
                onTurnEndEventValidated();
                break;
        }
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case send_letter:
                EventSendLetter eventSendLetter = (EventSendLetter) event;
                checkToApplyNotifications(eventSendLetter);
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 6;
    }


    private void checkToApplyNotifications(EventSendLetter eventSendLetter) {
        Letter letter = eventSendLetter.letter;
        EntitiesManager entitiesManager = coreModel.entitiesManager;
        for (Condition condition : letter.conditions) {
            if (condition.type != ConditionType.notification) continue;
            PlayerEntity sender = entitiesManager.getEntity(letter.senderColor);
            PlayerEntity recipient = entitiesManager.getEntity(letter.recipientColor);
            Relation relation = sender.getRelation(recipient);
            if (relation.isLocked()) continue;
            RelationType targetRelationType = condition.argRelationType;
            if (!entitiesManager.canChangeBeDoneUnilaterally(relation.type, targetRelationType)) continue;
            relation.setType(targetRelationType);
            if (targetRelationType == RelationType.war) {
                relation.setLock(3);
            }
        }
    }


    private void onTurnEndEventValidated() {
        HColor currentColor = coreModel.entitiesManager.getCurrentColor();
        for (int i = mailBasket.size() - 1; i >= 0; i--) {
            Letter letter = mailBasket.get(i);
            if (letter.recipientColor != currentColor) continue;
            mailBasket.remove(letter);
        }
    }


    public Letter getLetter(int id) {
        for (Letter letter : mailBasket) {
            if (letter.id == id) return letter;
        }
        return null;
    }


    @Override
    public String encode() {
        stringBuilder.setLength(0);
        stringBuilder.append(currentId).append(",");
        for (Letter letter : mailBasket) {
            stringBuilder.append(letter.encode()).append(",");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        mailBasket.clear();
        if (source.length() < 5) return;
        String[] split = source.split(",");
        for (int i = 0; i < split.length; i++) {
            String token = split[i];
            if (token.length() == 0) continue;
            if (i == 0) continue; // current id stored here
            Letter letter = new Letter();
            addLetterToBasket(letter);
            letter.decode(coreModel, token);
        }
        currentId = Integer.valueOf(split[0]);
    }


    public boolean isValid(Letter letter) {
        if (!isAlive(letter.senderColor)) return false;
        if (!isAlive(letter.recipientColor)) return false;
        for (Condition condition : letter.conditions) {
            if (!isValid(condition)) return false;
        }
        return true;
    }


    public void applyCondition(Condition condition) {
        Letter letter = condition.letter;
        EventsRefrigerator eventsRefrigerator = coreModel.eventsRefrigerator;
        EventsManager eventsManager = coreModel.eventsManager;
        HColor executorColor = condition.executorColor;
        switch (condition.type) {
            default:
                System.out.println("LettersManager.applyCondition: problem");
                break;
            case give_money:
                eventsManager.applyEvent(eventsRefrigerator.getEventGiveMoney(
                        executorColor,
                        letter.getOppositeColor(executorColor),
                        condition.argMoney
                ));
                break;
            case change_relation:
                eventsManager.applyEvent(eventsRefrigerator.getEventSetRelationSoftly(
                        condition.argRelationType,
                        executorColor,
                        getTargetColorForChangeRelation(condition),
                        condition.argLock
                ));
                break;
            case give_lands:
                HColor targetColor = letter.getOppositeColor(executorColor);
                updateTempMap(condition.argHexes);
                for (Hex hex : condition.argHexes) {
                    eventsManager.applyEvent(eventsRefrigerator.getChangeHexColorEvent(
                            hex,
                            targetColor
                    ));
                }
                applyTempMap();
                break;
            case notification:
                // this condition is applied when letter is sent
                break;
            case smileys:
                break;
        }
    }


    private void applyTempMap() {
        // some pieces can be screwed when transferring lands
        // that's because each 'change hex color' event can spawn a city
        EventsManager eventsManager = coreModel.eventsManager;
        for (Map.Entry<Hex, PieceType> entry : tempMap.entrySet()) {
            Hex hex = entry.getKey();
            PieceType pieceType = entry.getValue();
            coreModel.readinessManager.setReady(hex, false); // to prevent certain bugs
            if (hex.piece == pieceType) continue;
            eventsManager.applyEvent(coreModel.eventsRefrigerator.getDeletePieceEvent(hex));
            eventsManager.applyEvent(coreModel.eventsRefrigerator.getAddPieceEvent(hex, pieceType));
        }
    }


    private void updateTempMap(ArrayList<Hex> hexes) {
        tempMap.clear();
        for (Hex hex : hexes) {
            tempMap.put(hex, hex.piece);
        }
    }


    public boolean isValid(Condition condition) {
        HColor executorColor = condition.executorColor;
        if (!isAlive(executorColor)) return false;
        EntitiesManager entitiesManager = coreModel.entitiesManager;
        switch (condition.type) {
            default:
                return true;
            case give_money:
                int sumMoney = coreModel.provincesManager.getSumMoney(executorColor);
                return sumMoney >= condition.argMoney;
            case give_lands:
                if (condition.argHexes.size() == 0) return false;
                for (Hex hex : condition.argHexes) {
                    if (hex.color != executorColor) return false;
                    if (hex.getProvince() == null) return false;
                }
                return true;
            case change_relation:
                HColor targetColor = getTargetColorForChangeRelation(condition);
                if (!isAlive(targetColor)) return false;
                PlayerEntity executor = entitiesManager.getEntity(executorColor);
                if (executor == null) return false;
                PlayerEntity target = entitiesManager.getEntity(targetColor);
                if (target == null) return false;
                if (executor == target) return false;
                Relation relation = executor.getRelation(target);
                if (relation.type == condition.argRelationType) return false;
                if (relation.isLocked()) return false;
                if (condition.argColor != null && !coreModel.entitiesManager.canChangeBeDoneUnilaterally(relation.type, condition.argRelationType)) return false;
                return true;
            case notification:
                return true;
        }
    }


    private boolean willSellingHexesKillExecutor(HColor executorColor, ArrayList<Hex> argHexes) {
        for (Hex hex : coreModel.hexes) {
            hex.flag = false;
        }
        for (Hex hex : argHexes) {
            hex.flag = true;
        }
        for (Hex hex : coreModel.hexes) {
            if (hex.color != executorColor) continue;
            if (hex.flag) continue;
            for (Hex adjacentHex : hex.adjacentHexes) {
                if (adjacentHex.color != executorColor) continue;
                if (adjacentHex.flag) continue;
                return true;
            }
        }
        return false;
    }


    private HColor getTargetColorForChangeRelation(Condition condition) {
        if (condition.type != ConditionType.change_relation) {
            System.out.println("LettersManager.getTargetColorForChangeRelation");
            return null;
        }
        if (condition.argColor != null) return condition.argColor;
        return condition.letter.getOppositeColor(condition.executorColor);
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("LettersManager.showInConsole: " + coreModel);
        System.out.println("currentId = " + currentId);
        for (Letter letter : mailBasket) {
            System.out.println("- " + letter);
        }
    }


    private boolean isAlive(HColor color) {
        return coreModel.provincesManager.getProvince(color) != null;
    }
}
