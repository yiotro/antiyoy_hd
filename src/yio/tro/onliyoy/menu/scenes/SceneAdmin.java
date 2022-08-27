package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.elements.AdvancedLabelElement;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetAdminData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.RepeatYio;

public class SceneAdmin extends SceneYio{


    private AnnounceViewElement mainView;
    RepeatYio<SceneAdmin> repeatRequestInfo;
    private AdvancedLabelElement advancedLabelElement;
    long requestTime;


    public SceneAdmin() {
        initRepeats();
    }


    private void initRepeats() {
        repeatRequestInfo = new RepeatYio<SceneAdmin>(this, 60) {
            @Override
            public void performAction() {
                parent.doSendRequest();
            }
        };
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.mainLobby));
        createMainView();
        createAdvancedLabel();
        createButtons();
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(mainView)
                .setSize(0.66, 0.05)
                .alignBottom(0.02)
                .centerHorizontal()
                .setTouchOffset(0.02)
                .applyText("Shut down")
                .setReaction(getOpenSceneReaction(Scenes.setupShutDown));

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("SceneModerator")
                .setReaction(getOpenSceneReaction(Scenes.moderator));

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("Edit moderators")
                .setReaction(getOpenSceneReaction(Scenes.editModerators));

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("Find user")
                .setReaction(getFindUserReaction());

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("Distribute fish")
                .setReaction(getDistributeFishReaction());

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("Mod actions")
                .setReaction(getOpenSceneReaction(Scenes.modActions));

        uiFactory.getButton()
                .setParent(mainView)
                .clone(previousElement)
                .alignAbove(previousElement, 0.015)
                .centerHorizontal()
                .applyText("Reports")
                .setReaction(getOpenSceneReaction(Scenes.chooseAdminReport));
    }


    private Reaction getDistributeFishReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onDistributeFishButtonPressed();
            }
        };
    }


    private void onDistributeFishButtonPressed() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("Quantity");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                if (!Yio.isNumeric(input)) return;
                int value = Integer.valueOf(input);
                netRoot.sendMessage(NmType.distribute_fish, "" + value);
            }
        });
    }


    private Reaction getFindUserReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onFindUserButtonPressed();
            }
        };
    }


    private void onFindUserButtonPressed() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("Name");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                netRoot.sendMessage(NmType.do_find_user, input);
                Scenes.findUserByAdmin.create();
            }
        });
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


    @Override
    public void move() {
        super.move();
        repeatRequestInfo.move();
    }


    private void createMainView() {
        double h = 0.7;
        mainView = uiFactory.getAnnounceViewElement()
                .setSize(0.9, h)
                .alignBottom(0.45 - h / 2)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .setTitle("Admin panel")
                .setText(" ")
                .setTouchable(false);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        doSendRequest();
    }


    void doSendRequest() {
        requestTime = System.currentTimeMillis();
        netRoot.sendMessage(NmType.request_admin_info, "");
    }


    public void onAdminInfoReceived(NetAdminData netAdminData) {
        long deltaTime = System.currentTimeMillis() - requestTime;
        advancedLabelElement.applyText(
                "Ping: " + deltaTime + " ms" +
                "#Online: " + netAdminData.online + " [" + Yio.roundUp(netAdminData.averageOnline, 2) + "]" +
                "#Matches: " + netAdminData.matchesInPreparationState + " -> " + netAdminData.matchesInBattleState
        );
    }
}
