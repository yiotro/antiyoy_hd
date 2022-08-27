package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.core_model.SmileyType;
import yio.tro.onliyoy.menu.elements.resizable_element.RveChooseConditionTypeItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveSmileysItem;
import yio.tro.onliyoy.menu.elements.smileys.SmileyInputReaction;
import yio.tro.onliyoy.menu.elements.smileys.SmileysKeyboardElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;

import java.util.ArrayList;

public class SceneSetupSmileysCondition extends ModalSceneYio {

    RveChooseConditionTypeItem rveChooseConditionTypeItem;
    public SmileysKeyboardElement smileysKeyboardElement;


    @Override
    protected void initialize() {
        smileysKeyboardElement = uiFactory.getSmileysKeyboardElement()
                .setSize(1, 1)
                .setReaction(getReaction());
    }


    private SmileyInputReaction getReaction() {
        return new SmileyInputReaction() {
            @Override
            public void onSmileyInputReceived(ArrayList<SmileyType> input) {
                if (input == null) return;
                if (input.size() == 0) return;
                RveSmileysItem rveSmileysItem = new RveSmileysItem();
                rveSmileysItem.setValues(input);
                Scenes.composeLetter.rvElement.swapItem(rveChooseConditionTypeItem, rveSmileysItem);
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        forceElementsToTop();
    }


    public void setRveChooseConditionTypeItem(RveChooseConditionTypeItem rveChooseConditionTypeItem) {
        this.rveChooseConditionTypeItem = rveChooseConditionTypeItem;
    }
}
