package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class BorderIndicator implements ReusableYio {

    ViewableModel viewableModel;
    public Hex hex;
    public int direction;
    public CircleYio viewPosition;
    public FactorYio appearFactor;
    private boolean factorMoved;
    double angle;
    double targetDistance;
    public PointYio viewSize;


    public BorderIndicator(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
        viewSize = new PointYio();
    }


    @Override
    public void reset() {
        hex = null;
        direction = -1;
        viewPosition.reset();
        appearFactor.reset();
        factorMoved = false;
        viewSize.reset();
    }


    public void move() {
        moveFactor();
        updateViewPosition();
        updateViewSize();
    }


    private void updateViewSize() {
        viewSize.x = 0.25f * appearFactor.getValue() * viewPosition.radius;
        viewSize.y = getMovementValue() * 1.05f * viewPosition.radius;
    }


    public boolean isReadyToBeRemoved() {
        return appearFactor.isInDestroyState() && appearFactor.getValue() == 0;
    }


    public boolean isCurrentlyVisible() {
        if (appearFactor.getValue() == 0) return false;
        GameController gameController = viewableModel.objectsLayer.gameController;
        return gameController.cameraController.isCircleInViewFrame(viewPosition);
    }


    private void updateViewPosition() {
        if (!factorMoved) return;
        viewPosition.center.setBy(hex.position.center);
        viewPosition.center.relocateRadial(
                getMovementValue() * targetDistance,
                angle
        );
        viewPosition.radius = (0.2f + 0.8f * appearFactor.getValue()) * 0.518f * hex.position.radius;
    }


    private float getMovementValue() {
        return 0.75f + 0.25f * appearFactor.getValue();
    }


    private void moveFactor() {
        factorMoved = appearFactor.move();
    }


    public void spawn(Hex hex, int direction) {
        this.hex = hex;
        this.direction = direction;
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 5.1);
        angle = viewableModel.directionsManager.getAngle(direction);
        targetDistance = viewableModel.getHexRadius() * Math.cos(Math.PI / 6);
        viewPosition.angle = angle;
    }


    public void kill() {
        appearFactor.destroy(MovementType.lighty, 7);
    }
}
