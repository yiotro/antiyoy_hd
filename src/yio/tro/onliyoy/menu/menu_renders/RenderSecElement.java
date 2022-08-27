package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.setup_entities.SecButton;
import yio.tro.onliyoy.menu.elements.setup_entities.SingleEntityConfigureElement;
import yio.tro.onliyoy.stuff.*;

public class RenderSecElement extends RenderInterfaceElement{


    private SingleEntityConfigureElement secElement;
    private TextureRegion highlightTexture;
    private TextureRegion removeTexture;
    RectangleYio tempRectangle;
    private TextureRegion darkPixel;


    public RenderSecElement() {
        super();
        tempRectangle = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        highlightTexture = GraphicsYio.loadTextureRegion("menu/setup_entities/sec_highlight.png", true);
        removeTexture = GraphicsYio.loadTextureRegion("menu/setup_entities/sec_remove.png", true);
        darkPixel = GraphicsYio.loadTextureRegion("pixels/dark.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        secElement = (SingleEntityConfigureElement) element;

        renderShadow();

        batch.end();
        Masking.begin();

        prepareShapeRenderer();
        drawRoundRectShape(secElement.getViewPosition(), secElement.cornerRadius);
        shapeRenderer.end();

        batch.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();

        Masking.end(batch);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderInternals() {
        renderBackground();
        renderHighlights();
        renderButtons();
    }


    private void renderHighlights() {
        GraphicsYio.setBatchAlpha(batch, secElement.highlightFactor.getValue());
        GraphicsYio.drawByCircle(batch, highlightTexture, secElement.ptHighlight);
        if (secElement.clChosenButton != null) {
            GraphicsYio.drawByCircle(batch, highlightTexture, secElement.clHighlight);
        }
    }


    private void renderButtons() {
        GraphicsYio.setBatchAlpha(batch, 1);
        for (SecButton button : secElement.buttons) {
            if (button.getIconAlpha() == 0) continue;
            renderSingleButton(button);
        }
    }


    private void renderSingleButton(SecButton button) {
        renderIcon(button);
        renderSelection(button);
    }


    private void renderSelection(SecButton button) {
        if (!button.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, button.selectionEngineYio.getAlpha());
        MenuRenders.renderRoundShape.renderRoundShape(button.selectionPosition, BackgroundYio.black);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderIcon(SecButton button) {
        GraphicsYio.setBatchAlpha(batch, button.getIconAlpha());
        switch (button.secType) {
            default:
                System.out.println("RenderSecElement.renderIcon");
                break;
            case player_type:
                GraphicsYio.drawByCircle(
                        batch,
                        MenuRenders.renderEntitiesSetupElement.mapIcons.get(button.eseType),
                        button.iconPosition
                );
                break;
            case remove:
                GraphicsYio.drawByCircle(
                        batch,
                        removeTexture,
                        button.iconPosition
                );
                break;
            case color:
                if (button.borderFactor.getValue() > 0) {
                    GraphicsYio.setBatchAlpha(batch, button.borderFactor.getValue());
                    tempRectangle.setBy(button.iconPosition);
                    tempRectangle.increase(GraphicsYio.borderThickness);
                    GraphicsYio.drawByRectangle(batch, darkPixel, tempRectangle);
                    GraphicsYio.setBatchAlpha(batch, 1);
                }
                GraphicsYio.drawByCircle(
                        batch,
                        MenuRenders.renderUiColors.map.get(button.color),
                        button.iconPosition
                );
                break;
        }
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderUiColors.map.get(secElement.currentColor),
                secElement.getViewPosition()
        );
        if (secElement.colorChangeFactor.getValue() > 0) {
            GraphicsYio.setBatchAlpha(batch, 1 - secElement.colorChangeFactor.getValue());
            GraphicsYio.drawByRectangle(
                    batch,
                    MenuRenders.renderUiColors.map.get(secElement.previousColor),
                    secElement.getViewPosition()
            );
        }
        SelectionEngineYio selectionEngineYio = secElement.parentItem.selectionEngineYio;
        if (selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, selectionEngineYio.getAlpha());
            GraphicsYio.drawByRectangle(batch, blackPixel, secElement.getViewPosition());
        }
    }


    private void renderShadow() {
        GraphicsYio.setBatchAlpha(batch, secElement.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(secElement.getViewPosition(), 2 * secElement.cornerRadius);
    }
}
