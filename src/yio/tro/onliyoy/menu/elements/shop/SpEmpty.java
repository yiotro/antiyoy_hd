package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.net.shared.NetCustomizationData;
import yio.tro.onliyoy.net.shared.NetPurchasesData;
import yio.tro.onliyoy.net.shared.NetShopData;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

public class SpEmpty extends AbstractShopPage{

    String name;
    public RenderableTextYio title;


    public SpEmpty(ShopViewElement shopViewElement) {
        super(shopViewElement);
        this.name = "Empty page";
        initTitle();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        title.setString("empty");
        title.updateMetrics();
    }


    @Override
    public String getNameKey() {
        return name;
    }


    @Override
    public ShopPageType getType() {
        return ShopPageType.empty;
    }


    @Override
    public CveColorYio getAccentColor() {
        return CveColorYio.cyan;
    }


    @Override
    void onShopDataReceived(NetShopData netShopData) {

    }


    @Override
    void loadStatuses(NetPurchasesData netPurchasesData, NetCustomizationData customizationData) {

    }


    @Override
    void onMove() {
        updateTitlePosition();
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
        title.updateBounds();
    }


    @Override
    void onTouchDown(PointYio touchPoint) {

    }


    @Override
    void onTouchDrag(PointYio touchPoint) {

    }


    @Override
    void onTouchUp(PointYio touchPoint) {

    }


    @Override
    void onClick(PointYio touchPoint) {

    }
}
