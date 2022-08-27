package yio.tro.onliyoy.game.core_model.core_provinces;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.CmWaveWorker;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

import java.util.ArrayList;

public class ProvincesBuilder {

    ProvincesManager provincesManager;
    ArrayList<Hex> cumulativeList;
    private CmWaveWorker waveWorker;
    boolean permissionGranted;


    public ProvincesBuilder(ProvincesManager provincesManager) {
        this.provincesManager = provincesManager;
        cumulativeList = new ArrayList<>();
        permissionGranted = false;
        initWaveWorker();
    }


    private void initWaveWorker() {
        waveWorker = new CmWaveWorker() {
            @Override
            public boolean condition(Hex parentHex, Hex hex) {
                return parentHex.color == hex.color;
            }


            @Override
            public void action(Hex parentHex, Hex hex) {
                cumulativeList.add(hex);
            }
        };
    }


    public void apply() {
        doCheckPermission();
        clear();
        prepareFlags();
        for (Hex hex : getHexes()) {
            if (hex.flag) continue;
            if (!hex.isColored()) continue;
            if (!hex.isAdjacentToHexesOfSameColor()) continue;
            buildProvince(hex);
        }
    }


    private void doCheckPermission() {
        if (!permissionGranted) {
            System.out.println("ProvincesBuilder.apply: problem, calling apply() without permission");
        }
        permissionGranted = false;
    }


    private void buildProvince(Hex startHex) {
        cumulativeList.clear();
        waveWorker.apply(startHex);
        turnCumulativeListIntoProvince();
    }


    private void turnCumulativeListIntoProvince() {
        Province freshObject = provincesManager.addProvince();
        for (Hex hex : cumulativeList) {
            freshObject.addHex(hex);
        }
    }


    private void prepareFlags() {
        for (Hex hex : getHexes()) {
            hex.flag = false;
        }
    }


    public void doGrantPermission() {
        // an important reminder
        // this builder shouldn't be used frequently (every turn)
        // though this rule doesn't apply to editor mode or mass change
        permissionGranted = true;
    }


    private ArrayList<Hex> getHexes() {
        return provincesManager.coreModel.hexes;
    }


    private void clear() {
        provincesManager.clearProvinces();
    }
}
