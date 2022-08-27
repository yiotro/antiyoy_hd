package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetAdminData implements Encodeable, ReusableYio {

    public int online;
    public int matchesInPreparationState;
    public int matchesInBattleState;
    public float averageOnline;


    public NetAdminData() {
        reset();
    }


    @Override
    public void reset() {
        online = 0;
        matchesInPreparationState = 0;
        matchesInBattleState = 0;
        averageOnline = 0;
    }


    @Override
    public String encode() {
        return online + " " + matchesInPreparationState + " " + matchesInBattleState + " " + averageOnline;
    }


    public void decode(String source) {
        String[] split = source.split(" ");
        online = Integer.valueOf(split[0]);
        matchesInPreparationState = Integer.valueOf(split[1]);
        matchesInBattleState = Integer.valueOf(split[2]);
        averageOnline = Float.valueOf(split[3]);
    }
}
