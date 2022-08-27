package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Hex;

public class EventPieceDelete extends AbstractEvent{

    public Hex hex;


    public EventPieceDelete() {
        super();
        hex = null;
    }


    @Override
    public EventType getType() {
        return EventType.piece_delete;
    }


    @Override
    public boolean isValid() {
        if (hex == null) return false;
        if (!hex.hasPiece()) return false;
        return true;
    }


    @Override
    public void applyChange() {
        if (hex.hasUnit()) {
            hex.setUnitId(-1);
        }
        hex.setPiece(null);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventPieceDelete eventPieceDelete = (EventPieceDelete) srcEvent;
        setHex(coreModel.getHexWithSameCoordinates(eventPieceDelete.hex));
    }


    @Override
    protected String getLocalEncodedInfo() {
        return hex.coordinate1 + " " + hex.coordinate2;
    }


    public void setHex(Hex hex) {
        this.hex = hex;
    }


    @Override
    public String toString() {
        return "[" +
                getInternalName() + ": " + hex +
                "]";
    }
}
