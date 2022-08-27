package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.KickReasonType;

public class AnirKicked extends AbstractNetInputReaction{

    @Override
    public void apply() {
        root.postponedReactionsManager.suspendAllReactions();

        if (isInNetMatchCurrently()) {
            yioGdxGame.applyFullTransitionToUI();
            KickReasonType reasonType = extractKickReasonType();
            Scenes.kickedFromServer.setReasonType(reasonType);
            Scenes.kickedFromServer.create();
            return;
        }

        onKickedDuringSingleplayerPlay();
    }


    private boolean isInNetMatchCurrently() {
        if (Scenes.matchLobby.isCurrentlyVisible()) return true;
        if (!yioGdxGame.gameView.coversAllScreen()) return false;
        GameController gameController = yioGdxGame.gameController;
        return gameController.objectsLayer.viewableModel.isNetMatch();
    }


    private KickReasonType extractKickReasonType() {
        KickReasonType reasonType;
        try {
            reasonType = KickReasonType.valueOf(value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            reasonType = KickReasonType.unknown;
        }
        return reasonType;
    }


    private void onKickedDuringSingleplayerPlay() {
        String kickedString = LanguagesManager.getInstance().getString("kicked");
        String offlineModeString = LanguagesManager.getInstance().getString("offline_mode_enabled");
        Scenes.notification.show(kickedString + ", " + offlineModeString.toLowerCase());
        root.enableOfflineMode();
        if (Scenes.announceShutDown.isCurrentlyVisible()) {
            Scenes.announceShutDown.destroy();
        }
    }
}
