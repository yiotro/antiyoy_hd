package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractLetterTemplate {


    DiplomaticAI diplomaticAI;
    CoreModel coreModel;
    Letter letter;
    PlayerEntity currentEntity;
    Random random;
    PlayerEntity recipient;
    ArrayList<Hex> tempHexList;


    public AbstractLetterTemplate(DiplomaticAI diplomaticAI) {
        this.diplomaticAI = diplomaticAI;
        random = new Random();
        if (DebugFlags.determinedRandom) {
            random = new Random(0);
        }
        tempHexList = new ArrayList<>();
    }


    Letter make(PlayerEntity entity) {
        recipient = entity;
        coreModel = diplomaticAI.coreModel;
        currentEntity = coreModel.entitiesManager.getCurrentEntity();
        letter = diplomaticAI.startLetter(currentEntity, recipient);
        addConditions();
        return letter;
    }


    abstract void addConditions();


    protected ArrayList<Hex> getHexesForExchange(HColor color, int quantity) {
        tempHexList.clear();
        Province province = coreModel.provincesManager.getProvince(color);
        ArrayList<Hex> hexes = province.getHexes();
        if (quantity >= hexes.size()) {
            tempHexList.addAll(hexes);
            return tempHexList;
        }
        Hex startHex = hexes.get(random.nextInt(hexes.size()));
        tempHexList.add(startHex);
        while (tempHexList.size() < quantity) {
            Hex randomHex = hexes.get(random.nextInt(hexes.size()));
            if (tempHexList.contains(randomHex)) continue;
            if (!isAdjacentToTempHexList(randomHex)) continue;
            tempHexList.add(randomHex);
        }
        return tempHexList;
    }


    boolean isAdjacentToTempHexList(Hex targetHex) {
        for (Hex hex : targetHex.getProvince().getHexes()) {
            hex.flag = false;
        }
        for (Hex hex : tempHexList) {
            hex.flag = true;
        }
        for (Hex adjacentHex : targetHex.adjacentHexes) {
            if (adjacentHex.color != targetHex.color) continue;
            if (adjacentHex.flag) return true;
        }
        return false;
    }
}
