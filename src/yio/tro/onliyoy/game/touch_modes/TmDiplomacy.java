package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.view.game_renders.GameRendersList;
import yio.tro.onliyoy.game.viewable_model.GhDataContainer;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class TmDiplomacy extends TouchMode implements IEventListener {

    public ArrayList<ViewableEntityInfo> items;
    ObjectPoolYio<ViewableEntityInfo> poolItems;
    PointYio tempPoint;
    public SelectionEngineYio selectionEngineYio;
    public ArrayList<Hex> selectedHexes;
    boolean touchedCurrently;
    public FactorYio appearFactor;
    public GhDataContainer ghDataContainer;


    public TmDiplomacy(GameController gameController) {
        super(gameController);
        items = new ArrayList<>();
        tempPoint = new PointYio();
        selectionEngineYio = new SelectionEngineYio();
        selectedHexes = new ArrayList<>();
        appearFactor = new FactorYio();
        ghDataContainer = new GhDataContainer();
        initPools();
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<ViewableEntityInfo>(items) {
            @Override
            public ViewableEntityInfo makeNewObject() {
                return new ViewableEntityInfo();
            }
        };
    }


    @Override
    public void onModeBegin() {
        updateItems();
        selectedHexes.clear();
        touchedCurrently = false;
        getViewableModel().viewableRelationsManager.update();
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 2.8);
    }


    public void onEventChangedRelations() {
        updateItems();
    }


    protected void updateItems() {
        poolItems.clearExternalList();
        EntitiesManager entitiesManager = getViewableModel().entitiesManager;
        for (Province province : getViewableModel().provincesManager.provinces) {
            HColor color = province.getColor();
            if (color == entitiesManager.getCurrentColor()) continue;
            PlayerEntity entity = entitiesManager.getEntity(color);
            if (entity == null) continue; // yes, it's possible for provinces to not have entities
            if (isFullyCoveredByFog(province)) continue;
            ViewableEntityInfo freshObject = poolItems.getFreshObject();
            PointYio hookPoint = detectHookPoint(province);
            freshObject.setValues(entity, hookPoint);
        }
        moveItems();
    }


    private boolean isFullyCoveredByFog(Province province) {
        for (Hex hex : province.getHexes()) {
            if (!hex.fog) return false;
        }
        return true;
    }


    protected PointYio detectHookPoint(Province province) {
        tempPoint.reset();
        for (Hex hex : province.getHexes()) {
            tempPoint.add(hex.position.center);
        }
        tempPoint.x /= province.getHexes().size();
        tempPoint.y /= province.getHexes().size();
        Hex closestHex = getViewableModel().getClosestHex(tempPoint);
        if (closestHex != null && closestHex.getProvince() == province) {
            return tempPoint;
        }
        return getClosestHex(tempPoint, province).position.center;
    }


    private Hex getClosestHex(PointYio pointYio, Province province) {
        Hex closestHex = null;
        float minDistance = 0;
        for (Hex hex : province.getHexes()) {
            float currentDistance = pointYio.fastDistanceTo(hex.position.center);
            if (closestHex == null || currentDistance < minDistance) {
                closestHex = hex;
                minDistance = currentDistance;
            }
        }
        return closestHex;
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        // this actually shouldn't be called
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        updateItems();
    }


    @Override
    public int getListenPriority() {
        return 1;
    }


    @Override
    public void onModeEnd() {
        getViewableModel().viewableRelationsManager.hide();
        Scenes.mechanicsOverlay.updateFlagTexture();
    }


    @Override
    public void move() {
        moveItems();
        moveSelection();
        appearFactor.move();
    }


    private void moveSelection() {
        if (touchedCurrently) return;
        selectionEngineYio.move();
    }


    protected void moveItems() {
        for (ViewableEntityInfo viewableEntityInfo : items) {
            viewableEntityInfo.move();
        }
    }


    @Override
    public boolean isCameraMovementEnabled() {
        return true;
    }


    @Override
    public void onTouchDown() {
        touchedCurrently = true;
        checkToSelectProvince();
    }


    private void checkToSelectProvince() {
        Province province = getCurrentlyTouchedProvince();
        if (province == null) return;
        selectionEngineYio.applySelection();
        selectedHexes.clear();
        selectedHexes.addAll(province.getHexes());
        ghDataContainer.update(getViewableModel(), selectedHexes);
    }


    protected Province getCurrentlyTouchedProvince() {
        Hex closestHex = getViewableModel().getClosestHex(getCurrentTouchConverted());
        if (closestHex == null) return null;
        if (closestHex.fog) return null;
        Province province = closestHex.getProvince();
        if (province == null) return null;
        if (province.isOwnedByCurrentEntity()) return null;
        return province;
    }


    @Override
    public void onTouchDrag() {

    }


    @Override
    public void onTouchUp() {
        touchedCurrently = false;
    }


    @Override
    public boolean onClick() {
        Province province = getCurrentlyTouchedProvince();
        if (province != null) {
            EntitiesManager entitiesManager = getViewableModel().entitiesManager;
            PlayerEntity chosenEntity = entitiesManager.getEntity(province.getColor());
            Scenes.composeLetter.setRecipient(chosenEntity);
            Scenes.composeLetter.create();
            Scenes.composeLetter.loadValues();
            SoundManager.playSound(SoundType.select_province);
            return true;
        }
        return false;
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmDiplomacy;
    }


    @Override
    public String getNameKey() {
        return "diplomacy";
    }
}
