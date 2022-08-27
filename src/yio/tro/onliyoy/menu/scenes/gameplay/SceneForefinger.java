package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.menu.elements.forefinger.ForefingerElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneForefinger extends ModalSceneYio{

    public ForefingerElement forefinger;


    @Override
    protected void initialize() {
        forefinger = uiFactory.getForefingerElement()
                .setAppearParameters(MovementType.approach, 8);
    }


    @Override
    protected void onAppear() {
        forceElementsToTop();
    }


    @Override
    public boolean isCurrentlyVisible() {
        return forefinger != null && forefinger.getFactor().getValue() > 0;
    }
}
