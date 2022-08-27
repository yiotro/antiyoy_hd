package yio.tro.onliyoy.menu.elements.resizable_element;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RveTextItem extends AbstractRveItem{

    float height;
    public RenderableTextYio title;
    boolean centered;


    @Override
    protected void initialize() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        height = 0.05f * GraphicsYio.height;
        centered = false;
    }


    @Override
    protected void onMove() {
        moveTitle();
    }


    private void moveTitle() {
        title.centerVertical(position);
        if (centered) {
            title.centerHorizontal(position);
        } else {
            title.position.x = position.x + 0.03f * GraphicsYio.width;
        }
        title.updateBounds();
    }


    @Override
    public boolean isInsideDynamicPosition() {
        if (title.position.x + title.width > position.x + position.width) return false;
        return super.isInsideDynamicPosition();
    }


    @Override
    protected float getHeight() {
        return height;
    }


    public void setHeight(double h) {
        this.height = (float) (h * GraphicsYio.height);
    }


    public void setTitle(String key) {
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
    }


    public void setFont(BitmapFont font) {
        title.setFont(font);
        title.updateMetrics();
    }


    public void setCentered(boolean centered) {
        this.centered = centered;
    }


    @Override
    protected RenderableTextYio getRenderableTextYio() {
        return title;
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveTextItem;
    }

}
