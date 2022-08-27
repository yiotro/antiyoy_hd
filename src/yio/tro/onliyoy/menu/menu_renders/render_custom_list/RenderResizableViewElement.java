package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.menu.elements.resizable_element.ResizableViewElement;
import yio.tro.onliyoy.menu.elements.resizable_element.RveButton;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderResizableViewElement extends RenderInterfaceElement {


    private ResizableViewElement rvElement;
    private float bCornerRadius;


    public RenderResizableViewElement() {
        bCornerRadius = 0.015f * GraphicsYio.height;
    }


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        rvElement = (ResizableViewElement) element;
        if (rvElement.getFactor().getValue() < 0.01) return;

        renderShadow();
        GraphicsYio.setBatchAlpha(batch, alpha);
        renderBackground();
        renderItems();
        renderButtons();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderButtons() {
        if (rvElement.getFactor().getValue() < 0.5) return;
        for (RveButton button : rvElement.buttons) {
            float currentAlpha = alpha * button.appearFactor.getValue();
            if (currentAlpha == 0) continue;
            if (!rvElement.dynamicPosition.contains(button.position)) continue;
            GraphicsYio.setBatchAlpha(batch, currentAlpha);
            if (button.backgroundEnabled) {
                MenuRenders.renderRoundShape.renderRoundShape(button.position, BackgroundYio.gray, bCornerRadius);
            }
            if (button.iconTexture == null) {
                GraphicsYio.renderTextOptimized(batch, blackPixel, button.title, currentAlpha);
            } else {
                GraphicsYio.drawByCircle(batch, button.iconTexture, button.iconPosition);
            }
            if (button.selectionEngineYio.isSelected()) {
                GraphicsYio.setBatchAlpha(batch, currentAlpha * button.selectionEngineYio.getAlpha());
                MenuRenders.renderRoundShape.renderRoundShape(button.position, BackgroundYio.black, bCornerRadius);
                GraphicsYio.setBatchAlpha(batch, currentAlpha);
            }
        }
    }


    private void renderItems() {
        for (AbstractRveItem rveItem : rvElement.items) {
            if (rveItem.appearFactor.getValue() == 0) continue;
            if (!rveItem.isInsideDynamicPosition()) continue;
            rveItem.getRender().renderItem(rveItem, rvElement.getAlpha() * rveItem.appearFactor.getValue());
        }
    }


    private void renderBackground() {
        MenuRenders.renderRoundShape.renderRoundShape(
                rvElement.dynamicPosition,
                BackgroundYio.white,
                rvElement.cornerEngineYio.getCurrentRadius()
        );
    }


    private void renderShadow() {
        if (rvElement.getShadowAlpha() == 0) return;
        GraphicsYio.setBatchAlpha(batch, rvElement.getShadowAlpha() * alpha);
        MenuRenders.renderShadow.renderShadow(
                rvElement.dynamicPosition,
                rvElement.cornerEngineYio.getShadowRadius()
        );
    }
}
