package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class EventUnitMove extends AbstractEvent{

    public Hex start;
    public Hex finish;
    public boolean colorTransferEnabled;


    public EventUnitMove() {
        super();
        start = null;
        finish = null;
        colorTransferEnabled = true;
    }


    @Override
    public EventType getType() {
        return EventType.unit_move;
    }


    @Override
    public boolean isValid() {
        if (start == null) return false;
        if (finish == null) return false;
        if (start.getProvince() == null && finish.hasTree()) return false;
        if (!start.hasUnit()) return false;
        if (!isQuick() && !coreModel.readinessManager.isReady(start)) return false;
        if (areColorTransferConditionsSatisfied()) {
            coreModel.moveZoneManager.updateForUnit(start);
            if (!coreModel.moveZoneManager.contains(finish)) return false;
        }
        if (start.color == finish.color && finish.hasStaticPiece()) {
            return finish.hasTree() || finish.piece == PieceType.grave;
        }
        return true;
    }


    @Override
    public void applyChange() {
        if (finish.hasTree() && finish.color == start.color) {
            int reward = coreModel.ruleset.getTreeReward();
            Province province = start.getProvince();
            province.setMoney(province.getMoney() + reward);
        }
        finish.setPiece(start.piece);
        finish.setUnitId(start.unitId);
        start.setPiece(null);
        start.setUnitId(-1);
        coreModel.readinessManager.onUnitMoved(start);
        if (areColorTransferConditionsSatisfied()) {
            finish.setColor(start.color);
        }
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventUnitMove eventUnitMove = (EventUnitMove) srcEvent;
        setStart(coreModel.getHexWithSameCoordinates(eventUnitMove.start));
        setFinish(coreModel.getHexWithSameCoordinates(eventUnitMove.finish));
        setColorTransferEnabled(eventUnitMove.colorTransferEnabled);
    }


    public boolean areColorTransferConditionsSatisfied() {
        return colorTransferEnabled && start.color != finish.color;
    }


    @Override
    protected String getLocalEncodedInfo() {
        return start.coordinate1 + " " +
                start.coordinate2 + " " +
                finish.coordinate1 + " " +
                finish.coordinate2 + " " +
                Yio.convertBooleanToShortString(colorTransferEnabled);
    }


    public void setStart(Hex start) {
        this.start = start;
    }


    public void setFinish(Hex finish) {
        this.finish = finish;
    }


    public void setColorTransferEnabled(boolean colorTransferEnabled) {
        this.colorTransferEnabled = colorTransferEnabled;
    }


    @Override
    public String toString() {
        return "[" +
                getInternalName() + ": " +
                start + " -> " +
                finish + " " +
                Yio.convertBooleanToShortString(colorTransferEnabled) +
                "]";
    }
}
