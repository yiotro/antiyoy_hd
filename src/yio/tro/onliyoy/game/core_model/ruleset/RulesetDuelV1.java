package yio.tro.onliyoy.game.core_model.ruleset;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

import java.util.ArrayList;

public class RulesetDuelV1 extends AbstractRuleset {

    public RulesetDuelV1(CoreModel coreModel) {
        super(coreModel);
    }


    @Override
    public RulesType getRulesType() {
        return RulesType.duel;
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
            case farm:
                return 5;
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
            case strong_tower:
                return 35;
            case farm:
                return 12 + 2 * province.countPieces(PieceType.farm);
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
            case farm:
            case tower:
            case strong_tower:
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
                return 36;
            case tower:
                return 1;
            case strong_tower:
                return 6;
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
        return attackerStrength > getDefenseValue(hex) || attackerStrength == 4;
    }


    @Override
    public int getTreeReward() {
        return 3;
    }


    @Override
    public void updateMoveZoneForUnitConstruction(Province province, int strength) {
        MoveZoneManager moveZoneManager = coreModel.moveZoneManager;
        moveZoneManager.update(province.getFirstHex(), 999, strength);
        ArrayList<Hex> hexes = moveZoneManager.hexes;
        HColor provinceColor = province.getColor();
        for (int i = hexes.size() - 1; i >= 0; i--) {
            Hex hex = hexes.get(i);
            if (hex.color == provinceColor) continue;
            if (isAdjacentToFarmOrCity(hex, provinceColor)) continue;
            hexes.remove(i);
        }
    }


    private boolean isAdjacentToFarmOrCity(Hex hex, HColor color) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != color) continue;
            if (adjacentHex.piece != PieceType.farm && adjacentHex.piece != PieceType.city) continue;
            return true;
        }
        return false;
    }


    @Override
    public boolean isUnitReadyOnBuilt() {
        return false;
    }
}
