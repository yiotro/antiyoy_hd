package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;
import java.util.Random;

public class TreeManager implements IEventListener, ReusableYio {

    // this manager should be used as external manager


    CoreModel coreModel;
    ArrayList<Hex> tempHexList;
    Random random;


    public TreeManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        tempHexList = new ArrayList<>();
        random = new Random();
        if (DebugFlags.determinedRandom) {
            random = new Random(0);
        }
    }


    @Override
    public void reset() {
        coreModel = null;
        tempHexList.clear();
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case turn_end:
                onTurnEndEventApplied();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 7;
    }


    private void onTurnEndEventApplied() {
        spawnBreed();
        processGraves();
        processLonelyCities();
    }


    private void spawnBreed() {
        if (coreModel.turnsManager.turnIndex != 0) return;
        resetFlags();
        updateTempHexListByTrees();
        propagateTempListForTrees();
        spawnTreesOnFlaggedHexes();
    }


    private void spawnTreesOnFlaggedHexes() {
        for (Hex hex : coreModel.hexes) {
            if (!hex.flag) continue;
            if (random.nextFloat() > 0.33f) continue;
            doSpawnTreeWithPossibleMoneySubtraction(hex);
        }
    }


    private void propagateTempListForTrees() {
        for (Hex hex : tempHexList) {
            switch (hex.piece) {
                default:
                    System.out.println("TreeManager.spawnBreed: problem");
                    break;
                case palm:
                    propagatePalm(hex);
                    break;
                case pine:
                    propagatePine(hex);
                    break;
            }
        }
    }


    private void resetFlags() {
        for (Hex hex : coreModel.hexes) {
            hex.flag = false;
        }
    }


    private void propagatePine(Hex hex) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.hasPiece()) continue;
            if (countAdjacentTrees(adjacentHex) < 2) continue;
            adjacentHex.flag = true;
        }
    }


    private int countAdjacentTrees(Hex hex) {
        int c = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (!adjacentHex.hasTree()) continue;
            c++;
        }
        return c;
    }


    private void propagatePalm(Hex hex) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.hasPiece()) continue;
            if (adjacentHex.adjacentHexes.size() == 6) continue;
            adjacentHex.flag = true;
        }
    }


    private void updateTempHexListByTrees() {
        tempHexList.clear();
        for (Hex hex : coreModel.hexes) {
            if (!hex.hasTree()) continue;
            tempHexList.add(hex);
        }
    }


    private void processLonelyCities() {
        for (Hex hex : coreModel.hexes) {
            if (hex.color == HColor.gray) continue;
            if (hex.getProvince() != null) continue;
            if (hex.piece != PieceType.city) continue;
            turnIntoTree(hex);
        }
    }


    public void turnIntoTree(Hex hex) {
        if (hex.hasPiece()) {
            EventsManager eventsManager = coreModel.eventsManager;
            EventPieceDelete deletePieceEvent = eventsManager.factory.createDeletePieceEvent(hex);
            eventsManager.applyEvent(deletePieceEvent);
        }
        doSpawnTreeWithPossibleMoneySubtraction(hex);
    }


    private void processGraves() {
        for (Hex hex : coreModel.hexes) {
            if (hex.color != coreModel.entitiesManager.getCurrentColor()) continue;
            if (hex.piece != PieceType.grave) continue;
            turnIntoTree(hex);
        }
    }


    private void doSpawnTreeWithPossibleMoneySubtraction(Hex hex) {
        doSpawnTree(hex);
        checkForMoneySubtraction(hex);
    }


    private void checkForMoneySubtraction(Hex hex) {
        PlayerEntity[] entities = coreModel.entitiesManager.entities;
        if (entities == null) return;
        if (entities.length == 0) return;
        if (hex.color != entities[0].color) return;
        Province province = hex.getProvince();
        if (province == null) return;
        if (province.getMoney() <= 0) return; // this check is very important
        EventsManager eventsManager = coreModel.eventsManager;
        EventSubtractMoney event = eventsManager.factory.createEventSubtractMoney(hex, 1);
        eventsManager.applyEvent(event);
    }


    public void doSpawnTree(Hex hex) {
        PieceType pieceType = PieceType.palm;
        if (hex.adjacentHexes.size() == 6) {
            pieceType = PieceType.pine;
        }
        EventsManager eventsManager = coreModel.eventsManager;
        EventPieceAdd addPieceEvent = eventsManager.factory.createAddPieceEvent(hex, pieceType);
        eventsManager.applyEvent(addPieceEvent);
    }


    public void setCoreModel(CoreModel coreModel) {
        this.coreModel = coreModel;
    }
}
