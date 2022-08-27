package yio.tro.onliyoy.menu.elements.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class ViewTouchModeElement extends InterfaceElement<ViewTouchModeElement> {

    public RenderableTextYio title;
    public boolean hasText;
    FactorYio textAlphaFactor;
    public RectangleYio backgroundPosition;


    public ViewTouchModeElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);

        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        hasText = false;
        textAlphaFactor = new FactorYio();
        backgroundPosition = new RectangleYio();
    }


    @Override
    protected ViewTouchModeElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateTextPosition();
        moveTextAlpha();
        updateBackgroundPosition();
    }


    private void updateBackgroundPosition() {
        backgroundPosition.setBy(title.bounds);
        backgroundPosition.increase(0.02f * GraphicsYio.width);
    }


    private void moveTextAlpha() {
        if (!textAlphaFactor.move()) return;

        hasText = (textAlphaFactor.getValue() > 0 || textAlphaFactor.isInAppearState());
    }


    private void updateTextPosition() {
        if (!hasText) return;

        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
        title.updateBounds();
    }


    public void onTouchModeSet(TouchMode touchMode) {
        if (touchMode == null) {
            textAlphaFactor.destroy(MovementType.lighty, 1.7);
            return;
        }

        String nameKey = touchMode.getNameKey();

        if (nameKey == null) {
            textAlphaFactor.destroy(MovementType.lighty, 1.7);
            return;
        }

        hasText = true;
        title.setString(LanguagesManager.getInstance().getString(nameKey));
        title.updateMetrics();
        updateTextPosition();
        updateBackgroundPosition();

        textAlphaFactor.appear(MovementType.approach, 1.66);
    }


    public double getTextAlpha() {
        return textAlphaFactor.getValue();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

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
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderViewTouchMode;
    }
}
