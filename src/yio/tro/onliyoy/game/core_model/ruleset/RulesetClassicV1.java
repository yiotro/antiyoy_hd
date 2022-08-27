package yio.tro.onliyoy.game.core_model.ruleset;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class RulesetClassicV1 extends AbstractRuleset {

    public RulesetClassicV1(CoreModel coreModel) {
        super(coreModel);
    }


    @Override
    public RulesType getRulesType() {
        return RulesType.classic;
    }


    @Override
    public int getVersionCode() {
        return 1;
    }


    @Override
    public int getHexIncome(PieceType pieceType) {
        if (pieceType == null) return 1;
        switch (pieceType) {
            default:
                return 1;
            case pine:
            case palm:
                return 0;
        }
    }


    @Override
    public int getPrice(Province province, PieceType pieceType) {
        switch (pieceType) {
            default:
                System.out.println("RulesetDefault.getPrice: problem");
                return 0;
            case peasant:
                return 10;
            case spearman:
                return 20;
            case baron:
                return 30;
            case knight:
                return 40;
            case tower:
                return 15;
        }
    }


    @Override
    public boolean isBuildable(PieceType pieceType) {
        switch (pieceType) {
            default:
                return false;
            case peasant:
            case spearman:
            case baron:
            case knight:
            case tower:
                return true;
        }
    }


    @Override
    public int getConsumption(PieceType pieceType) {
        if (pieceType == null) return 0;
        switch (pieceType) {
            default:
                return 0;
            case peasant:
                return 2;
            case spearman:
                return 6;
            case baron:
                return 18;
            case knight:
                return 54;
        }
    }


    @Override
    public int getDefenseValue(PieceType pieceType) {
        if (pieceType == null) return 0;
        if (Core.isUnit(pieceType)) return Core.getStrength(pieceType);
        switch (pieceType) {
            default:
                return 0;
            case city:
                return 1;
            case tower:
                return 2;
            case strong_tower:
                return 3;
        }
    }


    @Override
    public int getDefenseValue(Hex hex) {
        int maxValue = getDefenseValue(hex.piece);
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            int value = getDefenseValue(adjacentHex.piece);
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }


    @Override
    public boolean canHexBeCaptured(Hex hex, int attackerStrength) {
        return attackerStrength > getDefenseValue(hex);
    }


    @Override
    public int getTreeReward() {
        return 0;
    }


    @Override
    public void updateMoveZoneForUnitConstruction(Province province, int strength) {
        coreModel.moveZoneManager.update(province.getFirstHex(), 999, strength);
    }


    @Override
    public boolean isUnitReadyOnBuilt() {
        return true;
    }
}
