package yio.tro.onliyoy.menu.scenes.info;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguageChooseItem;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.customizable_list.BigTextItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneSpecialThanks extends AbstractInfoScene {

    CustomizableListYio customizableListYio;


    @Override
    public void initialize() {
        createList();
        spawnBackButton(getOpenSceneReaction(Scenes.aboutGame));
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch)
                .setInternalOffset(0.02f * GraphicsYio.width)
                .setBackgroundEnabled(true);

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setHeight(0.01f * GraphicsYio.height);
        customizableListYio.addItem(titleListItem);

        BigTextItem bigTextItem = new BigTextItem();
        bigTextItem.applyText(customizableListYio, generateText());
        customizableListYio.addItem(bigTextItem);
    }


    private String generateText() {
        return languagesManager.getString("special_thanks_begin") +
                getPredefinedString() +
                getTranslatorsString();
    }


    private String getPredefinedString() {
        return "Skin AWW 2.0: Timur#" +
                "Skin 'Jannes': Jannes Peters#" +
                "Skin 'Matveismi': Matvej#" +
                "Skin 'Katuri': Laurent Hien#" +
                "Skin 'Shroomarts': Shroomarts#" +
                "Skin 'Old RPG': Kotofey#" +
                "Skin 'Medieval': Dan R#" +
                "Skin 'Feudalism': Lllemiaka#" +
                "Fix TR: Halil Murat Unay#";
    }


    private String getTranslatorsString() {
        ArrayList<LanguageChooseItem> chooseListItems = LanguagesManager.getInstance().getChooseListItems();
        StringBuilder builder = new StringBuilder();
        for (LanguageChooseItem chooseListItem : chooseListItems) {
            if (chooseListItem.author.equals("yiotro")) continue;
            builder.append("#").append(chooseListItem.name).append(": ").append(chooseListItem.author);
        }
        return builder.toString();
    }
}
