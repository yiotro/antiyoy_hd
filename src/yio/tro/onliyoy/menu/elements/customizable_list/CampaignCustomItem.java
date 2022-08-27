package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.campaign.CciType;
import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class CampaignCustomItem extends AbstractCustomListItem{

    public static final int ROW = 6;
    public ArrayList<CciInnerItem> items;
    public float iconSize = 0.11f * GraphicsYio.width;
    public float delta = 0.04f * GraphicsYio.width;
    private CciInnerItem touchedItem;
    public Difficulty difficulty;
    private RectangleYio refPosition;
    public FactorYio transitionFactor;


    @Override
    protected void initialize() {
        items = new ArrayList<>();
        refPosition = new RectangleYio();
        transitionFactor = new FactorYio();
    }


    public void set(int startIndex, int endIndex) {
        for (int index = startIndex; index <= endIndex; index++) {
            CciInnerItem innerItem = new CciInnerItem(this);
            innerItem.setIndex(index);
            innerItem.setType(CampaignManager.getInstance().getLevelType(index));
            items.add(innerItem);
        }
    }


    @Override
    public void onAppear() {
        super.onAppear();
        transitionFactor.reset();
        transitionFactor.appear(MovementType.approach, 1.9);
    }


    @Override
    protected RectangleYio getReferencePosition() {
        refPosition.setBy(customizableListYio.maskPosition);
        refPosition.x = customizableListYio.getPosition().x;
        return refPosition;
    }


    @Override
    protected void move() {
        for (CciInnerItem item : items) {
            item.move();
            if (!customizableListYio.touchedCurrently) {
                item.selectionEngineYio.move();
            }
        }
        moveTransitionFactor();
    }


    private void moveTransitionFactor() {
        if (customizableListYio.getFactor().getValue() < 0.98) return;
        transitionFactor.move();
    }


    @Override
    protected double getWidth() {
        return customizableListYio.getPosition().width;
    }


    @Override
    protected double getHeight() {
        return iconSize + delta;
    }


    @Override
    protected void onPositionChanged() {
        updateItemMetrics();
    }


    private void updateItemMetrics() {
        float lineWidth = ROW * iconSize + (ROW - 1) * delta;
        float x = (float) (getWidth() / 2 - lineWidth / 2);
        float y = (float) (getHeight() / 2 - iconSize / 2);
        for (CciInnerItem item : items) {
            item.delta.set(x, y);
            item.position.width = iconSize;
            item.position.height = iconSize;
            x += iconSize + delta;
        }
    }


    @Override
    public void onTouchDown(PointYio touchPoint) {
        touchedItem = findTouchedItem(touchPoint);
        if (touchedItem == null) return;

        touchedItem.selectionEngineYio.applySelection();
    }


    public boolean containsLevelIndex(int index) {
        for (CciInnerItem item : items) {
            if (item.index == index) return true;
        }
        return false;
    }


    private CciInnerItem findTouchedItem(PointYio touchPoint) {
        for (CciInnerItem item : items) {
            if (!item.selectionPosition.isPointInside(touchPoint)) continue;
            return item;
        }
        return null;
    }


    @Override
    protected void onClicked() {
        if (touchedItem == null) return;

        int index = touchedItem.index;
        if (index == 0) return;
        CciType type = touchedItem.type;
        if (type == CciType.unknown && !DebugFlags.superUserEnabled) return;

        CampaignManager.getInstance().launchCampaignLevel(index);
    }


    @Override
    protected void onLongTapped() {

    }


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderCampaignCustomItem;
    }
}
