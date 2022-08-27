package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class RveLandsItem extends AbstractRveConditionItem {

    public ArrayList<Hex> hexes;


    public RveLandsItem() {
        hexes = new ArrayList<>();
    }


    public void setHexes(ArrayList<Hex> src) {
        hexes.clear();
        hexes.addAll(src);
        setTitle(LanguagesManager.getInstance().getString("lands") + " " + hexes.size() + "x");
    }


    @Override
    protected void applyClickReaction() {
        Scenes.composeLetter.destroy();
        resizableViewElement.getGameController().setTouchMode(TouchMode.tmChooseLands);
        RveChooseConditionTypeItem chooseConditionTypeItem = new RveChooseConditionTypeItem();
        TouchMode.tmChooseLands.setRveChooseConditionTypeItem(chooseConditionTypeItem);
        for (Hex hex : hexes) {
            TouchMode.tmChooseLands.appleChange(hex);
        }
        resizableViewElement.swapItem(this, chooseConditionTypeItem);
    }


    @Override
    protected void onClickedInReadMode() {
        Scenes.readLetter.destroy();
        resizableViewElement.getGameController().setTouchMode(TouchMode.tmChooseLands);
        TouchMode.tmChooseLands.enableReadMode();
        for (Hex hex : hexes) {
            TouchMode.tmChooseLands.appleChange(hex);
        }
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveLandsItem;
    }
}