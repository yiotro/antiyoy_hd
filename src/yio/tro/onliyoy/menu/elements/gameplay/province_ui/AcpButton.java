package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class AcpButton implements ReusableYio {

    AdvancedConstructionPanelElement advancedConstructionPanelElement;
    public CircleYio viewPosition;
    public RectangleYio touchPosition;
    float delta;
    public SelectionEngineYio selectionEngineYio;
    public PieceType pieceType;


    public AcpButton(AdvancedConstructionPanelElement advancedConstructionPanelElement) {
        this.advancedConstructionPanelElement = advancedConstructionPanelElement;
        viewPosition = new CircleYio();
        touchPosition = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
    }


    void move() {
        updateViewPosition();
        updateTouchPosition();
        moveSelection();
    }


    private void moveSelection() {
        if (advancedConstructionPanelElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateTouchPosition() {
        RectangleYio src = advancedConstructionPanelElement.getViewPosition();
        float r = src.height / 2;
        touchPosition.x = viewPosition.center.x - r;
        touchPosition.y = viewPosition.center.y - r;
        touchPosition.width = 2 * r;
        touchPosition.height = 2 * r;
    }


    private void updateViewPosition() {
        RectangleYio src = advancedConstructionPanelElement.getViewPosition();
        viewPosition.center.x = src.x + delta + src.height / 2;
        viewPosition.center.y = src.y + src.height / 2;
        viewPosition.radius = 0.33f * src.height;
    }


    boolean isTouchedBy(PointYio touchPoint) {
        return touchPosition.isPointInside(touchPoint);
    }


    @Override
    public void reset() {
        viewPosition.reset();
        touchPosition.reset();
        delta = 0;
        selectionEngineYio.reset();
        pieceType = null;
    }
}
