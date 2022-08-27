package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SectionStartListItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderSectionStartListItem extends AbstractRenderCustomListItem{

    private SectionStartListItem slItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        slItem = (SectionStartListItem) item;

        GraphicsYio.setBatchAlpha(batch, 0.66f * alpha);
        HColor hColor = CampaignManager.getInstance().convertDifficultyIntoColor(slItem.difficulty);
        TextureRegion backgroundTexture = MenuRenders.renderUiColors.map.get(hColor);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, slItem.viewPosition);
        GraphicsYio.setBatchAlpha(batch, 1);

        renderTextOptimized(
                slItem.title,
                slItem.customizableListYio.getAlpha(),
                slItem.transitionFactor.getValue()
        );
    }
}
