package yio.tro.onliyoy.game.editor;

import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class ViewableChange implements ReusableYio {

    EditorManager editorManager;
    public Hex hex;
    public HColor previousColor;
    public HColor currentColor;
    public FactorYio colorFactor;
    public FactorYio pieceFactor;
    public PieceType pieceType;


    public ViewableChange(EditorManager editorManager) {
        this.editorManager = editorManager;
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
    }


    void move() {
        colorFactor.move();
        pieceFactor.move();
    }


    public boolean isCurrentlyVisible() {
        ObjectsLayer objectsLayer = editorManager.objectsLayer;
        GameController gameController = objectsLayer.gameController;
        return gameController.cameraController.isCircleInViewFrame(hex.position);
    }


    public void applyColorChange(HColor color) {
        if (currentColor == color) return;
        previousColor = currentColor;
        currentColor = color;
        colorFactor.reset();
        colorFactor.appear(MovementType.inertia, 2.2);
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
        if (this.pieceType == pieceType) return;
        PieceType previousPieceType = this.pieceType;
        this.pieceType = pieceType;
        onPieceTypeChanged(previousPieceType);
    }


    private void onPieceTypeChanged(PieceType previousPieceType) {
        pieceFactor.reset();
        if (pieceType == null) return;
        if (previousPieceType == null || Core.isUnit(previousPieceType)) {
            pieceFactor.reset();
            pieceFactor.appear(MovementType.approach, 6.5);
            return;
        }
        pieceFactor.setValue(1);
        pieceFactor.stop();
    }


    public void setHex(Hex hex) {
        this.hex = hex;
        currentColor = hex.color;
        if (hex.hasPiece()) {
            setPieceType(hex.piece);
        }
    }
}
