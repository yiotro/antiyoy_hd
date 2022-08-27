package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.PointYio;

public abstract class AbstractShopListPage extends AbstractShopPage{

    public InternalShopList internalShopList;


    public AbstractShopListPage(ShopViewElement shopViewElement) {
        super(shopViewElement);
        internalShopList = new InternalShopList(this);
        internalShopList.setClickReaction(new Reaction() {
            @Override
            protected void apply() {
                onClickedOnItem(internalShopList.getTargetItem());
            }
        });
    }


    public abstract ShopPageType getType();


    @Override
    void onAppear() {
        super.onAppear();
        internalShopList.onAppear();
    }


    @Override
    void onMove() {
        internalShopList.move();
    }


    abstract void onClickedOnItem(AbstractIslItem islItem);


    @Override
    void onTouchDown(PointYio touchPoint) {
        internalShopList.onTouchDown(touchPoint);
    }


    @Override
    void onTouchDrag(PointYio touchPoint) {
        internalShopList.onTouchDrag(touchPoint);
    }


    @Override
    void onTouchUp(PointYio touchPoint) {
        internalShopList.onTouchUp(touchPoint);
    }


    @Override
    public boolean onMouseWheelScrolled(int amount) {
        return internalShopList.onMouseWheelScrolled(amount);
    }


    @Override
    void onClick(PointYio touchPoint) {
        internalShopList.onClick(touchPoint);
    }
}