package yio.tro.onliyoy.menu.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class LabelElement extends InterfaceElement<LabelElement> {

    public RenderableTextYio title;
    private boolean leftAlignEnabled;
    private boolean rightAlignEnabled;
    public RectangleYio incBounds;
    public boolean backgroundEnabled;
    public TextureRegion cacheTitleTexture;
    boolean readyToUpdateCache;
    public float targetAlpha;


    public LabelElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        title = new RenderableTextYio();
        title.setFont(Fonts.buttonFont);
        leftAlignEnabled = false;
        incBounds = new RectangleYio();
        backgroundEnabled = false;
        rightAlignEnabled = false;
        cacheTitleTexture = null;
        targetAlpha = 1;
    }


    public LabelElement setString(String string) {
        return setTitle(string);
    }


    public LabelElement setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
        readyToUpdateCache = true;
        return this;
    }


    public LabelElement setFont(BitmapFont font) {
        if (title.font == font) return this;
        title.setFont(font);
        title.updateMetrics();
        return this;
    }


    @Override
    protected LabelElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveTitle();
        updateIncBounds();
        checkToUpdateCache();
    }


    private void checkToUpdateCache() {
        if (!readyToUpdateCache) return;
        updateCacheTitleTexture();
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(0.015f * GraphicsYio.dim);
    }


    private void moveTitle() {
        title.centerHorizontal(viewPosition);
        if (leftAlignEnabled) {
            title.position.x = viewPosition.x;
        }
        if (rightAlignEnabled) {
            title.position.x = viewPosition.x + viewPosition.width - title.width;
        }
        title.centerVertical(viewPosition);
        title.updateBounds();
    }


    @Override
    public LabelElement clone(InterfaceElement src) {
        super.clone(src);

        LabelElement srcLabel = (LabelElement) src;
        setTitle(srcLabel.title.string);
        setFont(srcLabel.title.font);
        setLeftAlignEnabled(srcLabel.leftAlignEnabled);

        return this;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        updateCacheTitleTexture();
    }


    private void updateCacheTitleTexture() {
        cacheTitleTexture = menuControllerYio.cacheTexturesManager.perform(this);
        readyToUpdateCache = false;
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public void onAppResume() {
        super.onAppResume();
        readyToUpdateCache = true;
    }


    public LabelElement setLeftAlignEnabled(boolean leftAlignEnabled) {
        this.leftAlignEnabled = leftAlignEnabled;
        return this;
    }


    public LabelElement setRightAlignEnabled(boolean rightAlignEnabled) {
        this.rightAlignEnabled = rightAlignEnabled;
        return this;
    }


    public LabelElement setBackgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }


    public LabelElement setTargetAlpha(float targetAlpha) {
        this.targetAlpha = targetAlpha;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderLabelElement;
    }
}
