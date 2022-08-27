package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.DetailedUlListItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderDetailedUlListItem extends AbstractRenderCustomListItem{

    DetailedUlListItem dulItem;
    private TextureRegion completionTexture;


    @Override
    public void loadTextures() {
        completionTexture = GraphicsYio.loadTextureRegion("menu/user_levels/completed.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        dulItem = (DetailedUlListItem) item;
        alpha *= dulItem.alphaFactor.getValue();
        GraphicsYio.setBatchAlpha(batch, alpha);
        renderBackground();
        renderCompletion();
        renderNameAndDescription();
        renderRating();
        renderDefaultSelection(dulItem);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderRating() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        if (dulItem.netUlCacheData.likes == 0 && dulItem.netUlCacheData.dislikes == 0) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, dulItem.ratingViewText, alpha);
    }


    private void renderNameAndDescription() {
        GraphicsYio.renderTextOptimized(batch, blackPixel, dulItem.name, alpha);
        if (dulItem.desc1.string.length() > 0) {
            GraphicsYio.renderTextOptimized(batch, blackPixel, dulItem.desc1, 0.5f * alpha);
        }
        GraphicsYio.renderTextOptimized(batch, blackPixel, dulItem.desc2, 0.5f * alpha);
    }


    private void renderCompletion() {
        if (!dulItem.completed) return;
        GraphicsYio.drawByCircle(batch, completionTexture, dulItem.beatIconPosition);
    }


    private void renderBackground() {
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderMultiButtonElement.mapBackgrounds.get(dulItem.backgroundYio),
                dulItem.viewPosition
        );
    }
}
