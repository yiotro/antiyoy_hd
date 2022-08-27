package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.net.ChooseColorElement;
import yio.tro.onliyoy.menu.elements.net.CcItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;

public class RenderChooseColorElement extends RenderInterfaceElement{


    private ChooseColorElement cmcElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        cmcElement = (ChooseColorElement) element;
        renderDarken();
        GraphicsYio.setBatchAlpha(batch, 1);
        renderShadow();
        renderBackground();
        renderItems();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderDarken() {
        GraphicsYio.setBatchAlpha(batch, 0.33f * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, cmcElement.darkenPosition);
    }


    private void renderBackground() {
        MenuRenders.renderRoundShape.renderRoundShape(
                cmcElement.getViewPosition(),
                BackgroundYio.white
        );
    }


    private void renderShadow() {
        MenuRenders.renderShadow.renderShadow(cmcElement.getViewPosition());
    }


    private void renderWithMasking() {
        batch.end();
        Masking.begin();
        prepareShapeRenderer();
        drawRoundRectShape(cmcElement.getViewPosition(), cmcElement.cornerEngineYio.getCurrentRadius());
        shapeRenderer.end();
        batch.begin();
        Masking.continueAfterBatchBegin();
        renderItems();
        Masking.end(batch);
    }


    private void renderItems() {
        for (CcItem item : cmcElement.items) {
            GraphicsYio.drawByRectangle(
                    batch,
                    MenuRenders.renderUiColors.map.get(item.color),
                    item.position
            );
            renderSelection(item);
        }
    }


    private void renderSelection(CcItem item) {
        if (!item.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * item.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, item.selectionPosition);
        GraphicsYio.setBatchAlpha(batch, alpha);
    }
}
