package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.HashMap;
import java.util.Map;

public class EventsRefrigerator {

    // refrigerator stores event objects that are used by internal core managers
    // this way they don't use too much memory
    // also, events taken from refrigerator are not stored in history manager
    CoreModel coreModel;
    HashMap<EventType, ObjectPoolYio<AbstractEvent>> map;


    public EventsRefrigerator(CoreModel coreModel) {
        this.coreModel = coreModel;
        initMap();
    }


    private void initMap() {
        map = new HashMap<>();
        for (EventType eventType : EventType.values()) {
            addMapEntry(eventType);
        }
    }


    private void addMapEntry(final EventType eventType) {
        map.put(eventType, new ObjectPoolYio<AbstractEvent>() {
            @Override
            public AbstractEvent makeNewObject() {
                AbstractEvent event = coreModel.eventsManager.factory.createEvent(eventType);
                event.setReusable(true);
                return event;
            }
        });
    }


    private AbstractEvent getEvent(EventType eventType) {
        return map.get(eventType).getNext();
    }


    public void utilizeEvent(AbstractEvent event) {
        if (!event.isReusable()) return;
        ObjectPoolYio<AbstractEvent> pool = map.get(event.getType());
        pool.add(event);
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("EventsRefrigerator.showInConsole");
        for (Map.Entry<EventType, ObjectPoolYio<AbstractEvent>> entry : map.entrySet()) {
            EventType eventType = entry.getKey();
            ObjectPoolYio<AbstractEvent> pool = entry.getValue();
            System.out.println(eventType + ": " + pool.getSize());
        }
    }


    public EventSetReady getSetReadyEvent(Hex hex, boolean value) {
        EventSetReady event = (EventSetReady) getEvent(EventType.set_ready);
        event.setHex(hex);
        event.setTargetValue(value);
        return event;
    }


    public EventSubtractMoney getEventSubtractMoney(Hex hex, int amount) {
        EventSubtractMoney event = (EventSubtractMoney) getEvent(EventType.subtract_money);
        event.setHex(hex);
        event.setAmount(amount);
        return event;
    }


    public EventGiveMoney getEventGiveMoney(HColor executorColor, HColor targetColor, int amount) {
        EventGiveMoney event = (EventGiveMoney) getEvent(EventType.give_money);
        event.setExecutorColor(executorColor);
        event.setTargetColor(targetColor);
        event.setAmount(amount);
        return event;
    }


    public EventSetRelationSoftly getEventSetRelationSoftly(RelationType relationType, HColor color1, HColor color2, int lock) {
        EventSetRelationSoftly event = (EventSetRelationSoftly) getEvent(EventType.set_relation_softly);
        event.setRelationType(relationType);
        event.setColor1(color1);
        event.setColor2(color2);
        event.setLock(lock);
        return event;
    }


    public EventSetMoney getSetMoneyEvent(Province province, int money) {
        EventSetMoney event = (EventSetMoney) getEvent(EventType.set_money);
        event.setProvinceId(province.getId());
        event.setMoney(money);
        return event;
    }


    public EventHexChangeColor getChangeHexColorEvent(Hex hex, HColor color) {
        EventHexChangeColor event = (EventHexChangeColor) getEvent(EventType.hex_change_color);
        event.setHex(hex);
        event.setColor(color);
        return event;
    }


    public EventPieceAdd getAddPieceEvent(Hex hex, PieceType pieceType) {
        EventPieceAdd event = (EventPieceAdd) getEvent(EventType.piece_add);
        event.setHex(hex);
        event.setPieceType(pieceType);
        return event;
    }


    public EventPieceDelete getDeletePieceEvent(Hex hex) {
        EventPieceDelete event = (EventPieceDelete) getEvent(EventType.piece_delete);
        event.setHex(hex);
        return event;
    }


    public EventGraphCreated getGraphCreatedEvent() {
        return (EventGraphCreated) getEvent(EventType.graph_created);
    }


    public EventMatchStarted getMatchStartedEvent() {
        return (EventMatchStarted) getEvent(EventType.match_started);
    }
}
