package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.customizable_list.*;
import yio.tro.onliyoy.menu.elements.gameplay.NetChatViewElement;
import yio.tro.onliyoy.menu.elements.net.MatchParametersViewElement;
import yio.tro.onliyoy.menu.elements.net.MatchPreparationViewElement;
import yio.tro.onliyoy.menu.elements.net.QmsElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.*;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneMatchLobby extends SceneYio implements IColorChoiceListener {

    public AnnounceViewElement mainLabel;
    private MatchParametersViewElement matchParametersViewElement;
    public CustomizableListYio customizableListYio;
    public MatchPreparationViewElement matchPreparationViewElement;
    public NetMatchLobbyData netMatchLobbyData;
    public boolean captainMode;
    public QmsElement qmsElement;
    public QmsElement duelSearchElement;
    private ArrayList<NetMlpData> mlpList;
    private boolean entryDetected;


    public SceneMatchLobby() {
        netMatchLobbyData = null;
        captainMode = false;
        mlpList = new ArrayList<>();
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getBackReaction());
        createMainLabel();
        createMatchParametersViewElement();
        createCustomizableList();
        createMatchPreparationViewElement();
        createPhraseButton();
        createQmsElement();
        createDuelSearchElement();
    }


    private void createDuelSearchElement() {
        double h = 0.085;
        duelSearchElement = uiFactory.getQmsElement()
                .setSize(0.8, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.center)
                .setAllowedToAppear(getDuelCondition());
    }


    private void createPhraseButton() {
        uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.04))
                .alignLeft(GraphicsYio.convertToWidth(0.0125))
                .alignBottom(0.01)
                .setTouchOffset(0.07)
                .loadCustomTexture("menu/net/black_message_icon.png")
                .setIgnoreResumePause(true)
                .setReaction(getPhraseReaction())
                .setAllowedToAppear(getCustomMatchCondition())
                .setAnimation(AnimationYio.down)
                .setSelectionTexture(getSelectionTexture());
    }


    private Reaction getPhraseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (!Scenes.choosePhrase.isAllowedToAppear()) {
                    Scenes.notification.show("not_so_fast");
                    return;
                }
                Scenes.choosePhrase.create();
            }
        };
    }


    public void onPhraseReceived(NetPhraseData netPhraseData) {
        AbstractCustomListItem item = customizableListYio.getItem(netPhraseData.speakerId);
        if (item == null) return;
        MlEntityItem mlEntityItem = (MlEntityItem) item;
        String generatedString = NetChatViewElement.generateValueString(netPhraseData);
        mlEntityItem.showMessage(generatedString);
    }


    private void createQmsElement() {
        double h = 0.085;
        qmsElement = uiFactory.getQmsElement()
                .setSize(0.8, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.center)
                .setAllowedToAppear(getQuickMatchCondition());
    }


    private void createMatchPreparationViewElement() {
        matchPreparationViewElement = uiFactory.getMatchPreparationViewElement()
                .setParent(mainLabel)
                .setSize(0.9, 0.05)
                .centerHorizontal()
                .setAllowedToAppear(getCustomMatchCondition())
                .alignBottom(0);
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                MenuSwitcher.getInstance().createChooseGameModeMenu();
                netRoot.sendMessage(NmType.exit_match_lobby, "");
            }
        };
    }


    private void createCustomizableList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(mainLabel)
                .enableEmbeddedMode()
                .setSize(0.9, 0.27)
                .centerHorizontal()
                .setAllowedToAppear(getCustomMatchCondition())
                .alignUnder(previousElement, 0);
    }


    private void createMatchParametersViewElement() {
        matchParametersViewElement = uiFactory.getMatchParametersViewElement()
                .setParent(mainLabel)
                .setSize(0.85, 0.09)
                .centerHorizontal()
                .setAllowedToAppear(getCustomMatchCondition())
                .alignTop(0.03);
    }


    private void createMainLabel() {
        double h = 0.47;
        mainLabel = uiFactory.getAnnounceViewElement()
                .setSize(0.9, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.from_touch)
                .setAllowedToAppear(getCustomMatchCondition())
                .setText(" ");
    }


    public void onListCodeReceived(String listCode) {
        if (checkForQmsToProcessListCode(listCode)) return;
        if (mainLabel.getFactor().getValue() < 1) return;
        updateMlpList(listCode);
        entryDetected = false;
        removeDeprecatedItems();
        addFreshItems();
        applyColors();
        checkToSignalEntry();
        updateCaptainMode();
    }


    private void applyColors() {
        for (NetMlpData netMlpData : mlpList) {
            MlEntityItem mlEntityItem = getItem(netMlpData.clientId);
            if (mlEntityItem == null) continue;
            mlEntityItem.setColor(netMlpData.color);
        }
    }


    private void addFreshItems() {
        int previousQuantity = countMlEntityItems();
        for (NetMlpData netMlpData : mlpList) {
            if (getItem(netMlpData.clientId) != null) continue;
            MlEntityItem item = new MlEntityItem();
            item.setByNetMlpData(netMlpData);
            item.setChooseColorAllowed(true);
            item.setMatchId(netMatchLobbyData.matchId);
            customizableListYio.addItem(item);
            entryDetected = true;
        }
        if (previousQuantity == 0) {
            entryDetected = false;
        }
    }


    private void removeDeprecatedItems() {
        ArrayList<AbstractCustomListItem> items = customizableListYio.items;
        for (int i = items.size() - 1; i >= 0; i--) {
            AbstractCustomListItem item = items.get(i);
            if (!(item instanceof MlEntityItem)) continue;
            MlEntityItem mlEntityItem = (MlEntityItem) item;
            if (getMlpData(mlEntityItem.clientId) != null) continue;
            customizableListYio.removeItem(item);
        }
    }


    private NetMlpData getMlpData(String clientId) {
        for (NetMlpData netMlpData : mlpList) {
            if (netMlpData.clientId.equals(clientId)) return netMlpData;
        }
        return null;
    }


    private void updateMlpList(String listCode) {
        mlpList.clear();
        for (String token : listCode.split(",")) {
            NetMlpData netMlpData = new NetMlpData();
            netMlpData.decode(token);
            mlpList.add(netMlpData);
        }
    }


    private boolean checkForQmsToProcessListCode(String listCode) {
        if (qmsElement.getFactor().isInAppearState()) {
            qmsElement.onListCodeReceived(listCode);
            return true;
        }
        if (duelSearchElement.getFactor().isInAppearState()) {
            duelSearchElement.onListCodeReceived(listCode);
            return true;
        }
        return false;
    }


    private void checkToSignalEntry() {
        if (!entryDetected) return;
        if (wasMatchCreatedByPlayer().get()) return;
        SoundManager.playSound(SoundType.build);
    }


    private int countMlEntityItems() {
        int c = 0;
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof MlEntityItem)) continue;
            c++;
        }
        return c;
    }


    private void updateCaptainMode() {
        boolean value = calculateCurrentCaptainMode();
        if (captainMode == value) return;
        captainMode = value;
        onCaptainModeChanged();
    }


    private void onCaptainModeChanged() {
        checkToShowCaptainUI();
        checkToHideCaptainUI();
    }


    private void checkToHideCaptainUI() {
        if (captainMode) return;
        if (!Scenes.captainUI.isCurrentlyVisible()) return;
        Scenes.captainUI.destroy();
    }


    private void checkToShowCaptainUI() {
        if (!captainMode) return;
        if (Scenes.captainUI.isCurrentlyVisible()) return;
        Scenes.captainUI.create();
    }


    public boolean isInCaptainMode() {
        return captainMode;
    }


    private boolean calculateCurrentCaptainMode() {
        if (!netMatchLobbyData.hasCreator) return false;
        return netMatchLobbyData.creatorId.equals(netRoot.userData.id);
    }


    private MlEntityItem getFirstEntityItem() {
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof MlEntityItem)) continue;
            return (MlEntityItem) item;
        }
        return null;
    }


    private void addBlankListItem() {
        ScrollListItem scrollListItem = new ScrollListItem();
        scrollListItem.setHeight(0.03f * GraphicsYio.height);
        scrollListItem.setTitle("");
        scrollListItem.setTouchable(false);
        scrollListItem.setColored(false);
        customizableListYio.addItem(scrollListItem);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        addBlankListItem();
        captainMode = false;
        matchParametersViewElement.setNetMatchParameters(netMatchLobbyData);
        matchPreparationViewElement.setCreationTime(netMatchLobbyData.creationTime);
        matchPreparationViewElement.setStartTime(netMatchLobbyData.startTime);
        qmsElement.setStartTime(netMatchLobbyData.startTime);
        qmsElement.initItems(3);
        duelSearchElement.setStartTime(netMatchLobbyData.startTime);
        duelSearchElement.initItems(2);
        checkToShowDuelRulesDescription();
    }


    private void checkToShowDuelRulesDescription() {
        if (netMatchLobbyData.matchType != NetMatchType.duel)  return;
        Scenes.toast.show("duel_rules_description", 250);
    }


    private MlEntityItem getItem(String clientId) {
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof MlEntityItem)) continue;
            MlEntityItem mlEntityItem = (MlEntityItem) item;
            if (!clientId.equals(mlEntityItem.clientId)) continue;
            return mlEntityItem;
        }
        return null;
    }


    @Override
    public void onColorChosen(HColor color) {
        MlEntityItem item = getItem(netRoot.userData.id);
        item.setColor(null);
        netRoot.sendMessage(NmType.match_lobby_choose_color, "" + color);
    }


    public void setNetMatchLobbyData(NetMatchLobbyData netMatchLobbyData) {
        this.netMatchLobbyData = netMatchLobbyData;
    }


    public void setMatchStartTime(long startTime) {
        matchPreparationViewElement.setStartTime(startTime);
        qmsElement.setStartTime(startTime);
        duelSearchElement.setStartTime(startTime);
        Scenes.captainUI.destroy();
        checkToShowCaptainUI();
        checkToSignalMatchStart(startTime);
    }


    private void checkToSignalMatchStart(long startTime) {
        if (startTime == 0) return;
        if (wasMatchCreatedByPlayer().get()) return;
        SoundManager.playSound(SoundType.hold_to_march);
    }


    private ConditionYio getCustomMatchCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return netMatchLobbyData.hasCreator;
            }
        };
    }


    public ConditionYio getQuickMatchCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return netMatchLobbyData.matchType == NetMatchType.quick;
            }
        };
    }


    public ConditionYio getDuelCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return netMatchLobbyData.matchType == NetMatchType.duel;
            }
        };
    }


    public ConditionYio wasMatchCreatedByPlayer() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return !netMatchLobbyData.hasCreator;
            }
        };
    }


    public void onVerificationMessageSent() {
        backButton.deactivate();
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
