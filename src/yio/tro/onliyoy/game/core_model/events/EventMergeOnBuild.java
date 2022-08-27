package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class EventMergeOnBuild extends AbstractEvent{

    public Hex hex;
    public PieceType pieceType;
    public int unitId;
    public int provinceId;


    public EventMergeOnBuild() {
        super();
        hex = null;
        pieceType = null;
        unitId = -1;
        provinceId = -1;
    }


    @Override
    public EventType getType() {
        return EventType.merge_on_build;
    }


    @Override
    public boolean isValid() {
        if (hex == null) return false;
        if (pieceType == null) return false;
        if (!Core.isUnit(pieceType)) return false;
        if (unitId == -1) return false;
        if (!coreModel.ruleset.isBuildable(pieceType)) return false;
        Province province = coreModel.provincesManager.getProvince(provinceId);
        if (province == null) return false;
        if (!province.canAfford(pieceType)) return false;
        if (!hex.hasUnit()) return false; // need second unit to merge
        if (hex.color != province.getColor()) return false; // merge should be peaceful
        if (Core.getMergeResult(pieceType, hex.piece) == null) return false;
        return true;
    }


    @Override
    public void applyChange() {
        Province province = coreModel.provincesManager.getProvince(provinceId);
        int price = coreModel.ruleset.getPrice(province, pieceType);
        hex.setPiece(Core.getMergeResult(hex.piece, pieceType));
        hex.setUnitId(unitId);
        if (!coreModel.ruleset.isUnitReadyOnBuilt()) {
            coreModel.readinessManager.setReady(hex, false);
        }
        province.setMoney(province.getMoney() - price);
    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {
        EventMergeOnBuild eventMergeOnBuild = (EventMergeOnBuild) srcEvent;
        setHex(coreModel.getHexWithSameCoordinates(eventMergeOnBuild.hex));
        setPieceType(eventMergeOnBuild.pieceType);
        setUnitId(eventMergeOnBuild.unitId);
        setProvinceId(eventMergeOnBuild.provinceId);
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
                "]";
    }
}
