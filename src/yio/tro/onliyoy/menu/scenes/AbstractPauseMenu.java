package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.multi_button.MultiButtonElement;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;
import yio.tro.onliyoy.menu.reactions.Reaction;

public abstract class AbstractPauseMenu extends SceneYio{


    public abstract BackgroundYio getBackgroundValue();


    @Override
    public void initialize() {
        createMultiButtonElement();
    }


    private void createMultiButtonElement() {
        TemporaryMbeItem[] mbeItems = getMbeItems();

        double h = 0.08 * countNotNullItems(mbeItems);
        MultiButtonElement multiButtonElement = uiFactory.getMultiButtonElement()
                .setSize(0.7, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.from_touch)
                .setHotkeyKeycode(Input.Keys.BACK);

        for (TemporaryMbeItem mbeItem : mbeItems) {
            if (mbeItem == null) continue;
            multiButtonElement.addLocalButton(mbeItem);
        }
    }


    private int countNotNullItems(TemporaryMbeItem[] mbeItems) {
        int c = 0;
        for (TemporaryMbeItem mbeItem : mbeItems) {
            if (mbeItem == null) continue;
            c++;
        }
        return c;
    }


    protected abstract TemporaryMbeItem[] getMbeItems();


    protected Reaction getResumeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.gameView.appear();
                MenuSwitcher.getInstance().createMenuOverlay();
                yioGdxGame.setGamePaused(false);
            }
        };
    }
}
