package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.gameplay.ViewTouchModeElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderViewTouchMode extends RenderInterfaceElement{


    private ViewTouchModeElement viewTouchModeElement;
    private BitmapFont font;
    private TextureRegion whitePixel;
    private RenderableTextYio title;
    RenderDiplomaticItems renderDiplomaticItems;
    RenderEditRelationsItems renderEditRelationsItems;


    public RenderViewTouchMode() {
        renderDiplomaticItems = new RenderDiplomaticItems();
        renderEditRelationsItems = new RenderEditRelationsItems();
    }


    @Override
    public void loadTextures() {
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        viewTouchModeElement = (ViewTouchModeElement) element;
        title = viewTouchModeElement.title;
        font = title.font;

        if (!viewTouchModeElement.hasText) return;
        if (viewTouchModeElement.getFactor().getValue() < 0.2) return;

        GraphicsYio.setBatchAlpha(batch, 0.25);
        GraphicsYio.drawByRectangle(batch, whitePixel, viewTouchModeElement.backgroundPosition);
        GraphicsYio.setBatchAlpha(batch, 1);

        GraphicsYio.setFontAlpha(font, viewTouchModeElement.getTextAlpha() * alpha);
        GraphicsYio.renderText(batch, title);
        GraphicsYio.setFontAlpha(font, 1);

        renderDiplomaticItems.render(batch, menuViewYio.yioGdxGame.gameController);
        renderEditRelationsItems.render(batch, menuViewYio.yioGdxGame.gameController);
    }

}
