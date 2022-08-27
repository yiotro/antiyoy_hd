package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetMatchStatisticsData implements ReusableYio, Encodeable {

    public int turnsMade;
    public int unitsBuilt;
    public int unitsMerged;
    public int unitsDied;
    public int moneySpent;
    public int treesFelled;
    public int maxProfit;
    public int firstAttackLap;


    public NetMatchStatisticsData() {
        reset();
    }


    @Override
    public void reset() {
        turnsMade = 0;
        unitsBuilt = 0;
        unitsMerged = 0;
        unitsDied = 0;
        moneySpent = 0;
        treesFelled = 0;
        maxProfit = 0;
        firstAttackLap = -1;
    }


    @Override
    public String encode() {
        return turnsMade + " " +
                unitsBuilt + " " +
                unitsMerged + " " +
                unitsDied + " " +
                moneySpent + " " +
                treesFelled + " " +
                maxProfit + " " +
                firstAttackLap;
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 5) return;
        String[] split = source.split(" ");
        if (split.length < 5) return;
        turnsMade = Integer.valueOf(split[0]);
        unitsBuilt = Integer.valueOf(split[1]);
        unitsMerged = Integer.valueOf(split[2]);
        unitsDied = Integer.valueOf(split[3]);
        moneySpent = Integer.valueOf(split[4]);
        treesFelled = Integer.valueOf(split[5]);
        maxProfit = Integer.valueOf(split[6]);
        firstAttackLap = Integer.valueOf(split[7]);
    }
}
