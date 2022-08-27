package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.RatingListItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderRatingListItem extends AbstractRenderCustomListItem{


    private RatingListItem ratingListItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        ratingListItem = (RatingListItem) item;

        GraphicsYio.renderItyOptimized(
                batch,
                blackPixel,
                MenuRenders.renderAvatars.getTexture(ratingListItem.avatarType, ratingListItem.iconTextYio),
                ratingListItem.iconTextYio,
                alpha
        );
        GraphicsYio.renderTextOptimized(batch, blackPixel, ratingListItem.valueViewText, alpha);
        renderDefaultSelection(item);
    }
}
