package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class PlaceholderListItem extends AbstractCustomListItem{

    public RenderableTextYio title;


    public PlaceholderListItem(String key) {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
    }


    @Override
    protected void initialize() {

    }


    @Override
    protected void move() {
        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
        title.updateBounds();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return getDefaultHeight();
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
        return MenuRenders.renderPlaceholderListItem;
    }
}
