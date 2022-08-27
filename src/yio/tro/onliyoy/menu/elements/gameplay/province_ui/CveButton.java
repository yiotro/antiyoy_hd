package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import yio.tro.onliyoy.game.core_model.AbstractRuleset;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

import java.util.ArrayList;

public class CveButton {

    ConstructionViewElement constructionViewElement;
    public CircleYio viewPosition;
    public CircleYio touchPosition;
    public PieceType pieceType;
    PieceType[] possiblePieceTypes;
    public SelectionEngineYio selectionEngineYio;
    private float defRadius;
    public boolean dynamic;
    public String key;


    public CveButton(ConstructionViewElement constructionViewElement, String key, PieceType[] pieceTypes) {
        this.constructionViewElement = constructionViewElement;
        this.key = key;
        initPossiblePieceTypes(pieceTypes);
        viewPosition = new CircleYio();
        touchPosition = new CircleYio();
        touchPosition.setRadius(0.12f * GraphicsYio.width);
        selectionEngineYio = new SelectionEngineYio();
        defRadius = 0.06f * GraphicsYio.width;
        dynamic = false;
    }


    private void initPossiblePieceTypes(PieceType[] pieceTypes) {
        ViewableModel viewableModel = constructionViewElement.getViewableModel();
        AbstractRuleset ruleset = viewableModel.ruleset;
        ArrayList<PieceType> tempList = new ArrayList<>();
        for (int i = 0; i < pieceTypes.length; i++) {
            if (!ruleset.isBuildable(pieceTypes[i])) continue;
            tempList.add(pieceTypes[i]);
        }
        possiblePieceTypes = new PieceType[tempList.size()];
        int index = 0;
        for (PieceType type : tempList) {
            possiblePieceTypes[index] = type;
            index++;
        }
    }


    boolean isTouchedBy(PointYio touchPoint) {
        return touchPoint.distanceTo(touchPosition.center) < touchPosition.radius;
    }


    void onIndicatorSpawned(PieceType pieceType) {
        int index = indexOfPieceType(pieceType);
        int nextIndex = getNextIndex(index);
        setPieceType(possiblePieceTypes[nextIndex]);
    }


    private int getNextIndex(int prevIndex) {
        int index = prevIndex + 1;
        if (index >= possiblePieceTypes.length) {
            index = 0;
        }
        return index;
    }


    private int indexOfPieceType(PieceType pieceType) {
        for (int i = 0; i < possiblePieceTypes.length; i++) {
            if (possiblePieceTypes[i] == pieceType) return i;
        }
        return -1;
    }


    void move() {
        updateViewPosition();
        updateTouchPosition();
        moveSelection();
    }


    private void updateTouchPosition() {
        touchPosition.center.setBy(viewPosition.center);
    }


    void resetPieceType() {
        setPieceType(possiblePieceTypes[0]);
    }


    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
        updateViewRadius();
    }


    public PieceType getDefaultPieceType() {
        return possiblePieceTypes[0];
    }


    private void updateViewRadius() {
        viewPosition.setRadius(defRadius);
        switch (getCurrentViewPieceType()) {
            default:
                break;
            case farm:
                viewPosition.radius *= 1.3f;
                break;
        }
    }


    public PieceType getCurrentViewPieceType() {
        if (dynamic) return pieceType;
        return possiblePieceTypes[0];
    }


    private void moveSelection() {
        if (constructionViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateViewPosition() {
        viewPosition.center.y = constructionViewElement.getViewPosition().y + defRadius;
    }


    public void setKey(String key) {
        this.key = key;
    }
}
