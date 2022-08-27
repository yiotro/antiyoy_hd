package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AdvancedLabelElement;
import yio.tro.onliyoy.menu.elements.AnimationYio;

public class SceneToast extends ModalSceneYio{


    private AdvancedLabelElement advancedLabelElement;


    @Override
    protected void initialize() {
        advancedLabelElement = uiFactory.getAdvancedLabelElement()
                .setSize(0.9, 0.01)
                .centerHorizontal()
                .alignTop(0.04)
                .setAnimation(AnimationYio.none)
                .setFont(Fonts.miniFont)
                .setBackgroundEnabled(true)
                .setCentered(true)
                .setBackgroundOpacity(0.95)
                .setTextOpacity(0.9);
    }


    @Override
    protected boolean shouldNotTouchFocusScene() {
        return true;
    }


    public void show(String key) {
        show(key, 150);
    }


    public void show(String key, int duration) {
        if (isCurrentlyVisible()) {
            destroy();
        }
        create();
        advancedLabelElement
                .setLifeDuration(duration)
                .applyText(key);
    }

}
