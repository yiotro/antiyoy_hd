package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetRejoinMatchData implements ReusableYio, Encodeable {

    public String name;
    public String matchId;
    public boolean hasCreator;
    public int turnSeconds;
    public boolean yourTurn;


    public NetRejoinMatchData() {
        reset();
    }


    @Override
    public void reset() {
        name = "-";
        matchId = "-";
        hasCreator = false;
        turnSeconds = -1;
        yourTurn = false;
    }


    @Override
    public String encode() {
        return name + "/" + matchId + "/" + hasCreator + "/" + turnSeconds + "/" + yourTurn;
    }


    public void decode(String source) {
        reset();
        String[] split = source.split("/");
        if (split.length < 4) return;
        name = split[0];
        matchId = split[1];
        hasCreator = Boolean.valueOf(split[2]);
        turnSeconds = Integer.valueOf(split[3]);
        yourTurn = Boolean.valueOf(split[4]);
    }


    public void setBy(NetRejoinMatchData src) {
        reset();
        name = src.name;
        matchId = src.matchId;
        hasCreator = src.hasCreator;
        turnSeconds = src.turnSeconds;
        yourTurn = src.yourTurn;
    }
}
