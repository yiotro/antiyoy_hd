package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetValues;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.net.shared.PhraseType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneChoosePhrase extends ModalSceneYio {


    private SliReaction clickReaction;
    private double h;
    private CustomizableListYio customizableListYio;
    public long lastTimePhraseSent;


    @Override
    protected void initialize() {
        lastTimePhraseSent = 0;
        h = 0.5;
        createCloseButton();
        createDarken();
        createDefaultPanel(h);
        initReactions();
        createList();
    }


    @Override
    protected void createDarken() {
        uiFactory.getDarkenElement()
                .setAllowedToAppear(getDarkenCondition())
                .setSize(1, 1);
    }


    private ConditionYio getDarkenCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return yioGdxGame.gamePaused;
            }
        };
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(defaultPanel)
                .setCornerRadius(0)
                .setSize(0.98, h - 0.02)
                .centerHorizontal()
                .centerVertical();
    }


    public boolean isAllowedToAppear() {
        return System.currentTimeMillis() > lastTimePhraseSent + NetValues.PHRASE_DELAY;
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        boolean darken = true;
        for (PhraseType phraseType : netRoot.customizationData.phrases) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setFont(Fonts.miniFont);
            scrollListItem.setTitle(languagesManager.getString("" + phraseType));
            scrollListItem.setKey("" + phraseType);
            scrollListItem.setColored(false);
            scrollListItem.setDarken(darken);
            scrollListItem.setHeight(0.057f * GraphicsYio.height);
            scrollListItem.setClickReaction(clickReaction);
            customizableListYio.addItem(scrollListItem);
            scrollListItem.checkToEnableSelfScroll();
            darken = !darken;
        }
    }


    private void initReactions() {
        clickReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                destroy();
                netRoot.sendMessage(NmType.phrase, item.key);
                lastTimePhraseSent = System.currentTimeMillis();
            }
        };
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
