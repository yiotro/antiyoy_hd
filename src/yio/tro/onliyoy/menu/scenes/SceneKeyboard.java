package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.keyboard.CustomKeyboardElement;
import yio.tro.onliyoy.menu.elements.keyboard.NativeKeyboardElement;

public class SceneKeyboard extends ModalSceneYio {

    public CustomKeyboardElement customKeyboardElement;


    @Override
    protected void initialize() {
        customKeyboardElement = uiFactory.getCustomKeyboardElement()
                .setSize(1, 0.1);
    }


    @Override
    protected void onAppear() {
        forceElementsToTop();
    }


    public void setReaction(AbstractKbReaction reaction) {
        if (customKeyboardElement == null) return;
        customKeyboardElement.setReaction(reaction);
    }


    public void setValue(String value) {
        if (customKeyboardElement == null) return;
        customKeyboardElement.setValue(value);
    }


    public void setHint(String key) {
        if (customKeyboardElement == null) return;
        customKeyboardElement.setHint(languagesManager.getString(key));
    }

}
