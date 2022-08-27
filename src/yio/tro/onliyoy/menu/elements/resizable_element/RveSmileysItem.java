package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.game.core_model.SmileyType;
import yio.tro.onliyoy.menu.elements.smileys.SkItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

import static yio.tro.onliyoy.menu.elements.resizable_element.RveIconType.executor;
import static yio.tro.onliyoy.menu.elements.smileys.SkViewField.isCondensed;

public class RveSmileysItem extends AbstractRveConditionItem{

    public ArrayList<SkItem> items;


    @Override
    protected void initialize() {
        super.initialize();
        items = new ArrayList<>();
    }


    @Override
    protected void initIcons() {
        super.initIcons();
        removeIcon(executor);
    }


    @Override
    protected void applyClickReaction() {

    }


    public void setValues(ArrayList<SmileyType> source) {
        items.clear();
        for (SmileyType smileyType : source) {
            SkItem skItem = new SkItem();
            skItem.smileyType = smileyType;
            skItem.setRadius(0.5 * iconRadius);
            items.add(skItem);
        }
        updateDeltas();
    }


    private void updateDeltas() {
        float x = 0.045f * GraphicsYio.width;
        float y = getHeight() / 2;
        for (SkItem skItem : items) {
            float r = skItem.position.radius;
            if (isCondensed(skItem.smileyType)) {
                r *= 0.66f;
            }
            x += r;
            skItem.delta.set(x, y);
            x += r;
        }
    }


    public ArrayList<SmileyType> generateOutputList() {
        ArrayList<SmileyType> list = new ArrayList<>();
        for (SkItem skItem : items) {
            list.add(skItem.smileyType);
        }
        return list;
    }


    @Override
    protected void onMove() {
        super.onMove();
        moveItems();
    }


    private void moveItems() {
        for (SkItem skItem : items) {
            skItem.move(position, false);
        }
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveSmileysItem;
    }
}
