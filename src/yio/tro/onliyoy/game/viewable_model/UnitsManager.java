package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.stuff.RepeatYio;

import java.util.ArrayList;

public class UnitsManager {

    ViewableModel viewableModel;
    public ArrayList<ViewableUnit> units;
    RepeatYio<UnitsManager> repeatRemoveDeadUnits;
    RepeatYio<UnitsManager> repeatApplyJumps;
    public UmSelector selector;


    public UnitsManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        units = new ArrayList<>();
        selector = new UmSelector(this);
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveDeadUnits = new RepeatYio<UnitsManager>(this, 60) {
            @Override
            public void performAction() {
                parent.removeDeadUnits();
            }
        };
        repeatApplyJumps = new RepeatYio<UnitsManager>(this, 50) {
            @Override
            public void performAction() {
                parent.applyJumps();
            }
        };
    }


    void move() {
        moveUnits();
        repeatRemoveDeadUnits.move();
        repeatApplyJumps.move();
        selector.move();
    }


    void applyJumps() {
        if (!viewableModel.entitiesManager.isHumanTurnCurrently()) return;
        prepareFlagsForJumps();
        viewableModel.readinessManager.updateFlags(true);
        for (ViewableUnit viewableUnit : units) {
            Province province = viewableUnit.hex.getProvince();
            if (province == null) continue;
            if (!province.isOwnedByCurrentEntity()) continue;
            if (!viewableUnit.hex.flag) continue;
            viewableUnit.doJump();
        }
    }


    private void prepareFlagsForJumps() {
        for (Province province : viewableModel.provincesManager.provinces) {
            if (!province.isOwnedByCurrentEntity()) continue;
            for (Hex hex : province.getHexes()) {
                hex.flag = false;
            }
        }
    }


    public void clear() {
        units.clear();
    }


    private void moveUnits() {
        for (ViewableUnit viewableUnit : units) {
            viewableUnit.move();
        }
    }


    private void removeDeadUnits() {
        for (int i = units.size() - 1; i >= 0; i--) {
            if (units.get(i).alive) continue;
            units.remove(i);
        }
    }


    public ViewableUnit getUnit(Hex hex) {
        // TODO speed up this method
        for (ViewableUnit viewableUnit : units) {
            if (!viewableUnit.alive) continue;
            if (viewableUnit.appearFactor.isInDestroyState()) continue;
            if (viewableUnit.hex == hex) return viewableUnit;
        }
        return null;
    }


    ViewableUnit getCurrentlySelectedUnit() {
        for (ViewableUnit viewableUnit : units) {
            if (!viewableUnit.isSelected()) continue;
            return viewableUnit;
        }
        return null;
    }


    public void onClickedOutside() {
        deselectEveryone();
    }


    public void onPieceChosenInConstructionView(PieceType pieceType) {
        deselectEveryone();
    }


    public void deselectEveryone() {
        for (ViewableUnit unit : units) {
            unit.deselect();
        }
    }


    public void onPlayerTriedToMergeInvalidPair(Hex hex) {
        ViewableUnit viewableUnit = getUnit(hex);
        if (viewableUnit == null) return;
        viewableUnit.applyEnlarge(250);
    }


    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case match_started:
                forceUnitAppearAnimations();
                break;
            case piece_add:
                onEventPieceAdd(event);
                break;
            case unit_move:
                onEventUnitMove(event);
                break;
            case piece_delete:
                onEventPieceDelete(event);
                break;
            case turn_end:
                onTurnEndEventApplied();
                break;
            case piece_build:
                onEventPieceBuild(event);
                break;
            case merge:
                onEventMerge(event);
                break;
            case merge_on_build:
                onEventMergeOnBuild(event);
                break;
        }
    }


    private void forceUnitAppearAnimations() {
        for (ViewableUnit viewableUnit : units) {
            viewableUnit.forceAppearance();
        }
    }


    private void onEventMergeOnBuild(AbstractEvent event) {
        EventMergeOnBuild eventMergeOnBuild = (EventMergeOnBuild) event;
        Hex hex = eventMergeOnBuild.hex;
        ViewableUnit viewableUnit = getUnit(hex);
        viewableUnit.pieceType = Core.getMergeResult(viewableUnit.pieceType, eventMergeOnBuild.pieceType);
    }


    private void onEventMerge(AbstractEvent event) {
        EventMerge eventMerge = (EventMerge) event;
        ViewableUnit unit1 = getUnit(eventMerge.startHex);
        ViewableUnit unit2 = getUnit(eventMerge.targetHex);
        PieceType mergeResult = Core.getMergeResult(unit1.pieceType, unit2.pieceType);
        unit1.kill();
        unit1.enableMergeOutAnimation(eventMerge.targetHex);
        unit2.kill();
        unit2.enableMergeInAnimation();
        ViewableUnit viewableUnit = new ViewableUnit(this);
        viewableUnit.spawn(VuSpawnType.merge, eventMerge.targetHex, mergeResult);
        if (viewableModel.entitiesManager.isHumanTurnCurrently()) {
            viewableUnit.applyEnlarge(250);
        }
        units.add(viewableUnit);
    }


    private void onEventPieceBuild(AbstractEvent event) {
        EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
        if (!Core.isUnit(eventPieceBuild.pieceType)) return;
        killUnitOnHex(eventPieceBuild.hex); // kill previous unit
        ViewableUnit viewableUnit = new ViewableUnit(this);
        viewableUnit.spawn(VuSpawnType.constructed, eventPieceBuild.hex, eventPieceBuild.pieceType);
        units.add(viewableUnit);
    }


    private void onTurnEndEventApplied() {
        // this method can be called after actual turn has started long time ago
        // so it's possible that player selected a unit and only then
        // viewable model processed 'turn end' event
        HColor currentColor = viewableModel.refModel.entitiesManager.getCurrentColor();
        for (ViewableUnit unit : units) {
            if (unit.hex.color == currentColor) continue;
            unit.deselect();
        }
        repeatApplyJumps.setCountDown(2);
    }


    void onEventPieceDelete(AbstractEvent event) {
        EventPieceDelete eventPieceDelete = (EventPieceDelete) event;
        ViewableUnit viewableUnit = getUnit(eventPieceDelete.hex);
        if (viewableUnit == null) return;
        viewableUnit.kill();
        if (eventPieceDelete.isQuick()) {
            viewableUnit.setDestroyType(VuDestroyType.quick);
        }
    }


    private void killUnitOnHex(Hex hex) {
        ViewableUnit viewableUnit = getUnit(hex);
        if (viewableUnit == null) return;
        viewableUnit.kill();
    }


    void onEventUnitMove(AbstractEvent event) {
        EventUnitMove eventUnitMove = (EventUnitMove) event;
        killUnitOnHex(eventUnitMove.finish); // unit can 'eat' another unit
        ViewableUnit viewableUnit = getUnit(eventUnitMove.start);
        if (viewableUnit == null) {
            System.out.println("UnitsManager.onEventUnitMove: null");
            return;
        }
        viewableUnit.relocate(eventUnitMove.finish, event.isQuick());
    }


    void onEventPieceAdd(AbstractEvent event) {
        EventPieceAdd eventPieceAdd = (EventPieceAdd) event;
        if (!Core.isUnit(eventPieceAdd.pieceType)) return;
        killUnitOnHex(eventPieceAdd.hex); // kill previous unit
        ViewableUnit viewableUnit = new ViewableUnit(this);
        viewableUnit.spawn(VuSpawnType.normal, eventPieceAdd.hex, eventPieceAdd.pieceType);
        units.add(viewableUnit);
    }
}

