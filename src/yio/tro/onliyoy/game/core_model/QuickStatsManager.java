package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;

import java.util.HashMap;

public class QuickStatsManager {

    CoreModel coreModel;
    public HashMap<HColor, Integer> mapQuantities;


    public QuickStatsManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        mapQuantities = new HashMap<>();
    }


    public void update() {
        updateQuantities();
    }


    public int getQuantity(HColor color) {
        if (!mapQuantities.containsKey(color)) return 0;
        return mapQuantities.get(color);
    }


    private void updateQuantities() {
        mapQuantities.clear();
        for (PlayerEntity entity : coreModel.entitiesManager.entities) {
            mapQuantities.put(entity.color, 0);
        }
        for (Province province : coreModel.provincesManager.provinces) {
            HColor color = province.getColor();
            if (!mapQuantities.containsKey(color)) continue;
            int currentQuantity = mapQuantities.get(color);
            mapQuantities.put(color, currentQuantity + province.getHexes().size());
        }
    }
}
