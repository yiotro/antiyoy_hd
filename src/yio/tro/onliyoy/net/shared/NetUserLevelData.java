package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class NetUserLevelData implements ReusableYio, Encodeable, Comparable<NetUserLevelData> {

    public String name;
    public LevelSize levelSize;
    public RulesType rulesType;
    public boolean diplomacy;
    public int colorsQuantity;
    public int likes;
    public int dislikes;
    public String id;
    public long creationTime;
    public long uploadTime;
    public long verificationTime;
    public UserLevelStatusType statusType;
    public String levelCode;
    public ArrayList<String> rateIps; // shouldn't be encoded


    public NetUserLevelData() {
        rateIps = new ArrayList<>();
        reset();
    }


    @Override
    public void reset() {
        name = "-";
        levelSize = LevelSize.tiny;
        rulesType = RulesType.def;
        diplomacy = false;
        colorsQuantity = 0;
        likes = 0;
        dislikes = 0;
        id = "-";
        creationTime = 0;
        uploadTime = 0;
        verificationTime = 0;
        statusType = UserLevelStatusType.unverified;
        levelCode = "-";
        rateIps.clear();
    }


    @Override
    public String encode() {
        return name + "/" +
                levelSize + "/" +
                rulesType + "/" +
                diplomacy + "/" +
                colorsQuantity + "/" +
                likes + "/" +
                dislikes + "/" +
                id + "/" +
                creationTime + "/" +
                uploadTime + "/" +
                verificationTime + "/" +
                statusType + "/" +
                levelCode;
    }


    public void decode(String source) {
        if (source.length() < 10) return;
        String[] split = source.split("/");
        if (split.length < 10) return;
        name = split[0];
        levelSize = LevelSize.valueOf(split[1]);
        rulesType = RulesType.valueOf(split[2]);
        diplomacy = Boolean.valueOf(split[3]);
        colorsQuantity = Integer.valueOf(split[4]);
        likes = Integer.valueOf(split[5]);
        dislikes = Integer.valueOf(split[6]);
        id = split[7];
        creationTime = Long.valueOf(split[8]);
        uploadTime = Long.valueOf(split[9]);
        verificationTime = Long.valueOf(split[10]);
        statusType = UserLevelStatusType.valueOf(split[11]);
        levelCode = split[12];
    }


    public int getSortingScore() {
        float ratio = (float) likes / (likes + dislikes);
        return (int) ((likes - dislikes) * ratio);
    }


    @Override
    public int compareTo(NetUserLevelData o) {
        return o.getSortingScore() - getSortingScore();
    }
}
