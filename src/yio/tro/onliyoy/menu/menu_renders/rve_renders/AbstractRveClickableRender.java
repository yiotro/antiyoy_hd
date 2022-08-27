package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.resizable_element.*;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

import java.util.HashMap;

public abstract class AbstractRveClickableRender extends AbstractRveRender{

    HashMap<RveIconType, TextureRegion> mapIconTextures;
    CircleYio tempCircle;
    protected TextureRegion redPixel;


    public AbstractRveClickableRender() {
        tempCircle = new CircleYio();
    }


    public void loadTextures() {
        mapIconTextures = new HashMap<>();
        for (RveIconType iconType : RveIconType.values()) {
            mapIconTextures.put(iconType, GraphicsYio.loadTextureRegion("menu/diplomacy/" + iconType + ".png", true));
        }
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
    }


    public abstract void renderItem(AbstractRveItem rveItem, double alpha);


    protected void renderIcons(AbstractRveClickableItem rveClickableItem, double alpha) {
        for (RveIcon rveIcon : rveClickableItem.icons) {
            GraphicsYio.setBatchAlpha(batch, alpha);
            GraphicsYio.drawByCircle(
                    batch,
                    mapIconTextures.get(rveIcon.type),
                    rveIcon.position
            );
            SelectionEngineYio selectionEngineYio = rveIcon.selectionEngineYio;
            if (selectionEngineYio.isSelected()) {
                tempCircle.setBy(rveIcon.position);
                tempCircle.setAngle(0);
                GraphicsYio.setBatchAlpha(batch, alpha * selectionEngineYio.getAlpha());
                GraphicsYio.drawByCircle(batch, blackPixel, tempCircle);
                GraphicsYio.setBatchAlpha(batch, alpha);
            }
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    protected void renderSelection(AbstractRveConditionItem rveConditionItem, double alpha) {
        SelectionEngineYio selectionEngineYio = rveConditionItem.selectionEngineYio;
        if (!selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, rveConditionItem.position);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    protected void renderTitle(AbstractRveConditionItem rveConditionItem, double alpha) {
        if (rveConditionItem.invalid) {
            BitmapFont font = rveConditionItem.title.font;
            font.setColor(0.56f, 0.2f, 0.2f, 1);
            GraphicsYio.renderTextOptimized(batch, redPixel, rveConditionItem.title, (float) alpha);
            font.setColor(Color.BLACK);
            return;
        }
        GraphicsYio.renderTextOptimized(batch, blackPixel, rveConditionItem.title, (float) alpha);
    }

}
