package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.stuff.GraphicsYio;

public class IslPhraseItem extends AbstractIslItem{


    public IslPhraseItem(InternalShopList internalShopList) {
        super(internalShopList);
    }


    @Override
    protected float getHeight() {
        return 0.09f * GraphicsYio.height;
    }
}
