package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PlayerEntity;

import java.util.ArrayList;
import java.util.Random;

public class LgClassicProvinceSpawner extends AbstractLgProvinceSpawner{

    private PlayerEntity[] entities;
    ArrayList<Hex> propagationList;


    public LgClassicProvinceSpawner(AbstractLevelGenerator abstractLevelGenerator) {
        super(abstractLevelGenerator);
        entities = null;
        propagationList = new ArrayList<>();
    }


    @Override
    void apply() {
        updateReferences();
        randomizeColors();
        ensurePresence();
    }


    private void ensurePresence() {
        for (int i = 0; i < entities.length; i++) {
            HColor color = entities[i].color;
            if (hasAtLeastOneProvince(color)) continue;
            ensurePresence(color);
        }
    }


    private void ensurePresence(HColor color) {
        Hex hex = getHexForNewProvince();
        spawnProvince(hex, color, 3);
    }


    private Hex getHexForNewProvince() {
        Hex bestHex = null;
        int maxValue = 0;
        for (Hex hex : getCoreModel().hexes) {
            int currentValue = countAdjacentLonelyHexes(hex);
            if (bestHex == null || currentValue > maxValue) {
                bestHex = hex;
                maxValue = currentValue;
            }
        }
        return bestHex;
    }


    private int countAdjacentLonelyHexes(Hex hex) {
        int c = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.isAdjacentToHexesOfSameColor()) continue;
            c++;
        }
        return c;
    }


    private boolean hasAtLeastOneProvince(HColor color) {
        for (Hex hex : getCoreModel().hexes) {
            if (hex.color != color) continue;
            if (!hex.isAdjacentToHexesOfSameColor()) continue;
            return true;
        }
        return false;
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
        Hex adjacentLonelyHex = getAdjacentLonelyHex(startHex);
        if (adjacentLonelyHex == null) return;
        adjacentLonelyHex.setColor(color);
    }


    private Hex getAdjacentLonelyHex(Hex hex) {
        if (!hasAdjacentLonelyHex(hex)) return null;
        int size = hex.adjacentHexes.size();
        while (true) {
            int index = abstractLevelGenerator.random.nextInt(size);
            Hex adjacentHex = hex.adjacentHexes.get(index);
            if (adjacentHex.isAdjacentToHexesOfSameColor()) continue;
            return adjacentHex;
        }
    }


    private boolean hasAdjacentLonelyHex(Hex hex) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.isAdjacentToHexesOfSameColor()) continue;
            return true;
        }
        return false;
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
                if (adjacentHex.isAdjacentToHexesOfSameColor()) continue;
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


    private void randomizeColors() {
        for (Hex hex : getCoreModel().hexes) {
            hex.setColor(getRandomColor());
        }
    }


    private HColor getRandomColor() {
        int index = abstractLevelGenerator.random.nextInt(entities.length);
        return entities[index].color;
    }


    private void updateReferences() {
        entities = abstractLevelGenerator.parameters.entities;
    }
}
