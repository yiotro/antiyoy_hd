package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;

import java.util.ArrayList;

public class IwCoreGraph extends AbstractImportWorker {

    CoreModel coreModel;


    public IwCoreGraph(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "hexes";
    }


    @Override
    protected void apply() {
        // requirement: graph has to be full at this point
        // this worker only removes hexes
        prepareHexes();
        tagHexes();
        removeNonTaggedHexes();
    }


    private void removeNonTaggedHexes() {
        ArrayList<Hex> hexes = coreModel.hexes;
        // I can also just use removeHex() method, it will be easier but much slower
        // remove links
        for (Hex hex : hexes) {
            if (!hex.flag) continue;
            ArrayList<Hex> adjacentHexes = hex.adjacentHexes;
            for (int i = adjacentHexes.size() - 1; i >= 0; i--) {
                Hex adjacentHex = adjacentHexes.get(i);
                if (adjacentHex.flag) continue;
                adjacentHexes.remove(i);
            }
        }
        // manually remove non flagged hexes
        for (int i = hexes.size() - 1; i >= 0; i--) {
            Hex hex = hexes.get(i);
            if (hex.flag) continue;
            hexes.remove(i);
            coreModel.posMapYio.removeObject(hex);
        }
    }


    private void removeNonTaggedHexesSlowly() {
        // this method is slow but reliable
        for (int i = coreModel.hexes.size() - 1; i >= 0; i--) {
            Hex hex = coreModel.hexes.get(i);
            if (hex.flag) continue;
            coreModel.removeHex(hex);
        }
    }


    private void tagHexes() {
        for (String token : source.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            int c1 = Integer.valueOf(split[0]);
            int c2 = Integer.valueOf(split[1]);
            Hex hex = coreModel.getHex(c1, c2);
            if (hex == null) {
                System.out.println("IwCoreGraph.tagHexes: " + c1 + " " + c2);
            }
            hex.flag = true;
        }
    }


    private void prepareHexes() {
        for (Hex hex : coreModel.hexes) {
            hex.flag = false;
        }
    }
}
