package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.view.game_renders.GameRendersList;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.PointYio;

public class TmEditRelations extends TmDiplomacy {


    public TmEditRelations(GameController gameController) {
        super(gameController);
    }


    @Override
    protected void updateItems() {
        poolItems.clearExternalList();
        EntitiesManager entitiesManager = getViewableModel().entitiesManager;
        Province selectedProvince = getViewableModel().provinceSelectionManager.selectedProvince;
        PlayerEntity selectedEntity = entitiesManager.getEntity(selectedProvince.getColor());
        for (Province province : getViewableModel().provincesManager.provinces) {
            HColor color = province.getColor();
            if (color == selectedProvince.getColor()) continue;
            PlayerEntity entity = entitiesManager.getEntity(color);
            if (entity == null) continue; // yes, it's possible for provinces to not have entities
            ViewableEntityInfo freshObject = poolItems.getFreshObject();
            PointYio hookPoint = detectHookPoint(province);
            freshObject.setValues(entity, selectedEntity, hookPoint);
        }
        moveItems();
    }


    @Override
    public boolean onClick() {
        Province province = getCurrentlyTouchedProvince();
        if (province == null || !isCurrentTouchInsideHex()) {
            gameController.resetTouchMode();
            return false;
        }
        Province selectedProvince = getViewableModel().getSelectedProvince();
        if (selectedProvince == null) return true;
        EntitiesManager entitiesManager = getViewableModel().entitiesManager;
        PlayerEntity selectedEntity = entitiesManager.getEntity(selectedProvince.getColor());
        if (selectedEntity == null) return true;
        PlayerEntity chosenEntity = entitiesManager.getEntity(province.getColor());
        if (chosenEntity == null) return true;
        Scenes.editRelation.setEntity1(selectedEntity);
        Scenes.editRelation.setEntity2(chosenEntity);
        Scenes.editRelation.create();
        SoundManager.playSound(SoundType.select_province);
        return true;
    }


    @Override
    protected Province getCurrentlyTouchedProvince() {
        Hex closestHex = getViewableModel().getClosestHex(getCurrentTouchConverted());
        if (closestHex == null) return null;
        Province province = closestHex.getProvince();
        if (province == null) return null;
        if (province == getViewableModel().getSelectedProvince()) return null;
        return province;
    }


    private boolean isCurrentTouchInsideHex() {
        Hex hex = getViewableModel().getClosestHex(getCurrentTouchConverted());
        return hex.position.center.distanceTo(getCurrentTouchConverted()) < getViewableModel().getHexRadius();
    }


    @Override
    public void onModeEnd() {
        super.onModeEnd();
        getViewableModel().provinceSelectionManager.onClickedOutside();
    }


    @Override
    public String getNameKey() {
        return "relations";
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmEditRelations;
    }
}
