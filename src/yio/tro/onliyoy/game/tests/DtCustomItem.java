package yio.tro.onliyoy.game.tests;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractSingleLineItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class DtCustomItem extends AbstractSingleLineItem {

    public AbstractTest test;


    @Override
    protected BitmapFont getTitleFont() {
        return Fonts.gameFont;
    }


    public void setTest(AbstractTest test) {
        this.test = test;
        setTitle(test.getName());
    }


    @Override
    protected double getHeight() {
        return 0.065f * GraphicsYio.height;
    }


    @Override
    protected void onClicked() {
        if (test.isQuantityRequired()) {
            Scenes.chooseIterationsQuantity.setTest(test);
            Scenes.chooseIterationsQuantity.create();
            return;
        }
        test.perform(getGameController());
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderSingleListItem;
    }
}
