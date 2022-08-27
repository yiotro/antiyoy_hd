package yio.tro.onliyoy.menu.elements.gameplay.income_graph;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class IgeItem implements ReusableYio {

    IncomeGraphElement incomeGraphElement;
    public RectangleYio targetPosition;
    public RectangleYio viewPosition;
    PointYio delta;
    public HColor color;
    FactorYio appearFactor;
    FactorYio prepareFactor;
    float targetHeight;
    float maxHeight;
    public RenderableTextYio text;
    public FactorYio borderFactor;
    public RectangleYio borderPosition;
    public boolean scouted;


    public IgeItem(IncomeGraphElement incomeGraphElement) {
        this.incomeGraphElement = incomeGraphElement;
        targetPosition = new RectangleYio();
        delta = new PointYio();
        appearFactor = new FactorYio();
        prepareFactor = new FactorYio();
        text = new RenderableTextYio();
        text.setFont(incomeGraphElement.getFont());
        borderFactor = new FactorYio();
        borderPosition = new RectangleYio();
        viewPosition = new RectangleYio();
    }


    @Override
    public void reset() {
        targetPosition.reset();
        viewPosition.reset();
        delta.reset();
        color = null;
        appearFactor.reset();
        targetHeight = 0;
        prepareFactor.reset();
        maxHeight = 0;
        borderFactor.reset();
        borderPosition.reset();
        scouted = true;

        prepareFactor.appear(MovementType.lighty, 3);
    }


    void move() {
        movePrepareFactor();
        appearFactor.move();
        updateTargetPosition();
        updateViewPosition();
        moveText();
        moveBorder();
        updateBorderPosition();
    }


    private void updateViewPosition() {
        if (appearFactor.getValue() < 1) {
            viewPosition.setBy(targetPosition);
            return;
        }

        viewPosition.x = targetPosition.x;
        viewPosition.y = targetPosition.y;
        viewPosition.width = targetPosition.width;
        viewPosition.height += 0.2f * (targetPosition.height - viewPosition.height);
    }


    private void updateBorderPosition() {
        borderPosition.setBy(viewPosition);
        borderPosition.increase(GraphicsYio.borderThickness / 2);
    }


    public void onDestroy() {
        borderFactor.destroy(MovementType.lighty, 4);
        appearFactor.destroy(MovementType.lighty, 5);
    }


    public void updateScoutedState() {
        scouted = true;
        YioGdxGame yioGdxGame = incomeGraphElement.menuControllerYio.yioGdxGame;
        ObjectsLayer objectsLayer = yioGdxGame.gameController.objectsLayer;
        ViewableModel viewableModel = objectsLayer.viewableModel;
        if (!viewableModel.fogOfWarManager.enabled) return;
        scouted = isColorVisible(viewableModel);
    }


    private boolean isColorVisible(CoreModel coreModel) {
        for (Province province : coreModel.provincesManager.provinces) {
            if (province.getColor() != color) continue;
            if (!isProvinceVisible(province)) continue;
            return true;
        }
        return false;
    }


    private boolean isProvinceVisible(Province province) {
        for (Hex hex : province.getHexes()) {
            if (!hex.fog) return true;
        }
        return false;
    }


    private void moveBorder() {
        borderFactor.move();
        if (borderFactor.getValue() > 0) return;
        if (appearFactor.getValue() <= 0.01 || appearFactor.isInDestroyState()) return;
        borderFactor.setValues(0.01, 0);
        borderFactor.appear(MovementType.approach, 2.5);
    }


    private void moveText() {
        text.centerHorizontal(targetPosition);
        text.position.y = incomeGraphElement.getViewPosition().y + incomeGraphElement.getLowerGap() / 2 + text.height / 2;
        text.updateBounds();
    }


    public boolean isTextInsideViewPosition() {
        if (incomeGraphElement.getFactor().getValue() == 1) return true;
        RectangleYio pos = incomeGraphElement.getViewPosition();
        if (text.bounds.y < pos.y) return false;
        if (text.bounds.y + text.bounds.height > pos.y + pos.height) return false;
        return true;
    }


    public boolean isColumnInsideViewPosition() {
        if (incomeGraphElement.getFactor().getValue() == 1) return true;
        RectangleYio pos = incomeGraphElement.getViewPosition();
        if (viewPosition.x < pos.x) return false;
        if (viewPosition.x + viewPosition.width > pos.x + pos.width) return false;
        return true;
    }


    private void movePrepareFactor() {
        if (prepareFactor.getValue() == 1) return;
        prepareFactor.move();
        if (prepareFactor.getValue() == 1) {
            appearFactor.appear(MovementType.material, 2.5);
        }
    }


    public boolean isTextVisible() {
        if (incomeGraphElement.maxValueItem == null) return true;
        return incomeGraphElement.maxValueItem == this;
    }


    private void updateTargetPosition() {
        targetPosition.x = incomeGraphElement.columnsArea.x + delta.x;
        targetPosition.y = incomeGraphElement.columnsArea.y + delta.y;
        targetPosition.height = Math.min(targetHeight, appearFactor.getValue() * maxHeight);
    }


    public void setTargetHeight(float targetHeight) {
        this.targetHeight = targetHeight;
    }


    public void setColor(HColor color) {
        this.color = color;
    }


    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }

}
