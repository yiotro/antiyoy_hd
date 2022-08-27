package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;

public class RenderCustomizableList extends RenderInterfaceElement {


    CustomizableListYio customizableListYio;
    private TextureRegion redPixel;


    @Override
    public void loadTextures() {
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        customizableListYio = (CustomizableListYio) element;

        if (customizableListYio.getFactor().getValue() < 0.01) return;

        renderShadow();
        renderDarken();
        GraphicsYio.setBatchAlpha(batch, alpha);
        renderBackground();
        renderBorders();

        renderItems();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderDarken() {
        if (!customizableListYio.darken) return;
        if (customizableListYio.getFactor().getValue() == 1) return;
        if (customizableListYio.getFactor().getValue() == 0) return;
        GraphicsYio.setBatchAlpha(batch, 0.15 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, customizableListYio.screenPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBorders() {
        if (!customizableListYio.areShowBordersEnabled()) return;
        GraphicsYio.renderBorder(batch, redPixel, customizableListYio.getViewPosition());
        GraphicsYio.renderBorder(batch, redPixel, customizableListYio.maskPosition);
    }


    private void renderBackground() {
        if (!customizableListYio.isBackgroundEnabled()) return;
        MenuRenders.renderRoundShape.renderRoundShape(
                customizableListYio.getViewPosition(),
                customizableListYio.backgroundColor,
                customizableListYio.cornerEngineYio.getCurrentRadius()
        );
    }


    private void renderShadow() {
        if (!customizableListYio.isShadowEnabled()) return;
        if (customizableListYio.getShadowAlpha() == 0) return;
        GraphicsYio.setBatchAlpha(batch, customizableListYio.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(
                customizableListYio.getViewPosition(),
                customizableListYio.cornerEngineYio.getShadowRadius()
        );
    }


    private void renderItems() {
        if (customizableListYio.getFactor().getValue() < 0.33) return;
        batch.end();
        Masking.begin();

        prepareShapeRenderer();
        drawRoundRectShape(customizableListYio.maskPosition, 0.92f * customizableListYio.cornerEngineYio.getCurrentRadius());
        shapeRenderer.end();

        batch.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();
        renderLtTarget();
        Masking.end(batch);
    }


    void renderInternals() {
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!item.isCurrentlyVisible()) continue;
            AbstractRenderCustomListItem renderSystem = item.getRender();
            renderSystem.setAlpha(alpha);
            renderSystem.renderItem(item);
        }
    }


    private void renderLtTarget() {
        if (!customizableListYio.ltActive) return;
        GraphicsYio.renderBorder(batch, redPixel, customizableListYio.ltTarget.viewPosition, 2 * GraphicsYio.borderThickness);
    }


}
