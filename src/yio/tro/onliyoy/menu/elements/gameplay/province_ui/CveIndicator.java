package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class CveIndicator implements ReusableYio {

    InterfaceElement parentElement;
    public PieceType pieceType;
    public CircleYio viewPosition;
    public FactorYio appearFactor;
    private float bottomDelta;
    public boolean alive;
    private float defRadius;
    public CveMode mode;
    public RenderableTextYio priceViewText;
    public boolean canAfford;


    public CveIndicator(InterfaceElement parentElement) {
        this.parentElement = parentElement;
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
        bottomDelta = 0.035f * GraphicsYio.height;
        defRadius = 0.08f * GraphicsYio.width;
        priceViewText = new RenderableTextYio();
        priceViewText.setFont(Fonts.miniFont);
    }


    @Override
    public void reset() {
        pieceType = null;
        alive = true;
        viewPosition.reset();
        appearFactor.reset();
        viewPosition.setRadius(defRadius);
        viewPosition.center.x = 0.5f * GraphicsYio.width;
        mode = null;
        canAfford = false;
    }


    void move() {
        appearFactor.move();
        checkToDie();
        updateViewPosition();
        updatePriceViewPosition();
    }


    private void updatePriceViewPosition() {
        priceViewText.position.x = viewPosition.center.x - priceViewText.width / 2;
        priceViewText.position.y = viewPosition.center.y - viewPosition.radius;
        priceViewText.updateBounds();
    }


    public boolean isPriceVisible() {
        if (appearFactor.isInDestroyState() && mode != CveMode.down) return false;
        return true;
    }


    private void updateViewPosition() {
        if (appearFactor.isInAppearState()) {
            viewPosition.center.y = parentElement.getViewPosition().y + bottomDelta + defRadius;
            viewPosition.center.y -= (1 - appearFactor.getValue()) * (bottomDelta + 2 * defRadius);
            return;
        }
        switch (mode) {
            default:
                System.out.println("CveIndicator.updateViewPosition: mode not set");
                break;
            case up:
                viewPosition.center.y = bottomDelta + defRadius + (1 - appearFactor.getValue()) * defRadius;
                viewPosition.radius = (0.5f + 0.5f * appearFactor.getValue()) * defRadius;
                break;
            case collapse:
                viewPosition.center.y = parentElement.getViewPosition().y + bottomDelta + defRadius;
                viewPosition.radius = appearFactor.getValue() * defRadius;
                break;
            case down:
                viewPosition.center.y = parentElement.getViewPosition().y + bottomDelta + defRadius;
                viewPosition.center.y -= (1 - appearFactor.getValue()) * (bottomDelta + 2 * defRadius);
                break;
        }
    }


    private void checkToDie() {
        if (!alive) return;
        if (appearFactor.isInAppearState()) return;
        if (appearFactor.getValue() > 0) return;
        alive = false;
    }


    public void spawn(PieceType pieceType) {
        this.pieceType = pieceType;
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 5);
        setMode(CveMode.collapse);
        updatePrice();
    }


    private void updatePrice() {
        ViewableModel viewableModel = parentElement.getViewableModel();
        Province selectedProvince = viewableModel.provinceSelectionManager.selectedProvince;
        int price = viewableModel.ruleset.getPrice(selectedProvince, pieceType);
        canAfford = selectedProvince.canAfford(pieceType);
        priceViewText.setString("$" + price);
        priceViewText.updateMetrics();
    }


    public void hide() {
        appearFactor.destroy(MovementType.lighty, 4.5);
    }


    public boolean isAlphaEnabled() {
        return mode == CveMode.up;
    }


    public void setBottomDelta(float bottomDelta) {
        this.bottomDelta = bottomDelta;
    }


    public void setMode(CveMode mode) {
        this.mode = mode;
        switch (mode) {
            case up:
                appearFactor.destroy(MovementType.lighty, 7); // speed up
                break;
        }
    }

}
