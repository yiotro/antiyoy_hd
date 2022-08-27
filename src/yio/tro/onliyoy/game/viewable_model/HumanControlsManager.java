package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;

public class HumanControlsManager {

    ViewableModel viewableModel;
    private UnitsManager unitsManager;
    private ViewableUnit currentlySelectedUnit;


    public HumanControlsManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
    }


    public void onHexClicked(Hex hex) {
        updateReferences();
        if (handleConstruction(hex)) return;
        handleUnitManagement(hex);
    }


    private boolean handleConstruction(Hex hex) {
        boolean built = checkToBuildPiece(hex);
        if (!built) return false;
        unitsManager.deselectEveryone();
        viewableModel.moveZoneViewer.hide();
        SoundManager.playSound(SoundType.build);
        Scenes.provinceManagement.onPieceBuiltByHand();
        TouchMode.tmDefault.doHighlight(hex);
        return true;
    }


    private boolean checkToBuildPiece(Hex hex) {
        PieceType chosenPieceType = Scenes.provinceManagement.getCurrentlyChosenPieceType();
        if (chosenPieceType == null) return false;
        Province selectedProvince = viewableModel.provinceSelectionManager.selectedProvince;
        if (selectedProvince == null) return false;
        return viewableModel.constructionManager.onEntityRequestedConstruction(
                selectedProvince,
                hex,
                chosenPieceType
        );
    }


    private void handleUnitManagement(Hex hex) {
        updateCurrentlySelectedUnit();
        if (currentlySelectedUnit == null) {
            onHexClickedWhileNobodyIsSelected(hex);
            return;
        }
        if (currentlySelectedUnit.hex == hex) return;
        updateMoveZoneByCurrentlySelectedUnit();
        if (hex.hasUnit() && hex.color == currentlySelectedUnit.hex.color) {
            onClickedOnAnotherUnitOfSameColor(hex);
            return;
        }
        if (isHexInsideMoveZone(hex)) {
            unitsManager.deselectEveryone();
            onClickedInsideMoveZone(hex);
            return;
        }
        unitsManager.deselectEveryone();
    }


    private boolean isHexInsideMoveZone(Hex hex) {
        return viewableModel.moveZoneManager.contains(hex);
    }


    private void updateMoveZoneByCurrentlySelectedUnit() {
        viewableModel.moveZoneManager.updateForUnit(currentlySelectedUnit.hex);
    }


    private void onClickedInsideMoveZone(Hex hex) {
        if (!hex.hasUnit()) {
            // move peacefully or capture enemy hex
            if (hex.color == currentlySelectedUnit.hex.color) {
                SoundManager.playSound(SoundType.walk);
            } else {
                SoundManager.playSound(SoundType.attack);
            }
            applyMoveUnitEvent(currentlySelectedUnit, hex);
        }
        if (hex.hasUnit() && hex.color != currentlySelectedUnit.hex.color) {
            // attack enemy unit
            applyMoveUnitEvent(currentlySelectedUnit, hex);
            SoundManager.playSound(SoundType.attack);
        }
    }


    private void onClickedOnAnotherUnitOfSameColor(Hex hex) {
        if (!isHexInsideMoveZone(hex)) {
            unitsManager.deselectEveryone();
            ViewableUnit unit = getUnit(hex);
            if (unit == null) return;
            unit.select();
            return;
        }
        if (Core.getMergeResult(currentlySelectedUnit.pieceType, hex.piece) != null) {
            EventsFactory factory = viewableModel.eventsManager.factory;
            EventMerge mergeEvent = factory.createMergeEvent(
                    currentlySelectedUnit.hex,
                    hex,
                    viewableModel.getIdForNewUnit()
            );
            applyHumanEvent(mergeEvent);
            unitsManager.deselectEveryone();
            SoundManager.playSound(SoundType.merge);
        } else {
            unitsManager.onPlayerTriedToMergeInvalidPair(hex);
        }
    }


    public void applyHumanEvent(AbstractEvent event) {
        event.setAuthor(viewableModel.entitiesManager.getCurrentEntity());
        viewableModel.eventsManager.applyEvent(event);
    }


    private void updateCurrentlySelectedUnit() {
        currentlySelectedUnit = unitsManager.getCurrentlySelectedUnit();
    }


    private void updateReferences() {
        unitsManager = viewableModel.unitsManager;
    }


    private void applyMoveUnitEvent(ViewableUnit currentlySelectedUnit, Hex hex) {
        EventsFactory factory = viewableModel.eventsManager.factory;
        EventUnitMove moveUnitEvent = factory.createMoveUnitEvent(currentlySelectedUnit.hex, hex);
        if (currentlySelectedUnit.hex.color == hex.color) {
            TouchMode.tmDefault.doHighlight(hex);
        }
        applyHumanEvent(moveUnitEvent);
    }


    private void onHexClickedWhileNobodyIsSelected(Hex hex) {
        if (!hex.hasUnit()) return;
        if (!viewableModel.readinessManager.isReady(hex)) return;
        if (viewableModel.isNetMatch()) {
            GameController gameController = viewableModel.objectsLayer.gameController;
            NetRoot netRoot = gameController.yioGdxGame.netRoot;
            HColor userColor = netRoot.currentMatchData.getColor(netRoot.userData.id);
            if (hex.color != userColor) return;
        }
        ViewableUnit unit = getUnit(hex);
        if (unit == null) return; // yes, it's possible
        unit.select();
        SoundManager.playSound(SoundType.select_unit);
    }


    public ViewableUnit getUnit(Hex hex) {
        return viewableModel.unitsManager.getUnit(hex);
    }

}
