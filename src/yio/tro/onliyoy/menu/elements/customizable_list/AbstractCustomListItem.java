package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.*;

public abstract class AbstractCustomListItem {

    public CustomizableListYio customizableListYio;
    public RectangleYio viewPosition;
    public PointYio positionDelta;
    public SelectionEngineYio selectionEngineYio;


    public AbstractCustomListItem() {
        customizableListYio = null;
        viewPosition = new RectangleYio();
        positionDelta = new PointYio();
        selectionEngineYio = new SelectionEngineYio();
        initialize();
    }


    public void moveItem() {
        updateViewPosition();
        move();
    }


    protected abstract void initialize();


    protected abstract void move();


    private void updateViewPosition() {
        RectangleYio refPos = getReferencePosition();
        if (customizableListYio.horizontalMode) {
            viewPosition.x = refPos.x + positionDelta.x - customizableListYio.hook;
            viewPosition.y = refPos.y + positionDelta.y;
        } else {
            viewPosition.x = refPos.x + positionDelta.x;
            viewPosition.y = refPos.y + positionDelta.y + customizableListYio.hook;
        }
        viewPosition.width = (float) getWidth();
        viewPosition.height = (float) getHeight();
    }


    public void onAppear() {

    }


    protected RectangleYio getReferencePosition() {
        return customizableListYio.maskPosition;
    }


    public boolean isCurrentlyVisible() {
        if (customizableListYio.horizontalMode) {
            if (viewPosition.x + viewPosition.width < customizableListYio.maskPosition.x) return false;
            if (viewPosition.x > customizableListYio.maskPosition.x + customizableListYio.maskPosition.width) return false;
        } else {
            if (viewPosition.y + viewPosition.height < customizableListYio.maskPosition.y) return false;
            if (viewPosition.y > customizableListYio.maskPosition.y + customizableListYio.maskPosition.height) return false;
        }

        return true;
    }


    public boolean isTouched(PointYio touchPoint) {
        return InterfaceElement.isTouchInsideRectangle(touchPoint, viewPosition);
    }


    protected abstract double getWidth();


    protected double getDefaultWidth() {
        return customizableListYio.maskPosition.width;
    }


    protected abstract double getHeight();


    protected double getDefaultHeight() {
        return 0.98 * customizableListYio.maskPosition.height;
    }


    protected abstract void onPositionChanged();


    public void onTouchDown(PointYio touchPoint) {
        // nothing by default
    }


    protected abstract void onClicked();


    protected GameController getGameController() {
        return customizableListYio.menuControllerYio.yioGdxGame.gameController;
    }


    protected abstract void onLongTapped();


    public abstract AbstractRenderCustomListItem getRender();


    protected void onAddedToList() {

    }


    protected void moveRenderableTextByDefault(RenderableTextYio renderableTextYio) {
        renderableTextYio.position.x = viewPosition.x + renderableTextYio.delta.x;
        renderableTextYio.position.y = viewPosition.y + renderableTextYio.delta.y;
        renderableTextYio.updateBounds();
    }


    public void setCustomizableListYio(CustomizableListYio customizableListYio) {
        this.customizableListYio = customizableListYio;
    }


    public String getKey() {
        return "none";
    }
}
