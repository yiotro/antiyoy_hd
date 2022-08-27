package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.menu.elements.resizable_element.ResizableViewElement;
import yio.tro.onliyoy.menu.elements.resizable_element.RveEmptyItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveTextItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetProblemsDetector;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.StoreLinksYio;

public class SceneEntry extends SceneYio {

    boolean readyToConnect;
    private ResizableViewElement rvElement;
    boolean readyToLogin;
    boolean readyToAuto;
    boolean googleLoginProcessActive;


    public SceneEntry() {

    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    protected void initialize() {
        rvElement = uiFactory.getResizableViewElement()
                .setSize(0.8, 0.01)
                .centerHorizontal()
                .setAnimation(AnimationYio.center)
                .alignBottom(0.45);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        readyToConnect = true;
        readyToLogin = false;
        googleLoginProcessActive = false;
        readyToAuto = false;
        rvElement.clearItems();
        addRveTextItem("connecting_to_server");
        checkForRussianToast();
    }


    private void checkForRussianToast() {
        if (!LanguagesManager.getInstance().getLanguage().contains("ru_RU")) return;
        Scenes.toast.show("Если вам не удаётся подключиться к серверу, то, пожалуйста, используйте VPN. Насколько мне известно, это обычно помогает.", 500);
    }


    public void addRveTextItem(String key) {
        removeBlankItem();
        RveTextItem rveTextItem = new RveTextItem();
        rveTextItem.setTitle(languagesManager.getString(key));
        rveTextItem.setHeight(0.035);
        rvElement.addItem(rveTextItem);
        addBlankItem(0.012);
    }


    private void removeBlankItem() {
        AbstractRveItem blankItem = rvElement.getItem("blank");
        if (blankItem != null) {
            rvElement.removeItem(blankItem);
        }
    }


    @Override
    public void move() {
        checkToConnect();
        moveGoogleLoginProcess();
        checkToAuto();
    }


    private void checkToAuto() {
        if (!readyToAuto) return;
        if (rvElement.getFactor().getValue() < 1) return;
        if (!rvElement.isHeightAtTarget()) return;
        readyToAuto = false;
        readyToLogin = true;
        onGoogleButtonPressed();
    }


    private void moveGoogleLoginProcess() {
        if (!googleLoginProcessActive) return;
        if (!yioGdxGame.signInManager.isReady()) return;
        googleLoginProcessActive = false;
        readyToLogin = false;
        netRoot.sendMessage(NmType.login_google, netRoot.signInData.idToken);
    }


    public void onFailToAuthorizeOnServer() {
        googleLoginProcessActive = false;
        readyToLogin = true;
        Scenes.notification.show("failed_to_login");
        SettingsManager.getInstance().autoLogin = false;
        SettingsManager.getInstance().saveValues();
    }


    public void onClientVersionDeprecated() {
        addBlankItem(0.07);
        addUpdateButton();
        addOfflineButton();
    }


    private void addUpdateButton() {
        rvElement.addButton()
                .setSize(0.35, 0.05)
                .alignBottom(0.015)
                .alignRight(0.03)
                .setTitle("update")
                .setReaction(getUpdateReaction());
    }


    public void onServerIsUpdating() {
        addBlankItem(0.07);
        addOfflineButton();
        rvElement.addButton()
                .setSize(0.35, 0.05)
                .alignBottom(0.015)
                .alignRight(0.03)
                .setTitle("quit")
                .setReaction(getQuitReaction());
    }


    private void addOfflineButton() {
        rvElement.addButton()
                .setSize(0.35, 0.05)
                .alignBottom(0.015)
                .alignLeft(0.03)
                .setTitle("offline")
                .setReaction(getOfflineModeReaction());
    }


    private Reaction getOfflineModeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.enableOfflineMode();
            }
        };
    }


    private Reaction getQuitReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.exitApp();
            }
        };
    }


    public void onProtocolIsValidMessageReceived() {
        addRveTextItem("protocol_is_valid");
        addRveTextItem("authorization_in_process");
        if (checkForPcAutoGuest()) return;
        if (checkForAutoLogin()) return;
        addBlankItem(0.07);

        rvElement.addButton()
                .setSize(0.35, 0.05)
                .alignBottom(0.015)
                .alignRight(0.03)
                .setKey("guest")
                .setTitle("guest")
                .setReaction(getGuestReaction());

        rvElement.addButton()
                .setSize(0.35, 0.05)
                .alignBottom(0.015)
                .alignLeft(0.03)
                .setTitle("Google")
                .setReaction(getGoogleReaction());

        readyToLogin = true;
    }


    private boolean checkForAutoLogin() {
        if (!SettingsManager.getInstance().autoLogin) return false;
        if (YioGdxGame.platformType == PlatformType.pc) return false;
        readyToAuto = true;
        return true;
    }


    private boolean checkForPcAutoGuest() {
        if (YioGdxGame.platformType != PlatformType.pc) return false;
        if (!netRoot.isInLocalMode()) {
            netRoot.sendMessage(NmType.login_guest, "");
            return true;
        }
        if (DebugFlags.pcDebugLogin1) {
            netRoot.sendMessage(NmType.login_debug, "1");
            return true;
        }
        if (DebugFlags.pcDebugLogin2) {
            netRoot.sendMessage(NmType.login_debug, "2");
            return true;
        }
        netRoot.sendMessage(NmType.login_guest, "");
        return true;
    }


    private Reaction getGoogleReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onGoogleButtonPressed();
            }
        };
    }


    private void onGoogleButtonPressed() {
        if (!readyToLogin) return;
        if (netRoot.signInData == null) {
            // yes, this is should be impossible
            // but there were a lot of null pointer exceptions in GP console
            // that can be only caused by this situation
            Scenes.notification.show("Sign in data is null");
            return;
        }
        if (!OneTimeInfo.getInstance().privacyPolicy) {
            Scenes.agreeToPrivacyPolicy.create();
            return;
        }
        googleLoginProcessActive = true;
        yioGdxGame.signInManager.apply(netRoot.signInData);
    }


    private Reaction getGuestReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (!readyToLogin) return;
                readyToLogin = false;
                netRoot.sendMessage(NmType.login_guest, "");
            }
        };
    }


    private void addBlankItem(double h) {
        RveEmptyItem blankItem = new RveEmptyItem(h);
        blankItem.setKey("blank");
        rvElement.addItem(blankItem);
    }


    private Reaction getUpdateReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                // yes, it should be called twice
                // there is a bug in GP where it doesn't show 'update' button first time
                // though it may be that this method doesn't help
                Gdx.net.openURI(StoreLinksYio.getInstance().getLink("onliyoy"));
                Gdx.net.openURI(StoreLinksYio.getInstance().getLink("onliyoy"));
                yioGdxGame.exitApp();
            }
        };
    }


    public void onWelcomeMessageReceived() {
        String reconnectionMatchId = netRoot.problemsDetector.reconnectionMatchId;
        if (reconnectionMatchId.length() > 0) {
            addRveTextItem(LanguagesManager.getInstance().getString("return_to_match_noun") + "...");
            yioGdxGame.rejoinWorker.applyRejoin(reconnectionMatchId);
            return;
        }
        PostponedReactionsManager.aprOpenMainLobby.launch();
    }


    private void checkToConnect() {
        if (!readyToConnect) return;
        if (yioGdxGame.generalBackgroundManager.isMovingCurrently()) return;
        readyToConnect = false;
        yioGdxGame.netRoot.connectionManager.doConnectToServer();
    }
}
