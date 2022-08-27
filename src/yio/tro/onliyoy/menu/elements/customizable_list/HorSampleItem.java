package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;

public class HorSampleItem extends AbstractCustomListItem{

    @Override
    protected void initialize() {

    }


    @Override
    protected void move() {

    }


    @Override
    protected double getWidth() {
        return 0.66 * getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return getDefaultHeight();
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {

    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderHorSampleItem;
    }
}
