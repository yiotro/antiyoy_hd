package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneSpectatorOverlay extends ModalSceneYio {

    @Override
    protected void initialize() {
        createCoinButton();
    }


    private void createCoinButton() {
        uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignLeft(0)
                .alignTop(0)
                .setTouchOffset(0.05)
                .loadCustomTexture("menu/province_ui/coin.png")
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.up)
                .setSelectionTexture(getSelectionTexture())
                .setClickSound(SoundType.coin)
                .setReaction(getOpenSceneReaction(Scenes.incomeGraph));
    }
}
