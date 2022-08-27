package yio.tro.onliyoy.menu.elements.resizable_element;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class RveButton {

    ResizableViewElement resizableViewElement;
    public RectangleYio position;
    VerticalAlignType verticalAlignType;
    HorizontalAlignType horizontalAlignType;
    float horOffset;
    float verOffset;
    public boolean backgroundEnabled;
    public RenderableTextYio title;
    public SelectionEngineYio selectionEngineYio;
    Reaction reaction;
    public TextureRegion iconTexture;
    public CircleYio iconPosition;
    public FactorYio appearFactor;
    String key;


    public RveButton(ResizableViewElement resizableViewElement) {
        this.resizableViewElement = resizableViewElement;
        verticalAlignType = VerticalAlignType.center;
        horizontalAlignType = HorizontalAlignType.center;
        position = new RectangleYio();
        horOffset = 0;
        verOffset = 0;
        backgroundEnabled = true;
        selectionEngineYio = new SelectionEngineYio();
        reaction = null;
        iconTexture = null;
        iconPosition = new CircleYio();
        appearFactor = new FactorYio();
        appearFactor.setValue(1);
        appearFactor.stop();
        key = "";
        initTitle();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        title.setString("-");
        title.updateMetrics();
    }


    void move() {
        if (appearFactor.getValue() == 0 && !appearFactor.isInAppearState()) return;
        appearFactor.move();
        updatePosition();
        moveTitle();
        moveSelection();
        moveIcon();
    }


    private void moveIcon() {
        if (iconTexture == null) return;
        iconPosition.center.x = position.x + position.width / 2;
        iconPosition.center.y = position.y + position.height / 2;
        iconPosition.radius = 0.5f * position.height;
    }


    private void moveSelection() {
        if (resizableViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void moveTitle() {
        if (iconTexture != null) return;
        title.centerHorizontal(position);
        title.centerVertical(position);
        title.updateBounds();
    }


    public void deactivate() {
        appearFactor.destroy(MovementType.lighty, 4.5);
    }


    public void activate() {
        appearFactor.appear(MovementType.approach, 5);
    }


    private void updatePosition() {
        applyHorizontalAlign();
        applyVerticalAlign();
    }


    private void applyVerticalAlign() {
        RectangleYio parentPos = resizableViewElement.dynamicPosition;
        switch (verticalAlignType) {
            default:
                System.out.println("RveButton.applyVerticalAlign: not defined");
                break;
            case center:
                position.y = parentPos.y + parentPos.height / 2 - position.height / 2;
                break;
            case top:
                position.y = parentPos.y + parentPos.height - verOffset - position.height;
                break;
            case bottom:
                position.y = parentPos.y + verOffset;
                break;
        }
    }


    private void applyHorizontalAlign() {
        RectangleYio parentPos = resizableViewElement.dynamicPosition;
        switch (horizontalAlignType) {
            default:
                break;
            case left:
                position.x = parentPos.x + horOffset;
                break;
            case right:
                position.x = parentPos.x + parentPos.width - horOffset - position.width;
                break;
            case center:
                position.x = parentPos.x + parentPos.width / 2 - position.width / 2;
                break;
        }
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        if (appearFactor.isInDestroyState()) return false;
        return position.isPointInside(touchPoint, 0.015f * GraphicsYio.width);
    }


    public RveButton setKey(String key) {
        this.key = key;
        return this;
    }


    public RveButton setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }


    public RveButton setIcon(TextureRegion textureRegion) {
        iconTexture = textureRegion;
        backgroundEnabled = false;
        return this;
    }


    public RveButton setBackgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }


    public RveButton setTitle(String key) {
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
        return this;
    }


    public RveButton alignLeft(double offset) {
        horizontalAlignType = HorizontalAlignType.left;
        horOffset = (float) (offset * GraphicsYio.width);
        return this;
    }


    public RveButton alignRight(double offset) {
        horizontalAlignType = HorizontalAlignType.right;
        horOffset = (float) (offset * GraphicsYio.width);
        return this;
    }


    public RveButton alignBottom(double offset) {
        verticalAlignType = VerticalAlignType.bottom;
        verOffset = (float) (offset * GraphicsYio.height);
        return this;
    }


    public RveButton alignTop(double offset) {
        verticalAlignType = VerticalAlignType.top;
        verOffset = (float) (offset * GraphicsYio.height);
        return this;
    }


    public RveButton setSize(double w) {
        position.width = (float) (w * GraphicsYio.width);
        position.height = (float) (w * GraphicsYio.width);
        return this;
    }


    public RveButton setSize(double w, double h) {
        position.width = (float) (w * GraphicsYio.width);
        position.height = (float) (h * GraphicsYio.height);
        return this;
    }
}
