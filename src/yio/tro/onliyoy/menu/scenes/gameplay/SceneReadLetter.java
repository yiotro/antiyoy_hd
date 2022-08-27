package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.Condition;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Letter;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.events.EventApplyLetter;
import yio.tro.onliyoy.game.core_model.events.EventDeclineLetter;
import yio.tro.onliyoy.game.core_model.events.EventsFactory;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.resizable_element.*;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneReadLetter extends ModalSceneYio {

    Letter letter;
    public ResizableViewElement rvElement;


    @Override
    protected void initialize() {
        createCloseButton();
        createRvElement();
    }


    private void createRvElement() {
        double rvWidth = 0.8;
        rvElement = uiFactory.getResizableViewElement()
                .setSize(rvWidth, 0.01)
                .centerHorizontal()
                .setKey("read_letter")
                .setAnimation(AnimationYio.from_touch)
                .alignBottom(0.45);

        double offset = 0.03;
        double bWidth = (rvWidth - 3 * offset) / 2;

        rvElement.addButton()
                .setSize(bWidth, 0.05)
                .alignBottom(0.015)
                .alignRight(offset)
                .setKey("agree")
                .setTitle("agree")
                .setReaction(getAgreeReaction());

        rvElement.addButton()
                .setSize(bWidth, 0.05)
                .alignBottom(0.015)
                .alignLeft(offset)
                .setTitle("decline")
                .setReaction(getDeclineReaction());
    }


    private Reaction getAgreeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onAgreeButtonPressed();
            }
        };
    }


    private void onAgreeButtonPressed() {
        ViewableModel viewableModel = getViewableModel();
        if (!viewableModel.lettersManager.isValid(letter)) {
            Scenes.notification.show("conditions_are_impracticable");
            return;
        }
        EventsFactory factory = viewableModel.eventsManager.factory;
        EventApplyLetter event = factory.createEventApplyLetter(letter.id);
        viewableModel.humanControlsManager.applyHumanEvent(event);
        destroyAndOpenInbox();
    }


    private Reaction getDeclineReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onDeclineButtonPressed();
            }
        };
    }


    private void onDeclineButtonPressed() {
        ViewableModel viewableModel = getViewableModel();
        EventsFactory factory = viewableModel.eventsManager.factory;
        EventDeclineLetter event = factory.createEventDeclineLetter(letter.id);
        viewableModel.humanControlsManager.applyHumanEvent(event);
        destroyAndOpenInbox();
    }


    public void loadValues(Letter letter) {
        this.letter = letter;
        rvElement.clearItems();

        RveTextItem titleItem = new RveTextItem();
        titleItem.setTitle("letter");
        titleItem.setHeight(0.05);
        titleItem.setFont(Fonts.buttonFont);
        titleItem.setCentered(true);
        rvElement.addItem(titleItem);

        RveTextItem rveTextItem = new RveTextItem();
        PlayerEntity sender = getViewableModel().entitiesManager.getEntity(letter.senderColor);
        String prefix = languagesManager.getString("sender") + ": ";
        String name = sender.name;
        rveTextItem.setTitle(prefix + name);
        rveTextItem.setHeight(0.035);
        rveTextItem.setCentered(true);
        rveTextItem.enableColor(rveTextItem.title.font, prefix, name, sender.color);
        rvElement.addItem(rveTextItem);

        for (Condition condition : this.letter.conditions) {
            loadCondition(condition);
        }

        RveEmptyItem defBlankItem = new RveEmptyItem(0.08);
        defBlankItem.setKey("def_blank");
        rvElement.addItem(defBlankItem);
    }


    private void loadCondition(Condition condition) {
        AbstractRveConditionItem rveConditionItem = null;
        switch (condition.type) {
            default:
                System.out.println("SceneReadLetter.loadCondition: problem");
                break;
            case give_money:
                rveConditionItem = new RveMoneyItem();
                ((RveMoneyItem) rveConditionItem).setValue(condition.argMoney);
                break;
            case give_lands:
                rveConditionItem = new RveLandsItem();
                ((RveLandsItem) rveConditionItem).setHexes(condition.argHexes);
                break;
            case change_relation:
                rveConditionItem = new RveRelationItem();
                PlayerEntity target = getViewableModel().entitiesManager.getEntity(condition.argColor);
                ((RveRelationItem) rveConditionItem).setValues(target, condition.argRelationType, condition.argLock);
                break;
            case notification:
                rveConditionItem = new RveNotificationItem();
                ((RveNotificationItem) rveConditionItem).setRelationType(condition.argRelationType);
                break;
            case smileys:
                rveConditionItem = new RveSmileysItem();
                ((RveSmileysItem) rveConditionItem).setValues(condition.smileys);
                break;
        }
        rveConditionItem.setArrowUpMode(getArrowUpMode(condition));
        rveConditionItem.enableReadMode();
        if (!getViewableModel().lettersManager.isValid(condition)) {
            rveConditionItem.setInvalid(true);
        }
        rvElement.addItem(rveConditionItem);
    }


    private boolean getArrowUpMode(Condition condition) {
        HColor currentColor = getViewableModel().entitiesManager.getCurrentColor();
        return condition.executorColor == currentColor;
    }


    public void destroyAndOpenInbox() {
        destroy();
        if (getViewableModel().lettersManager.containsLettersToCurrentEntity()) {
            Scenes.inbox.create();
        }
    }


    @Override
    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroyAndOpenInbox();
            }
        };
    }
}
