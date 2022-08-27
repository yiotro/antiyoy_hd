package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.net.shared.SkinType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class IslSkinItem extends AbstractIslItem{

    SkinType skinType;
    public ArrayList<IslPieceIcon> icons;


    public IslSkinItem(InternalShopList internalShopList, SkinType skinType) {
        super(internalShopList);
        this.skinType = skinType;
        initIcons();
    }


    private void initIcons() {
        icons = new ArrayList<>();
        float x = 0.03f * GraphicsYio.width;
        float y = 0.042f * GraphicsYio.height;
        for (PieceType pieceType : getPieceTypes()) {
            IslPieceIcon pieceIcon = new IslPieceIcon(this, pieceType);
            x += pieceIcon.position.radius;
            pieceIcon.delta.set(x, y);
            x += pieceIcon.position.radius + 0.015f * GraphicsYio.width;
            icons.add(pieceIcon);
        }
    }


    public static PieceType[] getPieceTypes() {
        return new PieceType[]{
                PieceType.peasant,
                PieceType.spearman,
                PieceType.baron,
                PieceType.farm,
                PieceType.palm,
                PieceType.tower,
        };
    }


    @Override
    void move() {
        super.move();
        for (IslPieceIcon islPieceIcon : icons) {
            islPieceIcon.move();
        }
    }


    @Override
    protected void updateNameTextPosition() {
        nameViewText.position.x = viewPosition.x + 0.04f * GraphicsYio.width;
        nameViewText.position.y = viewPosition.y + viewPosition.height - 0.03f * GraphicsYio.height;
        nameViewText.updateBounds();
    }


    @Override
    protected float getHeight() {
        return 0.12f * GraphicsYio.height;
    }


    public void setSkinType(SkinType skinType) {
        this.skinType = skinType;
    }
}
