package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.setup_entities.CondensedEntitiesViewElement;
import yio.tro.onliyoy.menu.elements.setup_entities.CevItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderCondensedEntitiesViewElement extends RenderInterfaceElement{

    private CondensedEntitiesViewElement evElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        evElement = (CondensedEntitiesViewElement) element;

        renderTitle();
        renderItems();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderItems() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        for (CevItem item : evElement.items) {
            renderBackground(item);
            GraphicsYio.renderText(batch, item.title);
            renderSelection(item);
        }
    }


    private void renderBackground(CevItem item) {
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderUiColors.map.get(item.color),
                item.incBounds
        );
    }


    private void renderSelection(CevItem item) {
        if (!item.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * item.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, item.touchPosition);
        GraphicsYio.setBatchAlpha(batch, alpha);
    }


    private void renderDebugBorder() {
        GraphicsYio.setBatchAlpha(batch, 0.2 * alpha);
        GraphicsYio.renderBorder(batch, blackPixel, evElement.getViewPosition());
    }


    private void renderTitle() {
        if (evElement.getViewPosition().width < evElement.title.width) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, evElement.title, alpha);
    }
}
