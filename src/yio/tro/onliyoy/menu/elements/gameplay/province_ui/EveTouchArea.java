package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class EveTouchArea {

    EconomicsViewElement economicsViewElement;
    public CircleYio position;
    public SelectionEngineYio selectionEngineYio;
    CircleYio parentCircle;
    Reaction reaction;


    public EveTouchArea(EconomicsViewElement economicsViewElement) {
        this.economicsViewElement = economicsViewElement;
        position = new CircleYio();
        selectionEngineYio = new SelectionEngineYio();
        parentCircle = null;
    }


    void move() {
        applyParentCircle();
        moveSelection();
    }


    private void moveSelection() {
        if (economicsViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    boolean isTouchedBy(PointYio touchPoint) {
        return touchPoint.distanceTo(position.center) < position.radius;
    }


    private void applyParentCircle() {
        if (parentCircle == null) return;
        position.setBy(parentCircle);
        position.radius *= 2;
    }


    public void setParentCircle(CircleYio parentCircle) {
        this.parentCircle = parentCircle;
    }


    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }
}
