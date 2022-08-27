package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.resizable_element.ResizableViewElement;
import yio.tro.onliyoy.menu.elements.resizable_element.RveEmptyItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveTextItem;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneKickedForTwoTurnSkip extends SceneYio{

    private ResizableViewElement rvElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
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
        String reason = languagesManager.getString("skipped_two_turns");
        addRveTextItem(prefix + ": " + reason.toLowerCase());
        rvElement.addItem(new RveEmptyItem(0.1));

        rvElement.addButton()
                .setSize(0.35, 0.05)
                .alignBottom(0.015)
                .alignRight(0.03)
                .setTitle("Ok")
                .setReaction(getOpenSceneReaction(Scenes.mainLobby));
    }


    public void addRveTextItem(String key) {
        RveTextItem rveTextItem = new RveTextItem();
        rveTextItem.setTitle(languagesManager.getString(key));
        rveTextItem.setHeight(0.035);
        rvElement.addItem(rveTextItem);
    }
}
