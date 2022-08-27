package yio.tro.onliyoy.menu.scenes.saves;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.game.export_import.IwCampaignInit;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneLoadFromSlot extends AbstractSavesManagementScene{

    @Override
    protected String getTitleKey() {
        return "save_slots";
    }


    @Override
    protected void initialize() {
        super.initialize();
        createReplaysButton();
    }


    private void createReplaysButton() {
        uiFactory.getButton()
                .setSize(0.4, 0.07)
                .alignRight(0.05)
                .alignTop(0.03)
                .setBackground(BackgroundYio.green)
                .applyText("replays")
                .setReaction(getOpenSceneReaction(Scenes.replays))
                .setAnimation(AnimationYio.up);
    }


    @Override
    protected boolean isInReadMode() {
        return true;
    }


    @Override
    protected boolean isSmItemVisible(SmItem smItem) {
        switch (smItem.type) {
            default:
                return false;
            case normal:
            case campaign:
                return true;
        }
    }


    @Override
    protected SaveType getCurrentSaveType() {
        return SaveType.normal;
    }


    @Override
    protected void onItemClicked(String key) {
        SmItem smItem = getSavesManager().getItem(key);
        if (smItem == null) return;
        String levelCode = getSavesManager().getLevelCode(key);
        switch (smItem.type) {
            default:
                System.out.println("SceneLoad.onItemClicked: problem");
                break;
            case campaign:
                (new IwCampaignInit(yioGdxGame, -1)).perform(levelCode); // level index will be loaded from level code
                break;
            case normal:
                (new IwClientInit(yioGdxGame, LoadingType.training_import)).perform(levelCode);
                break;
        }
    }


    @Override
    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                MenuSwitcher.getInstance().createChooseGameModeMenu();
            }
        };
    }
}
