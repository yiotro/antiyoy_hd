package yio.tro.onliyoy.menu.scenes.editor;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.AbstractPauseMenu;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneEditorPauseMenu extends AbstractPauseMenu {


    private LabelElement labelElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    public void initialize() {
        super.initialize();
        createSlotNameLabel();
    }


    private void createSlotNameLabel() {
        labelElement = uiFactory.getLabelElement()
                .setSize(0.01)
                .centerHorizontal()
                .alignTop(0.02)
                .setAnimation(AnimationYio.up)
                .setFont(Fonts.miniFont)
                .setTitle("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        getObjectsLayer().editorManager.onExitedToPauseMenu();
        updateLabelElement();
    }


    private void updateLabelElement() {
        labelElement.setTitle(extractSlotName());
    }


    private String extractSlotName() {
        if (EditorManager.currentSlotKey.length() == 0) return "";
        SmItem smItem = getGameController().savesManager.getItem(EditorManager.currentSlotKey);
        if (smItem == null) return "";
        return smItem.name;
    }


    @Override
    protected TemporaryMbeItem[] getMbeItems() {
        return new TemporaryMbeItem[]{
                new TemporaryMbeItem("resume", BackgroundYio.green, getResumeReaction()),
                new TemporaryMbeItem("launch", BackgroundYio.orange, getLaunchReaction()),
                new TemporaryMbeItem("export", BackgroundYio.cyan, getExportReaction()),
                new TemporaryMbeItem("main_lobby", BackgroundYio.red, getOpenSceneReaction(Scenes.mainLobby)),
        };
    }


    private Reaction getExportReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                getObjectsLayer().editorManager.performExportToClipboard();
            }
        };
    }


    private Reaction getLaunchReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                getObjectsLayer().editorManager.onLaunchButtonPressed();
            }
        };
    }
}
