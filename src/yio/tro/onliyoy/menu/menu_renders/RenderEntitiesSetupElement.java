package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.setup_entities.EntitiesSetupElement;
import yio.tro.onliyoy.menu.elements.setup_entities.EseItem;
import yio.tro.onliyoy.menu.elements.setup_entities.EseType;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderEntitiesSetupElement extends RenderInterfaceElement{


    private EntitiesSetupElement esElement;
    HashMap<EseType, TextureRegion> mapIcons;


    @Override
    public void loadTextures() {
        mapIcons = new HashMap<>();
        for (EseType eseType : EseType.values()) {
            mapIcons.put(eseType, GraphicsYio.loadTextureRegion("menu/setup_entities/" + eseType + ".png", true));
        }
    }


    @Override
    public void render(InterfaceElement element) {
        esElement = (EntitiesSetupElement) element;

        renderTitle();
        renderItems();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderItems() {
        for (int i = esElement.items.size() - 1; i >= 0; i--) {
            EseItem item = esElement.items.get(i);
            if (item.color != null) {
                GraphicsYio.setBatchAlpha(batch, alpha);
                GraphicsYio.drawByCircle(
                        batch,
                        MenuRenders.renderUiColors.map.get(item.color),
                        item.position
                );
            }
            GraphicsYio.setBatchAlpha(batch, alpha);
            GraphicsYio.drawByCircle(batch, mapIcons.get(item.type), item.iconPosition);
            if (item.selectionEngineYio.isSelected()) {
                GraphicsYio.setBatchAlpha(batch, alpha * item.selectionEngineYio.getAlpha());
                MenuRenders.renderRoundShape.renderRoundShape(item.selectionPosition, BackgroundYio.black);
            }
        }
    }


    private void renderDebugBorder() {
        GraphicsYio.setBatchAlpha(batch, 0.2 * alpha);
        GraphicsYio.renderBorder(batch, blackPixel, esElement.getViewPosition());
    }


    private void renderTitle() {
        if (esElement.getViewPosition().width < esElement.title.width) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, esElement.title, alpha);
    }
}
