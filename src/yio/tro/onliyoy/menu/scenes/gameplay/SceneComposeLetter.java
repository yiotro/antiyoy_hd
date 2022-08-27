package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.events.EventSendLetter;
import yio.tro.onliyoy.game.core_model.events.EventsFactory;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.resizable_element.*;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;

public class SceneComposeLetter extends ModalSceneYio {

    public PlayerEntity recipient;
    public ResizableViewElement rvElement;


    @Override
    protected void initialize() {
        createCloseButton();
        createRvElement();
    }


    private void createRvElement() {
        rvElement = uiFactory.getResizableViewElement()
                .setSize(0.8, 0.01)
                .centerHorizontal()
                .setKey("letter")
                .setAnimation(AnimationYio.from_touch)
                .alignBottom(0.45);

        rvElement.addButton()
                .setSize(0.33, 0.05)
                .alignBottom(0.015)
                .alignRight(0.03)
                .setKey("send")
                .setTitle("send")
                .setReaction(getSendReaction());
    }


    private Reaction getSendReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onSendButtonPressed();
            }
        };
    }


    private void onSendButtonPressed() {
        Letter letter = createLetter();
        if (letter.conditions.size() == 0) return;
        destroy();
        ViewableModel viewableModel = getViewableModel();
        EventsFactory factory = viewableModel.eventsManager.factory;
        EventSendLetter event = factory.createEventSendLetter(letter);
        viewableModel.humanControlsManager.applyHumanEvent(event);
    }


    public Letter createLetter() {
        ViewableModel viewableModel = getViewableModel();
        Letter letter = new Letter();
        letter.setSenderColor(viewableModel.entitiesManager.getCurrentEntity().color);
        letter.setRecipientColor(recipient.color);
        for (AbstractRveItem item : rvElement.items) {
            if (!(item instanceof AbstractRveConditionItem)) continue;
            Condition condition = new Condition();
            condition.executorColor = recipient.color;
            if (((AbstractRveConditionItem) item).arrowUpMode) {
                condition.executorColor = letter.senderColor;
            }
            if (item instanceof RveMoneyItem) {
                condition.type = ConditionType.give_money;
                condition.argMoney = ((RveMoneyItem) item).value;
            }
            if (item instanceof RveRelationItem) {
                condition.type = ConditionType.change_relation;
                RveRelationItem rveRelationItem = (RveRelationItem) item;
                condition.argRelationType = rveRelationItem.relationType;
                condition.argLock = rveRelationItem.lock;
                if (rveRelationItem.playerEntity != null) {
                    condition.argColor = rveRelationItem.playerEntity.color;
                }
            }
            if (item instanceof RveLandsItem) {
                condition.type = ConditionType.give_lands;
                condition.setArgHexes(((RveLandsItem) item).hexes);
            }
            if (item instanceof RveNotificationItem) {
                condition.type = ConditionType.notification;
                condition.argRelationType = ((RveNotificationItem) item).relationType;
            }
            if (item instanceof RveSmileysItem) {
                condition.type = ConditionType.smileys;
                condition.setSmileys(((RveSmileysItem) item).generateOutputList());
            }
            letter.addCondition(condition);
        }
        return letter;
    }


    public void loadValues() {
        rvElement.clearItems();

        RveTextItem titleItem = new RveTextItem();
        titleItem.setTitle("letter");
        titleItem.setHeight(0.05);
        titleItem.setFont(Fonts.buttonFont);
        titleItem.setCentered(true);
        rvElement.addItem(titleItem);

        RveTextItem rveTextItem = new RveTextItem();
        String prefix = languagesManager.getString("recipient") + ": ";
        String name = recipient.name;
        rveTextItem.setTitle(prefix + name);
        rveTextItem.setHeight(0.035);
        rveTextItem.setCentered(true);
        rveTextItem.enableColor(rveTextItem.title.font, prefix, name, recipient.color);
        rvElement.addItem(rveTextItem);

        RveAddConditionItem rveAddConditionItem = new RveAddConditionItem();
        rveAddConditionItem.setKey("add");
        rvElement.addItem(rveAddConditionItem);

        RveEmptyItem defBlankItem = new RveEmptyItem(0.08);
        defBlankItem.setKey("def_blank");
        rvElement.addItem(defBlankItem);
    }


    public void setRecipient(PlayerEntity recipient) {
        this.recipient = recipient;
    }
}
