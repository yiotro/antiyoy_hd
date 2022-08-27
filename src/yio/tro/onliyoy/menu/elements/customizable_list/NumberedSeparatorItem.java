package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class NumberedSeparatorItem extends AbstractCustomListItem{

    public RectangleYio leftBounds;
    public RectangleYio rightBounds;
    public RenderableTextYio title;
    private float offset;


    @Override
    protected void initialize() {
        leftBounds = new RectangleYio();
        leftBounds.height = 2 * GraphicsYio.borderThickness;
        rightBounds = new RectangleYio();
        rightBounds.height = 2 * GraphicsYio.borderThickness;
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        offset = 0.03f * GraphicsYio.width;
    }


    public void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    @Override
    protected void move() {
        updateTitlePosition();
        updateLeftBounds();
        updateRightBounds();
    }


    private void updateRightBounds() {
        rightBounds.width = leftBounds.width;
        rightBounds.x = viewPosition.x + viewPosition.width - offset - rightBounds.width;
        rightBounds.y = viewPosition.y + viewPosition.height / 2 - rightBounds.height / 2;
    }


    private void updateLeftBounds() {
        leftBounds.width = (viewPosition.width - 4 * offset - title.width) / 2;
        leftBounds.x = viewPosition.x + offset;
        leftBounds.y = viewPosition.y + viewPosition.height / 2 - leftBounds.height / 2;
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        title.centerHorizontal(viewPosition);
        title.updateBounds();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.04 * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {

    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderNumberedSeparatorItem;
    }
}
