package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class Pigeon implements ReusableYio {

    PigeonsManager pigeonsManager;
    public CircleYio viewPosition;
    public PointYio targetPosition;
    public PointYio previousPosition;
    public FactorYio relocationFactor;


    public Pigeon(PigeonsManager pigeonsManager) {
        this.pigeonsManager = pigeonsManager;
        viewPosition = new CircleYio();
        targetPosition = new PointYio();
        previousPosition = new PointYio();
        relocationFactor = new FactorYio();
    }


    @Override
    public void reset() {
        viewPosition.reset();
        targetPosition.reset();
        previousPosition.reset();
        relocationFactor.reset();
    }


    void move() {
        relocationFactor.move();
        updateViewPosition();
    }


    private void updateViewPosition() {
        viewPosition.center.setBy(previousPosition);
        float f = relocationFactor.getValue();
        viewPosition.center.x += f * (targetPosition.x - viewPosition.center.x);
        viewPosition.center.y += f * (targetPosition.y - viewPosition.center.y);
    }


    public void launch(Hex start, Hex finish, boolean quick) {
        previousPosition.setBy(start.position.center);
        targetPosition.setBy(finish.position.center);
        viewPosition.center.setBy(previousPosition);
        viewPosition.setRadius(0.6 * start.position.radius);
        relocationFactor.reset();
        launchRelocationFactor(quick);
    }


    private void launchRelocationFactor(boolean quick) {
        if (shouldSlightlySpeedUpPigeons()) {
            relocationFactor.appear(MovementType.inertia, 3);
            return;
        }
        if (quick) {
            relocationFactor.appear(MovementType.approach, 5);
        } else {
            relocationFactor.appear(MovementType.inertia, 1);
        }
    }


    private boolean shouldSlightlySpeedUpPigeons() {
        ViewableModel viewableModel = pigeonsManager.viewableModel;
        if (viewableModel.entitiesManager.isInAiOnlyMode()) return true;
        if (viewableModel.objectsLayer.replayManager.active) return true;
        return false;
    }


    public boolean isRelocating() {
        return relocationFactor.getValue() < 1;
    }


    public double getAlpha() {
        float f = relocationFactor.getValue();
        if (f < 0.02) {
            return f * 50;
        }
        if (f > 0.98) {
            return 50 * (1 - f);
        }
        return 1;
    }


    public boolean isCurrentlyVisible() {
        if (relocationFactor.getValue() == 0) return false;
        if (relocationFactor.getValue() == 1) return false;
        return getFrame().intersects(viewPosition);
    }


    private RectangleYio getFrame() {
        return getObjectsLayer().gameController.cameraController.frame;
    }


    private ObjectsLayer getObjectsLayer() {
        return pigeonsManager.viewableModel.objectsLayer;
    }
}
