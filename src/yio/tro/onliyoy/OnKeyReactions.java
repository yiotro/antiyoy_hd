package yio.tro.onliyoy;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.keyboard.CustomKeyboardElement;
import yio.tro.onliyoy.menu.elements.keyboard.NativeKeyboardElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class OnKeyReactions {

    YioGdxGame yioGdxGame;
    MenuControllerYio menuControllerYio;
    GameController gameController;


    public OnKeyReactions(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        menuControllerYio = yioGdxGame.menuControllerYio;
        gameController = yioGdxGame.gameController;
    }


    public void keyDown(int keycode) {
        if (checkForKeyboard(keycode)) return;

        if (keycode == Input.Keys.ESCAPE) {
            keycode = Input.Keys.BACK;
        }

        checkForHotkeyUiReaction(keycode);
        checkOtherStuff(keycode);
    }


    private void checkForHotkeyUiReaction(int keycode) {
        if (keycode == Input.Keys.ENTER && Scenes.keyboard.isCurrentlyVisible()) return;
        ArrayList<InterfaceElement> interfaceElements = menuControllerYio.getInterfaceElements();
        for (int i = interfaceElements.size() - 1; i >= 0; i--) {
            InterfaceElement element = interfaceElements.get(i);
            if (!element.isVisible()) continue;
            if (element.getFactor().getValue() < 0.95) continue;
            if (!element.acceptsKeycode(keycode)) continue;
            element.pressArtificially(keycode);
            break;
        }
    }


    private boolean checkForKeyboard(int keycode) {
        CustomKeyboardElement customKeyboardElement = Scenes.keyboard.customKeyboardElement;
        if (customKeyboardElement == null) return false;
        if (customKeyboardElement.getFactor().getValue() < 0.2) return false;
        customKeyboardElement.onPcKeyPressed(keycode);
        return true;
    }


    private void checkOtherStuff(int keycode) {
        if (YioGdxGame.platformType != PlatformType.pc) return;
        switch (keycode) {
            case Input.Keys.D:
                gameController.debugActions(); // debug
                break;
            case Input.Keys.L:
                loadTopNormalSlot();
                break;
            case Input.Keys.NUM_0:
                yioGdxGame.setGamePaused(true);
                yioGdxGame.gameView.destroy();
                Scenes.debugTests.create();
                break;
            case Input.Keys.NUM_1:
            case Input.Keys.NUM_2:
                Scenes.provinceManagement.onPcKeyPressed(keycode);
                break;
            case Input.Keys.C:
                yioGdxGame.setGamePaused(true);
                yioGdxGame.gameView.destroy();
                Scenes.secretScreen.create();
                break;
            case Input.Keys.S:
                yioGdxGame.slowMo = !yioGdxGame.slowMo;
                yioGdxGame.render();
                yioGdxGame.render();
                yioGdxGame.render();
                break;
            case Input.Keys.G:
                System.out.println("OnKeyReactions.checkOtherStuff");
                break;
            case Input.Keys.A:
                if (!Scenes.debugPanel.isCurrentlyVisible()) {
                    Scenes.debugPanel.create();
                } else {
                    Scenes.debugPanel.destroy();
                }
                break;
            case Input.Keys.X:
                yioGdxGame.applyFullTransitionToUI();
                Scenes.campaign.create();
                break;
            case Input.Keys.T:
                Scenes.testScreen.create();
                break;
            case Input.Keys.Z:
                gameController.cameraController.setTargetZoomLevel(gameController.cameraController.comfortableZoomLevel);
                break;
            case Input.Keys.U:
                if (!gameController.yioGdxGame.gamePaused) {
                    gameController.cameraController.changeZoomLevel(0.1);
                } else {
                    Scenes.userLevels.create();
                }
                break;
            case Input.Keys.I:
                if (!gameController.yioGdxGame.gamePaused) {
                    gameController.cameraController.changeZoomLevel(-0.1);
                } else {
                    Scenes.editorLobby.performImportFromClipboard();
                }
                break;
            case Input.Keys.K:
                loadLastEditorSlot();
                break;
            case Input.Keys.NUM_9:
                openCurrentLevelInEditor();
                break;
            case Input.Keys.J:
                Scenes.editorPrepareToLaunch.create();
                break;
            case Input.Keys.R:
                Scenes.replays.create();
                break;
            case Input.Keys.H:
                doFindHex();
                break;
        }
    }


    public void doFindHex() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("Hex coordinates");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                String[] split = input.split(" ");
                if (split.length != 2) return;
                if (!Yio.isNumeric(split[0])) return;
                if (!Yio.isNumeric(split[1])) return;
                int c1 = Integer.valueOf(split[0]);
                int c2 = Integer.valueOf(split[1]);
                GameController gameController = menuControllerYio.yioGdxGame.gameController;
                ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
                Hex hex = viewableModel.getHex(c1, c2);
                if (hex == null) {
                    System.out.println("SceneDebugPanel.onInputFromKeyboardReceived: no such hex");
                    return;
                }
                gameController.setTouchMode(TouchMode.tmDefault);
                TouchMode.tmDefault.doHighlight(hex);
                TouchMode.tmDefault.highlightFactor.destroy(MovementType.lighty, 1.1);
            }
        });
    }


    private void loadLastEditorSlot() {
        SavesManager savesManager = gameController.savesManager;
        SmItem lastItem = savesManager.getLastItem(SaveType.editor);
        if (lastItem == null) return;
        EditorManager.currentSlotKey = lastItem.key;
        (new IwClientInit(yioGdxGame, LoadingType.editor_import)).perform(lastItem.levelCode);
    }


    private void openCurrentLevelInEditor() {
        Scenes.openInEditor.create();
    }


    private void loadTopNormalSlot() {
        yioGdxGame.setGamePaused(true);
        yioGdxGame.gameView.destroy();
        SmItem lastItem = gameController.savesManager.getLastItem(SaveType.normal);
        if (lastItem == null) return;
        (new IwClientInit(yioGdxGame, LoadingType.training_import)).perform(lastItem.levelCode);
    }

}
