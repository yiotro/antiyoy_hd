package yio.tro.onliyoy.menu.scenes.gameplay;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class ScenePhraseButton extends ModalSceneYio {

    @Override
    protected void initialize() {
        uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.038))
                .alignLeft(GraphicsYio.convertToWidth(0.0125))
                .alignTop(0.01)
                .setTouchOffset(0.07)
                .setCustomTexture(getTextureFromAtlas("phrase_icon"))
                .setIgnoreResumePause(true)
                .setReaction(getReaction())
                .setAllowedToAppear(getCondition())
                .setAnimation(AnimationYio.up_then_fade)
                .setSelectionTexture(getSelectionTexture());
    }


    private ConditionYio getCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                if (!getViewableModel().isNetMatch()) return false;
                if (yioGdxGame.netRoot.isSpectatorCurrently()) return false;
                return true;
            }
        };
    }


    private Reaction getReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (!Scenes.choosePhrase.isAllowedToAppear()) {
                    Scenes.notification.show("not_so_fast");
                    return;
                }
                Scenes.choosePhrase.create();
            }
        };
    }
}
