package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.net.shared.NetRejoinMatchData;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneChooseMatchToRejoin extends ModalSceneYio{


    private CustomizableListYio customizableListYio;
    private double h;


    @Override
    protected void initialize() {
        createCloseButton();
        h = 0.3;
        createDarken();
        createDefaultPanel(h);
        createList();
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(defaultPanel)
                .setCornerRadius(0)
                .setSize(0.95, h - 0.02)
                .centerHorizontal()
                .centerVertical();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        boolean darken = true;
        for (NetRejoinMatchData netRejoinMatchData : yioGdxGame.rejoinWorker.getRejoinList()) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(generateTitle(netRejoinMatchData));
            scrollListItem.setKey(netRejoinMatchData.matchId);
            scrollListItem.setDarken(darken);
            scrollListItem.setColored(false);
            scrollListItem.setCentered(false);
            scrollListItem.setHeight(0.06f * GraphicsYio.height);
            scrollListItem.setClickReaction(getItemReaction());
            darken = !darken;
            customizableListYio.addItem(scrollListItem);
        }
    }


    private String generateTitle(NetRejoinMatchData netRejoinMatchData) {
        String turnDurationString = "";
        if (netRejoinMatchData.hasCreator) {
            turnDurationString = ", " + Yio.convertTimeToTurnDurationString(60 * netRejoinMatchData.turnSeconds);
        }
        String yourTurnString = "";
        if (netRejoinMatchData.yourTurn) {
            yourTurnString = " (" + LanguagesManager.getInstance().getString("your_turn") + ")";
        }
        return netRejoinMatchData.name + turnDurationString + yourTurnString;
    }


    private SliReaction getItemReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                yioGdxGame.rejoinWorker.applyRejoin(item.key);
            }
        };
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
