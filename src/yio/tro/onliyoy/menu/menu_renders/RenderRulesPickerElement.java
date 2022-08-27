package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.rules_picker.RpeArrow;
import yio.tro.onliyoy.menu.elements.rules_picker.RpeItem;
import yio.tro.onliyoy.menu.elements.rules_picker.RulesPickerElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;

public class RenderRulesPickerElement extends RenderInterfaceElement{


    private RulesPickerElement rpElement;
    private TextureRegion arrowTexture;


    @Override
    public void loadTextures() {
        arrowTexture = GraphicsYio.loadTextureRegion("menu/icons/rpe_arrow.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        rpElement = (RulesPickerElement) element;

        renderArrows();
        renderInternals();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderInternals() {
        if (rpElement.isInTransition()) {
            batch.end();
            Masking.begin();
            prepareShapeRenderer();
            drawRectShape(rpElement.maskPosition);
            shapeRenderer.end();
            batch.begin();
            Masking.continueAfterBatchBegin();
            renderItems();
            Masking.end(batch);
            return;
        }
        renderItems();
    }


    private void renderItems() {
        for (RpeItem rpeItem : rpElement.items) {
            if (!rpeItem.isCurrentlyVisible()) continue;
            if (rpElement.getFactor().getValue() < 1 && !rpeItem.title.bounds.isFullyInside(rpElement.getViewPosition())) continue;
            GraphicsYio.setFontAlpha(rpeItem.title.font, alpha);
            GraphicsYio.renderText(batch, rpeItem.title);
            GraphicsYio.setFontAlpha(rpeItem.title.font, 1);
        }
    }


    private void renderArrows() {
        for (RpeArrow arrow : rpElement.arrows) {
            GraphicsYio.setBatchAlpha(batch, alpha);
            GraphicsYio.drawByCircle(batch, arrowTexture, arrow.position);
            if (arrow.selectionEngineYio.isSelected()) {
                GraphicsYio.setBatchAlpha(batch, alpha * arrow.selectionEngineYio.getAlpha());
                GraphicsYio.drawByCircle(batch, blackPixel, arrow.position);
            }
        }
    }
}
