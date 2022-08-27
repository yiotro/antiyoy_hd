package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.IconTextYio;

public class IslAvatarItem extends AbstractIslItem{

    public AvatarType avatarType;
    public IconTextYio iconTextYio;


    public IslAvatarItem(InternalShopList internalShopList) {
        super(internalShopList);
        iconTextYio = new IconTextYio();
        iconTextYio.setFont(Fonts.gameFont);
        iconTextYio.setOffset(0.015f * GraphicsYio.width);
    }


    @Override
    protected float getHeight() {
        return 0.09f * GraphicsYio.height;
    }


    @Override
    void move() {
        super.move();
        updateIconTextPosition();
        iconTextYio.move();
    }


    private void updateIconTextPosition() {
        if (iconTextYio.iconEnabled) {
            iconTextYio.position.x = viewPosition.x + 0.02f * GraphicsYio.width;
        } else {
            iconTextYio.position.x = viewPosition.x + 0.03f * GraphicsYio.width;
        }
        iconTextYio.centerVertical(viewPosition);
        iconTextYio.onPositionChanged();
    }


    public void setAvatarType(AvatarType avatarType) {
        this.avatarType = avatarType;
        String string = LanguagesManager.getInstance().getString("" + avatarType);
        iconTextYio.setString(Yio.getCapitalizedString(string));
        iconTextYio.updateMetrics();
        iconTextYio.setupByAvatarType(avatarType);
    }

}
