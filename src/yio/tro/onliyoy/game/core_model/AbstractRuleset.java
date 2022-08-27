package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;

public abstract class AbstractRuleset {

    protected CoreModel coreModel;


    public AbstractRuleset(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    public abstract RulesType getRulesType();


    public abstract int getVersionCode();


    public abstract int getHexIncome(PieceType pieceType);


    public abstract int getPrice(Province province, PieceType pieceType);


    public abstract boolean isBuildable(PieceType pieceType);


    public abstract int getConsumption(PieceType pieceType);


    public abstract int getDefenseValue(PieceType pieceType);


    public abstract int getDefenseValue(Hex hex);


    public abstract boolean canHexBeCaptured(Hex hex, int attackerStrength);


    public abstract int getTreeReward();


    public abstract void updateMoveZoneForUnitConstruction(Province province, int strength);


    public abstract boolean isUnitReadyOnBuilt();

}
