package yio.tro.onliyoy.menu.elements.setup_entities;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.ArrayList;

public class CondensedEntitiesViewElement extends InterfaceElement<CondensedEntitiesViewElement> {

    public RenderableTextYio title;
    public ArrayList<CevItem> items;
    float incOffset;
    boolean readOnly;
    boolean touchedCurrently;
    CevItem targetItem;


    public CondensedEntitiesViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        items = new ArrayList<>();
        incOffset = 0.015f * GraphicsYio.width;
        readOnly = true;
        initTitle();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        title.setString(LanguagesManager.getInstance().getString("players"));
        title.updateMetrics();
    }


    @Override
    protected CondensedEntitiesViewElement getThis() {
        return this;
    }


    public void loadValues(PlayerEntity[] entities) {
        items.clear();
        for (PlayerEntity playerEntity : entities) {
            CevItem cevItem = new CevItem(this);
            cevItem.color = playerEntity.color;
            cevItem.setValues(playerEntity.type);
            items.add(cevItem);
        }
        updateDeltas();
    }


    public void applyToModel(CoreModel coreModel) {
        PlayerEntity[] entities = coreModel.entitiesManager.entities;
        for (PlayerEntity entity : entities) {
            CevItem cevItem = getItem(entity.color);
            if (cevItem == null) continue;
            if (cevItem.entityType == entity.type) continue;
            entity.type = cevItem.entityType;
        }
    }


    private CevItem getItem(HColor color) {
        for (CevItem cevItem : items) {
            if (cevItem.color != color) continue;
            return cevItem;
        }
        return null;
    }


    private void updateDeltas() {
        float x = 0;
        float cy = position.height - 0.06f * GraphicsYio.height;
        float internalDelta = incOffset;
        for (CevItem cevItem : items) {
            float incWidth = cevItem.title.width + 2 * incOffset;
            if (x + incWidth > position.width) {
                x = 0;
                cy -= 0.05f * GraphicsYio.height;
            }
            cevItem.delta.x = x + incOffset;
            cevItem.delta.y = cy + cevItem.title.height / 2;
            x += incWidth + internalDelta;
        }
    }


    @Override
    public void onMove() {
        updateTitlePosition();
        moveItems();
    }


    private void moveItems() {
        for (CevItem cevItem : items) {
            cevItem.move();
        }
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.position.y = viewPosition.y + viewPosition.height;
        title.updateBounds();
    }


    @Override
    public void onDestroy() {
        touchedCurrently = false;
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        targetItem = null;
    }


    @Override
    public boolean checkToPerformAction() {
        if (targetItem != null) {
            EntityType switchedEntityType = getSwitchedEntityType(targetItem.entityType);
            targetItem.setValues(switchedEntityType);
            updateDeltas();
            moveItems();
            targetItem = null;
            return true;
        }
        return false;
    }


    private EntityType getSwitchedEntityType(EntityType entityType) {
        if (entityType == EntityType.human) return EntityType.ai_balancer;
        return EntityType.human;
    }


    private CevItem getCurrentlyTouchedItem() {
        for (CevItem cevItem : items) {
            if (cevItem.isTouchedBy(currentTouch)) return cevItem;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = viewPosition.isPointInside(currentTouch, 0.01f * GraphicsYio.width);
        if (!touchedCurrently) return false;
        checkToSelect();
        return true;
    }


    private void checkToSelect() {
        if (readOnly) return;
        CevItem currentlyTouchedItem = getCurrentlyTouchedItem();
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
        if (readOnly) return;
        CevItem currentlyTouchedItem = getCurrentlyTouchedItem();
        if (currentlyTouchedItem == null) return;
        targetItem = currentlyTouchedItem;
        SoundManager.playSound(SoundType.button);
    }


    public CondensedEntitiesViewElement setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCondensedEntitiesViewElement;
    }
}
