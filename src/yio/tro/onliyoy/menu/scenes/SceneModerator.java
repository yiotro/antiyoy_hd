package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.elements.AdvancedLabelElement;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.shared.NetModeratorData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.RepeatYio;

public class SceneModerator extends SceneYio{

    private AnnounceViewElement mainView;
    private AdvancedLabelElement advancedLabelElement;
    private RepeatYio<SceneModerator> repeatRequestData;
    long requestTime;


    public SceneModerator() {
        initRepeats();
    }


    private void initRepeats() {
        repeatRequestData = new RepeatYio<SceneModerator>(this, 120) {
            @Override
            public void performAction() {
                parent.doSendRequest();
            }
        };
    }


    @Override
    public void move() {
        super.move();
        repeatRequestData.move();
    }


    private void doSendRequest() {
        requestTime = System.currentTimeMillis();
        netRoot.sendMessage(NmType.request_moderator_data, "");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        doSendRequest();
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getBackReaction());
        createMainView();
        createAdvancedLabel();
        createButtons();
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (netRoot.userData.role == NetRole.admin) {
                    Scenes.admin.create();
                    return;
                }
                Scenes.mainLobby.create();
            }
        };
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(mainView)
                .setSize(0.7, 0.05)
                .alignBottom(0.02)
                .centerHorizontal()
                .setTouchOffset(0.02)
                .applyText("check_level")
                .setReaction(getCheckLevelReaction());

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("check_renaming")
                .setReaction(getCheckRenamingReaction());

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("check_report")
                .setReaction(getCheckReportReaction());

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("instruction")
                .setReaction(getOpenSceneReaction(Scenes.modInstruction));
    }


    private Reaction getCheckRenamingReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.sendMessage(NmType.request_to_check_renaming, "");
                Scenes.waitToCheckRenaming.create();
            }
        };
    }


    private Reaction getCheckReportReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.sendMessage(NmType.request_check_report, "");
                Scenes.waitForReport.create();
            }
        };
    }


    private Reaction getCheckLevelReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.sendMessage(NmType.request_check_user_level, "");
                Scenes.waitToVerifyLevel.create();
            }
        };
    }


    private void createAdvancedLabel() {
        advancedLabelElement = uiFactory.getAdvancedLabelElement()
                .setParent(mainView)
                .setSize(0.84, 0.01)
                .centerHorizontal()
                .alignTop(0.06)
                .setFont(Fonts.miniFont)
                .applyText(" ");
    }


    private void createMainView() {
        double h = 0.62;
        mainView = uiFactory.getAnnounceViewElement()
                .setSize(0.9, h)
                .alignBottom(0.45 - h / 2)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .setTitle("moderator_panel")
                .setText(" ")
                .setTouchable(false);
    }


    public void onModeratorDataReceived(NetModeratorData netModeratorData) {
        String prefixOnline = languagesManager.getString("current_online");
        String prefixArchive = languagesManager.getString("archive");
        String prefixFresh = languagesManager.getString("fresh_levels");
        String prefixUnverified = languagesManager.getString("unverified");
        String prefixPing = languagesManager.getString("ping");
        String prefixReports = languagesManager.getString("reports");
        String prefixRenaming = languagesManager.getString("renaming");
        long deltaTime = System.currentTimeMillis() - requestTime;
        advancedLabelElement.applyText(
                prefixPing + ": " + deltaTime + " ms#" +
                prefixOnline + ": " + netModeratorData.currentOnline + "#" +
                prefixFresh + ": " + Yio.getCompactValueString(netModeratorData.fresh) + "#" +
                prefixArchive + ": " + Yio.getCompactValueString(netModeratorData.archive) + "#" +
                prefixReports + ": " + Yio.getCompactValueString(netModeratorData.reports) + "#" +
                prefixRenaming + ": " + Yio.getCompactValueString(netModeratorData.renaming) + "#" +
                prefixUnverified + ": " + Yio.getCompactValueString(netModeratorData.unverified) + "#"
        );
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
