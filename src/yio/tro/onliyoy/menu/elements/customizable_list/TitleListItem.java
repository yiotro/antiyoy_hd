package yio.tro.onliyoy.menu.elements.customizable_list;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class TitleListItem extends AbstractSingleLineItem{


    private float height;
    public float alpha;


    public TitleListItem() {
        height = 0.06f * GraphicsYio.height;
        alpha = 1;
    }


    @Override
    protected BitmapFont getTitleFont() {
        return Fonts.miniFont;
    }


    @Override
    protected double getHeight() {
        return height;
    }


    @Override
    protected void onClicked() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderTitleListItem;
    }


    public void setHeight(float height) {
        this.height = height;
    }


    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }


    @Override
    protected void moveRenderableTextByDefault(RenderableTextYio renderableTextYio) {
        renderableTextYio.position.x = viewPosition.x + viewPosition.width / 2 - title.width / 2;
        renderableTextYio.position.y = viewPosition.y + renderableTextYio.delta.y;
        renderableTextYio.updateBounds();
    }

}
