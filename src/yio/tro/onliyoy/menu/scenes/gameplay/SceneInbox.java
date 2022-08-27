package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Letter;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.LetterListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneInbox extends ModalSceneYio {


    private double panelHeight;
    private CustomizableListYio customizableListYio;


    @Override
    protected void initialize() {
        createCloseButton();
        panelHeight = 0.4;
        createPanel();
        createCustomizableList();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(languagesManager.getString("letters"));
        customizableListYio.addItem(titleListItem);
        HColor currentColor = getViewableModel().entitiesManager.getCurrentColor();
        for (Letter letter : getViewableModel().lettersManager.mailBasket) {
            if (letter.recipientColor != currentColor) continue;
            LetterListItem letterListItem = new LetterListItem();
            customizableListYio.addItem(letterListItem);
            letterListItem.setLetter(letter);
        }
    }


    private void createPanel() {
        createDefaultPanel(panelHeight);
    }


    private void createCustomizableList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(defaultPanel)
                .setSize(0.98, panelHeight)
                .setKey("mail_inbox")
                .centerHorizontal()
                .centerVertical();
    }
}
