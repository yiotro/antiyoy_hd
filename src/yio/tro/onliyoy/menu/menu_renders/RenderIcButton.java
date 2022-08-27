package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.ImportantConfirmationButton;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class RenderIcButton extends RenderInterfaceElement{


    private ImportantConfirmationButton icButton;
    private RectangleYio viewPosition;
    private SelectionEngineYio selectionEngineYio;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        icButton = (ImportantConfirmationButton) element;
        viewPosition = icButton.getViewPosition();

        renderBackground();
        renderBorder();
        renderTitle();
        renderSelection();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderTitle() {
        GraphicsYio.renderTextOptimized(batch, blackPixel, icButton.title, getTitleAlpha());
    }


    private float getTitleAlpha() {
        if (icButton.counter > 0) {
            return 0.4f * alpha;
        }
        return alpha;
    }


    private void renderSelection() {
        selectionEngineYio = icButton.selectionEngineYio;
        if (!selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * selectionEngineYio.getAlpha());
        MenuRenders.renderRoundShape.renderRoundShape(viewPosition, BackgroundYio.black, icButton.cornerRadius);
    }


    private void renderBorder() {
        if (icButton.isCounterAtZero()) return;
        batch.setColor(c.r, c.g, c.b, 0.25f * alpha);
        MenuRenders.renderRoundBorder.renderRoundBorder(viewPosition, icButton.cornerRadius);
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        MenuRenders.renderRoundShape.renderRoundShape(viewPosition, getBackgroundColor(), icButton.cornerRadius);
    }


    private BackgroundYio getBackgroundColor() {
        if (icButton.isCounterAtZero()) {
            return BackgroundYio.gray;
        }
        return BackgroundYio.white;
    }


}
