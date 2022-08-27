package yio.tro.onliyoy.menu.scenes.saves;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.AbstractConfirmationScene;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneConfirmSlotDeletion extends AbstractConfirmationScene {

    AbstractSavesManagementScene parentScene;
    SmItem smItem;


    @Override
    protected Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
            }
        };
    }


    @Override
    protected Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                getSavesManager().removeItem(smItem);
                destroy();
                parentScene.onListUpdateRequestedFromExternalSource();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return languagesManager.getString("confirm_deletion").replace("[slot_name]", smItem.name);
    }


    public void setParentScene(AbstractSavesManagementScene parentScene) {
        this.parentScene = parentScene;
    }


    public void setSmItem(SmItem smItem) {
        this.smItem = smItem;
    }


    protected SavesManager getSavesManager() {
        return yioGdxGame.gameController.savesManager;
    }
}
