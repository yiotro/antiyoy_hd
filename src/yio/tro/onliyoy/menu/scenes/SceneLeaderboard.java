package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.*;
import yio.tro.onliyoy.net.shared.NetRatingData;
import yio.tro.onliyoy.net.shared.NetRatingType;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneLeaderboard extends SceneYio{

    NetRatingType netRatingType;
    private CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
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
        netRoot.sendMessage(NmType.request_leaderboard, "" + netRatingType);
    }


    public void onCodeReceived(String code) {
        customizableListYio.clearItems();
        addTitle();

        int c = 0;
        ArrayList<NetRatingData> list = decode(code);
        for (NetRatingData netRatingData : list) {
            if (c == 10) {
                c = 0;
                addSeparator(list.indexOf(netRatingData));
            }
            RatingListItem ratingListItem = new RatingListItem();
            ratingListItem.setNetRatingType(netRatingType);
            ratingListItem.setBy(netRatingData);
            customizableListYio.addItem(ratingListItem);
            c++;
        }
    }


    private void addSeparator(int index) {
        NumberedSeparatorItem item = new NumberedSeparatorItem();
        item.setTitle("" + index);
        customizableListYio.addItem(item);
    }


    private void addTitle() {
        ScrollListItem titleItem = new ScrollListItem();
        titleItem.setCentered(true);
        titleItem.setColored(false);
        titleItem.setTitle(getTitle(netRatingType));
        titleItem.setHeight(0.12f * GraphicsYio.height);
        customizableListYio.addItem(titleItem);
    }


    private ArrayList<NetRatingData> decode(String code) {
        ArrayList<NetRatingData> list = new ArrayList<>();
        if (code == null) return list;
        if (code.length() < 5) return list;
        for (String token : code.split(",")) {
            if (token.length() < 5) continue;
            NetRatingData netRatingData = new NetRatingData();
            netRatingData.decode(token);
            list.add(netRatingData);
        }
        return list;
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(1, 1)
                .setAnimation(AnimationYio.up)
                .setCornerRadius(0)
                .setBackgroundColor(BackgroundYio.white)
                .setShadowEnabled(false);
    }


    public void setNetRatingType(NetRatingType netRatingType) {
        this.netRatingType = netRatingType;
    }


    private String getTitle(NetRatingType netRatingType) {
        switch (netRatingType) {
            default:
                return languagesManager.getString("" + netRatingType);
            case elp:
                return "ELP";
        }
    }
}
