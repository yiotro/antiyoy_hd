package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneExceptionReport extends SceneYio{

    Exception exception;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        uiFactory.getExceptionViewElement()
                .setSize(1, 1)
                .setAnimation(AnimationYio.down)
                .setException(exception);

        uiFactory.getButton()
                .setSize(1.1, 0.06)
                .centerHorizontal()
                .setAnimation(AnimationYio.down)
                .setBackground(BackgroundYio.green)
                .applyText("Ok")
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setReaction(getOkReaction());
    }


    private Reaction getOkReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.exitApp();
            }
        };
    }


    public void setException(Exception exception) {
        this.exception = exception;
    }
}
