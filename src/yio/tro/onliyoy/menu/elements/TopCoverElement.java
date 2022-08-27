package yio.tro.onliyoy.menu.elements;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.scroll_engine.ScrollEngineYio;

public class TopCoverElement extends InterfaceElement<TopCoverElement> {

    ScrollEngineYio targetScrollEngine;
    public RenderableTextYio title;
    public FactorYio visibilityFactor;
    boolean touchedCurrently;
    public RectangleYio shadowPosition;


    public TopCoverElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        targetScrollEngine = null;
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        visibilityFactor = new FactorYio();
        shadowPosition = new RectangleYio();
    }


    @Override
    protected TopCoverElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateTitlePosition();
        updateShadowPosition();
        moveVisibilityFactor();
    }


    private void updateShadowPosition() {
        if (!factorMoved) return;

        shadowPosition.setBy(viewPosition);
        shadowPosition.x -= 0.1f * GraphicsYio.width;
        shadowPosition.width += 0.2f * GraphicsYio.width;
        shadowPosition.height = GraphicsYio.height / 2;
    }


    @Override
    public float getShadowAlpha() {
        float value = getFactor().getValue() * visibilityFactor.getValue();
        if (value < 0.9f) return 0;
        return 10f * (value - 0.9f);
    }


    @Override
    public float getAlpha() {
        float value = getFactor().getValue() * visibilityFactor.getValue();
        if (value >= 0.9f) return 1;
        return (1f / 0.9f) * value;
    }


    private void moveVisibilityFactor() {
        visibilityFactor.move();
        boolean targetVisibleState = getTargetVisibleState();
        if (targetVisibleState && !visibilityFactor.isInAppearState()) {
            visibilityFactor.appear(MovementType.approach, 5);
        }
        if (!targetVisibleState && !visibilityFactor.isInDestroyState()) {
            visibilityFactor.destroy(MovementType.lighty, 7);
        }
    }


    private boolean getTargetVisibleState() {
        if (targetScrollEngine == null) return false;
        return targetScrollEngine.getDistanceToTop() > 0.04 * GraphicsYio.height;
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        visibilityFactor.reset();
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = visibilityFactor.getValue() > 0.5 && isTouchedBy(currentTouch);
        return touchedCurrently;
    }


    @Override
    public boolean touchDrag() {
        return touchedCurrently;
    }


    @Override
    public boolean touchUp() {
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        return true;
    }


    public TopCoverElement setTargetScrollEngine(ScrollEngineYio targetScrollEngine) {
        this.targetScrollEngine = targetScrollEngine;
        return this;
    }


    public TopCoverElement setTitle(String key) {
        String string = LanguagesManager.getInstance().getString(key);
        title.setString(string);
        title.updateMetrics();
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderTopCoverElement;
    }
}
