package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesManager;
import yio.tro.onliyoy.game.debug.DebugFlags;

public class EventsFactory {

    EventsManager eventsManager;


    public EventsFactory(EventsManager eventsManager) {
        this.eventsManager = eventsManager;
    }


    public AbstractEvent createCopy(AbstractEvent srcEvent) {
        AbstractEvent event = createEvent(srcEvent.getType());
        event.setQuick(srcEvent.isQuick());
        event.setReusable(srcEvent.isReusable());
        event.copyFrom(srcEvent);
        return event;
    }


    public AbstractEvent createEvent(EventType eventType) {
        // this method will be used in decoding
        AbstractEvent event = null;
        switch (eventType) {
            default:
                return null;
            case piece_add:
                event = new EventPieceAdd();
                break;
            case unit_move:
                event = new EventUnitMove();
                break;
            case piece_delete:
                event = new EventPieceDelete();
                break;
            case turn_end:
                event = new EventTurnEnd();
                break;
            case hex_change_color:
                event = new EventHexChangeColor();
                break;
            case set_money:
                event = new EventSetMoney();
                break;
            case piece_build:
                event = new EventPieceBuild();
                break;
            case graph_created:
                event = new EventGraphCreated();
                break;
            case match_started:
                event = new EventMatchStarted();
                break;
            case merge:
                event = new EventMerge();
                break;
            case merge_on_build:
                event = new EventMergeOnBuild();
                break;
            case set_relation_softly:
                event = new EventSetRelationSoftly();
                break;
            case send_letter:
                event = new EventSendLetter();
                break;
            case indicate_undo_letter:
                event = new EventIndicateUndoLetter();
                break;
            case decline_letter:
                event = new EventDeclineLetter();
                break;
            case apply_letter:
                event = new EventApplyLetter();
                break;
            case give_money:
                event = new EventGiveMoney();
                break;
            case subtract_money:
                event = new EventSubtractMoney();
                break;
            case set_ready:
                event = new EventSetReady();
                break;
        }
        if (event != null) {
            event.setCoreModel(eventsManager.coreModel);
        }
        return event;
    }


    public EventSetReady createEventSetReady(Hex hex, boolean value) {
        EventSetReady event = (EventSetReady) createEvent(EventType.set_ready);
        event.setHex(hex);
        event.setTargetValue(value);
        return event;
    }


    public EventSubtractMoney createEventSubtractMoney(Hex hex, int amount) {
        EventSubtractMoney event = (EventSubtractMoney) createEvent(EventType.subtract_money);
        event.setHex(hex);
        event.setAmount(amount);
        return event;
    }


    public EventGiveMoney createEventGiveMoney(HColor executorColor, HColor targetColor, int amount) {
        EventGiveMoney event = (EventGiveMoney) createEvent(EventType.give_money);
        event.setExecutorColor(executorColor);
        event.setTargetColor(targetColor);
        event.setAmount(amount);
        return event;
    }


    public EventApplyLetter createEventApplyLetter(int id) {
        EventApplyLetter event = (EventApplyLetter) createEvent(EventType.apply_letter);
        event.setId(id);
        return event;
    }


    public EventDeclineLetter createEventDeclineLetter(int id) {
        EventDeclineLetter event = (EventDeclineLetter) createEvent(EventType.decline_letter);
        event.setId(id);
        return event;
    }


    public EventIndicateUndoLetter createEventIndicateUndoLetter(Letter letter) {
        EventIndicateUndoLetter event = (EventIndicateUndoLetter) createEvent(EventType.indicate_undo_letter);
        event.setSenderColor(letter.senderColor);
        event.setRecipientColor(letter.recipientColor);
        return event;
    }


    public EventSendLetter createEventSendLetter(Letter letter) {
        EventSendLetter event = (EventSendLetter) createEvent(EventType.send_letter);
        event.setLetter(letter);
        return event;
    }


    public EventSetRelationSoftly createEventSetRelationSoftly(RelationType relationType, HColor color1, HColor color2, int lock) {
        EventSetRelationSoftly event = (EventSetRelationSoftly) createEvent(EventType.set_relation_softly);
        event.setRelationType(relationType);
        event.setColor1(color1);
        event.setColor2(color2);
        event.setLock(lock);
        return event;
    }


    public EventMergeOnBuild createEventMergeOnBuild(Hex hex, PieceType pieceType, int unitId, int provinceId) {
        EventMergeOnBuild event = (EventMergeOnBuild) createEvent(EventType.merge_on_build);
        event.setHex(hex);
        event.setPieceType(pieceType);
        event.setUnitId(unitId);
        event.setProvinceId(provinceId);
        return event;
    }


    public EventMerge createMergeEvent(Hex startHex, Hex targetHex, int unitId) {
        EventMerge event = (EventMerge) createEvent(EventType.merge);
        event.setStartHex(startHex);
        event.setTargetHex(targetHex);
        event.setUnitId(unitId);
        return event;
    }


    public EventPieceBuild createBuildPieceEvent(Hex hex, PieceType pieceType, int unitId, int provinceId) {
        EventPieceBuild event = (EventPieceBuild) createEvent(EventType.piece_build);
        event.setHex(hex);
        event.setPieceType(pieceType);
        event.setUnitId(unitId);
        event.setProvinceId(provinceId);
        return event;
    }


    public EventSetMoney createSetMoneyEvent(Province province, int money) {
        EventSetMoney event = (EventSetMoney) createEvent(EventType.set_money);
        event.setProvinceId(province.getId());
        event.setMoney(money);
        return event;
    }


    public EventHexChangeColor createChangeHexColorEvent(Hex hex, HColor color) {
        EventHexChangeColor event = (EventHexChangeColor) createEvent(EventType.hex_change_color);
        event.setHex(hex);
        event.setColor(color);
        return event;
    }


    public EventTurnEnd createEndTurnEvent() {
        EventTurnEnd event = (EventTurnEnd) createEvent(EventType.turn_end);
        event.setCurrentColor(eventsManager.coreModel.entitiesManager.getCurrentColor());
        return event;
    }


    public EventPieceAdd createAddPieceEvent(Hex hex, PieceType pieceType) {
        if (Core.isUnit(pieceType)) {
            System.out.println("EventsFactory.createAddPieceEvent: unit id not specified");
        }
        return createAddPieceEvent(hex, pieceType, -1);
    }


    public EventPieceAdd createAddPieceEvent(Hex hex, PieceType pieceType, int unitId) {
        EventPieceAdd event = (EventPieceAdd) createEvent(EventType.piece_add);
        event.setHex(hex);
        event.setPieceType(pieceType);
        event.setUnitId(unitId);
        return event;
    }


    public EventUnitMove createMoveUnitEvent(Hex start, Hex finish) {
        EventUnitMove event = (EventUnitMove) createEvent(EventType.unit_move);
        event.setStart(start);
        event.setFinish(finish);
        return event;
    }


    public EventPieceDelete createDeletePieceEvent(Hex hex) {
        EventPieceDelete event = (EventPieceDelete) createEvent(EventType.piece_delete);
        event.setHex(hex);
        return event;
    }
}
