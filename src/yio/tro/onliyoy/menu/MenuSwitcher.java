package yio.tro.onliyoy.menu;

import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class MenuSwitcher {

    private static MenuSwitcher instance;
    private MenuControllerYio menuControllerYio;
    private YioGdxGame yioGdxGame;


    public MenuSwitcher() {
        menuControllerYio = null;
    }


    public void onMenuControllerCreated(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
        yioGdxGame = menuControllerYio.yioGdxGame;
    }


    public static void initialize() {
        instance = null;
    }


    public static MenuSwitcher getInstance() {
        if (instance == null) {
            instance = new MenuSwitcher();
        }
        return instance;
    }


    public void createChooseGameModeMenu() {
        if (yioGdxGame.netRoot.offlineMode) {
            Scenes.offlineMenu.create();
            return;
        }
        Scenes.chooseGameMode.create();
    }


    public void createPauseMenu() {
        switch (getGameController().gameMode) {
            default:
                Scenes.defaultPauseMenu.create();
                break;
            case editor:
                Scenes.editorPauseMenu.create();
                break;
            case replay:
                Scenes.replayPauseMenu.create();
                break;
            case net_match:
                Scenes.netPauseMenu.create();
                break;
            case completion_check:
                Scenes.completionCheckPauseMenu.create();
                break;
            case verification:
                Scenes.verificationPauseMenu.create();
                break;
            case user_level:
                Scenes.userLevelPauseMenu.create();
                break;
            case report:
                Scenes.reportPauseMenu.create();
                break;
            case calendar:
                Scenes.calendarPauseMenu.create();
                break;
            case tutorial:
                Scenes.tutorialPauseMenu.create();
                break;
            case campaign:
                Scenes.campaignPauseMenu.create();
                break;
        }
    }


    public void createMenuOverlay() {
        Scenes.gameOverlay.create();
        Scenes.netOverlay.create();
        switch (getGameController().gameMode) {
            default:
                getGameController().syncMechanicsOverlayWithCurrentTurn();
                Scenes.phraseButton.create();
                getViewableModel().provinceSelectionManager.syncUI();
                checkForAiOnlyOverlay();
                checkForSpectatorOverlay();
                break;
            case replay:
                Scenes.replayOverlay.create();
                break;
            case editor:
                Scenes.editorOverlay.create();
                break;
            case verification:
                Scenes.verificationOverlay.create();
                break;
            case report:
                Scenes.reportOverlay.create();
                break;
        }
    }


    private void checkForAiOnlyOverlay() {
        if (!getViewableModel().entitiesManager.isInAiOnlyMode()) return;
        Scenes.aiOnlyOverlay.create();
    }


    private void checkForSpectatorOverlay() {
        if (!getViewableModel().isNetMatch()) return;
        if (!yioGdxGame.netRoot.isSpectatorCurrently()) return;
        Scenes.spectatorOverlay.create();
    }


    private ObjectsLayer getObjectsLayer() {
        return getGameController().objectsLayer;
    }


    private GameController getGameController() {
        return yioGdxGame.gameController;
    }


    private ViewableModel getViewableModel() {
        return getObjectsLayer().viewableModel;
    }
}
