package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.ArrayList;
import java.util.Random;

public class CityManager implements IEventListener {

    CoreModel coreModel;
    Random random;


    public CityManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        random = new Random();
        if (DebugFlags.determinedRandom) {
            random = new Random(0);
        }
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case match_started:
                doFixProvincesWithoutCities();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 7;
    }


    public void onHexColorChanged(Hex hex, HColor previousColor) {
        if (previousColor == HColor.gray) return;
        doFixProvincesWithoutCities();
    }


    public void doFixProvincesWithoutCities() {
        for (Province province : coreModel.provincesManager.provinces) {
            if (province.contains(PieceType.city)) continue;
            doFixProvince(province);
        }
    }


    private void doFixProvince(Province province) {
        updateSeed(province);
        Hex hex = pickHexForCity(province);
        if (hex == null) return;
        EventsManager eventsManager = coreModel.eventsManager;
        if (hex.hasPiece()) {
            eventsManager.applyEvent(coreModel.eventsRefrigerator.getDeletePieceEvent(hex));
        }
        eventsManager.applyEvent(coreModel.eventsRefrigerator.getAddPieceEvent(hex, PieceType.city));
    }


    private Hex pickHexForCity(Province province) {
        Hex randomEmptyHex = getRandomEmptyHex(province);
        if (randomEmptyHex != null) return randomEmptyHex;
        Hex randomHexWithoutTower = getRandomHexWithoutTower(province);
        if (randomHexWithoutTower != null) return randomHexWithoutTower;
        return getRandomHexFromProvince(province);
    }


    private Hex getRandomHexWithoutTower(Province province) {
        if (!hasHexWithoutTower(province)) return null;
        int c = 0;
        while (c < 1000) {
            c++;
            Hex hex = getRandomHexFromProvince(province);
            if (!hex.hasTower()) return hex;
        }
        return null;
    }


    private boolean hasHexWithoutTower(Province province) {
        for (Hex hex : province.getHexes()) {
            if (!hex.hasTower()) return true;
        }
        return false;
    }


    private Hex getRandomEmptyHex(Province province) {
        if (!doesProvinceHaveEmptyHexes(province)) return null;
        int c = 0;
        while (c < 1000) {
            c++;
            Hex hex = getRandomHexFromProvince(province);
            if (hex.isEmpty()) return hex;
        }
        return null;
    }


    private Hex getRandomHexFromProvince(Province province) {
        ArrayList<Hex> hexes = province.getHexes();
        int index = random.nextInt(hexes.size());
        return hexes.get(index);
    }


    private boolean doesProvinceHaveEmptyHexes(Province province) {
        for (Hex hex : province.getHexes()) {
            if (hex.isEmpty()) return true;
        }
        return false;
    }


    private void updateSeed(Province province) {
        random.setSeed(117 + 119L * province.getHexes().size());
    }
}
