package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.campaign.CciType;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class CciInnerItem {

    CampaignCustomItem campaignCustomItem;
    public RectangleYio position;
    public PointYio delta;
    public int index;
    public CciType type;
    public RenderableTextYio title;
    public RectangleYio selectionPosition;
    public SelectionEngineYio selectionEngineYio;


    public CciInnerItem(CampaignCustomItem campaignCustomItem) {
        this.campaignCustomItem = campaignCustomItem;
        position = new RectangleYio();
        delta = new PointYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        selectionPosition = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
    }


    void move() {
        updatePosition();
        moveText();
        updateSelectionPosition();
    }


    private void updateSelectionPosition() {
        selectionPosition.setBy(position);
        selectionPosition.increase(campaignCustomItem.delta / 2);
    }


    private void moveText() {
        title.centerHorizontal(position);
        title.centerVertical(position);
        title.updateBounds();
    }


    private void updatePosition() {
        position.x = campaignCustomItem.viewPosition.x + delta.x;
        position.y = campaignCustomItem.viewPosition.y + delta.y;
    }


    public void setType(CciType type) {
        this.type = type;
    }


    public void setIndex(int index) {
        this.index = index;
        title.setString("" + index);
        if (index == 0) {
            // tutorial
            title.setString("?");
        }
        title.updateMetrics();
    }
}
