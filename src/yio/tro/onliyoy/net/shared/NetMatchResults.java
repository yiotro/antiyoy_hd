package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.MatchResults;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetMatchResults implements ReusableYio, Encodeable {

    public HColor winnerColor;
    public EntityType winnerEntityType;
    public String winnerName;
    public int moneyDeltaValue;
    public int elpDeltaValue;
    public String matchId;


    @Override
    public void reset() {
        winnerColor = null;
        winnerEntityType = null;
        setWinnerName("");
        moneyDeltaValue = 0;
        elpDeltaValue = 0;
        matchId = "-";
    }


    @Override
    public String encode() {
        return winnerColor + "/" + winnerEntityType + "/" + winnerName + "/" + moneyDeltaValue + "/" + elpDeltaValue + "/" + matchId;
    }


    public void decode(String source) {
        try {
            String[] split = source.split("/");
            if (split.length < 3) return;
            winnerColor = HColor.valueOf(split[0]);
            winnerEntityType = EntityType.valueOf(split[1]);
            winnerName = split[2];
            moneyDeltaValue = Integer.valueOf(split[3]);
            elpDeltaValue = Integer.valueOf(split[4]);
            matchId = split[5];
        } catch (Exception e) {
            reset();
        }
    }


    public void setBy(MatchResults matchResults) {
        reset();
        winnerColor = matchResults.winnerColor;
        winnerEntityType = matchResults.entityType;
    }


    public void setWinnerColor(HColor winnerColor) {
        this.winnerColor = winnerColor;
    }


    public void setWinnerEntityType(EntityType winnerEntityType) {
        this.winnerEntityType = winnerEntityType;
    }


    public boolean areDeltaValuesEqualToZero() {
        return moneyDeltaValue == 0 && elpDeltaValue == 0;
    }


    public void setWinnerName(String winnerName) {
        if (winnerName.length() == 0) {
            winnerName = "-";
        }
        this.winnerName = winnerName;
    }
}
