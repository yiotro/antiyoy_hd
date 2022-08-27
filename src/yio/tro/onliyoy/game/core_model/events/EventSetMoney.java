package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class EventSetMoney extends AbstractEvent{

    int money;
    int provinceId;


    public EventSetMoney() {
        super();
        money = -1;
        provinceId = -1;
    }


    @Override
    public EventType getType() {
        return EventType.set_money;
    }


    @Override
    public boolean isValid() {
        if (provinceId == -1) return false;
        if (money == -1) return false;
        if (coreModel.provincesManager.getProvince(provinceId) == null) {
            System.out.println("EventSetMoney.isValid: bad province id");
            return false;
        }
        return true;
    }


    @Override
    public void applyChange() {
        Province province = coreModel.provincesManager.getProvince(provinceId);
        province.setMoney(money);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventSetMoney eventSetMoney = (EventSetMoney) srcEvent;
        setMoney(eventSetMoney.money);
        setProvinceId(eventSetMoney.provinceId);
    }


    @Override
    protected String getLocalEncodedInfo() {
        return provinceId + " " + money;
    }


    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }


    public void setMoney(int money) {
        this.money = money;
    }
}
