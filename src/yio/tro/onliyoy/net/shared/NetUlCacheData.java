package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetUlCacheData implements ReusableYio, Encodeable {

    public String name;
    public LevelSize levelSize;
    public RulesType rulesType;
    public boolean diplomacy;
    public int colorsQuantity;
    public int likes;
    public int dislikes;
    public String id;


    public NetUlCacheData() {
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
                id;
    }


    public void decode(String source) {
        if (source.length() < 10) return;
        String[] split = source.split("/");
        if (split.length < 7) return;
        name = split[0];
        levelSize = LevelSize.valueOf(split[1]);
        rulesType = RulesType.valueOf(split[2]);
        diplomacy = Boolean.valueOf(split[3]);
        colorsQuantity = Integer.valueOf(split[4]);
        likes = Integer.valueOf(split[5]);
        dislikes = Integer.valueOf(split[6]);
        id = split[7];
    }


    public void setBy(NetUserLevelData netUserLevelData) {
        name = netUserLevelData.name;
        levelSize = netUserLevelData.levelSize;
        rulesType = netUserLevelData.rulesType;
        diplomacy = netUserLevelData.diplomacy;
        colorsQuantity = netUserLevelData.colorsQuantity;
        likes = netUserLevelData.likes;
        dislikes = netUserLevelData.dislikes;
        id = netUserLevelData.id;
    }
}
