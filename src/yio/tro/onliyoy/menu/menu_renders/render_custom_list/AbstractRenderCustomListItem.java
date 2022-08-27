package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public abstract class AbstractRenderCustomListItem extends RenderInterfaceElement{


    public abstract void loadTextures();


    public abstract void renderItem(AbstractCustomListItem item);


    protected void renderDefaultSelection(AbstractCustomListItem item) {
        if (!item.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, item.selectionEngineYio.getAlpha() * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, item.viewPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    @Override
    public void render(InterfaceElement element) {

    }


    protected void renderTextOptimized(RenderableTextYio renderableTextYio, float alpha) {
        GraphicsYio.renderTextOptimized(batch, blackPixel, renderableTextYio, alpha);
    }


    protected void renderTextOptimized(RenderableTextYio renderableTextYio, float alpha, float transitionValue) {
        if (transitionValue == 1) {
            GraphicsYio.renderTextOptimized(batch, blackPixel, renderableTextYio, alpha);
            return;
        }
        GraphicsYio.setBatchAlpha(batch, 0.15 * alpha * (1 - transitionValue));
        GraphicsYio.drawByRectangle(batch, blackPixel, renderableTextYio.bounds);
        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.setFontAlpha(renderableTextYio.font, alpha * transitionValue);
        GraphicsYio.renderText(batch, renderableTextYio);
        GraphicsYio.setFontAlpha(renderableTextYio.font, 1);
    }
}
