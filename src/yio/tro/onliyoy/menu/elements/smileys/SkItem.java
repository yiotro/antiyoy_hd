package yio.tro.onliyoy.menu.elements.smileys;

import yio.tro.onliyoy.game.core_model.SmileyType;
import yio.tro.onliyoy.stuff.*;

public class SkItem {

    public CircleYio position;
    public RectangleYio viewPosition;
    public PointYio delta;
    public SmileyType smileyType;
    public SelectionEngineYio selectionEngineYio;
    public RectangleYio touchPosition;
    public SkType skType;
    public float angle;
    long selectionTime;


    public SkItem() {
        position = new CircleYio();
        delta = new PointYio();
        smileyType = null;
        viewPosition = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
        touchPosition = new RectangleYio();
        skType = null;
        selectionTime = 0;
        angle = 0;
    }


    public void move(RectangleYio parentPosition, boolean touchedCurrently) {
        updatePosition(parentPosition);
        updateViewPosition();
        moveSelection(touchedCurrently);
        updateTouchPosition();
        checkToRotate();
    }


    public void checkToRotate() {
        if (smileyType != SmileyType.hypno) return;
        angle += 0.05;
    }


    private void updateTouchPosition() {
        if (skType == SkType.space) {
            touchPosition.setBy(viewPosition);
            touchPosition.increase(0.01f * GraphicsYio.width);
            return;
        }
        touchPosition.setBy(viewPosition);
        touchPosition.increase(0.04f * GraphicsYio.width);
    }


    private void moveSelection(boolean touchedCurrently) {
        if (touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateViewPosition() {
        viewPosition.x = position.center.x - viewPosition.width / 2;
        viewPosition.y = position.center.y - viewPosition.height / 2;
    }


    private void updatePosition(RectangleYio parentPosition) {
        position.center.x = parentPosition.x + delta.x;
        position.center.y = parentPosition.y + delta.y;
    }


    public boolean isCurrentlyVisible(RectangleYio parentPosition) {
        return viewPosition.x + viewPosition.width <= parentPosition.x + parentPosition.width;
    }


    public void setRadius(double r) {
        position.setRadius(r);
        viewPosition.width = (float) (2 * 0.6f * r);
        viewPosition.height = (float) (2 * 0.6f * r);
        if (SkViewField.isCondensed(smileyType)) {
            viewPosition.width *= 0.9;
            viewPosition.height *= 0.9;
        }
        if (skType == SkType.space) {
            viewPosition.width *= 5;
        }
        if (skType == SkType.choose_color) {
            viewPosition.increase(-0.1 * viewPosition.width);
        }
    }


    public void applySelection() {
        selectionTime = System.currentTimeMillis();
        selectionEngineYio.applySelection();
    }


    boolean isTouchedBy(PointYio touchPoint, float offset) {
        return touchPosition.isPointInside(touchPoint, offset);
    }
}
