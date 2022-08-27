package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesManager;

public class EventGiveMoney extends AbstractEvent{

    public HColor executorColor;
    public HColor targetColor;
    public int amount;


    public EventGiveMoney() {
        executorColor = null;
        targetColor = null;
        amount = -1;
    }


    @Override
    public EventType getType() {
        return EventType.give_money;
    }


    @Override
    public boolean isValid() {
        if (executorColor == null) return false;
        EntitiesManager entitiesManager = coreModel.entitiesManager;
        if (entitiesManager.getEntity(executorColor) == null) return false;
        if (coreModel.provincesManager.getSumMoney(executorColor) < amount) return false;
        if (targetColor == null) return false;
        if (entitiesManager.getEntity(targetColor) == null) return false;
        if (amount == -1) return false;
        return true;
    }


    @Override
    public void applyChange() {
        ProvincesManager provincesManager = coreModel.provincesManager;
        Province targetProvince = provincesManager.getLargestProvince(targetColor);
        targetProvince.setMoney(targetProvince.getMoney() + amount);
        int value = amount;
        while (value > 0) {
            Province richestProvince = provincesManager.getRichestProvince(executorColor);
            if (richestProvince == null) break;
            int money = richestProvince.getMoney();
            if (money == 0) break;
            if (money >= value) {
                richestProvince.setMoney(money - value);
                break;
            }
            value -= money;
            richestProvince.setMoney(0);
        }
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventGiveMoney eventGiveMoney = (EventGiveMoney) srcEvent;
        executorColor = eventGiveMoney.executorColor;
        targetColor = eventGiveMoney.targetColor;
        amount = eventGiveMoney.amount;
    }


    @Override
    protected String getLocalEncodedInfo() {
        return "-"; // shouldn't be saved
    }


    public void setExecutorColor(HColor executorColor) {
        this.executorColor = executorColor;
    }


    public void setTargetColor(HColor targetColor) {
        this.targetColor = targetColor;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }
}
