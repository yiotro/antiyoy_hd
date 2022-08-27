package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetModeratorData implements ReusableYio, Encodeable {

    public int unverified;
    public int fresh;
    public int archive;
    public int currentOnline;
    public int reports;
    public int renaming;


    public NetModeratorData() {
        reset();
    }


    @Override
    public void reset() {
        unverified = 0;
        fresh = 0;
        archive = 0;
        currentOnline = 0;
        reports = 0;
        renaming = 0;
    }


    @Override
    public String encode() {
        return unverified + " " +
                fresh + " " +
                archive + " " +
                currentOnline + " " +
                reports + " " +
                renaming;
    }


    public void decode(String source) {
        if (source.length() < 6) return;
        String[] split = source.split(" ");
        if (split.length < 4) return;
        unverified = Integer.valueOf(split[0]);
        fresh = Integer.valueOf(split[1]);
        archive = Integer.valueOf(split[2]);
        currentOnline = Integer.valueOf(split[3]);
        reports = Integer.valueOf(split[4]);
        renaming = Integer.valueOf(split[5]);
    }
}
