package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderAnnounceViewElement extends RenderInterfaceElement {


    private AnnounceViewElement avElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        avElement = (AnnounceViewElement) element;

        if (avElement.getFactor().getValue() < 0.02) return;

        renderShadow();
        renderBackground();
        renderTitle();
        renderVisualTextContainer();
    }


    private void renderVisualTextContainer() {
        if (avElement.getFactor().getValue() < 0.2) return;
        for (RenderableTextYio renderableTextYio : avElement.visualTextContainer.viewList) {
            GraphicsYio.renderText(batch, renderableTextYio);
        }
    }


    private void renderTitle() {
        if (avElement.getViewPosition().width < avElement.title.width) return;
        GraphicsYio.renderText(batch, avElement.title);
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        MenuRenders.renderRoundShape.renderRoundShape(
                avElement.adjustedPosition,
                BackgroundYio.white
        );
    }


    private void renderShadow() {
        if (avElement.getShadowAlpha() == 0) return;
        GraphicsYio.setBatchAlpha(batch, avElement.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(avElement.adjustedPosition);
    }
}
