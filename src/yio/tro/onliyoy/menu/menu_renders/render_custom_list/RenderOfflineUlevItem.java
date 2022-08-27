package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.OfflineUlevItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderOfflineUlevItem extends AbstractRenderCustomListItem{

    OfflineUlevItem ouItem;
    private TextureRegion completionTexture;


    @Override
    public void loadTextures() {
        completionTexture = GraphicsYio.loadTextureRegion("menu/user_levels/completed.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        ouItem = (OfflineUlevItem) item;
        GraphicsYio.setBatchAlpha(batch, alpha);
        renderBackground();
        renderCompletion();
        renderNameAndDescription();
        renderDefaultSelection(ouItem);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderNameAndDescription() {
        renderTextOptimized(ouItem.name, alpha);
        renderTextOptimized(ouItem.description, 0.5f * alpha);
    }


    private void renderCompletion() {
        if (!ouItem.completed) return;
        GraphicsYio.drawByCircle(batch, completionTexture, ouItem.beatIconPosition);
    }


    private void renderBackground() {
        if (ouItem.backgroundYio == null) return;
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderMultiButtonElement.mapBackgrounds.get(ouItem.backgroundYio),
                ouItem.viewPosition
        );
    }
}
