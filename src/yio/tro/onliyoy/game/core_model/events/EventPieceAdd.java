package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;

public class EventPieceAdd extends AbstractEvent{

    public Hex hex;
    public PieceType pieceType;
    public int unitId;


    public EventPieceAdd() {
        super();
        hex = null;
        pieceType = null;
        unitId = -1;
    }


    @Override
    public EventType getType() {
        return EventType.piece_add;
    }


    @Override
    public boolean isValid() {
        if (hex == null) return false;
        if (pieceType == null) return false;
        if (hex.hasPiece()) return false;
        if (Core.isUnit(pieceType) && unitId == -1) return false;
        return true;
    }


    @Override
    public void applyChange() {
        hex.setPiece(pieceType);
        if (Core.isUnit(pieceType)) {
            hex.setUnitId(unitId);
        }
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventPieceAdd eventPieceAdd = (EventPieceAdd) srcEvent;
        setHex(coreModel.getHexWithSameCoordinates(eventPieceAdd.hex));
        setPieceType(eventPieceAdd.pieceType);
        setUnitId(eventPieceAdd.unitId);
    }


    @Override
    protected String getLocalEncodedInfo() {
        return hex.coordinate1 + " " + hex.coordinate2 + " " + pieceType + " " + unitId;
    }


    public void setHex(Hex hex) {
        this.hex = hex;
    }


    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }


    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }


    @Override
    public String toString() {
        return "[" +
                getInternalName() +
                ": " + pieceType + " on " + hex +
                "]";
    }
}
