package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.EconomicsManager;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class QimItem implements ReusableYio {

    QuickInfoManager quickInfoManager;
    public RectangleYio incBounds;
    PointYio hookPoint;
    public RenderableTextYio title;
    public FactorYio appearFactor;
    long timeToDie;
    PointYio tempPoint;
    RepeatYio<QimItem> repeatCheckToDie;


    public QimItem(QuickInfoManager quickInfoManager) {
        this.quickInfoManager = quickInfoManager;
        incBounds = new RectangleYio();
        hookPoint = new PointYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        appearFactor = new FactorYio();
        tempPoint = new PointYio();
        initRepeats();
    }


    private void initRepeats() {
        repeatCheckToDie = new RepeatYio<QimItem>(this, 6) {
            @Override
            public void performAction() {
                parent.checkToDie();
            }
        };
    }


    @Override
    public void reset() {
        incBounds.reset();
        hookPoint.reset();
        appearFactor.reset();
        tempPoint.reset();
        timeToDie = 0;
    }


    void move() {
        appearFactor.move();
        repeatCheckToDie.move();
    }


    void checkToDie() {
        if (!appearFactor.isInAppearState()) return;
        if (System.currentTimeMillis() < timeToDie) return;
        kill();
    }


    void launch(Province province) {
        timeToDie = System.currentTimeMillis() + 2500;
        appearFactor.appear(MovementType.approach, 2.5);
        updateHookPoint(province);
        updateTitle(province);
        updateIncBounds();
    }


    void kill() {
        appearFactor.destroy(MovementType.lighty, 4.5);
    }


    private void updateHookPoint(Province province) {
        tempPoint.reset();
        for (Hex hex : province.getHexes()) {
            tempPoint.add(hex.position.center);
        }
        tempPoint.x /= province.getHexes().size();
        tempPoint.y /= province.getHexes().size();
        Hex closestHex = getViewableModel().getClosestHex(tempPoint);
        if (closestHex != null && closestHex.getProvince() == province) {
            hookPoint.setBy(tempPoint);
            return;
        }
        hookPoint.setBy(getClosestHex(tempPoint, province).position.center);
    }


    private void updateTitle(Province province) {
        String moneyString = "$" + Yio.getCompactValueString(province.getMoney());
        EconomicsManager economicsManager = getViewableModel().economicsManager;
        int profit = economicsManager.calculateProvinceProfit(province);
        String profitString = Yio.getCompactValueString(profit);
        if (profit >= 0) {
            profitString = "+" + profitString;
        }
        title.setString(moneyString + "   " + profitString);
        title.updateMetrics();

        title.position.x = hookPoint.x - title.width / 2;
        title.position.y = hookPoint.y + title.height / 2;
        title.updateBounds();
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(0.015f * GraphicsYio.width);
    }


    private Hex getClosestHex(PointYio pointYio, Province province) {
        Hex closestHex = null;
        float minDistance = 0;
        for (Hex hex : province.getHexes()) {
            float currentDistance = pointYio.fastDistanceTo(hex.position.center);
            if (closestHex == null || currentDistance < minDistance) {
                closestHex = hex;
                minDistance = currentDistance;
            }
        }
        return closestHex;
    }


    private ViewableModel getViewableModel() {
        return quickInfoManager.viewableModel;
    }
}
