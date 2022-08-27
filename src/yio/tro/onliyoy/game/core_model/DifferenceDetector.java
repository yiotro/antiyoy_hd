package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DifferenceDetector {

    private CoreModel currentModel;
    private CoreModel targetModel;
    private ArrayList<AbstractEvent> result;
    private ArrayList<EventUnitMove> movementsList;
    private ObjectPoolYio<ComparisonItem> poolComparisonItems;
    private HashMap<Integer, ComparisonItem> mapComparisonItems;
    private MovementConflictsResolver conflictsResolver;


    public DifferenceDetector() {
        currentModel = null; // yes, current model has different behavior than target model
        targetModel = new CoreModel("target");
        result = new ArrayList<>();
        movementsList = new ArrayList<>();
        mapComparisonItems = new HashMap<>();
        conflictsResolver = new MovementConflictsResolver();
        initPools();
    }


    private void initPools() {
        poolComparisonItems = new ObjectPoolYio<ComparisonItem>() {
            @Override
            public ComparisonItem makeNewObject() {
                return new ComparisonItem();
            }
        };
    }


    public ArrayList<AbstractEvent> apply() {
        // result is a list of changes that has to be applied to
        // current model to turn it into target model
        result.clear();
        movementsList.clear();
        detectUnitChanges();
        detectStaticChanges();
        detectColorChanges();
        fixPieceAddEvents();
        detectLetterEvents();
        makeAllEventsQuick();
        return result;
    }


    private void detectLetterEvents() {
        for (Letter currentLetter : currentModel.lettersManager.mailBasket) {
            if (hasSameLetterInTargetModel(currentLetter)) continue;
            EventIndicateUndoLetter event = getFactory().createEventIndicateUndoLetter(currentLetter);
            result.add(event);
        }
    }


    private boolean hasSameLetterInTargetModel(Letter currentLetter) {
        for (Letter letter : targetModel.lettersManager.mailBasket) {
            if (letter.id == currentLetter.id) return true;
        }
        return false;
    }


    private void fixPieceAddEvents() {
        // event piece add is valid only when hex is empty
        // so in some case there should be a 'event piece delete' right before it
        for (int i = result.size() - 1; i >= 0; i--) {
            AbstractEvent event = result.get(i);
            if (event.getType() != EventType.piece_add) continue;
            EventPieceAdd eventPieceAdd = (EventPieceAdd) event;
            Hex hex = eventPieceAdd.hex;
            if (hex.isEmpty()) continue;
            if (Core.isUnit(hex.piece)) continue;
            EventPieceDelete eventPieceDelete = getFactory().createDeletePieceEvent(hex);
            result.add(i, eventPieceDelete);
        }
    }


    private void detectColorChanges() {
        for (Hex currentHex : currentModel.hexes) {
            Hex hexWithSameCoordinates = targetModel.getHexWithSameCoordinates(currentHex);
            detectColorChangeInHexPair(currentHex, hexWithSameCoordinates);
        }
    }


    private void detectColorChangeInHexPair(Hex currentHex, Hex targetHex) {
        if (currentHex.color == targetHex.color) return;
        addEventHexChangeColor(currentHex, targetHex.color);
    }


    private void makeAllEventsQuick() {
        for (AbstractEvent event : result) {
            event.setQuick(true);
        }
    }


    private void detectStaticChanges() {
        for (Hex currentHex : currentModel.hexes) {
            Hex hexWithSameCoordinates = targetModel.getHexWithSameCoordinates(currentHex);
            detectStaticChangeInHexPair(currentHex, hexWithSameCoordinates);
        }
    }


    private void detectStaticChangeInHexPair(Hex currentHex, Hex targetHex) {
        if (currentHex.piece == targetHex.piece) return;
        if (currentHex.hasStaticPiece() && !targetHex.hasUnit()) {
            addEventPieceDelete(currentHex);
        }
        if (targetHex.hasStaticPiece()) {
            addEventPieceAdd(currentHex, targetHex.piece, targetHex.unitId);
        }
    }


    private void addEventHexChangeColor(Hex hex, HColor color) {
        EventHexChangeColor eventHexChangeColor = getFactory().createChangeHexColorEvent(hex, color);
        result.add(eventHexChangeColor);
    }


    private void addEventPieceAdd(Hex currentHex, PieceType pieceType, int unitId) {
        EventPieceAdd eventPieceAdd = getFactory().createAddPieceEvent(currentHex, pieceType, unitId);
        result.add(eventPieceAdd);
    }


    private void addEventPieceDelete(Hex currentHex) {
        // in some cases unit can pop on top of another unit
        // so previous unit has to be deleted before new one is created
        EventPieceDelete eventPieceDelete = getFactory().createDeletePieceEvent(currentHex);
        result.add(0, eventPieceDelete);
    }


    private EventsFactory getFactory() {
        return currentModel.eventsManager.factory;
    }


    private void detectUnitChanges() {
        clearMapComparisonItems();
        populateMapByCurrentModel();
        supplementHashMapByTargetModel();
        removeInvalidComparisonItems();
        turnComparisonItemsIntoEvents();
        conflictsResolver.apply(movementsList);
        result.addAll(0, movementsList);
    }


    private void turnComparisonItemsIntoEvents() {
        for (ComparisonItem comparisonItem : mapComparisonItems.values()) {
            EventUnitMove eventUnitMove = getFactory().createMoveUnitEvent(
                    comparisonItem.currentHex,
                    currentModel.getHexWithSameCoordinates(comparisonItem.targetHex)
            );
            eventUnitMove.setColorTransferEnabled(false);
            movementsList.add(eventUnitMove);
        }
    }


    private void removeInvalidComparisonItems() {
        while (true) {
            ComparisonItem comparisonItem = getInvalidComparisonItem();
            if (comparisonItem == null) break;
            removeComparisonItem(comparisonItem.currentHex.unitId);
            if (comparisonItem.targetHex == null) {
                addEventPieceDelete(comparisonItem.currentHex);
            }
        }
    }


    private ComparisonItem getInvalidComparisonItem() {
        for (ComparisonItem comparisonItem : mapComparisonItems.values()) {
            if (comparisonItem.targetHex == null) return comparisonItem;
            if (comparisonItem.currentHex.hasSameCoordinatesAs(comparisonItem.targetHex)) return comparisonItem;
        }
        return null;
    }


    private void supplementHashMapByTargetModel() {
        for (Hex hex : targetModel.hexes) {
            if (!hex.hasUnit()) continue;
            ComparisonItem comparisonItem = mapComparisonItems.get(hex.unitId);
            if (comparisonItem == null) {
                addEventPieceAdd(currentModel.getHexWithSameCoordinates(hex), hex.piece, hex.unitId);
                continue;
            }
            comparisonItem.setTargetHex(hex);
        }
    }


    private void populateMapByCurrentModel() {
        for (Hex hex : currentModel.hexes) {
            if (!hex.hasUnit()) continue;
            ComparisonItem next = poolComparisonItems.getNext();
            next.setCurrentHex(hex);
            mapComparisonItems.put(hex.unitId, next);
        }
    }


    private void clearMapComparisonItems() {
        for (ComparisonItem comparisonItem : mapComparisonItems.values()) {
            poolComparisonItems.add(comparisonItem);
        }
        mapComparisonItems.clear();
    }


    private void removeComparisonItem(int unitId) {
        ComparisonItem comparisonItem = mapComparisonItems.get(unitId);
        if (comparisonItem == null) return;
        poolComparisonItems.add(comparisonItem);
        mapComparisonItems.remove(unitId);
    }


    public void prepareTargetGraph(CoreModel model) {
        targetModel.buildSimilarGraph(model);
    }


    public void setCurrentModel(CoreModel model) {
        currentModel = model;
    }


    public void setTargetModelBy(CoreModel model) {
        targetModel.setBy(model);
    }


    public void showResultInConsole() {
        System.out.println();
        System.out.println("DifferenceDetector.showResultInConsole");
        for (AbstractEvent event : result) {
            System.out.println("- " + event);
        }
    }
}
