package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.CornerEngineYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;

public class ResizableViewElement extends InterfaceElement<ResizableViewElement> {

    public RectangleYio dynamicPosition;
    public CornerEngineYio cornerEngineYio;
    public ArrayList<AbstractRveItem> items;
    public float targetHeight;
    public float currentHeight;
    public ArrayList<RveButton> buttons;
    boolean touchedCurrently;
    private PointYio tagPoint;


    public ResizableViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        dynamicPosition = new RectangleYio();
        cornerEngineYio = new CornerEngineYio();
        items = new ArrayList<>();
        targetHeight = 0;
        currentHeight = 0;
        buttons = new ArrayList<>();
        tagPoint = new PointYio();
    }


    @Override
    protected ResizableViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveCurrentHeight();
        updateDynamicPosition();
        moveCornerEngine();
        moveItems();
        moveButtons();
    }


    private void moveButtons() {
        for (RveButton rveButton : buttons) {
            rveButton.move();
        }
    }


    public RveButton addButton() {
        RveButton rveButton = new RveButton(this);
        buttons.add(rveButton);
        return rveButton;
    }


    private void moveCurrentHeight() {
        if (isHeightAtTarget()) return;
        if (Math.abs(currentHeight - targetHeight) < GraphicsYio.borderThickness) {
            currentHeight = targetHeight;
            return;
        }
        currentHeight += 0.2f * (targetHeight - currentHeight);
    }


    public boolean isHeightAtTarget() {
        return currentHeight == targetHeight;
    }


    private void moveItems() {
        for (AbstractRveItem rveItem : items) {
            rveItem.move();
        }
    }


    public void clearItems() {
        items.clear();
        onContentChanged();
    }


    public void clearButtons() {
        buttons.clear();
    }


    public void addItem(AbstractRveItem rveItem) {
        addItem(rveItem, items.size());
    }


    public void removeItem(AbstractRveItem rveItem) {
        items.remove(rveItem);
        onContentChanged();
    }


    public void addItem(AbstractRveItem rveItem, int index) {
        items.add(index, rveItem);
        rveItem.setResizableViewElement(this);
        rveItem.activate();
        onContentChanged();
    }


    public void swapItem(AbstractRveItem removalItem, AbstractRveItem addedItem) {
        int index = items.indexOf(removalItem);
        if (index == -1) return;
        removeItem(removalItem);
        addItem(addedItem, index);
        addedItem.activate();
    }


    public void deactivateButton(String key) {
        RveButton rveButton = getButton(key);
        if (rveButton == null) {
            System.out.println("ResizableViewElement.deactivateButton: problem");
            return;
        }
        rveButton.deactivate();
    }


    public RveButton getButton(String key) {
        for (RveButton rveButton : buttons) {
            if (key.equals(rveButton.key)) return rveButton;
        }
        return null;
    }


    public void deactivateItem(String key) {
        AbstractRveItem rveItem = getItem(key);
        if (rveItem == null) {
            System.out.println("ResizableViewElement.deactivateItem: problem");
            return;
        }
        rveItem.deactivate();
    }


    public AbstractRveItem getItem(String key) {
        for (AbstractRveItem rveItem : items) {
            if (key.equals(rveItem.key)) return rveItem;
        }
        return null;
    }


    public void onContentChanged() {
        updateDeltas();
    }


    private void updateDeltas() {
        float y = 0.01f * GraphicsYio.height;
        for (AbstractRveItem rveItem : items) {
            if (!rveItem.isActive()) continue;
            rveItem.delta.x = 0;
            rveItem.delta.y = y;
            y += rveItem.getHeight();
        }
        targetHeight = Math.max(y, 0.07f * GraphicsYio.height);
        prepareItemsForInvertedMode();
    }


    private void prepareItemsForInvertedMode() {
        for (AbstractRveItem rveItem : items) {
            rveItem.prepareDeltaForInvertedMode();
        }
    }


    private void moveCornerEngine() {
        cornerEngineYio.move(dynamicPosition, appearFactor, GraphicsYio.defCornerRadius);
    }


    private void updateDynamicPosition() {
        switch (animationType) {
            default:
                dynamicPosition.width = appearFactor.getValue() * viewPosition.width;
                dynamicPosition.x = viewPosition.x + viewPosition.width / 2 - dynamicPosition.width / 2;
                dynamicPosition.height = appearFactor.getValue() * currentHeight;
                dynamicPosition.y = viewPosition.y + viewPosition.height / 2 - dynamicPosition.height / 2;
                break;
            case from_touch:
                dynamicPosition.height = appearFactor.getValue() * currentHeight;
                dynamicPosition.y = viewPosition.y + viewPosition.height / 2 - dynamicPosition.height / 2;

                float hor = (float) (0.5 * getCurrentAnimValue() * viewPosition.width);
                float cx = viewPosition.x + 0.5f * viewPosition.width;
                tempPoint.setBy(animationPoint);
                if (appearFactor.isInAppearState()) {
                    cx -= (1 - getCurrentAnimValue()) * (cx - tempPoint.x);
                } else {
                    cx -= (1 - getCurrentAnimValue()) * (cx - 0.5f * GraphicsYio.width);
                }
                dynamicPosition.x = cx - hor;
                dynamicPosition.width = 2 * hor;
                break;
        }
    }


    RveButton getCurrentlyTouchedButton() {
        for (RveButton rveButton : buttons) {
            if (rveButton.isTouchedBy(currentTouch)) return rveButton;
        }
        return null;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        activateAllButtons();
    }


    private void activateAllButtons() {
        for (RveButton rveButton : buttons) {
            rveButton.activate();
        }
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = dynamicPosition.isPointInside(currentTouch);
        if (!touchedCurrently) return false;
        checkToSelectButton();
        for (AbstractRveItem rveItem : items) {
            if (!rveItem.isTouchedBy(currentTouch)) continue;
            if (rveItem.onTouchDown(currentTouch)) break;
        }
        return true;
    }


    private void checkToSelectButton() {
        RveButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return;
        currentlyTouchedButton.selectionEngineYio.applySelection();
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    private void onClick() {
        if (checkForButtonClickReaction()) return;
        for (AbstractRveItem rveItem : items) {
            if (!rveItem.isTouchedBy(currentTouch)) continue;
            if (rveItem.onClick(currentTouch)) break;
        }
    }


    @Override
    public void pressArtificially(int keycode) {
        if (buttons.size() == 0) return;
        super.pressArtificially(keycode);
        RveButton firstButton = buttons.get(0);
        RectangleYio pos = firstButton.position;
        menuControllerYio.currentTouchPoint.set(pos.x + pos.width / 2, pos.y + pos.height / 2);
        currentTouch.setBy(menuControllerYio.currentTouchPoint);
        checkToSelectButton();
        onClick();
    }


    @Override
    public PointYio getTagPosition(String argument) {
        if (argument == null) return super.getTagPosition(argument);
        switch (argument) {
            default:
                return super.getTagPosition(argument);
            case "add":
                AbstractRveItem addItem = getItem("add");
                if (addItem == null) return super.getTagPosition(argument);
                tagPoint.set(
                        addItem.position.x + addItem.position.width / 2,
                        addItem.position.y + addItem.position.height / 2
                );
                return tagPoint;
            case "relation":
                RveChooseConditionTypeItem chooseItem = (RveChooseConditionTypeItem) getItem("choose");
                if (chooseItem == null) return super.getTagPosition(argument);
                RveIcon icon = chooseItem.getIcon(RveIconType.relation);
                return icon.position.center;
            case "send":
                RveButton sendButton = getButton("send");
                tagPoint.set(
                        sendButton.position.x + sendButton.position.width / 2,
                        sendButton.position.y + sendButton.position.height / 2
                );
                return tagPoint;
            case "agree":
                RveButton agreeButton = getButton("agree");
                tagPoint.set(
                        agreeButton.position.x + agreeButton.position.width / 2,
                        agreeButton.position.y + agreeButton.position.height / 2
                );
                return tagPoint;
        }
    }


    @Override
    public boolean isTagTouched(String argument, PointYio touchPoint) {
        if (argument == null) return super.isTagTouched(argument, touchPoint);
        switch (argument) {
            default:
                return super.isTagTouched(argument, touchPoint);
            case "add":
                AbstractRveItem addItem = getItem("add");
                if (addItem == null) return super.isTagTouched(argument, touchPoint);
                return addItem.isTouchedBy(touchPoint);
            case "relation":
                RveChooseConditionTypeItem chooseItem = (RveChooseConditionTypeItem) getItem("choose");
                if (chooseItem == null) return super.isTagTouched(argument, touchPoint);
                RveIcon icon = chooseItem.getIcon(RveIconType.relation);
                return icon.isTouchedBy(touchPoint);
            case "send":
                RveButton sendButton = getButton("send");
                tagPoint.set(
                        sendButton.position.x + sendButton.position.width / 2,
                        sendButton.position.y + sendButton.position.height / 2
                );
                return sendButton.isTouchedBy(touchPoint);
            case "agree":
                RveButton agreeButton = getButton("agree");
                return agreeButton.isTouchedBy(touchPoint);
        }
    }


    private boolean checkForButtonClickReaction() {
        RveButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return false;
        SoundManager.playSound(SoundType.button);
        Reaction reaction = currentlyTouchedButton.reaction;
        if (reaction == null) return false;
        reaction.perform(menuControllerYio);
        return true;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderResizableViewElement;
    }
}
