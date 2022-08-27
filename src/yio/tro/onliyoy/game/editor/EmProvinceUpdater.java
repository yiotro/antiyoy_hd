package yio.tro.onliyoy.game.editor;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesBuilder;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class EmProvinceUpdater {

    EditorManager editorManager;
    ArrayList<Province> backupList;
    ObjectPoolYio<Province> poolProvinces;


    public EmProvinceUpdater(EditorManager editorManager) {
        this.editorManager = editorManager;
        backupList = new ArrayList<>();
        initPools();
    }


    private void initPools() {
        poolProvinces = new ObjectPoolYio<Province>(backupList) {
            @Override
            public Province makeNewObject() {
                return new Province();
            }
        };
    }


    public void apply() {
        updateBackup();
        buildProvinces();
        transferData();
    }


    private void transferData() {
        for (Province province : getViewableModel().provincesManager.provinces) {
            Province parent = findParent(province);
            if (parent == null) {
                applyDefaultValues(province);
                continue;
            }
            province.setMoney(parent.getMoney());
            province.setCityName(parent.getCityName());
        }
    }


    void applyDefaultValues(Province province) {
        province.setMoney(10);
    }


    private Province findParent(Province province) {
        HColor color = province.getColor();
        Province parent = null;
        int maxIntersection = -1;
        for (Province backupProvince : backupList) {
            if (backupProvince.getColor() != color) continue;
            int currentIntersection = countIntersection(province, backupProvince);
            if (currentIntersection == 0) continue;
            if (parent == null || currentIntersection > maxIntersection) {
                parent = backupProvince;
                maxIntersection = currentIntersection;
            }
        }
        return parent;
    }


    private int countIntersection(Province province1, Province province2) {
        resetFlags();
        for (Hex hex : province1.getHexes()) {
            hex.flag = true;
        }
        int count = 0;
        for (Hex hex : province2.getHexes()) {
            if (!hex.flag) continue;
            count++;
        }
        return count;
    }


    private void resetFlags() {
        for (Hex hex : getViewableModel().hexes) {
            hex.flag = false;
        }
    }


    private void buildProvinces() {
        getViewableModel().provincesManager.setCurrentId(0); // to stop ids from constantly changing
        ProvincesBuilder builder = getViewableModel().provincesManager.builder;
        builder.doGrantPermission();
        builder.apply();
    }


    private void updateBackup() {
        poolProvinces.clearExternalList();
        for (Province src : getViewableModel().provincesManager.provinces) {
            Province province = poolProvinces.getFreshObject();
            province.setCityName(src.getCityName());
            province.setMoney(src.getMoney());
            for (Hex hex : src.getHexes()) {
                province.addHex(hex);
            }
        }
    }


    private void showBackupInConsole() {
        System.out.println();
        System.out.println("EmProvinceUpdater.showBackupInConsole");
        for (Province province : backupList) {
            System.out.println("- " + province);
        }
    }


    private ViewableModel getViewableModel() {
        return editorManager.getViewableModel();
    }
}
