package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.events.*;

public class IwEventsList extends AbstractImportWorker {

    CoreModel coreModel;
    HistoryManager historyManager;


    public IwEventsList(CoreModel coreModel, HistoryManager historyManager) {
        this.coreModel = coreModel;
        this.historyManager = historyManager;
    }


    @Override
    protected String getDefaultSectionName() {
        return "events_list";
    }


    @Override
    protected void apply() {
        EventsFactory factory = coreModel.eventsManager.factory;
        for (String token : source.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            String eventKey = split[0];
            EventType eventType = EventKeys.convertKeyToType(eventKey);
            AbstractEvent event = factory.createEvent(eventType);
            restoreSingleEvent(event, split);
            historyManager.eventsList.add(event);
        }
    }


    public void restoreSingleEvent(AbstractEvent event, String[] split) {
        // important: split also contains event type at the beginning
        switch (event.getType()) {
            default:
                System.out.println("IwEventsList.restoreSingleEvent: need to add decoder here");
                break;
            case match_started:
            case graph_created:
            case indicate_undo_letter:
            case give_money:
                System.out.println("IwEventsList.restoreSingleEvent: " + event.getType() + " shouldn't be saved");
                break;
            case piece_add:
                restoreEventPieceAdd(event, split);
                break;
            case piece_delete:
                restoreEventPieceDelete(event, split);
                break;
            case unit_move:
                restoreEventUnitMove(event, split);
                break;
            case turn_end:
                restoreEventTurnEnd(event, split);
                break;
            case hex_change_color:
                restoreEventHexChangeColor(event, split);
                break;
            case set_money:
                restoreEventSetMoney(event, split);
                break;
            case piece_build:
                restoreEventPieceBuild(event, split);
                break;
            case merge:
                restoreEventMerge(event, split);
                break;
            case merge_on_build:
                restoreEventMergeOnBuild(event, split);
                break;
            case set_relation_softly:
                restoreEventSetRelation(event, split);
                break;
            case send_letter:
                restoreEventSendLetter(event, split);
                break;
            case decline_letter:
                restoreEventDeclineLetter(event, split);
                break;
            case apply_letter:
                restoreEventApplyLetter(event, split);
                break;
            case subtract_money:
                restoreEventSubtractMoney(event, split);
                break;
            case set_ready:
                restoreEventSetReady(event, split);
                break;
        }
    }


    private void restoreEventSetReady(AbstractEvent event, String[] split) {
        EventSetReady eventSetReady = (EventSetReady) event;
        int c1 = Integer.valueOf(split[1]);
        int c2 = Integer.valueOf(split[2]);
        Hex hex = coreModel.getHex(c1, c2);
        boolean value = Boolean.valueOf(split[3]);
        eventSetReady.setHex(hex);
        eventSetReady.setTargetValue(value);
    }


    private void restoreEventSubtractMoney(AbstractEvent event, String[] split) {
        EventSubtractMoney eventSubtractMoney = (EventSubtractMoney) event;
        int c1 = Integer.valueOf(split[1]);
        int c2 = Integer.valueOf(split[2]);
        Hex hex = coreModel.getHex(c1, c2);
        int amount = Integer.valueOf(split[3]);
        eventSubtractMoney.setHex(hex);
        eventSubtractMoney.setAmount(amount);
    }


    private void restoreEventTurnEnd(AbstractEvent event, String[] split) {
        if (split.length < 3) return;
        EventTurnEnd eventTurnEnd = (EventTurnEnd) event;
        long time = 0;
        if (split[1].length() > 5) {
            time = Long.valueOf(split[1]);
        }
        HColor currentColor = null;
        if (!split[2].equals("null")) {
            currentColor = HColor.valueOf(split[2]);
        }
        eventTurnEnd.setTargetEndTime(time);
        eventTurnEnd.setCurrentColor(currentColor);
    }


    private void restoreEventApplyLetter(AbstractEvent event, String[] split) {
        EventApplyLetter eventApplyLetter = (EventApplyLetter) event;
        int id = Integer.valueOf(split[1]);
        eventApplyLetter.setId(id);
    }


    private void restoreEventDeclineLetter(AbstractEvent event, String[] split) {
        EventDeclineLetter eventDeclineLetter = (EventDeclineLetter) event;
        int id = Integer.valueOf(split[1]);
        eventDeclineLetter.setId(id);
    }


    private void restoreEventSendLetter(AbstractEvent event, String[] split) {
        EventSendLetter eventSendLetter = (EventSendLetter) event;
        String code = split[1];
        Letter letter = new Letter();
        letter.decode(coreModel, code);
        eventSendLetter.setLetter(letter);
    }


    private void restoreEventSetRelation(AbstractEvent event, String[] split) {
        EventSetRelationSoftly eventSetRelationSoftly = (EventSetRelationSoftly) event;
        RelationType relationType = RelationType.valueOf(split[1]);
        HColor color1 = HColor.valueOf(split[2]);
        HColor color2 = HColor.valueOf(split[3]);
        int lock = Integer.valueOf(split[4]);
        eventSetRelationSoftly.setRelationType(relationType);
        eventSetRelationSoftly.setColor1(color1);
        eventSetRelationSoftly.setColor2(color2);
        eventSetRelationSoftly.setLock(lock);
    }


    private void restoreEventMergeOnBuild(AbstractEvent event, String[] split) {
        EventMergeOnBuild eventMergeOnBuild = (EventMergeOnBuild) event;
        int c1 = Integer.valueOf(split[1]);
        int c2 = Integer.valueOf(split[2]);
        PieceType pieceType = PieceType.valueOf(split[3]);
        int unitId = Integer.valueOf(split[4]);
        int provinceId = Integer.valueOf(split[5]);
        Hex hex = coreModel.getHex(c1, c2);
        eventMergeOnBuild.setHex(hex);
        eventMergeOnBuild.setPieceType(pieceType);
        eventMergeOnBuild.setUnitId(unitId);
        eventMergeOnBuild.setProvinceId(provinceId);
    }


    private void restoreEventMerge(AbstractEvent event, String[] split) {
        EventMerge eventMerge = (EventMerge) event;
        int c1 = Integer.valueOf(split[1]);
        int c2 = Integer.valueOf(split[2]);
        int c3 = Integer.valueOf(split[3]);
        int c4 = Integer.valueOf(split[4]);
        int unitId = Integer.valueOf(split[5]);
        Hex startHex = coreModel.getHex(c1, c2);
        Hex targetHex = coreModel.getHex(c3, c4);
        eventMerge.setStartHex(startHex);
        eventMerge.setTargetHex(targetHex);
        eventMerge.setUnitId(unitId);
    }


    private void restoreEventPieceBuild(AbstractEvent event, String[] split) {
        EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
        int c1 = Integer.valueOf(split[1]);
        int c2 = Integer.valueOf(split[2]);
        PieceType pieceType = PieceType.valueOf(split[3]);
        int unitId = Integer.valueOf(split[4]);
        int provinceId = Integer.valueOf(split[5]);
        Hex hex = coreModel.getHex(c1, c2);
        eventPieceBuild.setHex(hex);
        eventPieceBuild.setPieceType(pieceType);
        eventPieceBuild.setUnitId(unitId);
        eventPieceBuild.setProvinceId(provinceId);
    }


    private void restoreEventSetMoney(AbstractEvent event, String[] split) {
        EventSetMoney eventSetMoney = (EventSetMoney) event;
        int id = Integer.valueOf(split[1]);
        int money = Integer.valueOf(split[2]);
        eventSetMoney.setProvinceId(id);
        eventSetMoney.setMoney(money);
    }


    private void restoreEventHexChangeColor(AbstractEvent event, String[] split) {
        EventHexChangeColor eventHexChangeColor = (EventHexChangeColor) event;
        int c1 = Integer.valueOf(split[1]);
        int c2 = Integer.valueOf(split[2]);
        HColor color = HColor.valueOf(split[3]);
        Hex hex = coreModel.getHex(c1, c2);
        eventHexChangeColor.setHex(hex);
        eventHexChangeColor.setColor(color);
    }


    private void restoreEventUnitMove(AbstractEvent event, String[] split) {
        EventUnitMove eventUnitMove = (EventUnitMove) event;
        int start_c1 = Integer.valueOf(split[1]);
        int start_c2 = Integer.valueOf(split[2]);
        int finish_c1 = Integer.valueOf(split[3]);
        int finish_c2 = Integer.valueOf(split[4]);
        boolean colorTransferEnabled = Yio.convertShortStringToBoolean(split[5]);
        Hex startHex = coreModel.getHex(start_c1, start_c2);
        Hex finishHex = coreModel.getHex(finish_c1, finish_c2);
        eventUnitMove.setStart(startHex);
        eventUnitMove.setFinish(finishHex);
        eventUnitMove.setColorTransferEnabled(colorTransferEnabled);
    }


    private void restoreEventPieceDelete(AbstractEvent event, String[] split) {
        EventPieceDelete eventPieceDelete = (EventPieceDelete) event;
        int c1 = Integer.valueOf(split[1]);
        int c2 = Integer.valueOf(split[2]);
        Hex hex = coreModel.getHex(c1, c2);
        eventPieceDelete.setHex(hex);
    }


    private void restoreEventPieceAdd(AbstractEvent event, String[] split) {
        EventPieceAdd eventPieceAdd = (EventPieceAdd) event;
        int c1 = Integer.valueOf(split[1]);
        int c2 = Integer.valueOf(split[2]);
        PieceType pieceType = PieceType.valueOf(split[3]);
        int unitId = Integer.valueOf(split[4]);
        Hex hex = coreModel.getHex(c1, c2);
        eventPieceAdd.setHex(hex);
        eventPieceAdd.setPieceType(pieceType);
        eventPieceAdd.setUnitId(unitId);
    }
}
