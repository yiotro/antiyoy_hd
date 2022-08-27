package yio.tro.onliyoy.menu.scenes.editor;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneResetTouchMode extends ModalSceneYio{


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
                gameController.resetTouchMode();
                destroy();
            }
        };
    }
}
