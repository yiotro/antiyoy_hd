package yio.tro.onliyoy.game.core_model.core_provinces;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class PrwCluster implements ReusableYio {

    public ArrayList<Hex> hexes;


    public PrwCluster() {
        hexes = new ArrayList<>();
    }


    @Override
    public void reset() {
        hexes.clear();
    }


    boolean hasCity() {
        for (Hex hex : hexes) {
            if (hex.piece == PieceType.city) return true;
        }
        return false;
    }


    int countFarms() {
        int c = 0;
        for (Hex hex : hexes) {
            if (hex.piece != PieceType.farm) continue;
            c++;
        }
        return c;
    }


    @Override
    public String toString() {
        return "[PrwCluster: " +
                hexes.get(0).color +
                " " +
                hexes.size() +
                "]";
    }
}
