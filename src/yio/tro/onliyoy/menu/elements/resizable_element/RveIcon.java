package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class RveIcon {

    AbstractRveClickableItem holder;
    public final RveIconType type;
    public CircleYio position;
    public SelectionEngineYio selectionEngineYio;
    double targetAngle;
    boolean angleMovementMode;
    float touchOffset;


    public RveIcon(AbstractRveClickableItem holder, RveIconType type) {
        this.holder = holder;
        this.type = type;
        position = new CircleYio();
        selectionEngineYio = new SelectionEngineYio();
        targetAngle = 0;
        angleMovementMode = false;
        touchOffset = 0;
    }


    void move() {
        moveSelection();
        moveAngle();
    }


    private void moveAngle() {
        if (!angleMovementMode) return;
        if (Yio.distanceBetweenAngles(position.angle, targetAngle) < 0.01) {
            position.setAngle(targetAngle);
            angleMovementMode = false;
            return;
        }
        position.angle += 0.1 * (targetAngle - position.angle);
    }


    private void moveSelection() {
        if (holder.resizableViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        if (Math.abs(touchPoint.x - position.center.x) > position.radius + touchOffset) return false;
        if (Math.abs(touchPoint.y - position.center.y) > position.radius + touchOffset) return false;
        return true;
    }


    public void setTargetAngle(double targetAngle) {
        this.targetAngle = targetAngle;
        angleMovementMode = true;
        prepareTargetAngle();
    }


    private void prepareTargetAngle() {
        while (targetAngle > position.angle) {
            targetAngle -= 2 * Math.PI;
        }
    }


    public void setTouchOffset(float touchOffset) {
        this.touchOffset = touchOffset;
    }
}
