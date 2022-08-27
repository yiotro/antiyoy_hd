package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.posmap.AbstractPmObjectYio;

import java.util.ArrayList;

public class Hex extends AbstractPmObjectYio implements Encodeable {

    public CoreModel coreModel;
    public ArrayList<Hex> adjacentHexes;
    public int coordinate1;
    public int coordinate2;
    public CircleYio position;
    public PieceType piece;
    public int unitId;
    public HColor color;
    public boolean flag; // for algorithms
    public boolean lgFlag; // for algorithms
    public int counter; // for algorithms
    private Province province;
    public int farmDiversityIndex;
    public boolean fog;


    public Hex(CoreModel coreModel) {
        this.coreModel = coreModel;
        adjacentHexes = new ArrayList<>();
        coordinate1 = 0;
        coordinate2 = 0;
        position = new CircleYio();
        piece = null;
        unitId = -1;
        flag = false;
        color = HColor.gray;
        province = null;
        counter = 0;
        lgFlag = false;
        fog = false;
        updateFarmDiversityIndex();
    }


    public void updateFarmDiversityIndex() {
        farmDiversityIndex = (99999 + 101 * coordinate1 * coordinate2 + 7 * coordinate2) % 3;
    }


    public void copyFrom(Hex src) {
        piece = src.piece;
        unitId = src.unitId;
        color = src.color;
    }


    public boolean isLinkedTo(Hex hex) {
        return adjacentHexes.contains(hex);
    }


    public boolean isAdjacentToHexesOfSameColor() {
        for (Hex adjacentHex : adjacentHexes) {
            if (color == adjacentHex.color) return true;
        }
        return false;
    }


    public void addAdjacentHex(Hex hex) {
        adjacentHexes.add(hex);
    }


    @Override
    protected void updatePosMapPosition() {
        posMapPosition.setBy(position.center);
    }


    public void onAddedToProvince(Province province) {
        this.province = province;
    }


    public void onRemovedFromProvince(Province province) {
        if (this.province != province) return; // removed from one of previous provinces
        if (this.province == null) {
            System.out.println("Hex.onRemovedFromProvince: suspicious");
            Yio.printStackTrace();
        }
        this.province = null;
    }


    public void onProvinceInvalidated(Province province) {
        if (this.province != province) return; // on of previous owners invalidated
        if (this.province == null) {
            System.out.println("Hex.onProvinceInvalidated: suspicious");
            Yio.printStackTrace();
        }
        this.province = null;
    }


    public boolean hasPiece() {
        return piece != null;
    }


    public boolean isEmpty() {
        return piece == null;
    }


    public boolean hasUnit() {
        return hasPiece() && Core.isUnit(piece);
    }


    public boolean hasTree() {
        return piece == PieceType.pine || piece == PieceType.palm;
    }


    public boolean hasTower() {
        return piece == PieceType.tower || piece == PieceType.strong_tower;
    }


    public boolean hasStaticPiece() {
        return hasPiece() && !Core.isUnit(piece);
    }


    public boolean hasSameCoordinatesAs(Hex hex) {
        return coordinate1 == hex.coordinate1 && coordinate2 == hex.coordinate2;
    }


    public void setPiece(PieceType pieceType) {
        this.piece = pieceType;
    }


    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }


    @Override
    public String encode() {
        String necessaryPart = coordinate1 + " " + coordinate2 + " " + color;
        String piecePart = "";
        if (piece != null) {
            piecePart = " " + piece + " " + unitId;
        }
        return necessaryPart + piecePart;
    }


    public boolean hasCoordinates(int c1, int c2) {
        if (coordinate1 != c1) return false;
        if (coordinate2 != c2) return false;
        return true;
    }


    public void setColor(HColor hColor) {
        if (this.color == hColor) return;
        this.color = hColor;
    }


    public HColor getColor() {
        return color;
    }


    public Province getProvince() {
        return province;
    }


    public boolean isColored() {
        return color != HColor.gray;
    }


    public boolean isNeutral() {
        return color == HColor.gray;
    }


    @Override
    public String toString() {
        return "[" + encode() + "]";
    }
}
