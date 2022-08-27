package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class RveAddConditionItem extends AbstractRveItem{

    public RenderableTextYio renderableTextYio;
    public SelectionEngineYio selectionEngineYio;


    @Override
    protected void initialize() {
        renderableTextYio = new RenderableTextYio();
        renderableTextYio.setFont(Fonts.buttonFont);
        renderableTextYio.setString("+");
        renderableTextYio.updateMetrics();
        selectionEngineYio = new SelectionEngineYio();
    }


    @Override
    protected void onMove() {
        moveRenderableText();
        moveSelection();
    }


    private void moveSelection() {
        if (resizableViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void moveRenderableText() {
        renderableTextYio.centerHorizontal(position);
        renderableTextYio.centerVertical(position);
        renderableTextYio.updateBounds();
    }


    @Override
    boolean onTouchDown(PointYio touchPoint) {
        selectionEngineYio.applySelection();
        return true;
    }


    @Override
    boolean onClick(PointYio touchPoint) {
        SoundManager.playSound(SoundType.button);
        setInvertedDeltaMode(true);
        int index = resizableViewElement.items.size() - 2;
        RveChooseConditionTypeItem chooseConditionTypeItem = new RveChooseConditionTypeItem();
        chooseConditionTypeItem.setKey("choose");
        resizableViewElement.addItem(chooseConditionTypeItem, index);
        if (getConditionsQuantity() >= 3) {
            resizableViewElement.removeItem(this);
        }
        return true;
    }


    private int getConditionsQuantity() {
        int c = 0;
        for (AbstractRveItem rveItem : resizableViewElement.items) {
            if (isConditionItem(rveItem)) {
                c++;
            }
        }
        return c;
    }


    private boolean isConditionItem(AbstractRveItem rveItem) {
        if (rveItem instanceof RveChooseConditionTypeItem) return true;
        if (rveItem instanceof RveMoneyItem) return true;
        if (rveItem instanceof RveLandsItem) return true;
        if (rveItem instanceof RveRelationItem) return true;
        if (rveItem instanceof RveSmileysItem) return true;
        return false;
    }


    @Override
    protected float getHeight() {
        return 0.1f * GraphicsYio.height;
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveAddConditionItem;
    }
}
