package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.ArrayList;
import java.util.Random;

public class CmSearchWorker {

    // currently this worker is only used during map generation
    CoreModel coreModel;
    private Random random;


    public CmSearchWorker(CoreModel coreModel) {
        this.coreModel = coreModel;
        random = new Random();
        if (DebugFlags.determinedRandom) {
            random = new Random(0);
        }
    }


    public Hex getRandomHex() {
        int index = random.nextInt(getHexes().size());
        return getHexes().get(index);
    }


    public Hex getRandomEmptyHex() {
        if (!isThereAtLeastOneEmptyHex()) return null;
        int c = 0;
        while (c < 1000) {
            c++;
            Hex randomHex = getRandomHex();
            if (randomHex.isEmpty()) return randomHex;
        }
        return null;
    }


    public boolean isThereAtLeastOneEmptyHex() {
        for (Hex hex : getHexes()) {
            if (hex.isEmpty()) return true;
        }
        return false;
    }


    public Hex getRandomEmptyNeutralHex() {
        if (!isThereAtLeastOneEmptyNeutralHex()) return null;
        int c = 0;
        while (c < 1000) {
            c++;
            Hex hex = getRandomEmptyHex();
            if (hex.isNeutral()) return hex;
        }
        return null;
    }


    public boolean isThereAtLeastOneEmptyNeutralHex() {
        for (Hex hex : getHexes()) {
            if (!hex.isEmpty()) continue;
            if (!hex.isNeutral()) continue;
            return true;
        }
        return false;
    }


    public boolean isThereAtLeastOneColoredHex() {
        for (Hex hex : getHexes()) {
            if (!hex.isColored()) continue;
            return true;
        }
        return false;
    }


    public int countTrees() {
        int c = 0;
        for (Hex hex : getHexes()) {
            if (!hex.hasTree()) continue;
            c++;
        }
        return c;
    }


    public int countColorOrdinals() {
        int c = 0;
        for (Hex hex : getHexes()) {
            c += hex.color.ordinal();
        }
        return c;
    }


    public void setSeed(long seed) {
        random.setSeed(seed);
    }


    private ArrayList<Hex> getHexes() {
        return coreModel.hexes;
    }
}
