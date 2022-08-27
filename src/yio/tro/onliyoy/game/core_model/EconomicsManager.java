package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.debug.DebugFlags;

public class EconomicsManager implements IEventListener {

    CoreModel coreModel;


    public EconomicsManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
    }


    public int calculateProvinceIncome(Province province) {
        int income = 0;
        for (Hex hex : province.getHexes()) {
            income += coreModel.ruleset.getHexIncome(hex.piece);
        }
        return income;
    }


    public int calculateProvinceConsumption(Province province) {
        int consumption = 0;
        for (Hex hex : province.getHexes()) {
            consumption += coreModel.ruleset.getConsumption(hex.piece);
        }
        return consumption;
    }


    public int calculateProvinceProfit(Province province) {
        int income = calculateProvinceIncome(province);
        int consumption = calculateProvinceConsumption(province);
        return income - consumption;
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case turn_end:
                onTurnEndEventApplied();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 8;
    }


    private void onTurnEndEventApplied() {
        applyProfits();
    }


    private void applyProfits() {
        if (coreModel.turnsManager.lap == 0) return; // don't apply economics on first lap
        for (Province province : coreModel.provincesManager.provinces) {
            if (!province.isOwnedByCurrentEntity()) continue;
            int currentMoney = province.getMoney();
            int profit = calculateProvinceProfit(province);
            int money = currentMoney + profit;
            province.setMoney(money);
        }
    }


    public int getOverallMoney(HColor color) {
        int sum = 0;
        for (Province province : coreModel.provincesManager.provinces) {
            if (province.getColor() != color) continue;
            sum += province.getMoney();
        }
        return sum;
    }

}
