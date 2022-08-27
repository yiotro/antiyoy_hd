package yio.tro.onliyoy.menu.elements.multi_button;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.CornerEngineYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;

public class MultiButtonElement extends InterfaceElement<MultiButtonElement> {

    public CornerEngineYio cornerEngineYio;
    public ArrayList<MbLocalButton> localButtons;
    boolean touchedCurrently;
    private float touchOffset;
    Reaction targetReaction; // perform immediately if not null


    public MultiButtonElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        cornerEngineYio = new CornerEngineYio();
        localButtons = new ArrayList<>();
        touchOffset = 0.05f * GraphicsYio.width;
        targetReaction = null;
    }


    @Override
    protected MultiButtonElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        cornerEngineYio.move(viewPosition, appearFactor);
        moveLocalButtons();
    }


    private void moveLocalButtons() {
        for (MbLocalButton localButton : localButtons) {
            localButton.move();
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        targetReaction = null;
    }


    public MultiButtonElement addLocalButton(TemporaryMbeItem temporaryMbeItem) {
        return addLocalButton(temporaryMbeItem.key, temporaryMbeItem.backgroundYio, temporaryMbeItem.reaction);
    }


    public MultiButtonElement addLocalButton(String key, BackgroundYio backgroundYio, Reaction reaction) {
        MbLocalButton mbLocalButton = new MbLocalButton(this);
        mbLocalButton.setBackgroundYio(backgroundYio);
        mbLocalButton.setTitle(key);
        mbLocalButton.setReaction(reaction);
        localButtons.add(mbLocalButton);
        onLocalButtonAdded();
        return this;
    }


    private void onLocalButtonAdded() {
        float targetHeight = position.height / localButtons.size();
        for (int i = 0; i < localButtons.size(); i++) {
            MbLocalButton mbLocalButton = localButtons.get(i);
            mbLocalButton.setIndex(i);
            mbLocalButton.setTargetHeight(targetHeight);
        }
    }


    @Override
    public boolean checkToPerformAction() {
        if (targetReaction != null) {
            targetReaction.perform(menuControllerYio);
            targetReaction = null;
            return true;
        }
        return false;
    }


    @Override
    public boolean acceptsKeycode(int keycode) {
        return super.acceptsKeycode(keycode) || keycode == Input.Keys.ENTER;
    }


    private MbLocalButton getCurrentlyTouchedButton() {
        MbLocalButton currentlyTouchedButton = getCurrentlyTouchedButton(0);
        if (currentlyTouchedButton != null) return currentlyTouchedButton;
        return getCurrentlyTouchedButton(touchOffset);
    }


    private MbLocalButton getCurrentlyTouchedButton(float tOffset) {
        for (MbLocalButton localButton : localButtons) {
            if (localButton.isTouchedBy(currentTouch, tOffset)) return localButton;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = viewPosition.isPointInside(currentTouch, touchOffset);
        if (!touchedCurrently) return false;
        selectLocalButton();
        return true;
    }


    private void selectLocalButton() {
        MbLocalButton currentlyTouchedButton = getCurrentlyTouchedButton();
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
        MbLocalButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return;
        targetReaction = currentlyTouchedButton.reaction;
        SoundManager.playSound(SoundType.button);
    }


    @Override
    public void pressArtificially(int keycode) {
        if (localButtons.size() == 0) return;
        super.pressArtificially(keycode);
        int index = 0;
        if (keycode == Input.Keys.BACK) {
            index = localButtons.size() - 1;
        }
        MbLocalButton firstButton = localButtons.get(index);
        RectangleYio pos = firstButton.viewPosition;
        menuControllerYio.currentTouchPoint.set(pos.x + pos.width / 2, pos.y + pos.height / 2);
        currentTouch.setBy(menuControllerYio.currentTouchPoint);
        selectLocalButton();
        onClick();
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderMultiButtonElement;
    }
}
