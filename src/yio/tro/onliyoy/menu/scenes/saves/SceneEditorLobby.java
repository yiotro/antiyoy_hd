package yio.tro.onliyoy.menu.scenes.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Clipboard;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.export_import.legacy.LegacyImportWorker;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.Scenes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SceneEditorLobby extends AbstractSavesManagementScene{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected String getTitleKey() {
        return "editor_slots";
    }


    @Override
    protected boolean isInReadMode() {
        return false;
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
                .setBackground(BackgroundYio.cyan)
                .applyText("import")
                .setReaction(getImportReaction())
                .setAnimation(AnimationYio.up);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        changeCreationItemBackground();
    }


    private void changeCreationItemBackground() {
        ScrollListItem creationItem = (ScrollListItem) customizableListYio.getItem("create");
        creationItem.setBackground(BackgroundYio.yellow);
    }


    @Override
    protected String getCreationTitleKey() {
        return "create_new_level";
    }


    @Override
    protected SaveType getCurrentSaveType() {
        return SaveType.editor;
    }


    @Override
    protected void onItemClicked(String key) {
        if (key.equals("create")) {
            Scenes.editorCreate.create();
            return;
        }

        String levelCode = getSavesManager().getLevelCode(key);
        EditorManager.currentSlotKey = key;
        System.out.println("levelCode = " + levelCode);
        (new IwClientInit(yioGdxGame, LoadingType.editor_import)).perform(levelCode);
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
        String contents = clipboard.getContents();
        if (!isLevelCodeValid(contents)) {
            contents = checkForLegacyImport(contents);
            if (contents.length() < 3) return;
        }
        (new IwClientInit(yioGdxGame, LoadingType.editor_import)).perform(contents);
        EditorManager.prepareNewSaveSlot(getGameController().savesManager);
    }


    private String checkForLegacyImport(String source) {
        if (source == null) return "-";
        if (source.length() < 3) return "-";
        return (new LegacyImportWorker()).apply(source);
    }


    private boolean isLevelCodeValid(String levelCode) {
        if (levelCode == null) return false;
        if (levelCode.length() < 3) return false;
        if (!levelCode.contains("onliyoy_level_code")) return false;
        if (!levelCode.contains("core_init")) return false;
        if (!levelCode.contains("#hexes:")) return false;
        if (!levelCode.contains("#player_entities:")) return false;
        return true;
    }
}
