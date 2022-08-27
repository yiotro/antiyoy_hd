package yio.tro.onliyoy.game.core_model.core_provinces;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;
import java.util.Arrays;

public class Province implements ReusableYio, Encodeable {

    private ArrayList<Hex> hexes;
    private int money;
    private String cityName;
    private boolean valid;
    private int id;


    public Province() {
        hexes = new ArrayList<>();
    }


    @Override
    public void reset() {
        hexes.clear();
        money = 0;
        cityName = "";
        valid = true;
        id = -1;
    }


    public boolean contains(Hex hex) {
        checkValidity();
        return hexes.contains(hex);
    }


    public boolean contains(PieceType pieceType) {
        checkValidity();
        for (Hex hex : hexes) {
            if (hex.piece == pieceType) return true;
        }
        return false;
    }


    public HColor getColor() {
        checkValidity();
        // this method demands that all hexes in province are same color
        if (hexes.size() == 0) return null;
        return hexes.get(0).color;
    }


    public boolean isOwnedByCurrentEntity() {
        checkValidity();
        if (hexes.size() == 0) return false;
        Hex firstHex = getFirstHex();
        EntitiesManager entitiesManager = firstHex.coreModel.entitiesManager;
        return firstHex.color == entitiesManager.getCurrentColor();
    }


    public int countPieces(PieceType pieceType) {
        checkValidity();
        int c = 0;
        for (Hex hex : hexes) {
            if (hex.piece != pieceType) continue;
            c++;
        }
        return c;
    }


    public boolean canAfford(PieceType pieceType) {
        checkValidity();
        if (hexes.size() == 0) return false;
        int price = getRuleset().getPrice(this, pieceType);
        return money >= price;
    }


    private AbstractRuleset getRuleset() {
        return getFirstHex().coreModel.ruleset;
    }


    public ArrayList<Hex> getHexes() {
        checkValidity();
        return hexes;
    }


    public Hex getFirstHex() {
        checkValidity();
        return hexes.get(0);
    }


    public void addHex(Hex hex) {
        checkValidity();
        hexes.add(hex);
        hex.onAddedToProvince(this);
    }


    public void removeHex(Hex hex) {
        checkValidity();
        hexes.remove(hex);
        hex.onRemovedFromProvince(this);
    }


    public int getMoney() {
        checkValidity();
        return money;
    }


    public void setMoney(int money) {
        checkValidity();
        this.money = money;
    }


    public String getCityName() {
        checkValidity();
        return cityName;
    }


    public void setCityName(String cityName) {
        checkValidity();
        this.cityName = cityName;
    }


    public int getId() {
        checkValidity();
        if (id == -1) {
            System.out.println("Province.getId: id not set");
        }
        return id;
    }


    public void setId(int id) {
        checkValidity();
        this.id = id;
    }


    private void checkValidity() {
        if (valid) return;
        if (!DebugFlags.invalidProvinceAccessDetected) return;
        // it's necessary to prevent spam
        DebugFlags.invalidProvinceAccessDetected = false;
        System.out.println("Detected access to invalid province: " + Arrays.toString(hexes.toArray()));
        Yio.printStackTrace();
    }


    public void setValid(boolean valid) {
        if (this.valid == valid) return;
        this.valid = valid;
        if (!valid) {
            onInvalidated();
        }
    }


    private void onInvalidated() {
        for (Hex hex : hexes) {
            hex.onProvinceInvalidated(this);
        }
    }


    public boolean isValid() {
        return valid;
    }


    @Override
    public String encode() {
        Hex firstHex = hexes.get(0);
        return firstHex.coordinate1 + "<" + firstHex.coordinate2 + "<" + id + "<" + money + "<" + cityName;
    }


    @Override
    public String toString() {
        if (!isValid()) {
            return "[Invalid province]";
        }
        return "[Province: " +
                getColor() +
                "<" + id + ">, " +
                "$" + money + " " +
                cityName +
                "]";
    }
}
