package yio.tro.onliyoy.menu.elements.resizable_element;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public abstract class AbstractRveItem {

    ResizableViewElement resizableViewElement;
    public RectangleYio position;
    PointYio delta;
    public FactorYio appearFactor;
    public float blankOffset;
    String key;
    boolean invertedDeltaMode;
    public RveColorBounds colorBounds;


    public AbstractRveItem() {
        resizableViewElement = null;
        position = new RectangleYio();
        delta = new PointYio();
        appearFactor = new FactorYio();
        blankOffset = 0.015f * GraphicsYio.height;
        key = "";
        invertedDeltaMode = false;
        colorBounds = new RveColorBounds();
        initialize();
    }


    public void move() {
        updatePosition();
        moveFactor();
        moveColorBounds();
        onMove();
    }


    protected void moveColorBounds() {
        if (!colorBounds.enabled) return;
        float inc = getColorBoundsIncrease();
        RenderableTextYio rt = getRenderableTextYio();
        colorBounds.position.x = rt.bounds.x + colorBounds.offset - inc;
        colorBounds.position.y = rt.bounds.y - 2 * inc;
        colorBounds.position.height = rt.bounds.height + 4 * inc;
    }


    protected void moveFactor() {
        if (!appearFactor.isInAppearState()) {
            appearFactor.move();
            return;
        }
        if (!isInsideDynamicPosition()) return;
        appearFactor.move();
    }


    protected void updatePosition() {
        RectangleYio dynPosition = resizableViewElement.dynamicPosition;
        position.width = dynPosition.width;
        position.height = getHeight();
        position.x = dynPosition.x;
        if (!invertedDeltaMode) {
            position.y = dynPosition.y + dynPosition.height - delta.y - position.height;
        } else {
            position.y = dynPosition.y + delta.y;
        }
    }


    protected abstract void initialize();


    protected abstract void onMove();


    protected abstract float getHeight();


    public abstract AbstractRveRender getRender();


    public void setResizableViewElement(ResizableViewElement resizableViewElement) {
        this.resizableViewElement = resizableViewElement;
    }


    public boolean isInsideDynamicPosition() {
        RectangleYio dynPos = resizableViewElement.dynamicPosition;
        return position.y > dynPos.y - blankOffset;
    }


    public boolean isActive() {
        return appearFactor.isInAppearState();
    }


    public void setActive(boolean active) {
        if (!isActive() && active) {
            activate();
        }
        if (isActive() && !active) {
            deactivate();
        }
    }


    boolean onTouchDown(PointYio touchPoint) {
        return false;
    }


    boolean onClick(PointYio touchPoint) {
        return false;
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return position.isPointInside(touchPoint);
    }


    public void activate() {
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 1.9);
        resizableViewElement.onContentChanged();
    }


    public void deactivate() {
        appearFactor.destroy(MovementType.lighty, 5);
        resizableViewElement.onContentChanged();
    }


    public void setKey(String key) {
        this.key = key;
    }


    public void setBlankOffset(float blankOffset) {
        this.blankOffset = blankOffset;
    }


    public void setInvertedDeltaMode(boolean invertedDeltaMode) {
        this.invertedDeltaMode = invertedDeltaMode;
    }


    public void prepareDeltaForInvertedMode() {
        if (!invertedDeltaMode) return;
        delta.y = resizableViewElement.targetHeight - delta.y - position.height;
    }


    protected float getColorBoundsIncrease() {
        return 0.005f * GraphicsYio.width;
    }


    protected RenderableTextYio getRenderableTextYio() {
        // nothing by default
        // but has to be overrode if color will be enabled
        return null;
    }


    public void enableColor(BitmapFont font, String prefix, String coloredString, HColor color) {
        if (getRenderableTextYio() == null) {
            System.out.println("AbstractRveItem.enableColor: problem");
            return;
        }
        colorBounds.enabled = true;
        colorBounds.color = color;
        colorBounds.offset = GraphicsYio.getTextWidth(font, prefix);
        colorBounds.position.width = GraphicsYio.getTextWidth(font, coloredString) + 4 * getColorBoundsIncrease();
    }
}
