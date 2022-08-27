package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.ScrollHelperElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderScrollHelperElement extends RenderInterfaceElement{


    private ScrollHelperElement shElement;
    private float alpha;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        shElement = (ScrollHelperElement) element;
        alpha = shElement.getAlpha();

        renderRoad();
        renderWagon();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderRoad() {
        if (!shElement.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, 0.5 * shElement.selectionEngineYio.getAlpha() * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, shElement.roadPosition);
    }


    private void renderWagon() {
        if (shElement.fadeInFactor.getValue() == 0) return;
        GraphicsYio.setBatchAlpha(batch, (0.25 + 3 * shElement.selectionEngineYio.getAlpha()) * alpha * shElement.fadeInFactor.getValue());
        GraphicsYio.drawByRectangle(batch, blackPixel, shElement.wagonPosition);
    }

}
