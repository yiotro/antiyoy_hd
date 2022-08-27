package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.net.MatchParametersViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderMatchParametersViewElement extends RenderInterfaceElement{


    private MatchParametersViewElement mpvElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        mpvElement = (MatchParametersViewElement) element;
        renderViewText(mpvElement.title);
        renderDesc1();
        renderDesc2();
    }


    private void renderDesc1() {
        RectangleYio pos = mpvElement.getViewPosition();
        RectangleYio bounds = mpvElement.desc1.bounds;
        if (bounds.x < pos.x) return;
        if (bounds.x + bounds.width > pos.x + pos.width) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, mpvElement.desc1, alpha);
    }


    private void renderDesc2() {
        RectangleYio pos = mpvElement.getViewPosition();
        RectangleYio bounds = mpvElement.desc2.bounds;
        if (bounds.x < pos.x) return;
        if (bounds.x + bounds.width > pos.x + pos.width) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, mpvElement.desc2, alpha);
    }


    private void renderViewText(RenderableTextYio renderableTextYio) {
        if (!renderableTextYio.bounds.isFullyInside(mpvElement.getViewPosition())) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, renderableTextYio, alpha);
    }
}
