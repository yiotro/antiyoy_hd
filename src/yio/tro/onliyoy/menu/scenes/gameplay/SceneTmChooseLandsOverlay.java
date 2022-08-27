package yio.tro.onliyoy.menu.scenes.gameplay;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.resizable_element.RveChooseConditionTypeItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveLandsItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneTmChooseLandsOverlay extends ModalSceneYio {

    public ButtonYio applyButton;


    @Override
    protected void initialize() {
        applyButton = uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .centerHorizontal()
                .alignBottom(0)
                .setAnimation(AnimationYio.down)
                .setSelectionTexture(getSelectionTexture())
                .loadCustomTexture("menu/editor/apply_icon.png")
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setReaction(getApplyReaction());
    }


    private Reaction getApplyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onApplyButtonPressed();
            }
        };
    }


    void onApplyButtonPressed() {
        destroy();
        getGameController().setTouchMode(TouchMode.tmDiplomacy);
        ArrayList<Hex> chosenHexes = TouchMode.tmChooseLands.chosenHexes;
        Scenes.composeLetter.create();
        if (chosenHexes.size() == 0) return;
        RveLandsItem rveLandsItem = new RveLandsItem();
        rveLandsItem.setHexes(chosenHexes);
        if (!areChosenHexesOwnedByCurrentEntity(chosenHexes)) {
            rveLandsItem.setArrowUpMode(false);
        }
        RveChooseConditionTypeItem rveChooseConditionTypeItem = TouchMode.tmChooseLands.rveChooseConditionTypeItem;
        Scenes.composeLetter.rvElement.swapItem(rveChooseConditionTypeItem, rveLandsItem);
    }


    private boolean areChosenHexesOwnedByCurrentEntity(ArrayList<Hex> chosenHexes) {
        HColor currentColor = getViewableModel().entitiesManager.getCurrentColor();
        for (Hex hex : chosenHexes) {
            if (hex.color != currentColor) return false;
        }
        return true;
    }
}
