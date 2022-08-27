package yio.tro.onliyoy.menu.elements;

import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.scroll_engine.ScrollEngineYio;

public class ScrollHelperElement extends InterfaceElement<ScrollHelperElement> {

    public ScrollEngineYio scrollEngineYio;
    public RectangleYio roadPosition;
    public RectangleYio wagonPosition;
    boolean inverted;
    boolean currentlyTouched;
    float touchOffset;
    public SelectionEngineYio selectionEngineYio;
    public RectangleYio touchArea;
    boolean applianceMode;
    double targetValue;
    public FactorYio fadeInFactor;
    boolean autoHideEnabled;


    public ScrollHelperElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        roadPosition = new RectangleYio();
        wagonPosition = new RectangleYio();
        inverted = false;
        touchOffset = 0.07f * GraphicsYio.width;
        selectionEngineYio = new SelectionEngineYio();
        touchArea = new RectangleYio();
        fadeInFactor = new FactorYio();
        autoHideEnabled = true;
    }


    @Override
    protected ScrollHelperElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateRoadPosition();
        updateWagonPosition();
        moveApplianceMode();
        updateTouchArea();
        moveSelection();
        moveFadeInFactor();
    }


    private void moveFadeInFactor() {
        fadeInFactor.move();

        if (!fadeInFactor.isInAppearState() && Math.abs(scrollEngineYio.getSpeed()) > 0.01) {
            doFadeIn();
        }

        if (isReadyToFadeOut()) {
            fadeInFactor.destroy(MovementType.lighty, 1.1);
        }
    }


    private void doFadeIn() {
        fadeInFactor.appear(MovementType.approach, 5);
    }


    private boolean isReadyToFadeOut() {
        if (fadeInFactor.isInDestroyState()) return false;
        if (Math.abs(scrollEngineYio.getSpeed()) >= 0.01) return false;
        if (applianceMode) return false;
        if (currentlyTouched) return false;
        if (!autoHideEnabled) return false;
        return true;
    }


    private void moveApplianceMode() {
        if (!applianceMode) return;

        if (scrollEngineYio.applyNormalizedPosition(targetValue)) {
            updateWagonPosition();
        } else {
            applianceMode = false;
        }
    }


    private void updateTouchArea() {
        touchArea.setBy(wagonPosition);
        touchArea.increase(touchOffset);
    }


    private void moveSelection() {
        if (currentlyTouched) return;
        selectionEngineYio.move();
    }


    private void updateWagonPosition() {
        float aPos = (float) (scrollEngineYio.getSlider().a / scrollEngineYio.getLimits().getLength());
        float bPos = (float) (scrollEngineYio.getSlider().b / scrollEngineYio.getLimits().getLength());

        if (inverted) {
            float temp = 1 - aPos;
            aPos = 1 - bPos;
            bPos = temp;
        }

        wagonPosition.set(
                roadPosition.x,
                roadPosition.y + aPos * roadPosition.height,
                roadPosition.width,
                (bPos - aPos) * roadPosition.height
        );
    }


    private void updateRoadPosition() {
        roadPosition.setBy(viewPosition);
        roadPosition.width = 8 * GraphicsYio.borderThickness;
        roadPosition.x = GraphicsYio.width - roadPosition.width;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        currentlyTouched = false;
        if (!autoHideEnabled) {
            doFadeIn();
        }
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        currentlyTouched = touchArea.isPointInside(currentTouch) && fadeInFactor.getValue() > 0;
        if (currentlyTouched) {
            selectionEngineYio.applySelection();
            if (!fadeInFactor.isInAppearState()) {
                doFadeIn();
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDrag() {
        if (!currentlyTouched) return false;
        applyCurrentTouchToScrollEngine();
        return true;
    }


    private void applyCurrentTouchToScrollEngine() {
        targetValue = (currentTouch.y - roadPosition.y) / roadPosition.height;
        if (inverted) {
            targetValue = 1 - targetValue;
        }
        applianceMode = true;
        scrollEngineYio.setSpeed(0);
    }


    @Override
    public boolean touchUp() {
        if (!currentlyTouched) return false;
        currentlyTouched = false;
        applianceMode = false;

        return false;
    }


    public ScrollHelperElement setScrollEngineYio(ScrollEngineYio scrollEngineYio) {
        this.scrollEngineYio = scrollEngineYio;
        return this;
    }


    public ScrollHelperElement setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }


    public ScrollHelperElement setAutoHideEnabled(boolean autoHideEnabled) {
        this.autoHideEnabled = autoHideEnabled;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderScrollHelperElement;
    }
}
