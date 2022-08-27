package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.resizable_element.ResizableViewElement;
import yio.tro.onliyoy.menu.elements.resizable_element.RveEmptyItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveTextItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.KickReasonType;

public class SceneKickedFromServer extends SceneYio {

    KickReasonType reasonType;
    public ResizableViewElement rvElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.red;
    }


    @Override
    protected void initialize() {
        rvElement = uiFactory.getResizableViewElement()
                .setSize(0.8, 0.01)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .alignBottom(0.45);

        addRveTextItem("kicked");
        String prefix = languagesManager.getString("reason");
        String reason = languagesManager.getString("" + reasonType);
        addRveTextItem(prefix + ": " + reason.toLowerCase());
        rvElement.addItem(new RveEmptyItem(0.1));

        rvElement.addButton()
                .setSize(0.35, 0.05)
                .alignBottom(0.015)
                .alignRight(0.03)
                .setTitle("Ok")
                .setReaction(getExitReaction());
    }


    private Reaction getExitReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.exitApp();
            }
        };
    }


    public void addRveTextItem(String key) {
        RveTextItem rveTextItem = new RveTextItem();
        rveTextItem.setTitle(languagesManager.getString(key));
        rveTextItem.setHeight(0.035);
        rvElement.addItem(rveTextItem);
    }


    public void setReasonType(KickReasonType reasonType) {
        this.reasonType = reasonType;
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
