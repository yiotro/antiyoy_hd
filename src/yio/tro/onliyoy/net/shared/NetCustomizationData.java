package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NetCustomizationData implements ReusableYio, Encodeable {

    public ArrayList<PhraseType> phrases;
    StringBuilder stringBuilder;
    private Comparator<PhraseType> comparator;
    public SkinType skinType;
    public RankType rankType;
    public AvatarType avatarType;


    public NetCustomizationData() {
        phrases = new ArrayList<>();
        stringBuilder = new StringBuilder();
        skinType = SkinType.def;
        rankType = RankType.elp;
        avatarType = AvatarType.empty;
        initComparator();
    }


    private void initComparator() {
        comparator = new Comparator<PhraseType>() {
            @Override
            public int compare(PhraseType o1, PhraseType o2) {
                return o1.ordinal() - o2.ordinal();
            }
        };
    }


    @Override
    public void reset() {
        phrases.clear();
        addDefaultPhrases();
        skinType = SkinType.def;
        rankType = RankType.elp;
        avatarType = AvatarType.empty;
    }


    private void addDefaultPhrases() {
        phrases.add(PhraseType.hi);
        phrases.add(PhraseType.goodbye);
        phrases.add(PhraseType.thanks);
        phrases.add(PhraseType.check_my_win_rate);
        phrases.add(PhraseType.check_my_play_time);
        phrases.add(PhraseType.talk_on_discord_server);
        phrases.add(PhraseType.this_battle_will_be_epic);
        phrases.add(PhraseType.well_played);
        phrases.add(PhraseType.good_luck);
        phrases.add(PhraseType.check_my_elp);
        phrases.add(PhraseType.check_my_rank);
    }


    @Override
    public String encode() {
        return encodePhrases() + "/" + skinType + "/" + rankType + "/" + avatarType;
    }


    private String encodePhrases() {
        stringBuilder.setLength(0);
        for (PhraseType phraseType : phrases) {
            stringBuilder.append(phraseType).append(" ");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 3) return;
        if (source.equals("null")) return;
        String[] split = source.split("/");
        if (split.length < 1) return;
        decodePhrases(split[0]);
        if (split.length > 1) {
            skinType = SkinType.valueOf(split[1]);
        }
        if (split.length > 2) {
            rankType = RankType.valueOf(split[2]);
        }
        if (split.length > 3) {
            avatarType = AvatarType.valueOf(split[3]);
        }
    }


    private void decodePhrases(String source) {
        for (String token : source.split(" ")) {
            if (token.length() < 2) continue;
            PhraseType phraseType;
            try {
                phraseType = PhraseType.valueOf(token);
            } catch (IllegalArgumentException e) {
                continue;
            }
            if (phrases.contains(phraseType)) continue;
            phrases.add(phraseType);
        }
        Collections.sort(phrases, comparator);
    }
}
