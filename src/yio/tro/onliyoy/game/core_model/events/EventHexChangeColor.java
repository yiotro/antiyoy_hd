package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;

public class EventHexChangeColor extends AbstractEvent{

    public Hex hex;
    public HColor color;


    @Override
    public EventType getType() {
        return EventType.hex_change_color;
    }


    @Override
    public boolean isValid() {
        if (hex == null) return false;
        if (color == null) return false;
        return true;
    }


    @Override
    public void applyChange() {
        hex.setColor(color);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventHexChangeColor eventHexChangeColor = (EventHexChangeColor) srcEvent;
        setHex(coreModel.getHexWithSameCoordinates(eventHexChangeColor.hex));
        setColor(eventHexChangeColor.color);
    }


    @Override
    protected String getLocalEncodedInfo() {
        return hex.coordinate1 + " " + hex.coordinate2 + " " + color;
    }


    public void setHex(Hex hex) {
        this.hex = hex;
    }


    public void setColor(HColor color) {
        this.color = color;
    }
}
