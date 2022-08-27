package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.tutorial.TutorialType;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class TutorialListItem extends AbstractCustomListItem{

    public HColor backgroundColor;
    RectangleYio refPosition;
    public ArrayList<TliInnerItem> innerItems;
    float innerOffset;
    private TliInnerItem touchedItem;
    public FactorYio transitionFactor;


    @Override
    protected void initialize() {
        backgroundColor = CampaignManager.getInstance().convertDifficultyIntoColor(Difficulty.tutorial);
        refPosition = new RectangleYio();
        transitionFactor = new FactorYio();
        initInnerItems();
    }


    private void initInnerItems() {
        innerItems = new ArrayList<>();
        for (TutorialType tutorialType : TutorialType.values()) {
            innerItems.add(new TliInnerItem(this, tutorialType));
        }
    }


    @Override
    protected void move() {
        for (TliInnerItem tliInnerItem : innerItems) {
            tliInnerItem.move();
        }
        moveTransitionFactor();
    }


    private void moveTransitionFactor() {
        if (customizableListYio.getFactor().getValue() < 0.98) return;
        transitionFactor.move();
    }


    @Override
    protected RectangleYio getReferencePosition() {
        refPosition.setBy(customizableListYio.maskPosition);
        refPosition.x = customizableListYio.getPosition().x;
        return refPosition;
    }


    @Override
    protected double getWidth() {
        return customizableListYio.getPosition().width;
    }


    @Override
    protected double getHeight() {
        return 0.1f * GraphicsYio.height;
    }


    @Override
    public void onAppear() {
        for (TliInnerItem tliInnerItem : innerItems) {
            tliInnerItem.updateCompletedStatus();
        }
        updateDeltas();
        transitionFactor.reset();
        transitionFactor.appear(MovementType.approach, 1.8);
    }


    private void updateDeltas() {
        float fullLength = 0;
        for (TliInnerItem tliInnerItem : innerItems) {
            fullLength += tliInnerItem.title.width;
        }
        float freeSpace = (float) (getWidth() - fullLength);
        innerOffset = freeSpace / (innerItems.size() + 1);
        float x = innerOffset;
        float y;
        for (TliInnerItem tliInnerItem : innerItems) {
            y = (float) (getHeight() / 2 + tliInnerItem.title.height / 2);
            tliInnerItem.delta.set(x, y);
            x += tliInnerItem.title.width + innerOffset;
        }
    }


    @Override
    protected void onPositionChanged() {

    }


    private TliInnerItem getCurrentlyTouchedItem(PointYio touchPoint) {
        for (TliInnerItem tliInnerItem : innerItems) {
            if (tliInnerItem.isTouchedBy(touchPoint)) return tliInnerItem;
        }
        return null;
    }


    @Override
    public void onTouchDown(PointYio touchPoint) {
        super.onTouchDown(touchPoint);
        touchedItem = getCurrentlyTouchedItem(touchPoint);
        if (touchedItem == null) return;
        touchedItem.selectionEngineYio.applySelection();
    }


    @Override
    protected void onClicked() {
        if (touchedItem == null) return;
        YioGdxGame yioGdxGame = customizableListYio.menuControllerYio.yioGdxGame;
        GameController gameController = yioGdxGame.gameController;
        gameController.tutorialManager.launch(touchedItem.tutorialType);
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderTutorialListItem;
    }
}
