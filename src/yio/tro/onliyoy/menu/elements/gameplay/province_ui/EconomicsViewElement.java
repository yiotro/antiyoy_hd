package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.EconomicsManager;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.ArrayList;

public class EconomicsViewElement extends InterfaceElement<EconomicsViewElement> {

    public CircleYio iconPosition;
    public RenderableTextYio moneyViewText;
    public RenderableTextYio profitViewText;
    boolean touchedCurrently;
    public ArrayList<EveTouchArea> touchAreas;
    boolean ready;
    EveTouchArea targetArea;
    private CircleYio profitHookCircle;


    public EconomicsViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        iconPosition = new CircleYio();
        iconPosition.setRadius(0.02f * GraphicsYio.height);
        moneyViewText = new RenderableTextYio();
        moneyViewText.setFont(Fonts.gameFont);
        profitViewText = new RenderableTextYio();
        profitViewText.setFont(Fonts.gameFont);
        profitHookCircle = new CircleYio();
        initTouchAreas();
    }


    private void initTouchAreas() {
        touchAreas = new ArrayList<>();
        EveTouchArea coinArea = new EveTouchArea(this);
        coinArea.setParentCircle(iconPosition);
        coinArea.setReaction(new Reaction() {
            @Override
            protected void apply() {
                Scenes.incomeGraph.create();
            }
        });
        touchAreas.add(coinArea);
        EveTouchArea profitArea = new EveTouchArea(this);
        profitArea.setParentCircle(profitHookCircle);
        profitArea.setReaction(new Reaction() {
            @Override
            protected void apply() {
                Scenes.profitReport.setProvince(getSelectedProvince());
                Scenes.profitReport.create();
            }
        });
        touchAreas.add(profitArea);
    }


    @Override
    protected EconomicsViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateIconPosition();
        moveMoneyViewText();
        moveProfitViewText();
        updateProfitHookCircle();
        moveTouchAreas();
    }


    private void updateProfitHookCircle() {
        profitHookCircle.center.x = profitViewText.position.x + profitViewText.width / 2;
        profitHookCircle.center.y = profitViewText.position.y - profitViewText.height / 2;
        profitHookCircle.radius = 0.72f * profitViewText.width;
    }


    private void moveTouchAreas() {
        for (EveTouchArea eveTouchArea : touchAreas) {
            eveTouchArea.move();
        }
    }


    private void moveProfitViewText() {
        profitViewText.centerHorizontal(viewPosition);
        profitViewText.position.y = iconPosition.center.y + profitViewText.height / 2;
        profitViewText.updateBounds();
    }


    private void moveMoneyViewText() {
        moneyViewText.position.x = iconPosition.center.x + iconPosition.radius + 0.01f * GraphicsYio.width;
        moneyViewText.position.y = iconPosition.center.y + moneyViewText.height / 2;
        moneyViewText.updateBounds();
    }


    private void updateIconPosition() {
        iconPosition.center.x = 0.02f * GraphicsYio.width + iconPosition.radius;
        iconPosition.center.y = viewPosition.y + viewPosition.height - 0.02f * GraphicsYio.width - iconPosition.radius;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        ready = false;
        targetArea = null;
        syncWithSelectedProvince();
    }


    public void syncWithSelectedProvince() {
        Province selectedProvince = getSelectedProvince();
        if (selectedProvince == null) return;
        if (!selectedProvince.isValid()) return;
        int money = selectedProvince.getMoney();
        moneyViewText.setString(Yio.getCompactValueString(money));
        moneyViewText.updateMetrics();
        EconomicsManager economicsManager = getViewableModel().economicsManager;
        int profit = economicsManager.calculateProvinceProfit(selectedProvince);
        String profitString = Yio.getCompactValueString(profit);
        if (profit >= 0) {
            profitString = "+" + profitString;
        }
        profitViewText.setString(profitString);
        profitViewText.updateMetrics();
    }


    private Province getSelectedProvince() {
        GameController gameController = menuControllerYio.yioGdxGame.gameController;
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        return viewableModel.provinceSelectionManager.selectedProvince;
    }


    @Override
    public boolean checkToPerformAction() {
        if (ready) {
            ready = false;
            targetArea.reaction.perform(menuControllerYio);
            return true;
        }
        return false;
    }


    EveTouchArea getCurrentlyTouchedArea() {
        for (EveTouchArea eveTouchArea : touchAreas) {
            if (!eveTouchArea.isTouchedBy(currentTouch)) continue;
            return eveTouchArea;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = (getCurrentlyTouchedArea() != null);
        if (!touchedCurrently) return false;
        checkToSelect();
        return true;
    }


    private void checkToSelect() {
        EveTouchArea currentlyTouchedArea = getCurrentlyTouchedArea();
        if (currentlyTouchedArea == null) return;
        currentlyTouchedArea.selectionEngineYio.applySelection();
    }


    @Override
    public boolean touchDrag() {
        return touchedCurrently;
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


    @Override
    public PointYio getTagPosition(String argument) {
        if (argument.equals("coin")) {
            return iconPosition.center;
        }
        return super.getTagPosition(argument);
    }


    @Override
    public boolean isTagTouched(String argument, PointYio touchPoint) {
        currentTouch.setBy(touchPoint);
        return getCurrentlyTouchedArea() != null;
    }


    private void onClick() {
        EveTouchArea currentlyTouchedArea = getCurrentlyTouchedArea();
        if (currentlyTouchedArea == null) return;
        SoundManager.playSound(SoundType.coin);
        ready = true;
        targetArea = currentlyTouchedArea;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderEconomicsViewElement;
    }
}
