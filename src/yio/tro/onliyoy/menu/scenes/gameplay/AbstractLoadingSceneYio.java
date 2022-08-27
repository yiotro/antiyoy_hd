package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.scenes.SceneYio;

public abstract class AbstractLoadingSceneYio extends SceneYio{

    private LabelElement label;
    boolean ready;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        ready = false;

        label = uiFactory.getLabelElement()
                .setSize(0.7, 0.06)
                .centerHorizontal()
                .centerVertical()
                .setTitle("...");
    }


    @Override
    public void move() {
        super.move();

        if (!ready) return;
        if (label.getFactor().getValue() < 1) return;

        ready = false;
        applyAction();
    }


    protected abstract void applyAction();


    @Override
    protected void onAppear() {
        super.onAppear();

        yioGdxGame.applyFullTransitionToUI();
        ready = true;
    }
}
