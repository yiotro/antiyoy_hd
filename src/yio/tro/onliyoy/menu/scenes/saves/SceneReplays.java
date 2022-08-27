package yio.tro.onliyoy.menu.scenes.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneReplays extends AbstractSavesManagementScene {

    @Override
    protected String getTitleKey() {
        return "replays";
    }


    @Override
    protected boolean isInReadMode() {
        return true;
    }


    @Override
    protected void initialize() {
        super.initialize();
        createImportButton();
    }


    private void createImportButton() {
        uiFactory.getButton()
                .setSize(0.4, 0.07)
                .alignRight(0.05)
                .alignTop(0.03)
                .setBackground(BackgroundYio.yellow)
                .applyText("import")
                .setReaction(getImportReaction())
                .setAnimation(AnimationYio.up);
    }


    private Reaction getImportReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                performImportFromClipboard();
            }
        };
    }


    public void performImportFromClipboard() {
        Clipboard clipboard = Gdx.app.getClipboard();
        String levelCode = clipboard.getContents();
        importReplay(levelCode);
    }


    public void importReplay(String levelCode) {
        if (!isLevelCodeValid(levelCode)) return;
        (new IwClientInit(yioGdxGame, LoadingType.replay_open)).perform(levelCode);
        String name = LanguagesManager.getInstance().getString("import") + " " + EditorManager.getDateString();
        getGameController().savesManager.addItem(SaveType.replay, name, levelCode);
    }


    @Override
    protected SaveType getCurrentSaveType() {
        return null; // not used here
    }


    @Override
    protected boolean isSmItemVisible(SmItem smItem) {
        if (smItem.type == SaveType.normal) return true;
        if (smItem.type == SaveType.replay) return true;
        return false;
    }


    @Override
    protected void onItemClicked(String key) {
        String levelCode = getSavesManager().getLevelCode(key);
        if (DebugFlags.transferReplay) {
            Gdx.app.getClipboard().setContents(levelCode);
            System.out.println("Replay level code copied to clipboard, use pastebin to transfer it to PC");
        }
        (new IwClientInit(yioGdxGame, LoadingType.replay_open)).perform(levelCode);
    }


    @Override
    protected void onItemLongTapped(String key) {
        Scenes.replayContextMenu.create();
        Scenes.replayContextMenu.setValues(this, key);
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


    private boolean isLevelCodeValid(String levelCode) {
        if (levelCode == null) return false;
        if (levelCode.length() < 3) return false;
        if (!levelCode.contains("onliyoy_level_code")) return false;
        if (!levelCode.contains("core_init")) return false;
        if (!levelCode.contains("#hexes:")) return false;
        if (!levelCode.contains("#player_entities:")) return false;
        if (!levelCode.contains("#events_list:")) return false;
        return true;
    }
}
