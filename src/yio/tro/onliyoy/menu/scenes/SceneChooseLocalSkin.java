package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.SkinListItem;
import yio.tro.onliyoy.net.shared.SkinType;

public class SceneChooseLocalSkin extends SceneYio{


    private CustomizableListYio customizableListYio;
    private SkinType[] localSkinTypes;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(getBackScene()));
        initLocalSkinTypes();
        createList();
    }


    private SceneYio getBackScene() {
        if (YioGdxGame.platformType == PlatformType.ios) {
            return Scenes.settings;
        }
        return Scenes.mainLobby;
    }


    private void initLocalSkinTypes() {
        localSkinTypes = new SkinType[]{
                SkinType.def,
                SkinType.aww2,
                SkinType.jannes,
                SkinType.shroom,
                SkinType.matvei,
                SkinType.katuri,
                SkinType.old_rpg,
                SkinType.medieval,
                SkinType.feudalism,
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        SkinType currentlyChosenSkinType = SkinManager.getInstance().getSkinType();
        boolean darken = true;
        for (SkinType skinType : localSkinTypes) {
            SkinListItem item = new SkinListItem(skinType);
            item.setActive(currentlyChosenSkinType == skinType);
            item.setDarken(darken);
            darken = !darken;
            customizableListYio.addItem(item);
        }
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);
    }
}
