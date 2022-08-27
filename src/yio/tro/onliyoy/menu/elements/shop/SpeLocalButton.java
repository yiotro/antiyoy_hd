package yio.tro.onliyoy.menu.elements.shop;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.stuff.*;

public class SpeLocalButton {

    SpExchange spExchange;
    public RectangleYio viewPosition;
    public RenderableTextYio title;
    public SelectionEngineYio selectionEngineYio;


    public SpeLocalButton(SpExchange spExchange) {
        this.spExchange = spExchange;
        viewPosition = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
    }


    void initTitle(String key, BitmapFont font) {
        title = new RenderableTextYio();
        title.setFont(font);
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
    }


    void move() {
        updateTitlePosition();
        moveSelection();
    }


    private void moveSelection() {
        if (spExchange.shopViewElement.touched) return;
        selectionEngineYio.move();
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
        title.updateBounds();
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return viewPosition.isPointInside(touchPoint, 0.04f * GraphicsYio.width);
    }


    private void updateViewPosition() {

    }


    void setSize(double w, double h) {
        viewPosition.width = (float) (w * GraphicsYio.width);
        viewPosition.height = (float) (h * GraphicsYio.height);
    }
}
