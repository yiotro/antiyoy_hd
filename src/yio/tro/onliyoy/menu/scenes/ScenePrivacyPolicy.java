package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.BigTextItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class ScenePrivacyPolicy extends SceneYio{

    Reaction backReaction;
    private CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        initBackReaction();
        createList();
        spawnBackButton(backReaction);
        createScrollHelper();
    }


    private void createScrollHelper() {
        uiFactory.getScrollHelperElement()
                .setSize(1, 1)
                .setInverted(true)
                .setAutoHideEnabled(false)
                .setScrollEngineYio(customizableListYio.getScrollEngineYio());
    }


    private void initBackReaction() {
        backReaction = getOpenSceneReaction(Scenes.agreeToPrivacyPolicy);
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

        BigTextItem bigTextItem = new BigTextItem();
        bigTextItem.applyText(customizableListYio, getPrivacyPolicyText());
        customizableListYio.addItem(bigTextItem);
    }


    private String getPrivacyPolicyText() {
        FileHandle fileHandle = Gdx.files.internal("privacy policy.stf");
        String source = fileHandle.readString();
        StringBuilder builder = new StringBuilder();
        builder.append("# # # ");
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (c == '\n') {
                c = '#';
            }
            builder.append(c);
        }
        return builder.toString();
    }


    public void setBackReaction(Reaction backReaction) {
        this.backReaction = backReaction;
        backButton.setReaction(backReaction);
    }
}
