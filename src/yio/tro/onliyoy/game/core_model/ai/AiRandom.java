package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

import java.util.ArrayList;

public class AiRandom extends AbstractAI{

    private ArrayList<Hex> tempHexList;


    public AiRandom(CoreModel coreModel) {
        super(coreModel);
        tempHexList = new ArrayList<>();
    }


    @Override
    public int getVersionCode() {
        return 1;
    }


    @Override
    protected void apply() {
        ArrayList<Province> provinces = coreModel.provincesManager.provinces;
        for (int i = provinces.size() - 1; i >= 0; i--) {
            if (i >= provinces.size()) continue; // yes, it can happen
            Province province = provinces.get(i);
            if (!province.isValid()) continue;
            if (!isOwned(province)) continue;
            applyProvince(province);
        }
    }


    @Override
    protected DiplomaticAI getDiplomaticAI() {
        return new DiplomaticAiEasy(coreModel);
    }


    private void applyProvince(Province province) {
        moveUnitsRandomly(province);
        buildPeasantsRandomly(province);
    }


    private void buildPeasantsRandomly(Province province) {
        if (province.getMoney() < 15) return;
        if (random.nextFloat() > 0.33f) return;
        Hex emptyHex = getEmptyHex(province);
        if (emptyHex == null) return;
        commandUnitBuild(province, emptyHex, 1);
    }


    private Hex getEmptyHex(Province province) {
        if (!hasEmptyHexes(province)) return null;
        ArrayList<Hex> hexes = province.getHexes();
        for (int i = hexes.size() - 1; i >= 0; i--) {
            Hex hex = hexes.get(i);
            if (hex.isEmpty()) return hex;
        }
        return null;
    }


    private boolean hasEmptyHexes(Province province) {
        for (Hex hex : province.getHexes()) {
            if (hex.isEmpty()) return true;
        }
        return false;
    }


    private void moveUnitsRandomly(Province province) {
        populateTempHexListByUnits(province);
        for (Hex hex : tempHexList) {
            getMoveZoneManager().updateForUnit(hex);
            Hex targetHex = getRandomHexFromMoveZone();
            if (targetHex == null) continue;
            if (!targetHex.isEmpty() && !targetHex.hasTree()) continue;
            commandUnitMove(hex, targetHex);
        }
    }


    private void populateTempHexListByUnits(Province province) {
        tempHexList.clear();
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            if (!coreModel.readinessManager.isReady(hex)) continue;
            tempHexList.add(hex);
        }
    }


    private Hex getRandomHexFromMoveZone() {
        ArrayList<Hex> hexes = getMoveZoneManager().hexes;
        if (hexes.size() == 0) return null;
        int index = random.nextInt(hexes.size());
        return hexes.get(index);
    }

}
