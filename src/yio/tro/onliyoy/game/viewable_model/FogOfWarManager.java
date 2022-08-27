package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventType;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class FogOfWarManager implements IEventListener {

    CoreModel coreModel;
    public boolean enabled;
    public ArrayList<Hex> currentlyVisibleHexes;
    HColor targetColor;
    CmWaveWorker waveLight;
    public GhDataContainer ghDataContainer;
    DirectionsManager directionsManager;
    private ArrayList<FowItem> fowItems;
    private ObjectPoolYio<FowItem> poolItems;


    public FogOfWarManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        enabled = false;
        currentlyVisibleHexes = new ArrayList<>();
        ghDataContainer = new GhDataContainer();
        directionsManager = new DirectionsManager(coreModel);
        fowItems = new ArrayList<>();
        coreModel.eventsManager.addListener(this);
        initPools();
        initWaves();
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<FowItem>(fowItems) {
            @Override
            public FowItem makeNewObject() {
                return new FowItem();
            }
        };
    }


    private void initWaves() {
        waveLight = new CmWaveWorker() {
            @Override
            protected boolean condition(Hex parentHex, Hex hex) {
                return parentHex.counter > 0;
            }


            @Override
            protected void action(Hex parentHex, Hex hex) {
                hex.fog = false;
                if (parentHex != null) {
                    hex.counter = parentHex.counter - 1;
                }
            }
        };
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        if (!enabled) return;
        applyUpdate();
    }


    public void onUndoApplied() {
        if (!enabled) return;
        applyUpdate();
    }


    private void applyUpdate() {
        currentlyVisibleHexes.clear();
        if (coreModel.entitiesManager.entities == null) return;
        updateTargetColor();
        if (targetColor == null) return;
        resetFog();
        applyProvinces();
        updateList();
        updateFowItems();
        updateGhData();
    }


    private void updateFowItems() {
        poolItems.clearExternalList();
        for (Hex hex : currentlyVisibleHexes) {
            FowItem freshObject = poolItems.getFreshObject();
            freshObject.setValues(hex.position.center, hex.coordinate1, hex.coordinate2);
            for (int direction = 0; direction < 6; direction++) {
                Hex adjacentHex = directionsManager.getAdjacentHex(hex, direction);
                if (adjacentHex != null) continue;
                int c1 = directionsManager.getAdjCoordinate1();
                int c2 = directionsManager.getAdjCoordinate2();
                if (areCoordinatesAlreadyInItems(c1, c2)) continue;
                coreModel.applyCoordinatesToTempPoint(c1,c2);
                poolItems.getFreshObject().setValues(coreModel.getTempPoint(), c1, c2);
            }
        }
    }


    private boolean areCoordinatesAlreadyInItems(int c1, int c2) {
        for (FowItem item : fowItems) {
            if (item.coordinate1 == c1 && item.coordinate2 == c2) return true;
        }
        return false;
    }


    private void updateGhData() {
        ghDataContainer.updateByFowItems(coreModel, fowItems);
    }


    private void updateList() {
        currentlyVisibleHexes.clear();
        for (Hex hex : coreModel.hexes) {
            if (hex.fog) continue;
            currentlyVisibleHexes.add(hex);
        }
    }


    private void applyProvinces() {
        for (Province province : coreModel.provincesManager.provinces) {
            if (!shouldProvinceBeLightUpped(province)) continue;
            applyProvince(province);
        }
    }


    private boolean shouldProvinceBeLightUpped(Province province) {
        if (province.getColor() == targetColor) return true;
        if (!coreModel.diplomacyManager.enabled) return false;
        PlayerEntity entity = coreModel.entitiesManager.getEntity(province.getColor());
        if (entity == null) return false;
        PlayerEntity targetEntity = coreModel.entitiesManager.getEntity(targetColor);
        if (targetEntity == null) return false;
        Relation relation = entity.getRelation(targetEntity);
        if (relation == null) return false;
        if (relation.type == RelationType.friend || relation.type == RelationType.alliance) return true;
        return false;
    }


    private void applyProvince(Province province) {
        for (Hex hex : province.getHexes()) {
            prepareHexes();
            hex.counter = getLightRadius(hex);
            waveLight.apply(hex);
        }
    }


    private int getLightRadius(Hex hex) {
        if (hex.isEmpty()) return 1;
        switch (hex.piece) {
            default:
                return 1;
            case peasant:
            case spearman:
            case baron:
            case knight:
                return 2;
            case tower:
                return 3;
            case city:
                return 4;
            case strong_tower:
                return 5;
        }
    }


    private void resetFog() {
        for (Hex hex : coreModel.hexes) {
            hex.fog = true;
        }
    }


    private void prepareHexes() {
        for (Hex hex : coreModel.hexes) {
            hex.flag = false;
        }
    }


    private void updateTargetColor() {
        int humanColors = countHumanColors();
        switch (humanColors) {
            default:
                targetColor = coreModel.entitiesManager.getCurrentColor();
                break;
            case 0:
                targetColor = null;
                break;
            case 1:
                targetColor = getAliveHuman().color;
                break;
        }
    }


    public boolean isVisible() {
        return enabled && targetColor != null;
    }


    private PlayerEntity getAliveHuman() {
        for (PlayerEntity entity : coreModel.entitiesManager.entities) {
            if (!isAliveHuman(entity)) continue;
            return entity;
        }
        return null;
    }


    private int countHumanColors() {
        int c = 0;
        for (PlayerEntity entity : coreModel.entitiesManager.entities) {
            if (!isAliveHuman(entity)) continue;
            c++;
        }
        return c;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            applyUpdate();
        }
    }


    private boolean isAliveHuman(PlayerEntity entity) {
        return entity.isHuman() && coreModel.provincesManager.getProvince(entity.color) != null;
    }


    public void setBy(FogOfWarManager src) {
        setEnabled(src.enabled);
    }


    @Override
    public int getListenPriority() {
        return 2;
    }
}
