package yio.tro.onliyoy.menu.elements.smileys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.SmileyType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.IColorChoiceListener;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;

public class SmileysKeyboardElement extends InterfaceElement<SmileysKeyboardElement> implements IColorChoiceListener {

    SmileyInputReaction reaction;
    public RectangleYio panelPosition;
    public RectangleYio blackoutPosition;
    public RectangleYio sideShadowPosition;
    public ArrayList<SkItem> items;
    boolean touchedCurrently;
    boolean readyToDie;
    public SkViewField viewField;
    boolean readyToProcess;
    SkItem targetItem;
    boolean deletionMode;
    private long timeToDelete;


    public SmileysKeyboardElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        reaction = null;
        initPanelPosition();
        blackoutPosition = new RectangleYio();
        sideShadowPosition = new RectangleYio();
        viewField = new SkViewField(this);
        items = new ArrayList<>();
    }


    private void addItem(SkType skType, SmileyType smileyType) {
        SkItem skItem = new SkItem();
        skItem.skType = skType;
        skItem.smileyType = smileyType;
        skItem.setRadius(getDefaultItemRadius());
        if (skType == SkType.space) {
            skItem.setRadius(1.75 * getDefaultItemRadius());
        }
        items.add(skItem);
    }


    private void initPanelPosition() {
        panelPosition = new RectangleYio();
        panelPosition.set(0, 0, GraphicsYio.width, 0.39f * GraphicsYio.width);
    }


    @Override
    protected SmileysKeyboardElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updatePanelPosition();
        updateBlackoutPosition();
        updateSideShadowPosition();
        moveItems();
        viewField.move();
        checkToEnableDeletionMode();
        moveDeletionMode();
    }


    private void moveDeletionMode() {
        if (!deletionMode) return;
        if (!touchedCurrently && !Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            deactivateDeletionMode();
        }
        if (System.currentTimeMillis() < timeToDelete) return;
        SoundManager.playSound(SoundType.kb_press);
        viewField.removeLastItem();
        timeToDelete = System.currentTimeMillis() + 17;
    }


    private void deactivateDeletionMode() {
        deletionMode = false;
        getItem(SkType.backspace).selectionEngineYio.factorYio.reset();
    }


    private void checkToEnableDeletionMode() {
        if (deletionMode) return;
        SkItem backspaceItem = getItem(SkType.backspace);
        if (!backspaceItem.selectionEngineYio.isSelected()) return;
        if (System.currentTimeMillis() < backspaceItem.selectionTime + 500) return;
        if (getCurrentlyTouchedItem() != backspaceItem) return;
        deletionMode = true;
    }


    private void moveItems() {
        for (SkItem skItem : items) {
            skItem.move(panelPosition, touchedCurrently);
        }
    }


    private void updateSideShadowPosition() {
        sideShadowPosition.set(
                0, panelPosition.y + panelPosition.height - 0.035f * GraphicsYio.height,
                GraphicsYio.width, 0.045f * GraphicsYio.height
        );
    }


    private void updateBlackoutPosition() {
        blackoutPosition.x = 0;
        blackoutPosition.width = GraphicsYio.width;
        blackoutPosition.y = panelPosition.y + panelPosition.height;
        blackoutPosition.height = GraphicsYio.height - blackoutPosition.y;
    }


    private void updatePanelPosition() {
        panelPosition.y = -1.05f * (1 - appearFactor.getValue()) * panelPosition.height;
    }


    @Override
    public void onDestroy() {
        touchedCurrently = false;
    }


    @Override
    public void onAppear() {
        initItems();
        updateItemDeltas();
        touchedCurrently = false;
        viewField.onAppear();
        readyToDie = false;
        readyToProcess = false;
        deletionMode = false;
    }


    private void initItems() {
        items.clear();
        // first row
        addItem(SkType.smiley, SmileyType.one);
        addItem(SkType.smiley, SmileyType.two);
        addItem(SkType.smiley, SmileyType.three);
        addItem(SkType.smiley, SmileyType.four);
        addItem(SkType.smiley, SmileyType.five);
        addItem(SkType.smiley, SmileyType.six);
        addItem(SkType.smiley, SmileyType.seven);
        addItem(SkType.smiley, SmileyType.eight);
        addItem(SkType.smiley, SmileyType.nine);
        addItem(SkType.smiley, SmileyType.zero);
        addItem(SkType.backspace, null);
        // second row
        addItem(SkType.smiley, SmileyType.face_happy);
        addItem(SkType.smiley, SmileyType.face_neutral);
        addItem(SkType.smiley, SmileyType.face_surprised);
        addItem(SkType.smiley, SmileyType.face_scary);
        addItem(SkType.smiley, SmileyType.heart);
        addItem(SkType.smiley, SmileyType.skull);
        addItem(SkType.smiley, SmileyType.finger_up);
        addItem(SkType.smiley, SmileyType.finger_down);
        addItem(SkType.smiley, SmileyType.hex);
        addItem(SkType.smiley, SmileyType.swords);
        // third row
        addItem(SkType.smiley, SmileyType.plus);
        addItem(SkType.smiley, SmileyType.minus);
        addItem(SkType.smiley, SmileyType.equals);
        addItem(SkType.smiley, SmileyType.question);
        addItem(SkType.smiley, SmileyType.exclamation);
        addItem(SkType.smiley, SmileyType.dollar);
        addItem(SkType.smiley, SmileyType.arrow_left);
        addItem(SkType.smiley, SmileyType.arrow_right);
        addItem(SkType.smiley, SmileyType.hypno);
        // fourth row
        addItem(SkType.choose_color, null);
        addItem(SkType.enter, null);
        addItem(SkType.space, null); // should be last to avoid problems with touch
    }


    private void updateItemDeltas() {
        makeRow(0, SmileyType.one, 11);
        makeRow(1, SmileyType.face_happy, 10);
        makeRow(2, SmileyType.plus, 9);
        makeBottomRow();
    }


    private void makeBottomRow() {
        float r = getDefaultItemRadius();
        float y = panelPosition.height - r - 2 * r * 3;
        getItem(SkType.enter).delta.set(GraphicsYio.width - r, y);
        getItem(SkType.choose_color).delta.set(r, y);
        getItem(SkType.space).delta.set(GraphicsYio.width / 2, y);
    }


    private void makeRow(int rowIndex, SmileyType firstSmileyType, int length) {
        float r = getDefaultItemRadius();
        float w = 2 * r * length;
        float x = GraphicsYio.width / 2 - w / 2;
        float y = panelPosition.height - r - 2 * r * rowIndex;
        int startIndex = items.indexOf(getItem(firstSmileyType));
        for (int i = startIndex; i < startIndex + length; i++) {
            items.get(i).delta.set(x + r, y);
            x += 2 * r;
        }
    }


    private SkItem getItem(SmileyType smileyType) {
        for (SkItem skItem : items) {
            if (skItem.smileyType == smileyType) return skItem;
        }
        return null;
    }


    private SkItem getItem(SkType skType) {
        for (SkItem skItem : items) {
            if (skItem.skType == skType) return skItem;
        }
        return null;
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToDie) {
            readyToDie = false;
            Scenes.setupSmileysCondition.destroy();
            return true;
        }
        if (readyToProcess) {
            readyToProcess = false;
            processItem(targetItem);
            return true;
        }
        return false;
    }


    private void processItem(SkItem skItem) {
        switch (skItem.skType) {
            default:
                System.out.println("SmileysKeyboardElement.processItem: problem");
                break;
            case enter:
                if (reaction != null) {
                    reaction.onSmileyInputReceived(generateInput());
                }
                Scenes.setupSmileysCondition.destroy();
                break;
            case backspace:
                viewField.removeLastItem();
                break;
            case smiley:
                viewField.addItem(skItem.smileyType);
                break;
            case space:
                viewField.addItem(SmileyType.space);
                break;
            case choose_color:
                Scenes.chooseColor.create();
                Scenes.chooseColor.loadValues(getViewableModel());
                Scenes.chooseColor.setListener(this);
                break;
        }
    }


    @Override
    public void onColorChosen(HColor color) {
        viewField.addItem(convertColor(color));
    }


    private SmileyType convertColor(HColor color) {
        switch (color) {
            default:
                return null;
            case yellow:
                return SmileyType.yellow;
            case green:
                return SmileyType.green;
            case aqua:
                return SmileyType.aqua;
            case cyan:
                return SmileyType.cyan;
            case blue:
                return SmileyType.blue;
            case purple:
                return SmileyType.purple;
            case red:
                return SmileyType.red;
            case brown:
                return SmileyType.brown;
            case mint:
                return SmileyType.mint;
            case lavender:
                return SmileyType.lavender;
            case brass:
                return SmileyType.brass;
            case ice:
                return SmileyType.ice;
            case rose:
                return SmileyType.rose;
            case algae:
                return SmileyType.algae;
            case orchid:
                return SmileyType.orchid;
            case whiskey:
                return SmileyType.whiskey;
        }
    }


    private ArrayList<SmileyType> generateInput() {
        ArrayList<SmileyType> list = new ArrayList<>();
        for (SkItem skItem : viewField.items) {
            list.add(skItem.smileyType);
        }
        return list;
    }


    private SkItem getCurrentlyTouchedItem() {
        for (SkItem skItem : items) {
            if (skItem.isTouchedBy(currentTouch, 0)) return skItem;
        }
        for (SkItem skItem : items) {
            if (skItem.isTouchedBy(currentTouch, 0.03f * GraphicsYio.width)) return skItem;
        }
        return null;
    }


    private SkItem getClosestTouchedItem() {
        SkItem closestItem = null;
        float minDistance = 0;
        for (SkItem skItem : items) {
            float currentDistance = (float) skItem.viewPosition.distanceTo(currentTouch);
            if (closestItem == null || currentDistance < minDistance) {
                closestItem = skItem;
                minDistance = currentDistance;
            }
        }
        if (closestItem == null) return null;
        if (!closestItem.isTouchedBy(currentTouch, 0.02f * GraphicsYio.width)) return null;
        return closestItem;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = true;
        SkItem currentlyTouchedItem = getClosestTouchedItem();
        if (currentlyTouchedItem != null) {
            currentlyTouchedItem.applySelection();
            SoundManager.playSound(SoundType.kb_press);
            readyToProcess = true;
            targetItem = currentlyTouchedItem;
            return true;
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
        deletionMode = false;
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    private void onClick() {
        if (isCurrentTouchOutside()) {
            readyToDie = true;
            return;
        }
    }


    private boolean isCurrentTouchOutside() {
        if (panelPosition.isPointInside(currentTouch)) return false;
        return true;
    }


    public SmileysKeyboardElement setReaction(SmileyInputReaction reaction) {
        this.reaction = reaction;
        return this;
    }


    private float getDefaultItemRadius() {
        return GraphicsYio.width / 22f;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderSmileysKeyboardElement;
    }
}
