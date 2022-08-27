package yio.tro.onliyoy.menu.elements.setup_entities;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.Colors;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class EntitiesSetupElement extends InterfaceElement<EntitiesSetupElement> {

    public static final int MAX_ALLOWED_SIZE = 8;
    public RenderableTextYio title;
    protected float iSize;
    protected int columnsQuantity;
    public ArrayList<EseItem> items;
    ObjectPoolYio<EseItem> poolItems;
    private StringBuilder stringBuilder;
    boolean touchedCurrently;
    PointYio tempPoint;
    boolean calmAnimationMode;
    boolean titleCentered;


    public EntitiesSetupElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        columnsQuantity = 4;
        items = new ArrayList<>();
        tempPoint = new PointYio();
        stringBuilder = new StringBuilder();
        calmAnimationMode = false;
        titleCentered = false;
        initTitle();
        initPools();
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<EseItem>(items) {
            @Override
            public EseItem makeNewObject() {
                return new EseItem(EntitiesSetupElement.this);
            }
        };
    }


    private void updateISize() {
        iSize = (position.height - title.height - 0.012f * GraphicsYio.height) / 2;
    }


    public void clear() {
        poolItems.clearExternalList();
    }


    public EseItem addItem(EseType type, HColor color) {
        if (!canFitMoreItems()) {
            System.out.println("EntitiesSetupElement.addItem: problem");
        }
        EseItem freshObject = poolItems.getFreshObject();
        freshObject.setColor(color);
        freshObject.setType(type);
        checkToRemovePlusItem();
        checkToRelocatePlusItemToTheEnd();
        updateDeltas();
        return freshObject;
    }


    private void checkToRelocatePlusItemToTheEnd() {
        if (isPlusItemInTheEnd()) return;
        EseItem plusItem = getItem(EseType.plus);
        if (plusItem == null) return;
        // important, in other cases use pool to add or remove
        items.remove(plusItem);
        items.add(plusItem);
    }


    private void checkToRemovePlusItem() {
        if (canFitMoreItems()) return;
        removePlusItem();
    }


    private boolean isPlusItemInTheEnd() {
        EseItem plusItem = getItem(EseType.plus);
        if (plusItem == null) return false;
        return items.indexOf(plusItem) == items.size() - 1;
    }


    private boolean canFitMoreItems() {
        return countColoredItems() < MAX_ALLOWED_SIZE;
    }


    public int countColoredItems() {
        int c = 0;
        for (EseItem item : items) {
            if (item.color == null) continue;
            c++;
        }
        return c;
    }


    private void removePlusItem() {
        EseItem plusItem = getItem(EseType.plus);
        if (plusItem == null) return;
        poolItems.removeFromExternalList(plusItem);
    }


    private EseItem getItem(EseType eseType) {
        for (EseItem item : items) {
            if (item.type == eseType) return item;
        }
        return null;
    }


    private EseItem getItem(HColor color) {
        for (EseItem item : items) {
            if (item.color == color) return item;
        }
        return null;
    }


    private void updateDeltas() {
        float columnLength = iSize * columnsQuantity;
        float defX = position.width / 2 - columnLength / 2 + iSize / 2;
        float x = defX;
        float y = 1.5f * iSize;
        for (int i = 0; i < items.size(); i++) {
            EseItem eseItem = items.get(i);
            eseItem.targetDelta.set(x, y);
            x += iSize;
            if (i == 3) {
                x = defX;
                y -= iSize;
            }
        }
        checkToPlacePlusInTheCorner();
    }


    private void checkToPlacePlusInTheCorner() {
        EseItem plusItem = getItem(EseType.plus);
        if (plusItem == null) return;
        float columnLength = iSize * columnsQuantity;
        plusItem.targetDelta.set(
                position.width / 2 + columnLength / 2 - iSize / 2,
                0.5 * iSize
        );
        plusItem.forceDelta();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        title.setString(LanguagesManager.getInstance().getString("players"));
        title.updateMetrics();
    }


    @Override
    protected EntitiesSetupElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateTitlePosition();
        moveItems();
    }


    private void moveItems() {
        for (EseItem eseItem : items) {
            eseItem.move();
        }
    }


    private void updateTitlePosition() {
        if (titleCentered) {
            title.centerHorizontal(viewPosition);
        } else {
            title.position.x = viewPosition.x;
        }
        title.position.y = viewPosition.y + viewPosition.height;
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        checkToAddPlusItem();
        forceDeltas();
    }


    private void forceDeltas() {
        for (EseItem item : items) {
            item.forceDelta();
        }
    }


    private void checkToAddPlusItem() {
        if (getItem(EseType.plus) != null) return;
        if (!canFitMoreItems()) return;
        if (!touchable) return;
        addItem(EseType.plus, null);
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        updateISize();
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    private EseItem getCurrentlyTouchedItem() {
        for (EseItem eseItem : items) {
            if (eseItem.isTouchedBy(currentTouch)) return eseItem;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        if (!touchable) return false;
        touchedCurrently = viewPosition.isPointInside(currentTouch);
        if (!touchedCurrently) return false;
        selectItem();
        return true;
    }


    private void selectItem() {
        EseItem currentlyTouchedItem = getCurrentlyTouchedItem();
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


    private void onClickedOnItem(EseItem eseItem) {
        SoundManager.playSound(SoundType.button);
        switch (eseItem.type) {
            default:
                float y = (position.y + parent.getPosition().y) / GraphicsYio.height;
                float bottomOffset = Math.max(0.02f, y - 0.08f);
                Scenes.singleEntityConfigure.setBottomOffset(bottomOffset);
                Scenes.singleEntityConfigure.create();
                Scenes.singleEntityConfigure.setItem(this, eseItem);
                break;
            case plus:
                onPlusClicked();
                break;
        }
    }


    public void onItemDeletionRequested(EseItem eseItem) {
        poolItems.removeFromExternalList(eseItem);
        checkToAddPlusItem();
        updateDeltas();
    }


    public void applyColorChange(EseItem eseItem, HColor targetColor) {
        EseItem item = getItem(targetColor);
        if (item != null) {
            item.setColor(eseItem.color);
        }
        eseItem.setColor(targetColor);
    }


    public void onPlusClicked() {
        tempPoint.setBy(getItem(EseType.plus).targetDelta);
        EseItem lastColoredItem = getLastColoredItem();
        if (lastColoredItem != null) {
            tempPoint.setBy(lastColoredItem.targetDelta);
        }
        EseItem eseItem = addItem(EseType.robot, getColorForNewItem());
        checkToRelocatePlusItemToTheEnd();
        updateDeltas();
        eseItem.delta.setBy(tempPoint);
    }


    private EseItem getLastColoredItem() {
        for (int i = items.size() - 1; i >= 0; i--) {
            EseItem eseItem = items.get(i);
            if (eseItem.color == null) continue;
            return eseItem;
        }
        return null;
    }


    private HColor getColorForNewItem() {
        for (HColor color : Colors.def) {
            if (getItem(color) != null) continue;
            return color;
        }
        return null;
    }


    public void decode(String source, boolean net) {
        clear();
        for (String token : source.split(",")) {
            String[] split = token.split(" ");
            if (split.length < 2) continue;
            EseType eseType = EseType.valueOf(split[0]);
            HColor color = HColor.valueOf(split[1]);
            addItem(eseType, color);
        }

        if (countColoredItems() == 0) {
            if (net) {
                addItemsForOnlineMatch();
            } else {
                addDefaultItems();
            }
        }
    }


    public void addDefaultItems() {
        addItem(EseType.human, HColor.green);
        addItem(EseType.robot, HColor.red);
    }


    public void addItemsForOnlineMatch() {
        addItem(EseType.human, HColor.green);
        addItem(EseType.human, HColor.red);
        addItem(EseType.human, HColor.yellow);
        addItem(EseType.human, HColor.blue);
    }


    public String encode() {
        stringBuilder.setLength(0); // clear
        for (EseItem eseItem : items) {
            if (eseItem.color == null) continue; // plus item
            stringBuilder.append(eseItem.encode()).append(",");
        }
        return stringBuilder.toString();
    }


    public void copyFrom(PlayerEntity[] source) {
        clear();
        for (int i = 0; i < source.length; i++) {
            EseType eseType = EseType.human;
            if (source[i].isArtificialIntelligence()) {
                eseType = EseType.robot;
            }
            HColor color = source[i].color;
            addItem(eseType, color);
        }
    }


    public void applyToModel(CoreModel coreModel) {
        coreModel.entitiesManager.initialize(encode());
    }


    public EntitiesSetupElement setCalmAnimationMode(boolean calmAnimationMode) {
        this.calmAnimationMode = calmAnimationMode;
        return this;
    }


    public EntitiesSetupElement setTitleCentered(boolean titleCentered) {
        this.titleCentered = titleCentered;
        return this;
    }


    private void onClick() {
        EseItem currentlyTouchedItem = getCurrentlyTouchedItem();
        if (currentlyTouchedItem == null) return;
        onClickedOnItem(currentlyTouchedItem);
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("EntitiesSetupElement.showInConsole");
        for (EseItem eseItem : items) {
            System.out.println("- " + eseItem.type + " " + eseItem.color);
        }
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderEntitiesSetupElement;
    }
}
