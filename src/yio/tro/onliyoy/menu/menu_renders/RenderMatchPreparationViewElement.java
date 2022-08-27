package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.net.MatchPreparationViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderMatchPreparationViewElement extends RenderInterfaceElement{


    private MatchPreparationViewElement mpvElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        mpvElement = (MatchPreparationViewElement) element;
        if (!mpvElement.title.bounds.isFullyInside(mpvElement.getViewPosition())) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, mpvElement.title, 0.5f * alpha);
    }
}
