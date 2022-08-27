package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class VrmIndicator implements ReusableYio {

    ViewableModel viewableModel;
    Hex hex;
    int direction;
    public CircleYio viewPosition;
    public FactorYio appearFactor;
    private boolean factorMoved;
    double angle;
    double targetDistance;
    public PointYio viewSize;
    public RelationType relationType;
    boolean directionalAnimation;


    public VrmIndicator(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
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
        relationType = null;
        directionalAnimation = false;
    }


    void move() {
        moveFactor();
        updateViewPosition();
        updateViewSize();
    }


    private void updateViewSize() {
        viewSize.x = getMovementValue() * 0.09f * appearFactor.getValue() * viewPosition.radius;
        viewSize.y = 0.75f * viewPosition.radius;
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
        float f = getMovementValue();
        if (!directionalAnimation) {
            f = 1;
        }
        viewPosition.center.relocateRadial(
                f * targetDistance,
                angle
        );
        viewPosition.radius = 0.49f * hex.position.radius;
    }


    private float getMovementValue() {
        return 0.75f + 0.25f * appearFactor.getValue();
    }


    private void moveFactor() {
        factorMoved = appearFactor.move();
    }


    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }


    public void spawn(Hex hex, int direction) {
        this.hex = hex;
        this.direction = direction;
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 5.5);
        angle = viewableModel.directionsManager.getAngle(direction);
        targetDistance = viewableModel.getHexRadius() * Math.cos(Math.PI / 6);
        viewPosition.angle = angle;
    }


    public void kill() {
        appearFactor.destroy(MovementType.lighty, 7);
    }


    public void setDirectionalAnimation(boolean directionalAnimation) {
        this.directionalAnimation = directionalAnimation;
    }


    @Override
    public String toString() {
        return "[VrmIndicator: " +
                hex + " " + direction +
                "]";
    }
}

