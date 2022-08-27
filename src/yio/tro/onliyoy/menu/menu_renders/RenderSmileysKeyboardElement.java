package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.SmileyType;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.smileys.SkItem;
import yio.tro.onliyoy.menu.elements.smileys.SkType;
import yio.tro.onliyoy.menu.elements.smileys.SkViewField;
import yio.tro.onliyoy.menu.elements.smileys.SmileysKeyboardElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderSmileysKeyboardElement extends RenderInterfaceElement{

    SmileysKeyboardElement skElement;
    HashMap<SmileyType, TextureRegion> mapSmileys;
    private TextureRegion backgroundTexture;
    private TextureRegion sideShadowTexture;
    private TextureRegion selectionTexture;
    private TextureRegion backspaceTexture;
    private TextureRegion enterTexture;
    private TextureRegion spaceTexture;
    private TextureRegion chooseColorTexture;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/round_shape/white.png", false);
        sideShadowTexture = GraphicsYio.loadTextureRegion("menu/side_shadow.png", false);
        selectionTexture = GraphicsYio.loadTextureRegion("menu/keyboard/kb_selection.png", true);
        backspaceTexture = GraphicsYio.loadTextureRegion("menu/diplomacy/backspace_icon.png", true);
        enterTexture = GraphicsYio.loadTextureRegion("menu/diplomacy/enter_icon.png", true);
        spaceTexture = GraphicsYio.loadTextureRegion("menu/diplomacy/space_icon.png", true);
        chooseColorTexture = GraphicsYio.loadTextureRegion("menu/diplomacy/choose_color.png", true);
        mapSmileys = new HashMap<>();
        for (SmileyType smileyType : SmileyType.values()) {
            String path = "menu/diplomacy/smiley_" + smileyType + ".png";
            mapSmileys.put(smileyType, GraphicsYio.loadTextureRegion(path, true));
        }
    }


    @Override
    public void render(InterfaceElement element) {
        skElement = (SmileysKeyboardElement) element;

        renderBlackout();
        renderShadow();
        renderPanel();
        renderItems();
        renderViewField();
    }


    private void renderViewField() {
        if (skElement.getFactor().getValue() < 0.01) return;
        SkViewField skViewField = skElement.viewField;
        GraphicsYio.setBatchAlpha(batch, skElement.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(skViewField.viewPosition, skViewField.cornerEngineYio.getShadowRadius());
        GraphicsYio.setBatchAlpha(batch, alpha);
        MenuRenders.renderRoundShape.renderRoundShape(skViewField.viewPosition, BackgroundYio.white, skViewField.cornerEngineYio.getCurrentRadius());
        renderViewFieldInternals();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderViewFieldInternals() {
        if (skElement.getFactor().getValue() < 0.5) return;
        SkViewField skViewField = skElement.viewField;
        GraphicsYio.setBatchAlpha(batch, alpha);
        for (SkItem item : skViewField.items) {
            if (item.smileyType == SmileyType.space) continue;
            if (!item.isCurrentlyVisible(skViewField.viewPosition)) continue;
            GraphicsYio.drawRectangleRotatedByCenter(
                    batch, mapSmileys.get(item.smileyType),
                    item.viewPosition.x + item.viewPosition.width / 2,
                    item.viewPosition.y + item.viewPosition.height / 2,
                    item.viewPosition.width,
                    item.viewPosition.height,
                    item.angle
            );
        }
        if (skElement.getFactor().getValue() > 0.9) {
            GraphicsYio.setBatchAlpha(batch, alpha * skViewField.cursorFactor.getValue());
            GraphicsYio.drawByRectangle(batch, blackPixel, skViewField.cursorPosition);
        }
    }


    private void renderItems() {
        for (SkItem item : skElement.items) {
            GraphicsYio.setBatchAlpha(batch, alpha);
            GraphicsYio.drawByRectangle(batch, getIconTexture(item), item.viewPosition);
            renderSelection(item);
        }
    }


    private void renderSelection(SkItem item) {
        if (!item.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * item.selectionEngineYio.getAlpha());
        if (item.skType == SkType.space) {
            MenuRenders.renderRoundShape.renderRoundShape(item.touchPosition, BackgroundYio.black);
            return;
        }
        GraphicsYio.drawByRectangle(batch, selectionTexture, item.touchPosition);
    }


    private TextureRegion getIconTexture(SkItem item) {
        switch (item.skType) {
            default:
                return null;
            case smiley:
                return mapSmileys.get(item.smileyType);
            case backspace:
                return backspaceTexture;
            case enter:
                return enterTexture;
            case space:
                return spaceTexture;
            case choose_color:
                return chooseColorTexture;
        }
    }


    private void renderPanel() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, skElement.panelPosition);
    }


    private void renderShadow() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, sideShadowTexture, skElement.sideShadowPosition);
    }


    private void renderBlackout() {
        GraphicsYio.setBatchAlpha(batch, 0.33f * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, skElement.blackoutPosition);
    }
}
