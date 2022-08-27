package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class EventSubtractMoney extends AbstractEvent{

    public Hex hex;
    public int amount;


    public EventSubtractMoney() {
        hex = null;
        amount = -1;
    }


    @Override
    public EventType getType() {
        return EventType.subtract_money;
    }


    @Override
    public boolean isValid() {
        if (hex == null) return false;
        if (amount == -1) return false;
        if (hex.getProvince() == null) return false;
        return true;
    }


    @Override
    public void applyChange() {
        Province province = hex.getProvince();
        province.setMoney(province.getMoney() - amount);
//        System.out.println("EventSubtractMoney.applyChange: " + coreModel + " " + province);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventSubtractMoney eventSubtractMoney = (EventSubtractMoney) srcEvent;
        setHex(coreModel.getHexWithSameCoordinates(eventSubtractMoney.hex));
        amount = eventSubtractMoney.amount;
    }


    public void setHex(Hex hex) {
        this.hex = hex;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }


    @Override
    protected String getLocalEncodedInfo() {
        return hex.coordinate1 + " " + hex.coordinate2 + " " + amount;
    }


    @Override
    public String toString() {
        return "[" +
                getInternalName() + ": " + hex + " " + amount +
                "]";
    }
}
