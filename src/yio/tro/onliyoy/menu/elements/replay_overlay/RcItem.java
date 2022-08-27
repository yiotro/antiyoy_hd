package yio.tro.onliyoy.menu.elements.replay_overlay;

import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class RcItem {

    ReplayControlElement replayControlElement;
    public RcItemType type;
    public CircleYio viewPosition;
    PointYio delta;
    public RectangleYio selectionPosition;
    public RectangleYio touchPosition;
    public SelectionEngineYio selectionEngineYio;


    public RcItem(ReplayControlElement replayControlElement) {
        this.replayControlElement = replayControlElement;
        type = null;
        viewPosition = new CircleYio();
        delta = new PointYio();
        selectionEngineYio = new SelectionEngineYio();
        selectionPosition = new RectangleYio();
        touchPosition = new RectangleYio();
    }


    void move() {
        updateViewPosition();
        moveSelection();
        updateSelectionPosition();
        updateTouchPosition();
    }


    private void updateTouchPosition() {
        touchPosition.setBy(selectionPosition);
        touchPosition.height *= 2;
    }


    private void updateSelectionPosition() {
        selectionPosition.width = 4 * replayControlElement.itemTouchOffset;
        selectionPosition.height = replayControlElement.getPosition().height;
        selectionPosition.x = viewPosition.center.x - selectionPosition.width / 2;
        selectionPosition.y = replayControlElement.getViewPosition().y;
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return touchPosition.isPointInside(touchPoint);
    }


    private void moveSelection() {
        if (replayControlElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateViewPosition() {
        viewPosition.center.x = replayControlElement.getViewPosition().x + delta.x;
        viewPosition.center.y = replayControlElement.getViewPosition().y + delta.y;
    }


    public void setType(RcItemType type) {
        this.type = type;
    }
}
