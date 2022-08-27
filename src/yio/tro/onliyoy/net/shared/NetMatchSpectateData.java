package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class NetMatchSpectateData implements ReusableYio, Encodeable {


    public LevelSize levelSize;
    public RulesType rulesType;
    public boolean diplomacy;
    public boolean hasPassword;
    public String name;
    public String matchId;
    public ArrayList<String> participants;
    StringBuilder stringBuilder;


    public NetMatchSpectateData() {
        participants = new ArrayList<>();
        stringBuilder = new StringBuilder();
        reset();
    }


    @Override
    public void reset() {
        levelSize = null;
        rulesType = null;
        diplomacy = false;
        hasPassword = false;
        name = "-";
        matchId = "-";
        participants.clear();
        stringBuilder.setLength(0);
    }


    @Override
    public String encode() {
        return name + "/" + levelSize + "/" + rulesType + "/" + diplomacy + "/" + hasPassword + "/" + matchId + "/" + encodeParticipants();
    }


    private String encodeParticipants() {
        if (participants.size() == 0) return "-";
        stringBuilder.setLength(0);
        for (String string : participants) {
            stringBuilder.append(string).append("!");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        String[] split = source.split("/");
        name = split[0];
        levelSize = LevelSize.valueOf(split[1]);
        rulesType = RulesType.valueOf(split[2]);
        diplomacy = Boolean.valueOf(split[3]);
        hasPassword = Boolean.valueOf(split[4]);
        matchId = split[5];
        decodeParticipants(split[6]);
    }


    private void decodeParticipants(String source) {
        participants.clear();
        for (String token : source.split("!")) {
            if (token.length() == 0) continue;
            participants.add(token);
        }
    }


    public void setBy(NetMatchLobbyData netMatchLobbyData) {
        levelSize = netMatchLobbyData.levelSize;
        rulesType = netMatchLobbyData.rulesType;
        diplomacy = netMatchLobbyData.diplomacy;
        hasPassword = netMatchLobbyData.hasPassword();
        matchId = netMatchLobbyData.matchId;
    }
}
