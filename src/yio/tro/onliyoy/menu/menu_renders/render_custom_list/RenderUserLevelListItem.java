package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.UserLevelListItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderUserLevelListItem extends AbstractRenderCustomListItem{

    UserLevelListItem ullItem;
    private TextureRegion completionTexture;
    private TextureRegion lineBackgroundTexture;
    private TextureRegion lineForegroundTexture;
    private TextureRegion separatorTexture;


    @Override
    public void loadTextures() {
        completionTexture = GraphicsYio.loadTextureRegion("menu/user_levels/completed.png", true);
        lineBackgroundTexture = GraphicsYio.loadTextureRegion("pixels/white.png", true);
        lineForegroundTexture = GraphicsYio.loadTextureRegion("pixels/dark.png", true);
        separatorTexture = GraphicsYio.loadTextureRegion("menu/separator.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        ullItem = (UserLevelListItem) item;
        alpha *= ullItem.alphaFactor.getValue();
        GraphicsYio.setBatchAlpha(batch, alpha);
        renderBackground();
        renderCompletion();
        renderNameAndDescription();
        renderRating();
        renderDefaultSelection(ullItem);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderRating() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        if (ullItem.netUlCacheData.likes == 0 && ullItem.netUlCacheData.dislikes == 0) return;
        renderTextOptimized(ullItem.ratingViewText, alpha);
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, lineBackgroundTexture, ullItem.ratingBackground);
        GraphicsYio.drawByRectangle(batch, lineForegroundTexture, ullItem.ratingForeground);
        GraphicsYio.renderBorder(batch, separatorTexture, ullItem.ratingBackground);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderNameAndDescription() {
        renderTextOptimized(ullItem.name, alpha);
        if (ullItem.desc1.string.length() > 0) {
            renderTextOptimized(ullItem.desc1, 0.5f * alpha);
        }
        renderTextOptimized(ullItem.desc2, 0.5f * alpha);
    }


    private void renderCompletion() {
        if (!ullItem.completed) return;
        GraphicsYio.drawByCircle(batch, completionTexture, ullItem.beatIconPosition);
    }


    private void renderBackground() {
        if (ullItem.backgroundYio == null) return;
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderMultiButtonElement.mapBackgrounds.get(ullItem.backgroundYio),
                ullItem.viewPosition
        );
    }
}
