package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.BigTextItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneAdminTextReport extends SceneYio{

    private CustomizableListYio customizableListYio;
    String message;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    protected void initialize() {
        createList();
        spawnBackButton(getOpenSceneReaction(Scenes.admin));
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(1, 1)
                .centerHorizontal()
                .alignBottom(0)
                .setShadowEnabled(false)
                .setAnimation(AnimationYio.from_touch)
                .setInternalOffset(0.04f * GraphicsYio.width)
                .setCornerRadius(0)
                .setBackgroundEnabled(true);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        BigTextItem bigTextItem = new BigTextItem();
        bigTextItem.applyText(customizableListYio, message);
        customizableListYio.addItem(bigTextItem);
    }


    public void setMessage(String message) {
        this.message = "# # # #" + message;
    }
}
