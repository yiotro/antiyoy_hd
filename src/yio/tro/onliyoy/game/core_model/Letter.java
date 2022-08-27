package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class Letter implements Encodeable, ReusableYio {

    public HColor senderColor;
    public HColor recipientColor;
    public ArrayList<Condition> conditions;
    public int id;


    public Letter() {
        conditions = new ArrayList<>();
        reset();
    }


    @Override
    public void reset() {
        senderColor = null;
        recipientColor = null;
        conditions.clear();
        id = -1;
    }


    public void setSenderColor(HColor senderColor) {
        this.senderColor = senderColor;
    }


    public void setRecipientColor(HColor recipientColor) {
        this.recipientColor = recipientColor;
    }


    public void addCondition(Condition condition) {
        condition.onAddedToLetter(this);
        conditions.add(condition);
    }


    public void setId(int id) {
        this.id = id;
    }


    public boolean contains(ConditionType conditionType) {
        for (Condition condition : conditions) {
            if (condition.type == conditionType) return true;
        }
        return false;
    }


    public HColor getOppositeColor(HColor color) {
        if (color == recipientColor) return senderColor;
        if (color == senderColor) return recipientColor;
        return null;
    }


    public void decode(CoreModel coreModel, String code) {
        String[] split = code.split(">");
        setSenderColor(HColor.valueOf(split[0]));
        setRecipientColor(HColor.valueOf(split[1]));
        setId(Integer.valueOf(split[2]));
        for (int i = 3; i < split.length; i++) {
            Condition condition = new Condition();
            condition.decode(coreModel, split[i]);
            addCondition(condition);
        }
    }


    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(senderColor).append(">");
        builder.append(recipientColor).append(">");
        builder.append(id).append(">");
        for (Condition condition : conditions) {
            builder.append(condition.encode()).append(">");
        }
        return builder.toString();
    }


    @Override
    public String toString() {
        return "[Letter: <" +
                id + "> " +
                senderColor + " -> " +
                recipientColor + ", " +
                conditions.size() + "x" +
                "]";
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("Letter.showInConsole");
        System.out.println(this);
        for (Condition condition : conditions) {
            System.out.println("- " + condition);
        }
    }
}
