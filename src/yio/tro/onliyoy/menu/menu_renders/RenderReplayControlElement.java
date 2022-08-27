package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.replay_overlay.RcItem;
import yio.tro.onliyoy.menu.elements.replay_overlay.RcItemType;
import yio.tro.onliyoy.menu.elements.replay_overlay.ReplayControlElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderReplayControlElement extends RenderInterfaceElement{


    private TextureRegion backgroundTexture;
    HashMap<RcItemType, TextureRegion> mapIcons;
    private ReplayControlElement rcElement;


    @Override
    public void loadTextures() {
        backgroundTexture = loadLocalTexture("background", false);
        mapIcons = new HashMap<>();
        for (RcItemType rcItemType : RcItemType.values()) {
            mapIcons.put(rcItemType, loadLocalTexture("" + rcItemType, true));
        }
    }


    private TextureRegion loadLocalTexture(String name, boolean antialias) {
        return GraphicsYio.loadTextureRegion("menu/replay_overlay/" + name + ".png", antialias);
    }


    @Override
    public void render(InterfaceElement element) {
        rcElement = (ReplayControlElement) element;
        renderBackground();
        renderItems();
    }


    private void renderItems() {
        for (RcItem item : rcElement.items) {
            GraphicsYio.drawByCircle(
                    batch,
                    mapIcons.get(item.type),
                    item.viewPosition
            );
            if (item.selectionEngineYio.isSelected()) {
                GraphicsYio.setBatchAlpha(batch, item.selectionEngineYio.getAlpha());
                GraphicsYio.drawByRectangle(batch, blackPixel, item.selectionPosition);
                GraphicsYio.setBatchAlpha(batch, 1);
            }
        }
    }


    private void renderBackground() {
        GraphicsYio.drawByRectangle(batch, backgroundTexture, rcElement.getViewPosition());
    }
}
