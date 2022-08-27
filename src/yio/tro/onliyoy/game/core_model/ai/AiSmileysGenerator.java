package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.SmileyType;
import yio.tro.onliyoy.stuff.name_generator.NameGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AiSmileysGenerator {

    private Random random;
    private ArrayList<SmileyType> list;
    private NameGenerator nameGenerator;
    private StringBuilder stringBuilder;


    public AiSmileysGenerator() {
        random = new Random();
        list = new ArrayList<>();
        stringBuilder = new StringBuilder();
        initNameGenerator();
    }


    private void initNameGenerator() {
        nameGenerator = new NameGenerator();
        nameGenerator.setMasks(generateMasks());
        nameGenerator.setGroups(generateGroups());
        nameGenerator.setCapitalize(false);
    }


    private ArrayList<String> generateMasks() {
        ArrayList<String> list = new ArrayList<>();
        list.add("g g");
        list.add("g g g");
        list.add("g g l");
        list.add("$ d _ g");
        list.add("$ d _ g g");
        list.add("$ d _ b");
        list.add("$ d _ b b");
        list.add("$ d _ l");
        list.add("$ d _ l l");
        list.add("$ d d _ g");
        list.add("$ d d _ g g");
        list.add("$ d d _ b");
        list.add("$ d d _ b b");
        list.add("$ d d _ l");
        list.add("$ d d _ l l");
        list.add("b");
        list.add("b b");
        list.add("b b b");
        list.add("b b _ O");
        list.add("d m d = d");
        list.add("d m d = d _ O");
        list.add("d m d = d _ O O O");
        list.add("d m d = d _ g g");
        list.add("d m d = d d _ b b b");
        list.add("g g g _ > _ b");
        list.add("g g _ > _ b b");
        list.add("b b _ > _ g _ O O");
        return list;
    }


    private HashMap<String, String> generateGroups() {
        HashMap<String, String> map = new HashMap<>();
        map.put("$", convert(new SmileyType[]{SmileyType.dollar}));
        map.put("_", convert(new SmileyType[]{SmileyType.space}));
        map.put("O", convert(new SmileyType[]{SmileyType.hypno}));
        map.put("=", convert(new SmileyType[]{SmileyType.equals}));
        map.put(">", convert(new SmileyType[]{SmileyType.arrow_right}));
        map.put("g", convert(new SmileyType[]{
                SmileyType.face_happy,
                SmileyType.heart,
                SmileyType.finger_up,
                SmileyType.face_scary,
                SmileyType.hypno,
                SmileyType.exclamation,
        }));
        map.put("d", convert(new SmileyType[]{
                SmileyType.one,
                SmileyType.two,
                SmileyType.three,
                SmileyType.four,
                SmileyType.five,
                SmileyType.six,
                SmileyType.seven,
                SmileyType.eight,
                SmileyType.nine,
                SmileyType.zero,
        }));
        map.put("b", convert(new SmileyType[]{
                SmileyType.face_neutral,
                SmileyType.face_surprised,
                SmileyType.skull,
                SmileyType.swords,
                SmileyType.finger_down,
                SmileyType.question,
        }));
        map.put("l", convert(new SmileyType[]{
                SmileyType.heart,
                SmileyType.hypno,
        }));
        map.put("m", convert(new SmileyType[]{
                SmileyType.minus,
                SmileyType.plus,
        }));
        return map;
    }


    private String convert(SmileyType[] array) {
        stringBuilder.setLength(0);
        for (SmileyType smileyType : array) {
            stringBuilder.append(smileyType).append(" ");
        }
        return stringBuilder.toString();
    }


    public ArrayList<SmileyType> apply() {
        list.clear();
        String string = nameGenerator.generate();
        for (String token : string.split(" ")) {
            SmileyType smileyType = SmileyType.valueOf(token);
            list.add(smileyType);
        }
        return list;
    }
}
