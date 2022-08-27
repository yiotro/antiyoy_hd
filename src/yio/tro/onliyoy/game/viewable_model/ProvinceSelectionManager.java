package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventType;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class ProvinceSelectionManager implements IEventListener {

    ViewableModel viewableModel;
    public ArrayList<BorderIndicator> indicators;
    ObjectPoolYio<BorderIndicator> poolIndicators;
    RepeatYio<ProvinceSelectionManager> repeatRemove;
    public Province selectedProvince;


    public ProvinceSelectionManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        indicators = new ArrayList<>();
        viewableModel.eventsManager.addListener(this);
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemove = new RepeatYio<ProvinceSelectionManager>(this, 120) {
            @Override
            public void performAction() {
                parent.removeDeadIndicators();
            }
        };
    }


    private void initPools() {
        poolIndicators = new ObjectPoolYio<BorderIndicator>(indicators) {
            @Override
            public BorderIndicator makeNewObject() {
                return new BorderIndicator(viewableModel);
            }
        };
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        if (event.getType() == EventType.turn_end) {
            onEndTurnEventApplied();
        }
        if (Scenes.provinceManagement.isCurrentlyVisible()) {
            Scenes.provinceManagement.syncEconomicsView(); // any event may change economics of selected province
        }
    }


    @Override
    public int getListenPriority() {
        return 5;
    }


    public void onEndTurnEventApplied() {
        if (selectedProvince != null) {
            selectedProvince = null;
            viewableModel.onProvinceDeselected();
        }
        updateIndication();
        syncUI();
    }


    public void onUndoApplied(Hex hookHex) {
        if (hookHex != null) {
            selectedProvince = hookHex.getProvince();
        } else {
            selectedProvince = null;
        }
        updateIndication();
        syncUI();
    }


    public void onHexColorChanged(Hex modifiedHex) {
        if (selectedProvince == null) return;
        selectedProvince = modifiedHex.getProvince();
        updateIndication();
    }


    public void onClickedOutside() {
        selectedProvince = null;
        viewableModel.onProvinceDeselected();
        killInvalidIndicators();
        syncUI();
    }


    public void changeSelectionExternally(Province province) {
        changeSelectionExternally(province, true);
    }


    public void changeSelectionExternally(Province province, boolean soundAllowed) {
        selectedProvince = province;
        if (soundAllowed && selectedProvince != null) {
            SoundManager.playSound(SoundType.select_province);
        }
        killInvalidIndicators();
        updateIndication();
    }


    public void onHexClicked(Hex hex) {
        if (!viewableModel.entitiesManager.isHumanTurnCurrently()) return;
        Province province = hex.getProvince();
        if (hex.color != viewableModel.entitiesManager.getCurrentColor()) {
            province = null; // can only select owned provinces
        }
        if (selectedProvince != null && !selectedProvince.contains(hex) && Scenes.provinceManagement.getCurrentlyChosenPieceType() != null) {
            province = null; // can't select another province while choosing where to build
        }
        if (province == selectedProvince) return;
        selectedProvince = province;
        killInvalidIndicators();
        syncUI();
        if (selectedProvince == null) {
            viewableModel.onProvinceDeselected();
            return;
        }
        checkToPlaySelectionSound(hex);
        updateIndication();
        viewableModel.onProvinceSelected();
    }


    private void checkToPlaySelectionSound(Hex hex) {
        if (hex.hasUnit() && viewableModel.readinessManager.isReady(hex)) return;
        SoundManager.playSound(SoundType.select_province);
    }


    public void syncUI() {
        if (selectedProvince != null) {
            syncUiOnSelected();
            return;
        }
        syncUiOnNotSelected();
    }


    private void syncUiOnNotSelected() {
        if (Scenes.provinceManagement.isCurrentlyVisible()) {
            Scenes.provinceManagement.destroy();
        }
    }


    private void syncUiOnSelected() {
        if (!Scenes.provinceManagement.isCurrentlyVisible()) {
            Scenes.provinceManagement.create();
        }
        if (Scenes.provinceManagement.isCurrentlyVisible()) {
            Scenes.provinceManagement.syncEconomicsView();
        }
        viewableModel.exclamationsManager.onProvinceSelected(selectedProvince);
    }


    public void killInvalidIndicators() {
        for (BorderIndicator indicator : indicators) {
            if (isValid(indicator)) continue;
            indicator.kill();
        }
    }


    private boolean isValid(BorderIndicator borderIndicator) {
        if (selectedProvince == null) return false;
        if (borderIndicator.hex.getProvince() != selectedProvince) return false;
        return shouldBeIndicated(borderIndicator.hex, borderIndicator.direction);
    }


    public void updateIndication() {
        killInvalidIndicators();
        if (selectedProvince == null) return;
        for (Hex hex : selectedProvince.getHexes()) {
            updateIndication(hex);
        }
    }


    private void updateIndication(Hex hex) {
        for (int dir = 0; dir < 6; dir++) {
            if (!shouldBeIndicated(hex, dir)) continue;
            if (isAlreadyIndicated(hex, dir)) continue;
            addIndicator(hex, dir);
        }
    }


    private BorderIndicator getActiveIndicator(Hex hex, int direction) {
        for (BorderIndicator borderIndicator : indicators) {
            if (!borderIndicator.appearFactor.isInAppearState()) continue;
            if (borderIndicator.hex != hex) continue;
            if (borderIndicator.direction != direction) continue;
            return borderIndicator;
        }
        return null;
    }


    private boolean shouldBeIndicated(Hex hex, int direction) {
        Hex adjacentHex = viewableModel.directionsManager.getAdjacentHex(hex, direction);
        if (adjacentHex == null) return true;
        if (adjacentHex.color == hex.color) return false;
        return true;
    }


    private boolean isAlreadyIndicated(Hex hex, int direction) {
        BorderIndicator borderIndicator = getActiveIndicator(hex, direction);
        return borderIndicator != null;
    }


    private void addIndicator(Hex hex, int direction) {
        BorderIndicator freshObject = poolIndicators.getFreshObject();
        freshObject.spawn(hex, direction);
    }


    public void forceAppearance() {
        for (BorderIndicator borderIndicator : indicators) {
            FactorYio appearFactor = borderIndicator.appearFactor;
            if (appearFactor.isInAppearState()) {
                appearFactor.setValue(1);
            }
        }
    }


    public void move() {
        repeatRemove.move();
        moveIndicators();
    }


    private void removeDeadIndicators() {
        for (int i = indicators.size() - 1; i >= 0; i--) {
            BorderIndicator borderIndicator = indicators.get(i);
            if (!borderIndicator.isReadyToBeRemoved()) continue;
            indicators.remove(borderIndicator);
        }
    }


    private void moveIndicators() {
        for (BorderIndicator borderIndicator : indicators) {
            borderIndicator.move();
        }
    }
}
