package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class TmvViewItem implements ReusableYio {

    public Hex hex;
    public RenderableTextYio title;
    public RectangleYio incBounds;


    public TmvViewItem() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        incBounds = new RectangleYio();
        reset();
    }


    @Override
    public void reset() {
        hex = null;
        incBounds.reset();
    }


    public void update(Hex hex) {
        this.hex = hex;
        updateTitle();
        updateIncBounds();
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(0.015f * GraphicsYio.width);
    }


    private void updateTitle() {
        String moneyString = "$" + hex.getProvince().getMoney();
        String nameString = "";
        if (hex.coreModel.diplomacyManager.enabled) {
            nameString = hex.getProvince().getCityName() + ", ";
        }
        title.setString(nameString + moneyString);
        title.updateMetrics();
        title.position.x = hex.position.center.x - title.width / 2;
        title.position.y = hex.position.center.y + title.height / 2;
        title.updateBounds();
    }
}
