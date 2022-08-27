package yio.tro.onliyoy.menu.elements.editor;

import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class EpeButton {

    EditorPanelElement editorPanelElement;
    public CircleYio position;
    public CircleYio piecePosition;
    PointYio delta;
    public EpbType type;
    public SelectionEngineYio selectionEngineYio;


    public EpeButton(EditorPanelElement editorPanelElement) {
        this.editorPanelElement = editorPanelElement;
        position = new CircleYio();
        delta = new PointYio();
        type = null;
        selectionEngineYio = new SelectionEngineYio();
        piecePosition = new CircleYio();
    }


    void move() {
        applyDelta();
        moveSelection();
        updatePiecePosition();
    }


    private void updatePiecePosition() {
        piecePosition.setBy(position);
        piecePosition.radius *= 0.66f;
    }


    private void moveSelection() {
        if (editorPanelElement.touchedCurrently) return;
        if (selectionEngineYio.factorYio.getValue() == 0) return;
        ObjectsLayer objectsLayer = editorPanelElement.getGameController().objectsLayer;
        if (objectsLayer.editorManager.chosenType == type) return;
        selectionEngineYio.move();
    }


    boolean isTouchedBy(PointYio touchPoint) {
        if (Math.abs(touchPoint.x - position.center.x) > position.radius) return false;
        if (Math.abs(touchPoint.y - position.center.y) > position.radius) return false;
        return true;
    }


    private void applyDelta() {
        position.center.x = editorPanelElement.getViewPosition().x + delta.x;
        position.center.y = editorPanelElement.getViewPosition().y + delta.y;
    }

}
