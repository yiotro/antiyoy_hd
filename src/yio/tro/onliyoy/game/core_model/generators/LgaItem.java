package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class LgaItem implements Comparable<LgaItem>{

    Province province;
    double expansionPotential;
    int nearbyEnemies;
    double threat;
    double relativeEnemiesValue;
    double relativeSafetyValue;
    double attractiveness;
    double fixingMultiplier;


    @Override
    public String toString() {
        return "[LgaItem: " +
                province + " " +
                Yio.roundUp(attractiveness, 2) +
                "]";
    }


    String getDetailedInfoString() {
        return Yio.roundUp(expansionPotential, 2) + " | " +
                nearbyEnemies + " - " +
                Yio.roundUp(relativeEnemiesValue, 2) + " | " +
                Yio.roundUp(threat, 2) + " - " +
                Yio.roundUp(relativeSafetyValue, 2);
    }


    @Override
    public int compareTo(LgaItem o) {
        return (int) (1000 * (o.attractiveness - attractiveness));
    }
}
