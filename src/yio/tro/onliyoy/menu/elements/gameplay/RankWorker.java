package yio.tro.onliyoy.menu.elements.gameplay;

import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.net.shared.RankType;

public class RankWorker {


    public static String apply(RankType rankType, int elp) {
        String key = getKey(rankType, elp);
        String string = LanguagesManager.getInstance().getString(key);
        return string.toLowerCase();
    }


    private static String getKey(RankType rankType, int elp) {
        switch (rankType) {
            default:
                return "" + rankType;
            case elp:
                return getKey(elp);
        }
    }


    private static String getKey(int elp) {
        if (elp < 1100) return "apprentice";
        if (elp < 1200) return "bachelor";
        if (elp < 1300) return "rogue";
        if (elp < 1400) return "sentinel";
        if (elp < 1500) return "specialist";
        if (elp < 1600) return "professor";
        if (elp < 1700) return "shaman";
        if (elp < 1800) return "judge";
        if (elp < 1900) return "inquisitor";
        return "master";
    }

}
