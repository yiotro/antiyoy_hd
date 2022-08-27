package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class ViewableHex implements ReusableYio {

    LandsManager landsManager;
    public Hex hex;
    public HColor previousColor;
    public HColor currentColor;
    public FactorYio colorFactor;
    public FactorYio pieceFactor;
    public PieceType pieceType;
    public VhSpawnType pieceSpawnType;
    public boolean inTransition;


    public ViewableHex(LandsManager landsManager) {
        this.landsManager = landsManager;
        colorFactor = new FactorYio();
        pieceFactor = new FactorYio();
    }


    @Override
    public void reset() {
        hex = null;
        previousColor = null;
        currentColor = null;
        colorFactor.reset();
        colorFactor.setValue(1);
        colorFactor.stop();
        pieceFactor.reset();
        pieceType = null;
        pieceSpawnType = null;
        inTransition = false;
    }


    void move() {
        colorFactor.move();
        pieceFactor.move();
    }


    public boolean isCurrentlyVisible() {
        ObjectsLayer objectsLayer = landsManager.viewableModel.objectsLayer;
        GameController gameController = objectsLayer.gameController;
        return gameController.cameraController.isCircleInViewFrame(hex.position);
    }


    public void applyColorChange(HColor color) {
        if (currentColor == color) return;
        previousColor = currentColor;
        currentColor = color;
        colorFactor.reset();
        launchColorFactor();
        landsManager.viewableModel.cacheManager.onViewableHexChangedColor(this);
    }


    private void launchColorFactor() {
        landsManager.onHexBecameAnimated(this);
        colorFactor.appear(MovementType.inertia, 1.8);
    }


    public boolean isPieceFactorInMovementMode() {
        if (pieceFactor.getValue() == 1) return false;
        if (pieceFactor.getValue() == 0) return false;
        return true;
    }


    public boolean isColorFactorInMovementMode() {
        return colorFactor.getValue() < 1;
    }


    public void setPieceType(PieceType pieceType) {
        setPieceType(pieceType, VhSpawnType.normal);
    }


    public void setPieceType(PieceType pieceType, VhSpawnType vhSpawnType) {
        if (this.pieceType == pieceType) return;
        PieceType previousPieceType = this.pieceType;
        this.pieceType = pieceType;
        pieceSpawnType = vhSpawnType;
        onPieceTypeChanged(previousPieceType);
    }


    private void onPieceTypeChanged(PieceType previousPieceType) {
        pieceFactor.reset();
        if (pieceType == null) return;
        if (shouldAnimatePieceSpawnProcess(previousPieceType)) {
            pieceFactor.reset();
            pieceFactor.appear(MovementType.approach, 6.5);
            landsManager.onHexBecameAnimated(this);
            return;
        }
        pieceFactor.setValue(1);
        pieceFactor.stop();
        landsManager.onHexBecameAnimated(this);
    }


    private boolean shouldAnimatePieceSpawnProcess(PieceType previousPieceType) {
        if (!landsManager.viewableModel.transitionsEnabled) return false;
        if (previousPieceType == null) return true;
        if (Core.isUnit(previousPieceType)) return true;
        return false;
    }


    public void setHex(Hex hex) {
        this.hex = hex;
        currentColor = hex.color;
        if (hex.hasPiece()) {
            setPieceType(hex.piece);
        }
    }
}
