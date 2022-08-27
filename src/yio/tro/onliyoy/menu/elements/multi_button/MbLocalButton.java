package yio.tro.onliyoy.menu.elements.multi_button;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class MbLocalButton {

    MultiButtonElement multiButtonElement;
    public String key;
    public RenderableTextYio title;
    public BackgroundYio backgroundYio;
    public RectangleYio viewPosition;
    Reaction reaction;
    private int index;
    private float targetHeight;
    public SelectionEngineYio selectionEngineYio;


    public MbLocalButton(MultiButtonElement multiButtonElement) {
        this.multiButtonElement = multiButtonElement;
        key = "";
        title = new RenderableTextYio();
        viewPosition = new RectangleYio();
        title.setFont(Fonts.buttonFont);
        selectionEngineYio = new SelectionEngineYio();
        backgroundYio = null;
        reaction = null;
    }


    void move() {
        updateViewPosition();
        updateTitlePosition();
        moveSelection();
    }


    private void moveSelection() {
        if (multiButtonElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        title.centerHorizontal(viewPosition);
        title.updateBounds();
    }


    private void updateViewPosition() {
        RectangleYio srcPos = multiButtonElement.getViewPosition();
        viewPosition.x = srcPos.x;
        viewPosition.width = srcPos.width;
        viewPosition.height = targetHeight + (1 - multiButtonElement.getFactor().getValue()) * 0.9f * targetHeight;
        viewPosition.y = srcPos.y + srcPos.height - (index + 1) * viewPosition.height;
    }


    boolean isTouchedBy(PointYio touchPoint, float offset) {
        return viewPosition.isPointInside(touchPoint, offset);
    }


    public void setTitle(String key) {
        this.key = key;
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
    }


    public void setBackgroundYio(BackgroundYio backgroundYio) {
        this.backgroundYio = backgroundYio;
    }


    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }


    public void setIndex(int index) {
        this.index = index;
    }


    public void setTargetHeight(float targetHeight) {
        this.targetHeight = targetHeight;
    }
}
