package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.generators.AbstractLevelGenerator;
import yio.tro.onliyoy.game.core_model.generators.LevelGeneratorFactory;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.game.viewable_model.ViewableUnit;
import yio.tro.onliyoy.menu.scenes.gameplay.SceneDebugPanel;

public class TmDebug extends TouchMode {

    public TmDebug(GameController gameController) {
        super(gameController);
    }


    @Override
    public void onModeBegin() {

    }


    @Override
    public void onModeEnd() {

    }


    @Override
    public void move() {

    }


    @Override
    public boolean isCameraMovementEnabled() {
        return true;
    }


    @Override
    public void onTouchDown() {

    }


    @Override
    public void onTouchDrag() {

    }


    @Override
    public void onTouchUp() {

    }


    @Override
    public boolean onClick() {
        ViewableModel viewableModel = getViewableModel();
        Hex hex = viewableModel.getClosestHex(getCurrentTouchConverted());
        if (hex == null) return true;
        switch (SceneDebugPanel.clickMode) {
            default:
            case 0:
                System.out.println("TmDebug.onClick");
                break;
            case 1:
                System.out.println("province = " + hex.getProvince());
                break;
            case 2:
                doInspectHex(hex);
                break;
        }

        return true;
    }


    private void doInspectHex(Hex hex) {
        if (DebugFlags.showRefModel) {
            System.out.println();
            System.out.println("TmDebug.doInspectHex [ref model]");
            CoreModel refModel = getObjectsLayer().viewableModel.refModel;
            Hex refHex = refModel.getHexWithSameCoordinates(hex);
            System.out.println("refHex = " + refHex);
            System.out.println("province: " + refHex.getProvince());
            return;
        }
        System.out.println();
        System.out.println("TmDebug.doInspectHex");
        System.out.println("hex = " + hex);
//        System.out.println("hex.adjacentHexes = " + hex.adjacentHexes.size() + " " + getAdjacentHexesString(hex));

        ViewableModel viewableModel = getObjectsLayer().viewableModel;
        ViewableUnit unit = viewableModel.unitsManager.getUnit(hex);
        if (unit != null) {
            System.out.println("unit = " + unit);
            System.out.println("unit ready: " + viewableModel.readinessManager.isReady(hex));
        }
        Province province = hex.getProvince();
        if (province != null) {
            System.out.println("province = " + province);
            System.out.println("profit = " + viewableModel.economicsManager.calculateProvinceProfit(province));
        }
    }


    private String getAdjacentHexesString(Hex hex) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < hex.adjacentHexes.size(); i++) {
            builder.append(hex.adjacentHexes.get(i));
            if (i < hex.adjacentHexes.size() - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }


    @Override
    public String getNameKey() {
        return null;
    }
}
