package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.ArrayList;
import java.util.Random;

public class InvalidityConcealer {

    // this worker applies minimal changes to model
    // in order to make event valid
    // technically it can be applied anywhere but it should only be used for replays

    CoreModel coreModel;
    EventsRefrigerator refrigerator;
    private EventsManager eventsManager;
    private ArrayList<Province> tempProvinces;


    public InvalidityConcealer(CoreModel coreModel) {
        this.coreModel = coreModel;
        tempProvinces = new ArrayList<>();
    }


    public void apply(AbstractEvent event) {
        if (event.isValid()) {
            System.out.println("InvalidityConcealer.apply: " + event + " is already valid");
            return;
        }
        DebugFlags.invalidEventInReplayDetected = true;
        updateReferences();
        switch (event.getType()) {
            default:
                System.out.println("InvalidityConcealer.apply: can't fix " + event);
                break;
            case merge:
                fixEventMerge(event);
                break;
            case merge_on_build:
                fixEventMergeOnBuild(event);
                break;
            case piece_build:
                fixEventPieceBuild(event);
                break;
            case unit_move:
                fixEventUnitMove(event);
                break;
            case piece_add:
                fixEventPieceAdd(event);
                break;
        }
        if (!event.isValid()) {
            System.out.println("Unable to conceal " + event + "   <--");
        } else {
            System.out.println("Successfully concealed " + event);
        }
    }


    private void fixEventPieceAdd(AbstractEvent event) {
        EventPieceAdd eventPieceAdd = (EventPieceAdd) event;
        Hex hex = eventPieceAdd.hex;
        ensureHexEmpty(hex);
    }


    private void fixEventUnitMove(AbstractEvent event) {
        EventUnitMove eventUnitMove = (EventUnitMove) event;
        Hex start = eventUnitMove.start;
        Hex finish = eventUnitMove.finish;
        if (start == null) return;
        if (finish == null) return;
        ensureUnit(start);
        ensureMoveZone(start, finish);
        ensureReady(start);
        ensurePeacefulMovement(start, finish);
        if (!event.isValid()) {
            // still not valid?
            // ok, let's at least let's change hex color
            changeColor(finish, start.color);
        }
    }


    private void ensurePeacefulMovement(Hex start, Hex finish) {
        if (start == null) return;
        if (finish == null) return;
        if (!start.hasUnit()) return;
        if (start.color != finish.color) return;
        if (finish.isEmpty()) return;
        ensureHexEmpty(finish);
    }


    private void fixEventPieceBuild(AbstractEvent event) {
        EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
        Hex hex = eventPieceBuild.hex;
        int provinceId = eventPieceBuild.provinceId;
        PieceType pieceType = eventPieceBuild.pieceType;
        ensureProvinceNearby(hex, provinceId);
        ensureProvinceCanAfford(provinceId, pieceType);
        ensureHexEmpty(hex);
    }


    private void fixEventMergeOnBuild(AbstractEvent event) {
        EventMergeOnBuild eventMergeOnBuild = (EventMergeOnBuild) event;
        Hex hex = eventMergeOnBuild.hex;
        PieceType pieceType = eventMergeOnBuild.pieceType;
        int provinceId = eventMergeOnBuild.provinceId;
        ensureUnit(hex);
        ensureMergeResult(hex, pieceType);
        ensureProvinceCanAfford(provinceId, pieceType);
    }


    private void fixEventMerge(AbstractEvent event) {
        EventMerge eventMerge = (EventMerge) event;
        Hex startHex = eventMerge.startHex;
        Hex targetHex = eventMerge.targetHex;
        ensureUnit(startHex);
        ensureUnit(targetHex);
        ensureMergeResult(startHex, targetHex);
    }


    private void ensureProvinceNearby(Hex hex, int provinceId) {
        if (hex == null) return;
        Province targetProvince = coreModel.provincesManager.getProvince(provinceId);
        if (targetProvince == null) return;
        if (isProvinceNearby(hex, targetProvince)) return;
        updateTempProvinces(hex);
        if (tempProvinces.size() != 1) return;
        Province nearbyProvince = tempProvinces.get(0);
        swapProvinceIds(targetProvince, nearbyProvince);
        System.out.println("InvalidityConcealer.ensureProvinceNearby: applied to " + hex + " and province id <" + provinceId + ">");
    }


    private void updateTempProvinces(Hex hex) {
        tempProvinces.clear();
        if (hex.getProvince() != null) {
            tempProvinces.add(hex.getProvince());
        }
        for (Hex adjacentHex : hex.adjacentHexes) {
            Province province = adjacentHex.getProvince();
            if (province == null) continue;
            if (tempProvinces.contains(province)) continue;
            tempProvinces.add(province);
        }
    }


    private boolean isProvinceNearby(Hex hex, Province province) {
        if (hex.getProvince() == province) return true;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.getProvince() == province) return true;
        }
        return false;
    }


    private void swapProvinceIds(Province province1, Province province2) {
        int tempId = province1.getId();
        province1.setId(province2.getId());
        province2.setId(tempId);
    }


    private void ensureMoveZone(Hex start, Hex finish) {
        if (start == null) return;
        if (finish == null) return;
        if (!start.hasUnit()) return;
        MoveZoneManager moveZoneManager = coreModel.moveZoneManager;
        int c = 10;
        while (c > 0) {
            c--;
            int strength = Core.getStrength(start.piece);
            if (strength == 4) break;
            moveZoneManager.updateForUnit(start);
            if (moveZoneManager.contains(finish)) break;
            deletePiece(start);
            addPiece(start, Core.getUnitByStrength(strength + 1));
            System.out.println("InvalidityConcealer.ensureMoveZone: applied to " + start);
        }
    }


    private void ensureReady(Hex hex) {
        if (hex == null) return;
        if (coreModel.readinessManager.isReady(hex)) return;
        eventsManager.applyEvent(refrigerator.getSetReadyEvent(hex, true));
        System.out.println("InvalidityConcealer.ensureReady: applied to " + hex);
    }


    private void ensureHexEmpty(Hex hex) {
        // well, some pieces can be built on top of other pieces
        // for example, units on trees or strong tower on normal towers
        // but this is still an ok solution
        if (hex == null) return;
        if (hex.isEmpty()) return;
        deletePiece(hex);
        System.out.println("InvalidityConcealer.ensureHexEmpty: applied to " + hex);
    }


    private void ensureProvinceCanAfford(int provinceId, PieceType pieceType) {
        if (pieceType == null) return;
        Province province = coreModel.provincesManager.getProvince(provinceId);
        if (province == null) return;
        if (!province.isValid()) return;
        if (province.canAfford(pieceType)) return;
        int price = coreModel.ruleset.getPrice(province, pieceType);
        int delta = price - province.getMoney() + 1;
        giveMoney(province, delta);
        System.out.println("InvalidityConcealer.ensureProvinceCanAfford: applied to <" + provinceId + "> building " + pieceType);
    }


    private void ensureMergeResult(Hex hex, PieceType addition) {
        if (hex == null) return;
        if (addition == null) return;
        if (!hex.hasUnit()) return;
        if (addition == PieceType.knight) return;
        int c = 10;
        while (c > 0 && Core.getStrength(addition) + Core.getStrength(hex.piece) > 4) {
            c--;
            weakenUnit(hex);
        }
    }


    private void ensureMergeResult(Hex hex1, Hex hex2) {
        if (hex1 == null) return;
        if (hex2 == null) return;
        if (!hex1.hasUnit()) return;
        if (!hex2.hasUnit()) return;
        int c = 10;
        while (c > 0 && Core.getStrength(hex1.piece) + Core.getStrength(hex2.piece) > 4) {
            c--;
            Hex strongerHex = getHexWithStrongerUnit(hex1, hex2);
            weakenUnit(strongerHex);
        }
    }


    private Hex getHexWithStrongerUnit(Hex hex1, Hex hex2) {
        if (Core.getStrength(hex1.piece) > Core.getStrength(hex2.piece)) return hex1;
        return hex2;
    }


    private void weakenUnit(Hex hex) {
        int strength = Core.getStrength(hex.piece);
        if (strength < 2) {
            System.out.println("InvalidityConcealer.weakenUnit: problem " + hex);
        }
        deletePiece(hex);
        addPiece(hex, Core.getUnitByStrength(strength - 1));
        System.out.println("InvalidityConcealer.weakenUnit: applied to " + hex);
    }


    private void ensureUnit(Hex hex) {
        if (hex == null) return;
        if (hex.hasUnit()) return;
        if (hex.hasPiece()) {
            // not unit, need to remove
            deletePiece(hex);
        }
        addPiece(hex, PieceType.peasant);
        System.out.println("InvalidityConcealer.ensureUnit: applied to " + hex);
    }


    private void giveMoney(Province province, int value) {
        int money = province.getMoney();
        eventsManager.applyEvent(refrigerator.getSetMoneyEvent(province, money + value));
    }


    private void addPiece(Hex hex, PieceType pieceType) {
        EventPieceAdd addPieceEvent = refrigerator.getAddPieceEvent(hex, pieceType);
        if (Core.isUnit(pieceType)) {
            int unitId = (new Random()).nextInt(); // yes, it can lead to problems but other solutions are worse
            addPieceEvent.setUnitId(unitId);
        }
        eventsManager.applyEvent(addPieceEvent);
    }


    private void deletePiece(Hex hex) {
        eventsManager.applyEvent(refrigerator.getDeletePieceEvent(hex));
    }


    private void changeColor(Hex hex, HColor targetColor) {
        eventsManager.applyEvent(refrigerator.getChangeHexColorEvent(hex, targetColor));
    }


    private void updateReferences() {
        eventsManager = coreModel.eventsManager;
        refrigerator = coreModel.eventsRefrigerator;
    }
}
