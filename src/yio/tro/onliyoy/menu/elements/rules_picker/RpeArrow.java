package yio.tro.onliyoy.menu.elements.rules_picker;

import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class RpeArrow {

    RulesPickerElement rulesPickerElement;
    public boolean right;
    public CircleYio position;
    public SelectionEngineYio selectionEngineYio;


    public RpeArrow(RulesPickerElement rulesPickerElement, boolean right) {
        this.rulesPickerElement = rulesPickerElement;
        this.right = right;
        position = new CircleYio();
        selectionEngineYio = new SelectionEngineYio();
        if (!right) {
            position.angle = Math.PI;
        }
    }


    void move() {
        moveSelection();
        updatePosition();
    }


    private void updatePosition() {
        RectangleYio srcPos = rulesPickerElement.getViewPosition();
        position.radius = srcPos.height / 2;
        position.center.y = srcPos.y + position.radius;
        if (right) {
            position.center.x = srcPos.x + srcPos.width - position.radius;
        } else {
            position.center.x = srcPos.x + position.radius;
        }
    }


    private void moveSelection() {
        if (rulesPickerElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    boolean isTouchedBy(PointYio touchPoint, float offset) {
        if (Math.abs(touchPoint.x - position.center.x) > position.radius + offset) return false;
        if (Math.abs(touchPoint.y - position.center.y) > position.radius + offset) return false;
        return true;
    }
}
