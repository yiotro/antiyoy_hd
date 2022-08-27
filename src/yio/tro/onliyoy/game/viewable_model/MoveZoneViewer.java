package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class MoveZoneViewer {

    ViewableModel viewableModel;
    public ArrayList<Hex> hexes;
    public ArrayList<BorderIndicator> indicators;
    ObjectPoolYio<BorderIndicator> poolIndicators;
    RepeatYio<MoveZoneViewer> repeatRemove;
    public GhDataContainer ghDataContainer;
    public FactorYio darkenFactor;


    public MoveZoneViewer(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        hexes = new ArrayList<>();
        indicators = new ArrayList<>();
        ghDataContainer = new GhDataContainer();
        darkenFactor = new FactorYio();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemove = new RepeatYio<MoveZoneViewer>(this, 120) {
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


    public void killAllIndicators() {
        // to hide move zone use hide() method instead of this
        for (BorderIndicator indicator : indicators) {
            indicator.kill();
        }
    }


    public void onProvinceDeselected() {
        hide();
    }


    public void doIndicate() {
        killAllIndicators();
        prepareFlags();
        for (Hex hex : hexes) {
            doIndicate(hex);
        }
        ghDataContainer.update(viewableModel, hexes);
        commandDarkenFactor();
    }


    private void commandDarkenFactor() {
        if (hexes.size() == 0) {
            darkenFactor.destroy(MovementType.lighty, 7);
            return;
        }
        darkenFactor.appear(MovementType.approach, 8);
    }


    private void doIndicate(Hex hex) {
        for (int dir = 0; dir < 6; dir++) {
            if (!shouldBeIndicated(hex, dir)) continue;
            addIndicator(hex, dir);
        }
    }


    private void prepareFlags() {
        resetFlags();
        for (Hex hex : hexes) {
            hex.flag = true;
        }
    }


    private void resetFlags() {
        for (Hex hex : viewableModel.hexes) {
            hex.flag = false;
        }
    }


    private boolean shouldBeIndicated(Hex hex, int direction) {
        Hex adjacentHex = viewableModel.directionsManager.getAdjacentHex(hex, direction);
        if (adjacentHex == null) return true;
        if (adjacentHex.flag == hex.flag) return false;
        return hex.flag;
    }


    private void addIndicator(Hex hex, int direction) {
        BorderIndicator freshObject = poolIndicators.getFreshObject();
        freshObject.spawn(hex, direction);
    }


    public void move() {
        repeatRemove.move();
        moveIndicators();
        darkenFactor.move();
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


    public void onUnitSelected(ViewableUnit viewableUnit) {
        viewableModel.moveZoneManager.updateForUnit(viewableUnit.hex);
        applySync();
    }


    public void onUnitDeselected(ViewableUnit viewableUnit) {
        viewableModel.moveZoneManager.clear();
        applySync();
    }


    public void onPieceChosenInConstructionView(PieceType pieceType) {
        Province selectedProvince = viewableModel.provinceSelectionManager.selectedProvince;
        if (selectedProvince == null) {
            System.out.println("MoveZoneViewer.onUnitChosenToBuild: problem");
            return;
        }
        viewableModel.moveZoneManager.clear();
        if (Core.isUnit(pieceType)) {
            int strength = Core.getStrength(pieceType);
            viewableModel.ruleset.updateMoveZoneForUnitConstruction(selectedProvince, strength);
        }
        if (pieceType == PieceType.farm) {
            viewableModel.moveZoneManager.updateForFarm(selectedProvince);
        }
        applySync();
    }


    public void hide() {
        hexes.clear();
        doIndicate();
    }


    public void applySync() {
        hexes.clear();
        hexes.addAll(viewableModel.moveZoneManager.hexes);
        doIndicate();
    }
}
