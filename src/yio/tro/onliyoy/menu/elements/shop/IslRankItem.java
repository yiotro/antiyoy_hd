package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.stuff.GraphicsYio;

public class IslRankItem extends AbstractIslItem{

    public IslRankItem(InternalShopList internalShopList) {
        super(internalShopList);
    }


    @Override
    protected float getHeight() {
        return 0.09f * GraphicsYio.height;
    }
}
