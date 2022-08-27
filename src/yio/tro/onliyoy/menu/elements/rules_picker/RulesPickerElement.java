package yio.tro.onliyoy.menu.elements.rules_picker;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;

public class RulesPickerElement extends InterfaceElement<RulesPickerElement> {

    boolean touchedCurrently;
    public ArrayList<RpeArrow> arrows;
    private float touchOffset;
    public ArrayList<RpeItem> items;
    private int targetIndex;
    float hook;
    private float targetHook;
    public RectangleYio maskPosition;
    private float maxItemWidth;


    public RulesPickerElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        touchOffset = 0;
        targetIndex = 0;
        hook = 0;
        targetHook = 0;
        maskPosition = new RectangleYio();
        initArrows();
        initItems();
    }


    private void initItems() {
        items = new ArrayList<>();
        for (RulesType rulesType : RulesType.values()) {
            items.add(new RpeItem(this, rulesType));
        }
        updateMaxItemWidth();
    }


    private void updateMaxItemWidth() {
        maxItemWidth = 0;
        for (RpeItem rpeItem : items) {
            maxItemWidth = Math.max(maxItemWidth, rpeItem.title.width + 2 * GraphicsYio.borderThickness);
        }
    }


    private void initArrows() {
        arrows = new ArrayList<>();
        arrows.add(new RpeArrow(this, false));
        arrows.add(new RpeArrow(this, true));
    }


    @Override
    protected RulesPickerElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveArrows();
        moveItems();
        updateMaskPosition(viewPosition);
        moveHook();
    }


    private void updateMaskPosition(RectangleYio srcPos) {
//        if (!isInTransition()) return;
        maskPosition.setBy(srcPos);
        float offset = srcPos.height;
        maskPosition.width = Math.max(maxItemWidth, srcPos.width - 2 * offset);
        maskPosition.x = srcPos.x + srcPos.width / 2 - maskPosition.width / 2;
    }


    private void moveHook() {
        if (targetHook == hook) return;
        if (Math.abs(targetHook - hook) < GraphicsYio.borderThickness) {
            hook = targetHook;
            return;
        }
        hook += 0.18f * (targetHook - hook);
    }


    private void moveItems() {
        for (RpeItem rpeItem : items) {
            rpeItem.move();
        }
    }


    private void moveArrows() {
        for (RpeArrow rpeArrow : arrows) {
            rpeArrow.move();
        }
    }


    public boolean isInTransition() {
//        if (appearFactor.getValue() < 0.9) return true;
        if (hook != targetHook) return true;
        return false;
    }


    @Override
    public void onDestroy() {
        touchedCurrently = false;
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        updateDeltas();
    }


    private void updateDeltas() {
        float delta = 0;
        updateMaskPosition(position);
        float tabWidth = maskPosition.width;
        for (int i = 0; i < items.size(); i++) {
            RpeItem currentItem = items.get(i);
            currentItem.setDelta(delta);
            if (i == items.size() - 1) break;
            RpeItem nextItem = items.get(i + 1);
            float currentWidth = currentItem.title.width;
            float nextWidth = nextItem.title.width;
            float maxWidth = Math.max(currentWidth, nextWidth);
            delta += tabWidth / 2 + 0.01f * GraphicsYio.width + maxWidth / 2;
        }
        updateTargetHook();
        hook = targetHook;
    }


    @Override
    public RulesPickerElement setParent(InterfaceElement parent) {
        setSize(parent.getPosition().width / GraphicsYio.width, 0.07);
        return super.setParent(parent);
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    private RpeArrow getCurrentlyTouchedArrow() {
        for (RpeArrow rpeArrow : arrows) {
            if (rpeArrow.isTouchedBy(currentTouch, touchOffset)) return rpeArrow;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = viewPosition.isPointInside(currentTouch, touchOffset);
        if (!touchedCurrently) return false;
        RpeArrow currentlyTouchedArrow = getCurrentlyTouchedArrow();
        if (currentlyTouchedArrow != null) {
            currentlyTouchedArrow.selectionEngineYio.applySelection();
        }
        return true;
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
        RpeArrow currentlyTouchedArrow = getCurrentlyTouchedArrow();
        if (currentlyTouchedArrow == null) return;
        SoundManager.playSound(SoundType.tick);
        if (currentlyTouchedArrow.right) {
            int rightIndex = targetIndex + 1;
            if (rightIndex > items.size() - 1) return;
            setTargetIndex(rightIndex);
            showToast();
        } else {
            int leftIndex = targetIndex - 1;
            if (leftIndex < 0) return;
            setTargetIndex(leftIndex);
            showToast();
        }
    }


    private void showToast() {
        RpeItem rpeItem = items.get(targetIndex);
        String key = rpeItem.key;
        int duration = getToastDuration(rpeItem.type);
        Scenes.toast.show(key + "_description", duration);
    }


    private int getToastDuration(RulesType rulesType) {
        switch (rulesType) {
            default:
            case def:
                return 100;
            case classic:
                return 350;
            case experimental:
                return 250;
            case duel:
                return 350;
        }
    }


    public RulesPickerElement setTouchOffset(float touchOffset) {
        this.touchOffset = touchOffset;
        return this;
    }


    public void setRules(RulesType rulesType) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).type != rulesType) continue;
            setTargetIndex(i);
            return;
        }
        System.out.println("RulesPickerElement.setRules: problem");
    }


    public void setRules(String stringFromPrefs) {
        try {
            RulesType rulesType = RulesType.valueOf(stringFromPrefs);
            setRules(rulesType);
        } catch (Exception e) {
            setRules(RulesType.def);
        }
    }


    public void setTargetIndex(int targetIndex) {
        this.targetIndex = targetIndex;
        updateTargetHook();
    }


    private void updateTargetHook() {
        targetHook = -items.get(targetIndex).delta;
    }


    public RulesType getRules() {
        return items.get(targetIndex).type;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderRulesPickerElement;
    }
}
