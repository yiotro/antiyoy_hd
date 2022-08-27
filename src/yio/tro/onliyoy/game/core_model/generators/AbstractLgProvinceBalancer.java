package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.EventsManager;

import java.util.ArrayList;

public abstract class AbstractLgProvinceBalancer {

    AbstractLevelGenerator abstractLevelGenerator;


    public AbstractLgProvinceBalancer(AbstractLevelGenerator abstractLevelGenerator) {
        this.abstractLevelGenerator = abstractLevelGenerator;
    }


    abstract void apply();


    void ensureCities() {
        CoreModel coreModel = getCoreModel();
        EventsManager eventsManager = coreModel.eventsManager;
        for (Province province : coreModel.provincesManager.provinces) {
            if (province.contains(PieceType.city)) continue;
            Hex hex = getRandomEmptyHex(province);
            if (hex == null) {
                System.out.println("LgProvinceBalancer.ensureCities: problem");
                continue;
            }
            if (hex.hasPiece()) {
                eventsManager.applyEvent(eventsManager.factory.createDeletePieceEvent(hex));
            }
            eventsManager.applyEvent(eventsManager.factory.createAddPieceEvent(hex, PieceType.city));
        }
    }


    private Hex getRandomEmptyHex(Province province) {
        if (province.getHexes().size() == 0) return null;
        if (containsEmptyHex(province)) return province.getFirstHex();
        int c = 0;
        while (c < 1000) {
            c++;
            Hex hex = getRandomHex(province);
            if (hex.isEmpty()) return hex;
        }
        return null;
    }


    private Hex getRandomHex(Province province) {
        ArrayList<Hex> hexes = province.getHexes();
        int index = abstractLevelGenerator.random.nextInt(hexes.size());
        return hexes.get(index);
    }


    private boolean containsEmptyHex(Province province) {
        for (Hex hex : province.getHexes()) {
            if (hex.isEmpty()) return true;
        }
        return false;
    }


    protected CoreModel getCoreModel() {
        return abstractLevelGenerator.coreModel;
    }
}
