package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.*;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetMatchListData;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneCustomMatches extends SceneYio {

    private CustomizableListYio customizableListYio;


    public SceneCustomMatches() {
        customizableListYio = null;
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        createList();
        spawnBackButton(getBackReaction());
        createSetupButton();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        customizableListYio.addItem(new PlaceholderListItem("..."));
        netRoot.sendMessage(NmType.get_custom_matches, "");
    }


    public void onCodeReceived(String code) {
        customizableListYio.clearItems();
        addTitleItem();
        for (String token : code.split(",")) {
            if (token.length() < 5) continue;
            NetMatchListData netMatchListData = new NetMatchListData();
            netMatchListData.decode(token);
            CustomMatchItem item = new CustomMatchItem();
            customizableListYio.addItem(item);
            item.setNetCustomMatchData(netMatchListData);
        }
        checkToAddEmptyItem();
    }


    private void checkToAddEmptyItem() {
        if (customizableListYio.items.size() > 1) return;
        customizableListYio.addItem(new PlaceholderListItem("empty"));
    }


    private void addTitleItem() {
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(languagesManager.getString("matches"));
        customizableListYio.addItem(titleListItem);
    }


    private void createSetupButton() {
        uiFactory.getButton()
                .setSize(0.5, 0.07)
                .alignRight(0.05)
                .alignTop(0.03)
                .setBackground(BackgroundYio.green)
                .applyText("create")
                .setReaction(getOpenSceneReaction(Scenes.setupCustomMatch))
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setAnimation(AnimationYio.up);
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                MenuSwitcher.getInstance().createChooseGameModeMenu();
            }
        };
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
