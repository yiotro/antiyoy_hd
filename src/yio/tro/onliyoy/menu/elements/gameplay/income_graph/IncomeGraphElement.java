package yio.tro.onliyoy.menu.elements.gameplay.income_graph;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.GameRules;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.Arrays;

public class IncomeGraphElement extends InterfaceElement<IncomeGraphElement> {

    boolean touchedCurrently;
    public RenderableTextYio title;
    public RectangleYio columnsArea;
    public RectangleYio separatorPosition;
    public ArrayList<IgeItem> items;
    ObjectPoolYio<IgeItem> poolItems;
    private int[] incomeArray;
    RepeatYio<IncomeGraphElement> repeatUpdateColumns;
    public CornerEngineYio cornerEngineYio;
    IgeItem maxValueItem;


    public IncomeGraphElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        initTitle();
        columnsArea = new RectangleYio();
        separatorPosition = new RectangleYio();
        items = new ArrayList<>();
        cornerEngineYio = new CornerEngineYio();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatUpdateColumns = new RepeatYio<IncomeGraphElement>(this, 10) {
            @Override
            public void performAction() {
                parent.updateColumnsDynamically();
            }
        };
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<IgeItem>(items) {
            @Override
            public IgeItem makeNewObject() {
                return new IgeItem(IncomeGraphElement.this);
            }
        };
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        title.setString(LanguagesManager.getInstance().getString("income"));
        title.updateMetrics();
    }


    @Override
    protected IncomeGraphElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveTitle();
        checkToMoveColumnsInRealTime();
        updateColumnsArea();
        updateSeparatorPosition();
        moveItems();
        cornerEngineYio.move(viewPosition, appearFactor);
    }


    private void checkToMoveColumnsInRealTime() {
        if (getViewableModel().entitiesManager.contains(EntityType.human)) return;
        if (getGameController().speedManager.getSpeed() == 0) return;
        repeatUpdateColumns.move();
    }


    private void updateColumnsDynamically() {
        updateIncomeArray();
        updateItemDeltas();
    }


    private void moveItems() {
        for (IgeItem item : items) {
            item.move();
        }
    }


    private void updateSeparatorPosition() {
        separatorPosition.setBy(columnsArea);
        separatorPosition.height = GraphicsYio.borderThickness;
    }


    private void updateColumnsArea() {
        columnsArea.width = 0.9f * position.width;
        columnsArea.x = viewPosition.x + viewPosition.width / 2 - columnsArea.width / 2;
        columnsArea.height = 0.72f * position.height;
        columnsArea.y = viewPosition.y + viewPosition.height / 2 - columnsArea.height / 2;
    }


    private void moveTitle() {
        title.centerHorizontal(viewPosition);
        title.position.y = (float) (viewPosition.y + viewPosition.height - 0.01f * GraphicsYio.height);
        title.updateBounds();
    }


    public boolean isSeparatorInsideViewPosition() {
        if (appearFactor.getValue() == 1) return true;
        if (separatorPosition.y < viewPosition.y) return false;
        if (separatorPosition.x < viewPosition.x) return false;
        return true;
    }


    @Override
    public void onDestroy() {
        for (IgeItem item : items) {
            item.onDestroy();
        }
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        maxValueItem = null;
        loadValues();
    }


    private void loadValues() {
        createItemsList();
        initIncomeArray();
        updateIncomeArray();
        updateItemDeltas();
    }


    private void initIncomeArray() {
        incomeArray = new int[items.size()];
    }


    private void updateItemDeltas() {
        updateColumnsArea();
        int maxIncomeValue = getMaxIncomeValue();
        float deltaX = columnsArea.width / (items.size() + 1);
        float x = deltaX / 2;
        float y = 2 * GraphicsYio.borderThickness;
        float cw = Math.min(0.09f * GraphicsYio.width, 0.9f * deltaX);
        float maxHeight = columnsArea.height - 4 * GraphicsYio.borderThickness;
        for (IgeItem item : items) {
            item.delta.x = x;
            item.delta.y = y;
            item.targetPosition.width = cw;
            item.setMaxHeight(maxHeight);
            int index = convertColorToIndex(item.color);
            item.setTargetHeight(((float)incomeArray[index] / maxIncomeValue) * maxHeight);
            item.text.setString(Yio.getCompactValueString(incomeArray[index]));
            item.text.updateMetrics();
            item.updateScoutedState();
            x += deltaX;
        }
    }


    float getLowerGap() {
        return columnsArea.y - viewPosition.y;
    }


    private int getMaxIncomeValue() {
        int maxValue = -1;
        for (int i = 0; i < incomeArray.length; i++) {
            if (maxValue == -1 || incomeArray[i] > maxValue) {
                maxValue = incomeArray[i];
            }
        }
        return maxValue;
    }


    private void createItemsList() {
        poolItems.clearExternalList();
        for (PlayerEntity entity : getViewableModel().entitiesManager.entities) {
            poolItems.getFreshObject().setColor(entity.color);
        }
    }


    private void updateIncomeArray() {
        Arrays.fill(incomeArray, 0);
        for (Province province : getViewableModel().provincesManager.provinces) {
            int value = getViewableModel().economicsManager.calculateProvinceIncome(province);
            int index = convertColorToIndex(province.getColor());
            if (index == -1) continue;
            incomeArray[index] = incomeArray[index] + value;
        }
        updateMaxValueItem();
    }


    private void updateMaxValueItem() {
        maxValueItem = null;
        if (items.size() <= 8) return;
        int maxValue = 0;
        for (IgeItem igeItem : items) {
            int index = convertColorToIndex(igeItem.color);
            if (maxValueItem == null || incomeArray[index] > maxValue) {
                maxValueItem = igeItem;
                maxValue = incomeArray[index];
            }
        }
    }


    private int convertColorToIndex(HColor color) {
        for (int i = 0; i < incomeArray.length; i++) {
            if (items.get(i).color == color) return i;
        }
        return -1;
    }


    public BitmapFont getFont() {
        return Fonts.miniFont;
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderIncomeGraphElement;
    }
}
