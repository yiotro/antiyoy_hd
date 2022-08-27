package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.export_import.Encodeable;

import java.util.ArrayList;

public class Condition implements Encodeable {

    public Letter letter;
    public ConditionType type;
    public HColor executorColor;
    public RelationType argRelationType;
    public HColor argColor;
    public int argMoney;
    public int argLock;
    public ArrayList<Hex> argHexes;
    public ArrayList<SmileyType> smileys;


    public Condition() {
        letter = null;
        type = null;
        executorColor = null;
        argRelationType = null;
        argColor = null;
        argMoney = 0;
        argLock = 0;
        argHexes = new ArrayList<>();
        smileys = new ArrayList<>();
    }


    public void setBy(CoreModel coreModel, Condition source) {
        type = source.type;
        executorColor = source.executorColor;
        argRelationType = source.argRelationType;
        argColor = source.argColor;
        argMoney = source.argMoney;
        argLock = source.argLock;
        argHexes.clear();
        for (Hex srcHex : source.argHexes) {
            argHexes.add(coreModel.getHexWithSameCoordinates(srcHex));
        }
        smileys.clear();
        smileys.addAll(source.smileys);
    }


    public void decode(CoreModel coreModel, String code) {
        String[] split = code.split("@");
        type = ConditionType.valueOf(split[0]);
        executorColor = HColor.valueOf(split[1]);
        if (!split[2].equals("null")) {
            argRelationType = RelationType.valueOf(split[2]);
        }
        if (!split[3].equals("null")) {
            argColor = HColor.valueOf(split[3]);
        }
        argMoney = Integer.valueOf(split[4]);
        argLock = Integer.valueOf(split[5]);
        decodeHexes(coreModel, split[6]);
        if (split.length > 7) {
            decodeSmileys(split[7]);
        }
    }


    public void decodeSmileys(String code) {
        smileys.clear();
        if (code == null) return;
        if (code.length() < 3) return;
        for (String token : code.split("%")) {
            if (token.length() < 3) continue;
            SmileyType smileyType = SmileyType.valueOf(token);
            smileys.add(smileyType);
        }
    }


    @Override
    public String encode() {
        return type + "@" +
                executorColor + "@" +
                argRelationType + "@" +
                argColor + "@" +
                argMoney + "@" +
                argLock + "@" +
                encodeHexes() + "@" +
                encodeSmileys();
    }


    public String encodeSmileys() {
        if (smileys.size() == 0) return "-";
        StringBuilder builder = new StringBuilder();
        for (SmileyType smileyType : smileys) {
            builder.append(smileyType).append("%");
        }
        return builder.toString();
    }


    public void setArgHexes(ArrayList<Hex> src) {
        argHexes.clear();
        argHexes.addAll(src);
    }


    public void setSmileys(ArrayList<SmileyType> src) {
        smileys.clear();
        smileys.addAll(src);
    }


    public void decodeHexes(CoreModel coreModel, String code) {
        String[] split = code.split("%");
        if (split.length < 2) return;
        for (int i = 0; i < split.length; i += 2) {
            int c1 = Integer.valueOf(split[i]);
            int c2 = Integer.valueOf(split[i + 1]);
            Hex hex = coreModel.getHex(c1, c2);
            if (hex == null) continue;
            argHexes.add(hex);
        }
    }


    private String encodeHexes() {
        StringBuilder builder = new StringBuilder();
        for (Hex hex : argHexes) {
            builder.append(hex.coordinate1).append("%").append(hex.coordinate2).append("%");
        }
        if (builder.length() == 0) return "-";
        return builder.toString();
    }


    public void onAddedToLetter(Letter letter) {
        this.letter = letter;
    }


    @Override
    public String toString() {
        return "[Condition: " + encode() + "]";
    }
}
