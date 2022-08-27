package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.HColor;

import java.util.ArrayList;
import java.util.Random;

public class NecAnalyzer {

    // this is temporary class, it should be removed in future
    private Random random;
    private String levelCode;
    private ArrayList<String> acceptableColorStrings;
    private boolean localMode;


    public NecAnalyzer(boolean localMode) {
        this.localMode = localMode;
        random = new Random();
        initAcceptableColorStrings();
    }


    private void initAcceptableColorStrings() {
        acceptableColorStrings = new ArrayList<>();
        for (HColor color : HColor.values()) {
            acceptableColorStrings.add("" + color);
        }
    }


    public synchronized void apply(String src) throws NecException {
        if (!localMode && random.nextInt(10) != 0) return; // to decrease number of entries in logs
        levelCode = src;
        if (levelCode == null) return;
        if (levelCode.length() < 10) return;
        analyzeSection("hexes");
        analyzeSection("starting_hexes");
    }


    private void analyzeSection(String name) throws NecException {
        String section = getSection(name);
        if (section == null) return;
        for (String token : section.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 3) continue;
            String colorString = split[2];
            if (isAcceptable(colorString)) continue;
            throw new NecException(colorString);
        }
    }


    private boolean isAcceptable(String colorString) {
        if (colorString == null) return false;
        if (colorString.length() < 2) return false;
        for (String s : acceptableColorStrings) {
            if (s.equals(colorString)) return true;
        }
        return false;
    }


    private String getSection(String name) {
        int sectionIndex = levelCode.indexOf("#" + name);
        if (sectionIndex == -1) return null;
        int colonIndex = levelCode.indexOf(":", sectionIndex);
        int hashIndex = levelCode.indexOf("#", colonIndex);
        if (hashIndex - colonIndex < 2) return null;
        return levelCode.substring(colonIndex + 1, hashIndex);
    }

}
