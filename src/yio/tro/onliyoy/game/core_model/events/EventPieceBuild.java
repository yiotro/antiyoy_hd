package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class EventPieceBuild extends AbstractEvent{

    public Hex hex;
    public PieceType pieceType;
    public int unitId;
    public int provinceId;


    public EventPieceBuild() {
        super();
        hex = null;
        pieceType = null;
        unitId = -1;
        provinceId = -1;
    }


    @Override
    public EventType getType() {
        return EventType.piece_build;
    }


    @Override
    public boolean isValid() {
        if (hex == null) return false;
        if (pieceType == null) return false;
        boolean isUnit = Core.isUnit(pieceType);
        if (!isUnit && !canBuildStaticPiece()) return false;
        if (isUnit && unitId == -1) return false;
        if (!coreModel.ruleset.isBuildable(pieceType)) return false;
        Province province = coreModel.provincesManager.getProvince(provinceId);
        if (province == null) return false;
        if (!province.canAfford(pieceType)) return false;
        if (isUnit) {
            int strength = Core.getStrength(pieceType);
            coreModel.ruleset.updateMoveZoneForUnitConstruction(province, strength);
            if (!coreModel.moveZoneManager.contains(hex)) return false;
        }
        if (isUnit && hex.color == province.getColor() && hex.hasStaticPiece()) {
            // merge on build should be handled by another event
            // also, player can't build units on owned structures
            return hex.hasTree() || hex.piece == PieceType.grave;
        }
        return true;
    }


    private boolean canBuildStaticPiece() {
        Province province = hex.getProvince();
        if (province == null || !province.isValid() || province.getColor() != hex.color) return false;
        if (pieceType == PieceType.strong_tower && hex.piece == PieceType.tower) return true;
        return hex.isEmpty();
    }


    @Override
    public void applyChange() {
        Province province = coreModel.provincesManager.getProvince(provinceId);
        // price may change after event was applied, so it should be calculated before
        int price = coreModel.ruleset.getPrice(province, pieceType);
        boolean ready = false;
        if (hex.isEmpty() && hex.color == province.getColor() && coreModel.ruleset.isUnitReadyOnBuilt()) {
            ready = true;
        }
        if (hex.hasTree() && hex.color == province.getColor()) {
            int reward = coreModel.ruleset.getTreeReward();
            province.setMoney(province.getMoney() + reward);
        }
        hex.setPiece(pieceType);
        if (Core.isUnit(pieceType)) {
            hex.setUnitId(unitId);
            hex.setColor(province.getColor());
            coreModel.readinessManager.setReady(hex, ready);
        }
        province.setMoney(province.getMoney() - price);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventPieceBuild eventPieceBuild = (EventPieceBuild) srcEvent;
        setHex(coreModel.getHexWithSameCoordinates(eventPieceBuild.hex));
        setPieceType(eventPieceBuild.pieceType);
        setUnitId(eventPieceBuild.unitId);
        setProvinceId(eventPieceBuild.provinceId);
    }


    @Override
    protected String getLocalEncodedInfo() {
        return hex.coordinate1 + " " + hex.coordinate2 + " " + pieceType + " " + unitId + " " + provinceId;
    }


    public void setHex(Hex hex) {
        this.hex = hex;
    }


    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }


    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }


    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }


    @Override
    public String toString() {
        return "[" +
                getInternalName() +
                ": " + pieceType + " on " + hex +
                " by " + provinceId +
                "]";
    }
}
