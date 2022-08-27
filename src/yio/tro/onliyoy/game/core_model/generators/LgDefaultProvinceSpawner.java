package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PlayerEntity;

import java.util.ArrayList;
import java.util.Random;

public class LgDefaultProvinceSpawner extends AbstractLgProvinceSpawner{

    ArrayList<Hex> propagationList;


    public LgDefaultProvinceSpawner(AbstractLevelGenerator abstractLevelGenerator) {
        super(abstractLevelGenerator);
        propagationList = new ArrayList<>();
    }


    @Override
    void apply() {
        PlayerEntity[] entities = abstractLevelGenerator.parameters.entities;
        for (PlayerEntity playerEntity : entities) {
            Hex hex = getHexForNewProvince();
            spawnProvince(hex, playerEntity.color, 2);
        }
    }


    private Hex getHexForNewProvince() {
        CoreModel coreModel = getCoreModel();
        if (!coreModel.searchWorker.isThereAtLeastOneColoredHex()) {
            return coreModel.searchWorker.getRandomHex();
        }

        prepareForAnalysis();
        performAnalysis();
        return findHexWithHighestCounter();
    }


    private Hex findHexWithHighestCounter() {
        Hex bestHex = null;
        for (Hex hex : getCoreModel().hexes) {
            if (bestHex == null || hex.counter > bestHex.counter) {
                bestHex = hex;
            }
        }
        return bestHex;
    }


    private void performAnalysis() {
        boolean expanded;
        int step = 0;
        while (true) {
            expanded = false;
            for (Hex hex : getCoreModel().hexes) {
                if (hex.counter != step) continue;
                for (Hex adjacentHex : hex.adjacentHexes) {
                    if (adjacentHex.counter != -1) continue;
                    adjacentHex.counter = step + 1;
                    expanded = true;
                }
            }
            if (!expanded) break;
            step++;
        }
    }


    private void prepareForAnalysis() {
        for (Hex hex : getCoreModel().hexes) {
            if (hex.isNeutral()) {
                hex.counter = -1;
                continue;
            }
            hex.counter = 0;
        }
    }


    public void spawnProvince(Hex startHex, HColor color, int radius) {
        clearLgFlags();
        tag(startHex, radius);
        for (Hex hex : getCoreModel().hexes) {
            if (!hex.lgFlag) continue;
            hex.setColor(color);
        }
        checkToFixLoneliness(startHex, color);
    }


    private void checkToFixLoneliness(Hex startHex, HColor color) {
        if (startHex.isAdjacentToHexesOfSameColor()) return;
        int size = startHex.adjacentHexes.size();
        if (size == 0) return;
        int index = abstractLevelGenerator.random.nextInt(size);
        Hex hex = startHex.adjacentHexes.get(index);
        hex.setColor(color);
    }


    private void clearLgFlags() {
        for (Hex hex : getCoreModel().hexes) {
            hex.lgFlag = false;
        }
    }


    private void tag(Hex startHex, int radius) {
        if (startHex.lgFlag) return;
        clearFlags();
        addToPropagationList(startHex, radius);
        Random random = abstractLevelGenerator.random;
        while (propagationList.size() > 0) {
            Hex firstHex = propagationList.get(0);
            propagationList.remove(0);
            if (random.nextInt(radius) >= firstHex.counter) continue;
            firstHex.lgFlag = true; // marked to be turned into land
            for (Hex adjacentHex : firstHex.adjacentHexes) {
                if (adjacentHex.lgFlag) continue;
                if (adjacentHex.flag) continue;
                addToPropagationList(adjacentHex, firstHex.counter - 1);
            }
        }
    }


    private void clearFlags() {
        for (Hex hex : getCoreModel().hexes) {
            hex.flag = false;
        }
    }


    private void addToPropagationList(Hex hex, int counter) {
        hex.flag = true;
        hex.counter = counter;
        propagationList.add(hex);
    }

}
