package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TliInnerItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TutorialListItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderTutorialListItem extends AbstractRenderCustomListItem {

    TutorialListItem tlItem;
    private TextureRegion completedTexture;


    @Override
    public void loadTextures() {
        completedTexture = GraphicsYio.loadTextureRegion("menu/campaign/campaign_completed.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        tlItem = (TutorialListItem) item;

        GraphicsYio.setBatchAlpha(batch, 0.66f * alpha);
        TextureRegion backgroundTexture = MenuRenders.renderUiColors.map.get(tlItem.backgroundColor);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, tlItem.viewPosition);
        GraphicsYio.setBatchAlpha(batch, 1);

        for (TliInnerItem tliInnerItem : tlItem.innerItems) {
            renderSingleItem(tliInnerItem);
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderSingleItem(TliInnerItem tliInnerItem) {
        renderInnerBackground(tliInnerItem);
        renderIcon(tliInnerItem);
        renderSelection(tliInnerItem);
        renderTitle(tliInnerItem);
    }


    private void renderIcon(TliInnerItem tliInnerItem) {
        if (!tliInnerItem.completed) return;
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByCircle(batch, completedTexture, tliInnerItem.iconPosition);
    }


    private void renderSelection(TliInnerItem tliInnerItem) {
        if (!tliInnerItem.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * tliInnerItem.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, tliInnerItem.touchPosition);
    }


    private void renderTitle(TliInnerItem tliInnerItem) {
        if (tliInnerItem.completed) return;
        if (tlItem.customizableListYio.getFactor().getValue() < 0.8) return;
        renderTextOptimized(tliInnerItem.title, alpha, tlItem.transitionFactor.getValue());
    }


    private void renderInnerBackground(TliInnerItem tliInnerItem) {
        if (tliInnerItem.completed) return;
        GraphicsYio.setBatchAlpha(batch, 0.85f * alpha);
        MenuRenders.renderRoundShape.renderRoundShape(
                tliInnerItem.incBounds,
                BackgroundYio.white,
                0.02f * GraphicsYio.width
        );
    }
}
