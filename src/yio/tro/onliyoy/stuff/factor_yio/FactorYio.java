package yio.tro.onliyoy.stuff.factor_yio;

import yio.tro.onliyoy.RefreshRateDetector;
import yio.tro.onliyoy.Yio;

public class FactorYio {
	
    boolean inAppearState;
    boolean inDestroyState;
    double value;
    double gravity;
    double dy;
    double speedMultiplier;
    AbstractMoveBehavior behavior;
    boolean needsToMove;


    public FactorYio() {
        behavior = null;
        inAppearState = false;
        inDestroyState = false;
    }


    public boolean move() {
        if (!needsToMove) return false;
        behavior.move(this);
        behavior.updateNeedsToMoveValue(this);
        return true;
    }


    public void appear(MovementType movementType, double speed) {
        setBehavior(movementType);
        prepareForMovement(speed);
        gravity = 0.01;
        inAppearState = true;
        behavior.onAppear(this);
    }


    public void destroy(MovementType movementType, double speed) {
        setBehavior(movementType);
        prepareForMovement(speed);
        gravity = -0.01;
        inDestroyState = true;
        behavior.onDestroy(this);
    }


    private void prepareForMovement(double speed) {
        setDy(0);
        speedMultiplier = 0.3 * speed * RefreshRateDetector.getInstance().multiplier;
        inDestroyState = false;
        inAppearState = false;
        needsToMove = true;
    }


    private void setBehavior(MovementType movementType) {
        behavior = MbFactoryYio.getInstance().getBehavior(movementType);
    }


    public void setValues(double f, double dy) {
        this.value = f;
        this.dy = dy;
    }


    public void setDy(double dy) {
        this.dy = dy;
    }


    public void stop() {
        appear(MovementType.stay, 1);
        needsToMove = false;
        inAppearState = false;
        inDestroyState = false;
    }


    public float getValue() {
        return (float) value;
    }


    public void setValue(double value) {
        this.value = value;
    }


    public boolean isInAppearState() {
        return inAppearState;
    }


    public boolean isInDestroyState() {
        return inDestroyState;
    }



    public void reset() {
        setValues(0, 0);
        stop();
    }


    @Override
    public String toString() {
        return "Factor: " +
                "state(" + inAppearState + ", " + inDestroyState + "), " +
                "value(" + Yio.roundUp(value, 2) + "), " +
                "dy(" + Yio.roundUp(dy, 2) + "), " +
                "gravity(" + Yio.roundUp(gravity, 2) + ")";
    }
}