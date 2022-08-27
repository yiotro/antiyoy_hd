package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.EventHexChangeColor;
import yio.tro.onliyoy.game.core_model.events.EventPieceAdd;
import yio.tro.onliyoy.game.core_model.events.EventsFactory;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.ArrayList;
import java.util.Collections;

public class LgDefaultProvinceBalancer extends AbstractLgProvinceBalancer{

    private ArrayList<LgaItem> analysisItems;
    private LgaItem mostAttractiveItem;
    private ArrayList<Hex> tempHexList;
    int baseIncome; // income of most attractive province


    public LgDefaultProvinceBalancer(AbstractLevelGenerator abstractLevelGenerator) {
        super(abstractLevelGenerator);
        tempHexList = new ArrayList<>();
    }


    @Override
    void apply() {
        LgProvinceAnalyzer provinceAnalyzer = abstractLevelGenerator.provinceAnalyzer;
        analysisItems = provinceAnalyzer.perform();
        updateFixingMultipliers();
        updateBaseIncome();
        showAnalysisResultsInConsole();
        changeEconomicsToAchieveBalance();
    }


    private void changeEconomicsToAchieveBalance() {
        for (LgaItem lgaItem : analysisItems) {
            if (lgaItem == mostAttractiveItem) continue;
            int targetIncome = (int) (lgaItem.fixingMultiplier * baseIncome);
            achieveIncome(lgaItem.province, targetIncome);
        }
    }


    private void achieveIncome(Province province, int targetIncome) {
        EconomicsManager economicsManager = getCoreModel().economicsManager;
        int income = economicsManager.calculateProvinceIncome(province);
        int delta = targetIncome - income;
        if (delta == 0) return;
        if (delta < 0) {
            removeMultipleHexes(province, Math.abs(delta));
            return;
        }
        giveAdditionalIncome(province, delta);
    }


    private void giveAdditionalIncome(Province province, int delta) {
        AbstractRuleset ruleset = getCoreModel().ruleset;
        int additionalIncomePerFarm = ruleset.getHexIncome(PieceType.farm) - ruleset.getHexIncome(null);
        while (additionalIncomePerFarm <= delta) {
            buildFarm(province);
            delta -= additionalIncomePerFarm;
        }
        if (delta == 0) return;
        int hexesToRemoveForFarmManeuver = additionalIncomePerFarm - delta;
        if (province.getHexes().size() - hexesToRemoveForFarmManeuver >= 2 && !province.contains(PieceType.farm)) {
            removeMultipleHexes(province, hexesToRemoveForFarmManeuver);
            buildFarm(province);
            return;
        }
        addMultipleHexes(province, delta);
    }


    private void addMultipleHexes(Province province, int quantity) {
        for (int i = 0; i < quantity; i++) {
            addOneHex(province);
        }
    }


    private void addOneHex(Province province) {
        Hex hex = getAdjacentNeutralHex(province);
        if (hex == null) return;
        EventsManager eventsManager = getCoreModel().eventsManager;
        EventsFactory factory = eventsManager.factory;
        EventHexChangeColor event = factory.createChangeHexColorEvent(hex, province.getColor());
        eventsManager.applyEvent(event);
    }


    private Hex getAdjacentNeutralHex(Province province) {
        updateTempHexListByAdjacentNeutralHexes(province);
        return findMostAdjacentHex(tempHexList, province.getColor());
    }


    private Hex findMostAdjacentHex(ArrayList<Hex> list, HColor color) {
        Hex bestHex = null;
        int maxValue = 0;
        for (Hex hex : list) {
            int currentValue = countAdjacentColoredHexes(hex, color);
            if (bestHex == null || currentValue > maxValue) {
                bestHex = hex;
                maxValue = currentValue;
            }
        }
        return bestHex;
    }


    private int countAdjacentColoredHexes(Hex hex, HColor targetColor) {
        int c = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != targetColor) continue;
            c++;
        }
        return c;
    }


    private void updateTempHexListByAdjacentNeutralHexes(Province province) {
        tempHexList.clear();
        for (Hex hex : province.getHexes()) {
            for (Hex adjacentHex : hex.adjacentHexes) {
                if (!adjacentHex.isNeutral()) continue;
                if (tempHexList.contains(adjacentHex)) continue;
                tempHexList.add(adjacentHex);
            }
        }
    }


    private void removeMultipleHexes(Province province, int quantity) {
        for (int i = 0; i < quantity; i++) {
            if (province.getHexes().size() < 3) break;
            removeOneHex(province);
        }
    }


    private void buildFarm(Province province) {
        Hex emptyHex = getEmptyHex(province);
        EventsManager eventsManager = getCoreModel().eventsManager;
        EventsFactory factory = eventsManager.factory;
        EventPieceAdd event = factory.createAddPieceEvent(emptyHex, PieceType.farm);
        eventsManager.applyEvent(event);
    }


    private Hex getEmptyHex(Province province) {
        for (Hex hex : province.getHexes()) {
            if (hex.isEmpty()) return hex;
        }
        return null;
    }


    private void removeOneHex(Province province) {
        Hex mostLonelyHex = findMostLonelyHex(province);
        EventsManager eventsManager = getCoreModel().eventsManager;
        EventsFactory factory = eventsManager.factory;
        EventHexChangeColor event = factory.createChangeHexColorEvent(mostLonelyHex, HColor.gray);
        eventsManager.applyEvent(event);
        if (mostLonelyHex.hasPiece()) {
            eventsManager.applyEvent(factory.createDeletePieceEvent(mostLonelyHex));
        }
    }


    private Hex findMostLonelyHex(Province province) {
        Hex bestHex = null;
        int minValue = 0;
        for (Hex hex : province.getHexes()) {
            int currentValue = countFriendlyAdjacentHexes(hex);
            if (bestHex == null || currentValue < minValue) {
                bestHex = hex;
                minValue = currentValue;
            }
        }
        return bestHex;
    }


    private int countFriendlyAdjacentHexes(Hex hex) {
        int c = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            c++;
        }
        return c;
    }


    private void updateBaseIncome() {
        mostAttractiveItem = getMostAttractiveItem();
        if (mostAttractiveItem == null) return;
        checkToMakeMostAttractiveProvinceWeaker();
        Province province = mostAttractiveItem.province;
        EconomicsManager economicsManager = getCoreModel().economicsManager;
        baseIncome = economicsManager.calculateProvinceIncome(province);
    }


    private void checkToMakeMostAttractiveProvinceWeaker() {
        Province province = mostAttractiveItem.province;
        while (province.getHexes().size() > 3) {
            removeOneHex(province);
        }
    }


    private LgaItem getMostAttractiveItem() {
        LgaItem bestItem = null;
        for (LgaItem lgaItem : analysisItems) {
            if (bestItem == null || lgaItem.attractiveness > bestItem.attractiveness) {
                bestItem = lgaItem;
            }
        }
        return bestItem;
    }


    private void updateFixingMultipliers() {
        double maxAttractiveness = getMaxAttractiveness();
        for (LgaItem lgaItem : analysisItems) {
            lgaItem.fixingMultiplier = maxAttractiveness / lgaItem.attractiveness;
            // people generally don't like it when game gives kingdoms too many farms
            // so this line reduces impact of balancing algorithm
            lgaItem.fixingMultiplier = Math.sqrt(lgaItem.fixingMultiplier);
            lgaItem.fixingMultiplier *= abstractLevelGenerator.parameters.balancerIntensity;
        }
    }


    private double getMaxAttractiveness() {
        double maxValue = -1;
        for (LgaItem lgaItem : analysisItems) {
            if (maxValue == -1 || lgaItem.attractiveness > maxValue) {
                maxValue = lgaItem.attractiveness;
            }
        }
        return maxValue;
    }


    private void showAnalysisResultsInConsole() {
        if (!DebugFlags.debugLevelGenerator) return;
        Collections.sort(analysisItems);
        System.out.println();
        System.out.println("LgProvinceBalancer.apply");
        for (LgaItem analysisItem : analysisItems) {
            System.out.println("- " + analysisItem + "  ->  " + analysisItem.getDetailedInfoString());
        }
    }

}
