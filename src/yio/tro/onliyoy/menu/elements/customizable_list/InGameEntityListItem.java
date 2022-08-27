package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;

public class InGameEntityListItem extends ScrollListItem {

    public HColor color;


    public void setColor(HColor color) {
        this.color = color;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderInGameEntityListItem;
    }
}
