package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.ReadinessManager;

public class EventMerge extends AbstractEvent{

    public Hex startHex;
    public Hex targetHex;
    public int unitId;


    public EventMerge() {
        super();
        startHex = null;
        targetHex = null;
        unitId = -1;
    }


    @Override
    public EventType getType() {
        return EventType.merge;
    }


    @Override
    public boolean isValid() {
        if (startHex == null) return false;
        if (targetHex == null) return false;
        if (unitId == -1) return false;
        if (!startHex.hasUnit()) return false;
        if (!targetHex.hasUnit()) return false;
        if (startHex.color != targetHex.color) return false;
        if (startHex.getProvince() == null) return false;
        if (startHex.getProvince() != targetHex.getProvince()) return false;
        if (coreModel.entitiesManager.getEntity(startHex.color) == null) return false;
        coreModel.moveZoneManager.updateForUnit(startHex);
        if (!coreModel.moveZoneManager.contains(targetHex)) return false;
        if (Core.getMergeResult(startHex.piece, targetHex.piece) == null) return false;
        return true;
    }


    @Override
    public void applyChange() {
        PieceType mergeResult = Core.getMergeResult(startHex.piece, targetHex.piece);
        ReadinessManager readinessManager = coreModel.readinessManager;
        boolean ready = readinessManager.isReady(startHex) && readinessManager.isReady(targetHex);
        startHex.setPiece(null);
        startHex.setUnitId(-1);
        targetHex.setPiece(mergeResult);
        targetHex.setUnitId(unitId);
        readinessManager.setReady(startHex, false);
        readinessManager.setReady(targetHex, ready);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventMerge eventMerge = (EventMerge) srcEvent;
        setStartHex(coreModel.getHexWithSameCoordinates(eventMerge.startHex));
        setTargetHex(coreModel.getHexWithSameCoordinates(eventMerge.targetHex));
        setUnitId(eventMerge.unitId);
    }


    @Override
    protected String getLocalEncodedInfo() {
        return startHex.coordinate1 + " " + startHex.coordinate2 + " " + targetHex.coordinate1 + " " + targetHex.coordinate2 + " " + unitId;
    }


    public void setStartHex(Hex startHex) {
        this.startHex = startHex;
    }


    public void setTargetHex(Hex targetHex) {
        this.targetHex = targetHex;
    }


    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}
