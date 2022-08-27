package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class NetPurchasesData implements ReusableYio, Encodeable {

    public ArrayList<PhraseType> phrases;
    public ArrayList<SkinType> skins;
    public ArrayList<RankType> ranks;
    public ArrayList<AvatarType> avatars;
    StringBuilder stringBuilder;


    public NetPurchasesData() {
        stringBuilder = new StringBuilder();
        phrases = new ArrayList<>();
        skins = new ArrayList<>();
        ranks = new ArrayList<>();
        avatars = new ArrayList<>();
    }


    @Override
    public void reset() {
        phrases.clear();
        skins.clear();
        skins.add(SkinType.def);
        ranks.clear();
        ranks.add(RankType.elp);
        avatars.clear();
        avatars.add(AvatarType.empty);
    }


    @Override
    public String encode() {
        return encodeList(phrases) + "/" +
                encodeList(skins) + "/" +
                encodeList(ranks) + "/" +
                encodeList(avatars);
    }


    private String encodeList(ArrayList<?> list) {
        stringBuilder.setLength(0);
        for (Object o : list) {
            stringBuilder.append(o).append(" ");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 3) return;
        String[] split = source.split("/");
        if (split.length < 1) return;
        decodePhrases(split[0]);
        if (split.length > 1) {
            decodeSkins(split[1]);
        }
        if (split.length > 2) {
            decodeRanks(split[2]);
        }
        if (split.length > 3) {
            decodeAvatars(split[3]);
        }
    }


    private void decodeAvatars(String source) {
        for (String token : source.split(" ")) {
            if (token.length() < 2) continue;
            AvatarType avatarType = AvatarType.valueOf(token);
            if (avatars.contains(avatarType)) continue;
            avatars.add(avatarType);
        }
    }


    private void decodeRanks(String source) {
        for (String token : source.split(" ")) {
            if (token.length() < 2) continue;
            RankType rankType = RankType.valueOf(token);
            if (ranks.contains(rankType)) continue;
            ranks.add(rankType);
        }
    }


    private void decodeSkins(String source) {
        for (String token : source.split(" ")) {
            if (token.length() < 2) continue;
            SkinType skinType = SkinType.valueOf(token);
            if (skins.contains(skinType)) continue;
            skins.add(skinType);
        }
    }


    private void decodePhrases(String source) {
        for (String token : source.split(" ")) {
            if (token.length() < 2) continue;
            PhraseType phraseType = PhraseType.valueOf(token);
            phrases.add(phraseType);
        }
    }
}
