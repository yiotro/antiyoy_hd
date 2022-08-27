package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.PlaceholderListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SpectateMatchItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.net.shared.NetMatchSpectateData;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneChooseMatchToSpectate extends SceneYio {

    private CustomizableListYio customizableListYio;


    public SceneChooseMatchToSpectate() {
        customizableListYio = null;
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        createList();
        spawnBackButton(getOpenSceneReaction(Scenes.mainLobby));
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        customizableListYio.addItem(new PlaceholderListItem("..."));
        netRoot.sendMessage(NmType.get_recently_launched_matches, "");
    }


    public void onCodeReceived(String code) {
        customizableListYio.clearItems();
        addTitleItem();
        for (String token : code.split(",")) {
            if (token.length() < 5) continue;
            NetMatchSpectateData netMatchSpectateData = new NetMatchSpectateData();
            netMatchSpectateData.decode(token);
            SpectateMatchItem spectateMatchItem = new SpectateMatchItem();
            customizableListYio.addItem(spectateMatchItem);
            spectateMatchItem.setNetMatchSpectateData(netMatchSpectateData);
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


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
