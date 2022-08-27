package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetPlayerStatisticsData implements ReusableYio, Encodeable {

    public int matchesPlayed;
    public int matchesWon;
    public long timeOnline;
    public long signInTime; // not saved, used only during session
    public long experience;


    public NetPlayerStatisticsData() {
        reset();
    }


    @Override
    public void reset() {
        matchesPlayed = 0;
        matchesWon = 0;
        timeOnline = 0;
        signInTime = System.currentTimeMillis();
        experience = 0;
    }


    @Override
    public String encode() {
        return matchesPlayed + " " + matchesWon + " " + timeOnline + " " + experience;
    }


    public void decode(String source) {
        reset();
        if (source.length() < 5) return;
        String[] split = source.split(" ");
        if (split.length < 3) return;
        matchesPlayed = Integer.valueOf(split[0]);
        matchesWon = Integer.valueOf(split[1]);
        timeOnline = Long.valueOf(split[2]);
        if (split.length > 3) {
            experience = Long.valueOf(split[3]);
        }
    }


    public long getHoursOnline() {
        return timeOnline / (1000 * 60 * 60);
    }
}
