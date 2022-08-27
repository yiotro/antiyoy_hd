package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetUserDossierData implements ReusableYio, Encodeable {

    public String name;
    public int money;
    public NetRole role;
    public int matchesPlayed;
    public int matchesWon;
    public long timeOnline;
    public long experience;
    public int elp;
    public RankType rankType;
    public boolean hidden;


    public NetUserDossierData() {
        reset();
    }


    @Override
    public void reset() {
        name = "-";
        money = 0;
        role = null;
        matchesPlayed = 0;
        matchesWon = 0;
        timeOnline = 0;
        experience = 0;
        elp = 0;
        rankType = RankType.elp;
        hidden = false;
    }


    @Override
    public String encode() {
        return name + "/" + money + "/" + role + "/" + matchesPlayed + "/" + matchesWon + "/" + timeOnline + "/" + experience + "/" + elp + "/" + rankType + "/" + hidden;
    }


    public void decode(String source) {
        reset();
        String[] split = source.split("/");
        name = split[0];
        money = Integer.valueOf(split[1]);
        if (!split[2].equals("null")) {
            role = NetRole.valueOf(split[2]);
        }
        matchesPlayed = Integer.valueOf(split[3]);
        matchesWon = Integer.valueOf(split[4]);
        timeOnline = Long.valueOf(split[5]);
        if (split.length > 6) {
            experience = Long.valueOf(split[6]);
        }
        if (split.length > 7) {
            elp = Integer.valueOf(split[7]);
        }
        if (split.length > 8) {
            rankType = RankType.valueOf(split[8]);
        }
        if (split.length > 9) {
            hidden = Boolean.valueOf(split[9]);
        }
    }


    public void copyFrom(NetUserData netUserData) {
        name = netUserData.name;
        money = netUserData.money;
        role = netUserData.role;
        experience = netUserData.experience;
        elp = netUserData.elp;
    }
}
