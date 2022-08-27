package yio.tro.onliyoy.menu.elements.setup_entities;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.Colors;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class SingleEntityConfigureElement extends InterfaceElement<SingleEntityConfigureElement> {

    public EseItem parentItem;
    public RectangleYio parentPosition;
    public ArrayList<SecButton> buttons;
    public float cornerRadius;
    public HColor currentColor;
    protected float iSize;
    boolean touchedCurrently;
    public SecButton ptChosenButton;
    public CircleYio ptHighlight;
    public FactorYio highlightFactor;
    boolean readyToShowInternalStuff;
    private EntitiesSetupElement entitiesSetupElement;
    public FactorYio colorChangeFactor;
    public HColor previousColor;
    public SecButton clChosenButton;
    public CircleYio clHighlight;


    public SingleEntityConfigureElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        parentItem = null;
        parentPosition = new RectangleYio();
        iSize = 0.07f * GraphicsYio.height;
        ptHighlight = new CircleYio();
        ptHighlight.setRadius(0.55f * iSize);
        clHighlight = new CircleYio();
        clHighlight.setRadius(ptHighlight.radius);
        highlightFactor = new FactorYio();
        previousColor = null;
        colorChangeFactor = new FactorYio();
        initButtons();
    }


    private void initButtons() {
        buttons = new ArrayList<>();
        addButton(SecType.player_type).setEseType(EseType.human);
        addButton(SecType.player_type).setEseType(EseType.robot);
        addButton(SecType.remove);
        for (HColor color : Colors.def) {
            if (color == HColor.gray) continue;
            addButton(SecType.color).setColor(color);
        }
    }


    private SecButton addButton(SecType type) {
        SecButton secButton = new SecButton(this);
        secButton.setSecType(type);
        buttons.add(secButton);
        return secButton;
    }


    @Override
    protected SingleEntityConfigureElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        highlightFactor.move();
        colorChangeFactor.move();
        checkToShowInternalStuff();
        updateCornerRadius();
        moveButtons();
        moveHighlights();
    }


    private void checkToShowInternalStuff() {
        if (!readyToShowInternalStuff) return;
        if (getFactor().isInDestroyState()) return;
        if (getFactor().getValue() < 0.97) return;
        readyToShowInternalStuff = false;
        showInternalStuff();
    }


    private void showInternalStuff() {
        highlightFactor.appear(MovementType.approach, 2.8);
        for (SecButton secButton : buttons) {
            secButton.appearFactor.appear(MovementType.approach, 2.8);
        }
    }


    private void moveHighlights() {
        ptHighlight.center.x += 0.2 * (ptChosenButton.iconPosition.center.x - ptHighlight.center.x);
        ptHighlight.center.y += 0.2 * (ptChosenButton.iconPosition.center.y - ptHighlight.center.y);
        if (clChosenButton != null) {
            clHighlight.center.x += 0.2 * (clChosenButton.iconPosition.center.x - clHighlight.center.x);
            clHighlight.center.y += 0.2 * (clChosenButton.iconPosition.center.y - clHighlight.center.y);
        }
    }


    private void moveButtons() {
        for (SecButton secButton : buttons) {
            secButton.move();
        }
    }


    private void updateCornerRadius() {
        cornerRadius = appearFactor.getValue() * GraphicsYio.defCornerRadius;
    }


    public void applyColorChange(HColor color) {
        previousColor = currentColor;
        currentColor = color;
        colorChangeFactor.reset();
        colorChangeFactor.appear(MovementType.inertia, 1.5);
    }


    @Override
    protected void updateViewPosition() {
        if (appearFactor.getValue() == 1) {
            viewPosition.setBy(position);
            return;
        }
        viewPosition.setBy(parentPosition);
        viewPosition.x += appearFactor.getValue() * (position.x - viewPosition.x);
        viewPosition.y += appearFactor.getValue() * (position.y - viewPosition.y);
        viewPosition.width += appearFactor.getValue() * (position.width - viewPosition.width);
        viewPosition.height += appearFactor.getValue() * (position.height - viewPosition.height);
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        updateDeltas();
    }


    private void updateDeltas() {
        float offset = 0.15f * iSize;
        float r = 0.5f * iSize;
        SecButton humanButton = getButton(EseType.human);
        humanButton.delta.set(offset + r, position.height - offset - r);
        SecButton robotButton = getButton(EseType.robot);
        robotButton.delta.set(humanButton.delta.x + 2 * r, humanButton.delta.y);
        SecButton removeButton = getButton(SecType.remove);
        removeButton.delta.set(position.width - offset - r, humanButton.delta.y);

        float defX = position.width / 2 - 3 * r;
        float x = defX;
        float y = offset + r;
        int i = 0;
        for (HColor color : Colors.def) {
            if (color == HColor.gray) continue;
            SecButton colorButton = getButton(color);
            colorButton.delta.set(x, y);
            x += 2 * r;
            if (i == 3) {
                x = defX;
                y += 2 * r;
            }
            i++;
        }
    }


    private SecButton getButton(HColor color) {
        for (SecButton secButton : buttons) {
            if (secButton.secType != SecType.color) continue;
            if (secButton.color != color) continue;
            return secButton;
        }
        return null;
    }


    private SecButton getButton(SecType secType) {
        for (SecButton secButton : buttons) {
            if (secButton.secType == secType) return secButton;
        }
        return null;
    }


    private SecButton getButton(EseType eseType) {
        for (SecButton secButton : buttons) {
            if (secButton.secType != SecType.player_type) continue;
            if (secButton.eseType != eseType) continue;
            return secButton;
        }
        return null;
    }


    @Override
    public void onDestroy() {
        updateParentPosition();
        hideInternalStuff();
        applyValues();
    }


    private void hideInternalStuff() {
        highlightFactor.destroy(MovementType.lighty, 40);
        for (SecButton secButton : buttons) {
            secButton.appearFactor.destroy(MovementType.lighty, 8);
        }
    }


    private void applyValues() {
        parentItem.setType(ptChosenButton.eseType);
        if (clChosenButton != null) {
            entitiesSetupElement.applyColorChange(parentItem, clChosenButton.color);
        }
        updateSpecialAnimMode();
    }


    @Override
    public void onAppear() {
        ptChosenButton = null;
        clChosenButton = null;
        readyToShowInternalStuff = true;
        highlightFactor.reset();
        for (SecButton secButton : buttons) {
            secButton.onAppear();
        }
    }



    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    private SecButton getCurrentlyTouchedButton() {
        for (SecButton secButton : buttons) {
            if (secButton.isTouchedBy(currentTouch)) return secButton;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = viewPosition.isPointInside(currentTouch);
        if (!touchedCurrently) return false;
        selectItem();
        return true;
    }


    private void selectItem() {
        SecButton currentlyTouchedButton = getCurrentlyTouchedButton();
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
        SecButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return;
        SoundManager.playSound(SoundType.button);
        switch (currentlyTouchedButton.secType) {
            default:
                System.out.println("SingleEntityConfigureElement.onClick");
                break;
            case player_type:
                ptChosenButton = currentlyTouchedButton;
                break;
            case remove:
                onRemoveButtonClicked();
                break;
            case color:
                clChosenButton = currentlyTouchedButton;
                applyColorChange(clChosenButton.color);
                break;
        }
    }


    private void onRemoveButtonClicked() {
        Scenes.singleEntityConfigure.destroy();
        entitiesSetupElement.onItemDeletionRequested(parentItem);
        parentPosition.setBy(viewPosition);
        parentPosition.y = -0.05f * GraphicsYio.height - parentPosition.height;
    }


    public void loadValues(EntitiesSetupElement esElement, EseItem eseItem) {
        entitiesSetupElement = esElement;
        parentItem = eseItem;
        updateParentPosition();
        currentColor = eseItem.color;
        loadHighlights();
        updateSpecialAnimMode();
    }


    private void updateParentPosition() {
        parentPosition.setBy(parentItem.position);
    }


    private void updateSpecialAnimMode() {
        for (SecButton secButton : buttons) {
            secButton.specialAnimMode = false;
        }
        getButton(ptChosenButton.eseType).specialAnimMode = true;
    }


    private void loadHighlights() {
        ptChosenButton = getButton(parentItem.type);
        clChosenButton = getButton(parentItem.color);
        ptHighlight.center.set(parentPosition.x + parentPosition.width / 2, parentPosition.y + parentPosition.height / 2);
        clHighlight.center.setBy(ptHighlight.center);
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderSecElement;
    }
}
