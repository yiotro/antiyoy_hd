package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetPhraseData implements ReusableYio, Encodeable {

    public String speakerId;
    public PhraseType phraseType;
    public String argument;


    @Override
    public void reset() {
        speakerId = "-";
        phraseType = null;
        argument = "-";
    }


    @Override
    public String encode() {
        return speakerId + "/" + phraseType + "/" + argument;
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 5) return;
        String[] split = source.split("/");
        if (split.length < 3) return;
        if (split[1].equals("null")) return;
        speakerId = split[0];
        phraseType = PhraseType.valueOf(split[1]);
        argument = split[2];
    }
}
