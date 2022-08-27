package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.campaign.CciType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CampaignCustomItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CciInnerItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderCampaignCustomItem extends AbstractRenderCustomListItem {


    private CampaignCustomItem campaignCustomItem;
    HashMap<CciType, TextureRegion> mapTextures;
    private float alpha;


    @Override
    public void loadTextures() {
        mapTextures = new HashMap<>();
        for (CciType cciType : CciType.values()) {
            mapTextures.put(cciType, GraphicsYio.loadTextureRegion("menu/campaign/campaign_" + cciType + ".png", true));
        }
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        campaignCustomItem = (CampaignCustomItem) item;
        alpha = campaignCustomItem.customizableListYio.getAlpha();

        GraphicsYio.setBatchAlpha(batch, 0.66f * alpha);
        HColor hColor = CampaignManager.getInstance().convertDifficultyIntoColor(campaignCustomItem.difficulty);
        TextureRegion backgroundTexture = MenuRenders.renderUiColors.map.get(hColor);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, campaignCustomItem.viewPosition);

        GraphicsYio.setBatchAlpha(batch, alpha);
        for (CciInnerItem cciInnerItem : campaignCustomItem.items) {
            renderSingleInnerItem(cciInnerItem);
        }

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderSingleInnerItem(CciInnerItem cciInnerItem) {
        if (cciInnerItem.index == 0) return;
        GraphicsYio.drawByRectangle(batch, mapTextures.get(cciInnerItem.type), cciInnerItem.position);

        if (campaignCustomItem.customizableListYio.getFactor().getValue() < 0.8) return;
        if (cciInnerItem.type == CciType.unlocked) {
            renderTextOptimized(cciInnerItem.title, alpha, campaignCustomItem.transitionFactor.getValue());
            GraphicsYio.setBatchAlpha(batch, alpha);
        }

        if (cciInnerItem.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, cciInnerItem.selectionEngineYio.getAlpha());
            GraphicsYio.drawByRectangle(batch, blackPixel, cciInnerItem.selectionPosition);
            GraphicsYio.setBatchAlpha(batch, alpha);
        }
    }
}
