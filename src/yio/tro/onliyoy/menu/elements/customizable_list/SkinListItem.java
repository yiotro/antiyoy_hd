package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.shop.IslSkinItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.net.shared.SkinType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.ArrayList;

public class SkinListItem extends AbstractCustomListItem{

    public SkinType skinType;
    public boolean active;
    public boolean darken;
    public ArrayList<SliLocalPieceIcon> icons;
    public RenderableTextYio title;
    public CircleYio checkmarkPosition;


    public SkinListItem(SkinType skinType) {
        this.skinType = skinType;
        checkmarkPosition = new CircleYio();
        checkmarkPosition.setRadius(0.022f * GraphicsYio.height);
        initTitle();
        initIcons();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        String string = LanguagesManager.getInstance().getString("" + skinType);
        title.setString(Yio.getCapitalizedString(string));
        title.updateMetrics();
    }


    private void initIcons() {
        icons = new ArrayList<>();
        float x = 0.03f * GraphicsYio.width;
        float y = 0.042f * GraphicsYio.height;
        for (PieceType pieceType : IslSkinItem.getPieceTypes()) {
            SliLocalPieceIcon pieceIcon = new SliLocalPieceIcon(this, pieceType);
            x += pieceIcon.position.radius;
            pieceIcon.delta.set(x, y);
            x += pieceIcon.position.radius + 0.015f * GraphicsYio.width;
            icons.add(pieceIcon);
        }
    }


    @Override
    protected void initialize() {

    }


    @Override
    protected void move() {
        updateTitlePosition();
        updateCheckmarkPosition();
        moveIcons();
    }


    private void updateCheckmarkPosition() {
        checkmarkPosition.center.y = viewPosition.y + viewPosition.height / 2;
        checkmarkPosition.center.x = viewPosition.x + viewPosition.width - viewPosition.height / 2;
    }


    private void updateTitlePosition() {
        title.position.x = viewPosition.x + 0.04f * GraphicsYio.width;
        title.position.y = viewPosition.y + viewPosition.height - 0.03f * GraphicsYio.height;
        title.updateBounds();
    }


    private void moveIcons() {
        for (SliLocalPieceIcon sliLocalPieceIcon : icons) {
            sliLocalPieceIcon.move();
        }
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.12f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {
        if (active) return;
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof SkinListItem)) continue;
            SkinListItem skinListItem = (SkinListItem) item;
            skinListItem.setActive(false);
        }
        setActive(true);
        SkinManager.getInstance().setSkinType(skinType);
    }


    @Override
    protected void onLongTapped() {

    }


    public void setActive(boolean active) {
        this.active = active;
    }


    public void setDarken(boolean darken) {
        this.darken = darken;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderSkinListItem;
    }
}
