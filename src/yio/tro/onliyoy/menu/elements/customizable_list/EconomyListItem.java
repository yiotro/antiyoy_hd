package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class EconomyListItem extends AbstractCustomListItem{

    public RenderableTextYio name;
    public RenderableTextYio value;
    public boolean darken;


    @Override
    protected void initialize() {
        darken = false;
        name = new RenderableTextYio();
        name.setFont(Fonts.miniFont);
        value = new RenderableTextYio();
        value.setFont(Fonts.miniFont);
    }


    public void setValues(String nameKey, int deltaValue) {
        name.setString(LanguagesManager.getInstance().getString(nameKey));
        name.updateMetrics();
        String deltaString = Yio.getCompactValueString(deltaValue);
        if (deltaValue > 0) {
            deltaString = "+" + deltaString;
        }
        value.setString(deltaString);
        value.updateMetrics();
    }


    public void setDarken(boolean darken) {
        this.darken = darken;
    }


    @Override
    protected void move() {
        updateNamePosition();
        updateValuePosition();
    }


    private void updateValuePosition() {
        value.position.x = viewPosition.x + viewPosition.width - 0.03f * GraphicsYio.width - value.width;
        value.centerVertical(viewPosition);
        value.updateBounds();
    }


    private void updateNamePosition() {
        name.position.x = viewPosition.x + 0.03f * GraphicsYio.width;
        name.centerVertical(viewPosition);
        name.updateBounds();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.07f * GraphicsYio.height;
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
        return MenuRenders.renderEconomyListItem;
    }
}
