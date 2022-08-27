package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetMatchListData implements ReusableYio, Encodeable {


    public LevelSize levelSize;
    public RulesType rulesType;
    public boolean diplomacy;
    public int playersQuantity;
    public int turnSeconds;
    public boolean hasPassword;
    public String name;
    public String matchId;
    public int currentQuantity;
    public int maxHumanQuantity;
    public boolean fog;


    public NetMatchListData() {
        reset();
    }


    @Override
    public void reset() {
        levelSize = null;
        rulesType = null;
        diplomacy = false;
        playersQuantity = 0;
        turnSeconds = 0;
        hasPassword = false;
        name = "-";
        matchId = "-";
        currentQuantity = 0;
        maxHumanQuantity = 0;
        fog = false;
    }


    @Override
    public String encode() {
        return name + "/" +
                levelSize + "/" +
                rulesType + "/" +
                diplomacy + "/" +
                playersQuantity + "/" +
                turnSeconds + "/" +
                hasPassword + "/" +
                matchId + "/" +
                currentQuantity + "/" +
                maxHumanQuantity + "/" +
                fog;
    }


    public void decode(String source) {
        String[] split = source.split("/");
        name = split[0];
        levelSize = LevelSize.valueOf(split[1]);
        rulesType = RulesType.valueOf(split[2]);
        diplomacy = Boolean.valueOf(split[3]);
        playersQuantity = Integer.valueOf(split[4]);
        turnSeconds = Integer.valueOf(split[5]);
        hasPassword = Boolean.valueOf(split[6]);
        matchId = split[7];
        currentQuantity = Integer.valueOf(split[8]);
        maxHumanQuantity = Integer.valueOf(split[9]);
        if (split.length > 10) {
            fog = Boolean.valueOf(split[10]);
        }
    }


    public void setBy(NetMatchLobbyData netMatchLobbyData) {
        levelSize = netMatchLobbyData.levelSize;
        rulesType = netMatchLobbyData.rulesType;
        diplomacy = netMatchLobbyData.diplomacy;
        playersQuantity = netMatchLobbyData.entities.size();
        turnSeconds = netMatchLobbyData.turnSeconds;
        hasPassword = netMatchLobbyData.hasPassword();
        fog = netMatchLobbyData.fog;
        maxHumanQuantity = 0;
        for (PlayerEntity entity : netMatchLobbyData.entities) {
            if (!entity.isHuman()) continue;
            maxHumanQuantity++;
        }
    }
}
