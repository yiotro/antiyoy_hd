package yio.tro.onliyoy.menu.elements.replay_overlay;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.viewable_model.ReplayManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class ReplayControlElement extends InterfaceElement<ReplayControlElement> {

    final float itemTouchOffset;
    public ArrayList<RcItem> items;
    boolean touchedCurrently;


    public ReplayControlElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        itemTouchOffset = 0.05f * GraphicsYio.width;
        initItems();
    }


    private void initItems() {
        items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            items.add(new RcItem(this));
        }
        items.get(0).setType(RcItemType.stop);
        items.get(1).setType(RcItemType.play);
        items.get(2).setType(RcItemType.fast_forward);
    }


    @Override
    protected ReplayControlElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveItems();
    }


    private void moveItems() {
        for (RcItem rcItem : items) {
            rcItem.move();
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        updateSizes();
        updateDeltas();
    }


    private void updateSizes() {
        for (RcItem rcItem : items) {
            rcItem.viewPosition.radius = position.height / 2;
        }
    }


    private void updateDeltas() {
        float step = 4 * itemTouchOffset;
        float x = GraphicsYio.width / 2 - step;
        for (RcItem rcItem : items) {
            rcItem.delta.set(x, rcItem.viewPosition.radius);
            x += step;
        }
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    private RcItem getCurrentlyTouchedItem() {
        for (RcItem rcItem : items) {
            if (rcItem.isTouchedBy(currentTouch)) return rcItem;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        RcItem currentlyTouchedItem = getCurrentlyTouchedItem();
        touchedCurrently = viewPosition.isPointInside(currentTouch);
        if (currentlyTouchedItem != null) {
            touchedCurrently = true;
        }
        if (!touchedCurrently) return false;
        selectItem();
        return true;
    }


    private void selectItem() {
        RcItem currentlyTouchedItem = getCurrentlyTouchedItem();
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
        RcItem currentlyTouchedItem = getCurrentlyTouchedItem();
        if (currentlyTouchedItem == null) return;
        playClickSound(currentlyTouchedItem);
        applyButtonAction(currentlyTouchedItem);
    }


    private void playClickSound(RcItem rcItem) {
        switch (rcItem.type) {
            default:
                SoundManager.playSound(SoundType.button);
                break;
            case stop:
                SoundManager.playSound(SoundType.hold_to_march);
                break;
        }
    }


    private void applyButtonAction(RcItem rcItem) {
        ReplayManager replayManager = getGameController().objectsLayer.replayManager;
        switch (rcItem.type) {
            default:
                System.out.println("ReplayControlElement.applyButtonAction");
                break;
            case play:
                replayManager.setActive(true);
                break;
            case pause:
                replayManager.setActive(false);
                break;
            case stop:
                replayManager.onStopButtonPressed();
                break;
            case fast_forward:
                replayManager.onFastForwardButtonPressed();
                break;
        }
    }


    public void syncWithReplayManager() {
        ReplayManager replayManager = getGameController().objectsLayer.replayManager;
        RcItem ppItem = items.get(1);
        if (replayManager.active) {
            ppItem.setType(RcItemType.pause);
        } else {
            ppItem.setType(RcItemType.play);
        }
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderReplayControlElement;
    }
}
