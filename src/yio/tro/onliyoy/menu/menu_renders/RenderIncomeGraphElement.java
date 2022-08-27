package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.gameplay.income_graph.IgeItem;
import yio.tro.onliyoy.menu.elements.gameplay.income_graph.IncomeGraphElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderIncomeGraphElement extends RenderInterfaceElement{

    private IncomeGraphElement incomeGraphElement;
    private TextureRegion borderTexture;


    @Override
    public void loadTextures() {
        borderTexture = GraphicsYio.loadTextureRegion("menu/separator.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        incomeGraphElement = (IncomeGraphElement) element;
        if (incomeGraphElement.getFactor().getValue() < 0.1) return;

        GraphicsYio.setBatchAlpha(batch, alpha);
        renderShadow();
        renderBackground();
        renderTitle();
        renderSeparator();
        renderBorders();
        renderItems();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderShadow() {
        MenuRenders.renderShadow.renderShadow(
                incomeGraphElement.getViewPosition(),
                incomeGraphElement.cornerEngineYio.getShadowRadius()
        );
    }


    private void renderBorders() {
        for (IgeItem item : incomeGraphElement.items) {
            if (item.borderFactor.getValue() == 0) continue;
            if (item.text.string.equals("0")) continue;
            if (!item.scouted) continue;
            GraphicsYio.setBatchAlpha(batch, alpha * item.borderFactor.getValue());
            GraphicsYio.renderBorder(batch, borderTexture, item.borderPosition);
        }
        GraphicsYio.setBatchAlpha(batch, alpha);
    }


    private void renderItems() {
        for (IgeItem item : incomeGraphElement.items) {
            renderSingleItem(item);
        }
    }


    private void renderSingleItem(IgeItem item) {
        if (item.isColumnInsideViewPosition()) {
            GraphicsYio.drawByRectangle(
                    batch,
                    getTextureForItem(item),
                    item.viewPosition
            );
        }
        if (item.isTextVisible() && item.isTextInsideViewPosition()) {
            GraphicsYio.renderTextOptimized(batch, blackPixel, item.text, alpha);
        }
    }


    public TextureRegion getTextureForItem(IgeItem item) {
        HashMap<HColor, TextureRegion> mapBackgrounds = MenuRenders.renderUiColors.map;
        if (!item.scouted) return mapBackgrounds.get(HColor.gray);
        return mapBackgrounds.get(item.color);
    }


    private void renderSeparator() {
        if (!incomeGraphElement.isSeparatorInsideViewPosition()) return;
        GraphicsYio.drawByRectangle(batch, blackPixel, incomeGraphElement.separatorPosition);
    }


    private void renderTitle() {
        BitmapFont titleFont = incomeGraphElement.title.font;
        Color previousColor = titleFont.getColor();
        titleFont.setColor(Color.BLACK);
        GraphicsYio.renderTextOptimized(batch, blackPixel, incomeGraphElement.title, alpha);
        titleFont.setColor(previousColor);
    }


    private void renderBackground() {
        MenuRenders.renderRoundShape.renderRoundShape(
                incomeGraphElement.getViewPosition(),
                BackgroundYio.white,
                incomeGraphElement.cornerEngineYio.getCurrentRadius()
        );
    }
}
