package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NetShopData implements ReusableYio, Encodeable {

    public int fishPrice;
    public HashMap<PhraseType, Integer> mapPhrases;
    public HashMap<SkinType, Integer> mapSkins;
    public HashMap<RankType, Integer> mapRanks;
    public HashMap<AvatarType, Integer> mapAvatars;
    private StringBuilder stringBuilder;


    public NetShopData() {
        mapPhrases = new LinkedHashMap<>();
        mapSkins = new LinkedHashMap<>();
        mapRanks = new LinkedHashMap<>();
        mapAvatars = new LinkedHashMap<>();
        stringBuilder = new StringBuilder();
        reset();
    }


    @Override
    public void reset() {
        fishPrice = 0;
        mapPhrases.clear();
        mapSkins.clear();
        mapRanks.clear();
        mapAvatars.clear();
    }


    @Override
    public String encode() {
        return fishPrice + "/" +
                encodeMap(mapPhrases) + "/" +
                encodeMap(mapSkins) + "/" +
                encodeMap(mapRanks) + "/" +
                encodeMap(mapAvatars);
    }


    private String encodeMap(HashMap<?, Integer> map) {
        stringBuilder.setLength(0);
        for (Map.Entry<?, Integer> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" ").append(entry.getValue()).append(",");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        String[] split = source.split("/");
        if (split.length == 0) return;
        fishPrice = Integer.valueOf(split[0]);
        decodePhrases(split[1]);
        if (split.length > 2) {
            decodeSkins(split[2]);
        }
        if (split.length > 3) {
            decodeRanks(split[3]);
        }
        if (split.length > 4) {
            decodeAvatars(split[4]);
        }
    }


    private void decodeAvatars(String source) {
        for (String token : source.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 2) continue;
            AvatarType avatarType = AvatarType.valueOf(split[0]);
            int price = Integer.valueOf(split[1]);
            mapAvatars.put(avatarType, price);
        }
    }


    private void decodeRanks(String source) {
        for (String token : source.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 2) continue;
            RankType rankType = RankType.valueOf(split[0]);
            int price = Integer.valueOf(split[1]);
            mapRanks.put(rankType, price);
        }
    }


    private void decodeSkins(String source) {
        for (String token : source.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 2) continue;
            SkinType skinType = SkinType.valueOf(split[0]);
            int price = Integer.valueOf(split[1]);
            mapSkins.put(skinType, price);
        }
    }


    private void decodePhrases(String source) {
        for (String token : source.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 2) continue;
            PhraseType phraseType = PhraseType.valueOf(split[0]);
            int price = Integer.valueOf(split[1]);
            mapPhrases.put(phraseType, price);
        }
    }
}
