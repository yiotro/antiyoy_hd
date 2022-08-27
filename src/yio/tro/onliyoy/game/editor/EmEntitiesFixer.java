package yio.tro.onliyoy.game.editor;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.net.shared.NetRandomNicknameArguments;
import yio.tro.onliyoy.stuff.name_generator.NameGenerator;

import java.util.ArrayList;
import java.util.Arrays;

public class EmEntitiesFixer {

    ObjectsLayer objectsLayer;


    public EmEntitiesFixer(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
    }


    void doFixEntities() {
        doRemoveExcessiveEntities();
        doAddMissingEntities();
    }


    private void doRemoveExcessiveEntities() {
        if (!isThereAtLeastOneColorOnTheMap()) return;
        if (getEntitiesManager() == null) return;
        if (getEntitiesManager().entities == null) return;
        if (!isThereAtLeastOneExcessiveEntity()) return;
        if (getEntitiesManager().entities.length <= 2) return;
        EntitiesManager entitiesManager = getEntitiesManager();
        ArrayList<PlayerEntity> tempList = new ArrayList<>(Arrays.asList(entitiesManager.entities));
        for (int i = tempList.size() - 1; i >= 0; i--) {
            if (!isEntityExcessive(tempList.get(i))) continue;
            tempList.remove(i);
        }
        entitiesManager.initialize(tempList);
    }


    private boolean isThereAtLeastOneExcessiveEntity() {
        for (PlayerEntity entity : getEntitiesManager().entities) {
            if (isEntityExcessive(entity)) return true;
        }
        return false;
    }


    private boolean isEntityExcessive(PlayerEntity playerEntity) {
        for (Hex hex : getViewableModel().hexes) {
            if (hex.color != playerEntity.color) continue;
            if (!hex.isAdjacentToHexesOfSameColor()) continue;
            return false;
        }
        return true;
    }


    private void doAddMissingEntities() {
        if (!isThereAtLeastOneProvinceWithoutEntity()) return;
        EntitiesManager entitiesManager = getEntitiesManager();
        ArrayList<PlayerEntity> tempList = new ArrayList<>(Arrays.asList(entitiesManager.entities));
        NameGenerator nameGenerator = createNameGenerator();
        for (Province province : getViewableModel().provincesManager.provinces) {
            HColor color = province.getColor();
            if (entitiesManager.getEntity(color) != null) continue;
            if (containsColor(tempList, color)) continue;
            PlayerEntity playerEntity = new PlayerEntity(entitiesManager, EntityType.ai_balancer, color);
            playerEntity.setName(nameGenerator.generate());
            tempList.add(playerEntity);
        }
        entitiesManager.initialize(tempList);
    }


    private boolean containsColor(ArrayList<PlayerEntity> tempList, HColor color) {
        for (PlayerEntity playerEntity : tempList) {
            if (playerEntity.color == color) return true;
        }
        return false;
    }


    private NameGenerator createNameGenerator() {
        NameGenerator nameGenerator = new NameGenerator();
        NetRandomNicknameArguments nrmArguments = new NetRandomNicknameArguments();
        nameGenerator.setGroups(nrmArguments.groups);
        nameGenerator.setMasks(nrmArguments.masks);
        nameGenerator.setCapitalize(true);
        return nameGenerator;
    }


    private boolean isThereAtLeastOneProvinceWithoutEntity() {
        EntitiesManager entitiesManager = getEntitiesManager();
        for (Province province : getViewableModel().provincesManager.provinces) {
            HColor color = province.getColor();
            if (entitiesManager.getEntity(color) == null) return true;
        }
        return false;
    }


    private boolean isThereAtLeastOneColorOnTheMap() {
        for (Hex hex : getViewableModel().hexes) {
            if (hex.isColored()) return true;
        }
        return false;
    }


    private EntitiesManager getEntitiesManager() {
        return getViewableModel().entitiesManager;
    }


    ViewableModel getViewableModel() {
        return objectsLayer.viewableModel;
    }
}
