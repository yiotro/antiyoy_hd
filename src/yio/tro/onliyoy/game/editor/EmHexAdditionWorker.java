package yio.tro.onliyoy.game.editor;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.editor.EpbType;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;

public class EmHexAdditionWorker {

    EditorManager editorManager;
    EpbType[] colorTypes;
    PointYio tempPoint;
    private int coordinate1;
    private int coordinate2;


    public EmHexAdditionWorker(EditorManager editorManager) {
        this.editorManager = editorManager;
        tempPoint = new PointYio();
        colorTypes = new EpbType[]{
                EpbType.gray,
                EpbType.yellow,
                EpbType.green,
                EpbType.aqua,
                EpbType.cyan,
                EpbType.blue,
                EpbType.purple,
                EpbType.red,
                EpbType.brown,
                EpbType.mint,
                EpbType.lavender,
                EpbType.brass,
                EpbType.rose,
                EpbType.ice,
                EpbType.algae,
                EpbType.orchid,
                EpbType.whiskey,
        };
    }


    public void onPointTouched(PointYio touchPoint) {
        if (!isColorType(editorManager.chosenType)) return;
        if (isGraphNodePresent(touchPoint)) return;
        ViewableModel viewableModel = getViewableModel();
        if (!viewableModel.bounds.isPointInside(touchPoint)) return;
        updateCoordinatesByPoint(touchPoint);
        if (viewableModel.getHex(coordinate1, coordinate2) != null) return; // yes, this check is necessary
        Hex hex = viewableModel.addHex(coordinate1, coordinate2);
        if (!viewableModel.bounds.contains(hex.position)) {
            viewableModel.removeHex(hex);
            return;
        }
        linkToNearbyHexes(hex);
        editorManager.onGraphNodeAdded(hex);
    }


    private void linkToNearbyHexes(Hex hex) {
        tryToLink(hex, 1, 0);
        tryToLink(hex, 0, 1);
        tryToLink(hex, -1, 1);
        tryToLink(hex, -1, 0);
        tryToLink(hex, 0, -1);
        tryToLink(hex, 1, -1);
    }


    private void tryToLink(Hex hex, int delta1, int delta2) {
        int c1 = hex.coordinate1 + delta1;
        int c2 = hex.coordinate2 + delta2;
        Hex nearbyHex = getViewableModel().getHex(c1, c2);
        if (nearbyHex == null) return;
        if (!hex.adjacentHexes.contains(nearbyHex)) {
            hex.addAdjacentHex(nearbyHex);
        }
        if (!nearbyHex.adjacentHexes.contains(hex)) {
            nearbyHex.addAdjacentHex(hex);
        }
    }


    private void updateCoordinatesByPoint(PointYio point) {
        // this can be checked via CoreModel.applyCoordinatesToTempPoint()
        float hexRadius = getViewableModel().getHexRadius();
        float dy = (float) (hexRadius * Math.cos(Math.PI / 6));
        float dx = 1.5f * hexRadius;
        RectangleYio bounds = getViewableModel().bounds;
        float cx = bounds.x + bounds.width / 2;
        float cy = bounds.y + bounds.height / 2;
        float fc2 = (point.x - cx) / dx;
        float fc1 = (point.y - cy - dy * fc2) / (2 * dy);
        coordinate1 = getClosestInteger(fc1);
        coordinate2 = getClosestInteger(fc2);
    }


    private int getClosestInteger(double value) {
        if (value < 0) {
            return (int) (value - 0.5);
        }
        return (int) (value + 0.5);
    }


    private ViewableModel getViewableModel() {
        return editorManager.objectsLayer.viewableModel;
    }


    private boolean isGraphNodePresent(PointYio touchPoint) {
        Hex closestHex = getViewableModel().getClosestHex(touchPoint);
        if (closestHex == null) return false;
        return isTouchInsideHex(closestHex, touchPoint);
    }


    private boolean isTouchInsideHex(Hex hex, PointYio touchPoint) {
        return hex.position.center.distanceTo(touchPoint) < getViewableModel().getHexRadius();
    }


    boolean isColorType(EpbType epbType) {
        if (epbType == null) return false;
        for (int i = 0; i < colorTypes.length; i++) {
            if (colorTypes[i] == epbType) return true;
        }
        return false;
    }
}
