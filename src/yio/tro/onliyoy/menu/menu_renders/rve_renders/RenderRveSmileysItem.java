package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.SmileyType;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveConditionItem;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveSmileysItem;
import yio.tro.onliyoy.menu.elements.smileys.SkItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderRveSmileysItem extends AbstractRveClickableRender {

    HashMap<SmileyType, TextureRegion> mapSmileys;
    private RveSmileysItem rveSmileysItem;


    @Override
    public void loadTextures() {
        super.loadTextures();
        mapSmileys = new HashMap<>();
        for (SmileyType smileyType : SmileyType.values()) {
            String path = "menu/diplomacy/smiley_" + smileyType + ".png";
            mapSmileys.put(smileyType, GraphicsYio.loadTextureRegion(path, true));
        }
    }


    @Override
    public void renderItem(AbstractRveItem rveItem, double alpha) {
        rveSmileysItem = (RveSmileysItem) rveItem;

        renderItems(alpha);
        renderIcons((AbstractRveConditionItem) rveItem, alpha);
        renderSelection((AbstractRveConditionItem) rveItem, alpha);

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderItems(double alpha) {
        GraphicsYio.setBatchAlpha(batch, alpha);
        for (SkItem item : rveSmileysItem.items) {
            GraphicsYio.drawRectangleRotatedByCenter(
                    batch, mapSmileys.get(item.smileyType),
                    item.viewPosition.x + item.viewPosition.width / 2,
                    item.viewPosition.y + item.viewPosition.height / 2,
                    item.viewPosition.width,
                    item.viewPosition.height,
                    item.angle
            );
        }
    }
}
