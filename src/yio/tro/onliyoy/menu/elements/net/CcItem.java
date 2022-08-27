package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.stuff.*;

public class CcItem {

    ChooseColorElement cmcElement;
    public RectangleYio position;
    PointYio delta;
    public HColor color;
    public SelectionEngineYio selectionEngineYio;
    public RectangleYio selectionPosition;
    private float sOffset;


    public CcItem(ChooseColorElement chooseColorElement) {
        this.cmcElement = chooseColorElement;
        position = new RectangleYio();
        delta = new PointYio();
        color = null;
        selectionEngineYio = new SelectionEngineYio();
        selectionPosition = new RectangleYio();
        sOffset = 0.02f * GraphicsYio.width;
    }


    void move() {
        moveSelection();
        updatePosition();
        updateSelectionPosition();
    }


    private void updateSelectionPosition() {
        selectionPosition.setBy(position);
        selectionPosition.increase(sOffset);
    }


    private void updatePosition() {
        position.x = cmcElement.getViewPosition().x + delta.x;
        position.y = cmcElement.getViewPosition().y + delta.y;
    }


    private void moveSelection() {
        if (cmcElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    boolean isTouchedBy(PointYio touchPoint) {
        return position.isPointInside(touchPoint, sOffset);
    }
}
