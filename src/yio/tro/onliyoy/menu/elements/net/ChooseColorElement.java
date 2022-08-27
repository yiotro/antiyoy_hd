package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.CornerEngineYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;

public class ChooseColorElement extends InterfaceElement<ChooseColorElement> {

    boolean touchedCurrently;
    public ArrayList<CcItem> items;
    boolean ready;
    CcItem targetItem;
    public CornerEngineYio cornerEngineYio;
    public RectangleYio darkenPosition;


    public ChooseColorElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        items = new ArrayList<>();
        cornerEngineYio = new CornerEngineYio();
        darkenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
    }


    @Override
    protected ChooseColorElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        cornerEngineYio.move(viewPosition, appearFactor);
        moveItems();
    }


    private void moveItems() {
        for (CcItem ccItem : items) {
            ccItem.move();
        }
    }


    @Override
    public void onDestroy() {
        touchedCurrently = false;
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        ready = false;
        targetItem = null;
    }


    public void loadValues(ArrayList<HColor> validColors) {
        items.clear();
        int rowQuantity = 8;
        float leftOffset = 0.03f * GraphicsYio.width;
        float iOffset = 0.03f * GraphicsYio.width;
        float rowWidth = position.width - 2 * leftOffset;
        float size = (rowWidth - (rowQuantity - 1) * iOffset) / rowQuantity;
        float x = leftOffset;
        float y = position.height - iOffset - size;
        for (int i = 0; i < validColors.size(); i++) {
            CcItem ccItem = new CcItem(this);
            ccItem.position.width = size;
            ccItem.position.height = size;
            ccItem.delta.set(x, y);
            ccItem.color = validColors.get(i);
            x += size + iOffset;
            if (i > 0 && (i + 1) % rowQuantity == 0) {
                x = leftOffset;
                y -= iOffset + size;
            }
            items.add(ccItem);
        }
    }


    @Override
    public boolean checkToPerformAction() {
        if (ready) {
            ready = false;
            Scenes.chooseColor.onColorChosen(targetItem.color);
            return true;
        }
        return false;
    }


    private CcItem getCurrentlyTouchedItem() {
        for (CcItem ccItem : items) {
            if (!ccItem.isTouchedBy(currentTouch)) continue;
            return ccItem;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = viewPosition.isPointInside(currentTouch);
        if (!touchedCurrently) return false;
        checkToSelect();
        return true;
    }


    private void checkToSelect() {
        CcItem currentlyTouchedItem = getCurrentlyTouchedItem();
        if (currentlyTouchedItem == null) return;
        currentlyTouchedItem.selectionEngineYio.applySelection();
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
        CcItem currentlyTouchedItem = getCurrentlyTouchedItem();
        if (currentlyTouchedItem == null) return;
        SoundManager.playSound(SoundType.button);
        ready = true;
        targetItem = currentlyTouchedItem;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderChooseColorElement;
    }
}
