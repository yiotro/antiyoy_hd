package yio.tro.onliyoy.menu.elements;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.*;

public class AnnounceViewElement extends InterfaceElement<AnnounceViewElement> {

    public RectangleYio adjustedPosition;
    public RenderableTextYio title;
    public VisualTextContainer visualTextContainer;
    float tDelta;
    public RectangleYio tempTextPosition;
    boolean onlyTitleMode;


    public AnnounceViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        adjustedPosition = new RectangleYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        visualTextContainer = new VisualTextContainer();
        tDelta = 0.06f * GraphicsYio.height;
        tempTextPosition = new RectangleYio();
        onlyTitleMode = false;
    }


    @Override
    protected AnnounceViewElement getThis() {
        return this;
    }


    public AnnounceViewElement setTitle(String key) {
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
        return this;
    }


    public AnnounceViewElement setText(String key) {
        visualTextContainer.setSize(0.9 * position.width, 0);
        String string = LanguagesManager.getInstance().getString(key);
        visualTextContainer.applyManyTextLines(Fonts.miniFont, string);
        visualTextContainer.updateHeightToMatchText(0.03f * GraphicsYio.height + tDelta);
        adjustedPosition.height = visualTextContainer.position.height;
        if (string.length() < 2) {
            onlyTitleMode = true;
        }
        return this;
    }


    @Override
    public void onMove() {
        moveAdjustedPosition();
        updateTempTextPosition();
        visualTextContainer.move(tempTextPosition);
        updateTitlePosition();
    }


    private void updateTempTextPosition() {
        tempTextPosition.setBy(adjustedPosition);
        tempTextPosition.x += 0.05 * viewPosition.width;
        tempTextPosition.y -= tDelta;
    }


    private void updateTitlePosition() {
        title.centerHorizontal(adjustedPosition);
        title.position.y = adjustedPosition.y + adjustedPosition.height - 0.02f * GraphicsYio.height;
        title.updateBounds();
    }


    private void moveAdjustedPosition() {
        if (onlyTitleMode) {
            adjustedPosition.setBy(viewPosition);
            return;
        }
        adjustedPosition.x = viewPosition.x;
        adjustedPosition.y = viewPosition.y + viewPosition.height / 2 - adjustedPosition.height / 2;
        adjustedPosition.width = viewPosition.width;
    }


    @Override
    protected RectangleYio getHookPositionForChildren() {
        return adjustedPosition;
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
        return MenuRenders.renderAnnounceViewElement;
    }
}
