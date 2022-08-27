package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventSendLetter;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class ViewableRelationsManager implements IEventListener {

    ViewableModel viewableModel;
    public ArrayList<VrmIndicator> indicators;
    ObjectPoolYio<VrmIndicator> poolIndicators;
    RepeatYio<ViewableRelationsManager> repeatRemove;


    public ViewableRelationsManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        viewableModel.eventsManager.addListener(this);
        indicators = new ArrayList<>();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemove = new RepeatYio<ViewableRelationsManager>(this, 120) {
            @Override
            public void performAction() {
                parent.removeDeadIndicators();
            }
        };
    }


    private void initPools() {
        poolIndicators = new ObjectPoolYio<VrmIndicator>(indicators) {
            @Override
            public VrmIndicator makeNewObject() {
                return new VrmIndicator(viewableModel);
            }
        };
    }


    public void update() {
        if (!isEnabled()) return;
        killAllIndicators();
        for (Province province : viewableModel.provincesManager.provinces) {
            for (Hex hex : province.getHexes()) {
                for (int dir = 0; dir < 3; dir++) {
                    checkToIndicate(hex, dir);
                }
            }
        }
    }


    public void hide() {
        if (!isEnabled()) return;
        killAllIndicators();
    }


    public void onProvinceSelected() {
        if (!isEnabled()) return;
        updateIndicatorsToAllDirections(getSelectedProvince());
        enableDirectionalAnimations();
    }


    private void checkToIndicate(Hex hex, int dir) {
        PlayerEntity playerEntity = viewableModel.entitiesManager.getEntity(hex.color);
        if (playerEntity == null) return;
        Hex adjacentHex = viewableModel.directionsManager.getAdjacentHex(hex, dir);
        if (adjacentHex == null) return;
        if (adjacentHex.color == hex.color) return;
        if (adjacentHex.getProvince() == null) return;
        PlayerEntity entity = viewableModel.entitiesManager.getEntity(adjacentHex.color);
        if (entity == null) return;
        Relation relation = playerEntity.getRelation(entity);
        if (relation.type == RelationType.neutral) return;
        addIndicator(hex, dir, relation.type);
    }


    private void enableDirectionalAnimations() {
        for (VrmIndicator vrmIndicator : indicators) {
            vrmIndicator.setDirectionalAnimation(true);
        }
    }


    public void onProvinceDeselected() {
        hide();
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        if (!isEnabled()) return;
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        if (!isEnabled()) return;
        switch (event.getType()) {
            default:
                break;
            case set_relation_softly:
                onEventChangedRelations();
                break;
            case send_letter:
                EventSendLetter eventSendLetter = (EventSendLetter) event;
                Letter letter = eventSendLetter.letter;
                if (!letter.contains(ConditionType.notification)) break;
                updateIndicatorsToAllDirections();
                notifyTouchModeAboutChange();
                break;
            case indicate_undo_letter:
                onEventChangedRelations();
                break;
            case unit_move:
            case piece_build:
            case hex_change_color:
                updateIndicatorsToAllDirections();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 5;
    }


    public void onUndoApplied() {
        onEventChangedRelations();
    }


    public void onSyncApplied() {
        onEventChangedRelations();
    }


    private void updateIndicatorsToAllDirections() {
        if (!shouldReactToEvents()) return;
        killAllIndicators();
        HColor currentColor = viewableModel.entitiesManager.getCurrentColor();
        for (Province province : viewableModel.provincesManager.provinces) {
            if (province.getColor() != currentColor) continue;
            for (Hex hex : province.getHexes()) {
                for (int dir = 0; dir < 6; dir++) {
                    checkToIndicate(hex, dir);
                }
            }
        }
    }


    private void updateIndicatorsToAllDirections(Province province) {
        killAllIndicators();
        for (Hex hex : province.getHexes()) {
            for (int dir = 0; dir < 6; dir++) {
                checkToIndicate(hex, dir);
            }
        }
    }


    private Province getSelectedProvince() {
        return viewableModel.provinceSelectionManager.selectedProvince;
    }


    private void onEventChangedRelations() {
        if (!shouldReactToEvents()) return;
        applySoftUpdate();
        notifyTouchModeAboutChange();
    }


    private boolean shouldReactToEvents() {
        if (viewableModel.entitiesManager.isHumanTurnCurrently()) return true;
        GameController gameController = viewableModel.objectsLayer.gameController;
        if (gameController.gameMode == GameMode.editor) return true;
        NetRoot netRoot = gameController.yioGdxGame.netRoot;
        if (viewableModel.isNetMatch() && netRoot.isSpectatorCurrently()) return true;
        return false;
    }


    private void notifyTouchModeAboutChange() {
        GameController gameController = viewableModel.objectsLayer.gameController;
        if (gameController.touchMode == TouchMode.tmDiplomacy) {
            TouchMode.tmDiplomacy.onEventChangedRelations();
        }
        if (gameController.touchMode == TouchMode.tmEditRelations) {
            TouchMode.tmEditRelations.onEventChangedRelations();
        }
    }


    private void applySoftUpdate() {
        // soft update = only change colors
        for (VrmIndicator vrmIndicator : indicators) {
            Hex hex = vrmIndicator.hex;
            PlayerEntity playerEntity = viewableModel.entitiesManager.getEntity(hex.color);
            if (playerEntity == null) continue;
            Hex adjacentHex = viewableModel.directionsManager.getAdjacentHex(hex, vrmIndicator.direction);
            if (adjacentHex == null) return;
            PlayerEntity adjacentEntity = viewableModel.entitiesManager.getEntity(adjacentHex.color);
            if (adjacentEntity == null) continue;
            if (playerEntity == adjacentEntity) {
                System.out.println("ViewableRelationsManager.applySoftUpdate: problem");
                continue;
            }
            Relation relation = playerEntity.getRelation(adjacentEntity);
            if (vrmIndicator.relationType == relation.type) continue;
            vrmIndicator.setRelationType(relation.type);
        }
    }


    private boolean isEnabled() {
        return viewableModel.diplomacyManager.enabled;
    }


    public void killAllIndicators() {
        for (VrmIndicator vrmIndicator : indicators) {
            vrmIndicator.kill();
        }
    }


    private VrmIndicator getActiveIndicator(Hex hex, int direction) {
        for (VrmIndicator vrmIndicator : indicators) {
            if (!vrmIndicator.appearFactor.isInAppearState()) continue;
            if (vrmIndicator.hex != hex) continue;
            if (vrmIndicator.direction != direction) continue;
            return vrmIndicator;
        }
        return null;
    }


    private boolean isAlreadyIndicated(Hex hex, int direction) {
        return getActiveIndicator(hex, direction) != null;
    }


    private void addIndicator(Hex hex, int direction, RelationType relationType) {
        if (viewableModel.objectsLayer.gameController.gameMode == GameMode.editor) return; // buggy in editor
        VrmIndicator freshObject = poolIndicators.getFreshObject();
        freshObject.setRelationType(relationType);
        freshObject.spawn(hex, direction);
    }


    public void move() {
        repeatRemove.move();
        moveIndicators();
    }


    private void removeDeadIndicators() {
        for (int i = indicators.size() - 1; i >= 0; i--) {
            VrmIndicator vrmIndicator = indicators.get(i);
            if (!vrmIndicator.isReadyToBeRemoved()) continue;
            indicators.remove(vrmIndicator);
        }
    }


    private void moveIndicators() {
        for (VrmIndicator vrmIndicator : indicators) {
            vrmIndicator.move();
        }
    }

}
