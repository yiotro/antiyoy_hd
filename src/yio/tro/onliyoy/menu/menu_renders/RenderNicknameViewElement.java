package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.net.NicknameViewElement;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderNicknameViewElement extends RenderInterfaceElement{


    private NicknameViewElement nvElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        nvElement = (NicknameViewElement) element;
        renderBackground();
        renderIty();
        renderSelection();
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, 0.4f * alpha);
        MenuRenders.renderRoundShape.renderRoundShape(
                nvElement.backgroundPosition,
                BackgroundYio.light,
                0.3f * nvElement.backgroundPosition.height
        );
    }


    private void renderSelection() {
        if (!nvElement.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * nvElement.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, nvElement.touchPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderIty() {
        GraphicsYio.renderItyOptimized(
                batch,
                blackPixel,
                MenuRenders.renderAvatars.getTexture(nvElement.avatarType, nvElement.iconTextYio),
                nvElement.iconTextYio,
                alpha
        );
    }
}
