package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetReportData;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneUserLevelPauseMenu extends AbstractPauseMenu{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected TemporaryMbeItem[] getMbeItems() {
        if (YioGdxGame.platformType == PlatformType.ios) {
            return new TemporaryMbeItem[]{
                    new TemporaryMbeItem("resume", BackgroundYio.green, getResumeReaction()),
                    new TemporaryMbeItem("restart", BackgroundYio.yellow, getOpenSceneReaction(Scenes.confirmRestart)),
                    new TemporaryMbeItem("main_lobby", BackgroundYio.red, getOpenSceneReaction(Scenes.mainLobby)),
            };
        }
        return new TemporaryMbeItem[]{
                new TemporaryMbeItem("resume", BackgroundYio.green, getResumeReaction()),
                new TemporaryMbeItem("restart", BackgroundYio.yellow, getOpenSceneReaction(Scenes.confirmRestart)),
                new TemporaryMbeItem("dislike", BackgroundYio.orange, getOpenSceneReaction(Scenes.confirmDislikeLevel)),
                new TemporaryMbeItem("do_report", BackgroundYio.magenta, getReportReaction()),
                new TemporaryMbeItem("main_lobby", BackgroundYio.red, getOpenSceneReaction(Scenes.mainLobby)),
        };
    }


    private Reaction getReportReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onReportButtonPressed();
            }
        };
    }


    private void onReportButtonPressed() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("reason");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                NetReportData netReportData = new NetReportData();
                netReportData.userId = netRoot.userData.id;
                netReportData.levelId = netRoot.tempUlTransferData.id;
                netReportData.message = input;
                netRoot.sendMessage(NmType.report_level, netReportData.encode());
                Scenes.userLevels.create();
                Scenes.notification.show("report_sent");
            }
        });
    }

}
