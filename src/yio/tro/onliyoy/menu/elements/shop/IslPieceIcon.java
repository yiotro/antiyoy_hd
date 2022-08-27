package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;

public class IslPieceIcon {

    IslSkinItem item;
    public PieceType pieceType;
    public CircleYio position;
    public PointYio delta;
    public String key;


    public IslPieceIcon(IslSkinItem item, PieceType pieceType) {
        this.item = item;
        this.pieceType = pieceType;
        position = new CircleYio();
        position.radius = 0.03f * GraphicsYio.width;
        delta = new PointYio();
        key = item.skinType + "_" + pieceType;
    }


    void move() {
        position.center.x = item.viewPosition.x + delta.x;
        position.center.y = item.viewPosition.y + delta.y;
    }
}
