package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.keyboard.NativeKeyboardElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderNativeKeyboard extends RenderInterfaceElement{

    private NativeKeyboardElement nativeKeyboardElement;
    private TextureRegion tfBackground;


    @Override
    public void loadTextures() {
        tfBackground = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        nativeKeyboardElement = (NativeKeyboardElement) element;

        renderBlackout();
        renderFrame();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderFrame() {
        if (nativeKeyboardElement.tfFactor.getValue() < 0.1) return;

        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.drawByRectangle(batch, tfBackground, nativeKeyboardElement.tfFrame);
        GraphicsYio.renderBorder(batch, blackPixel, nativeKeyboardElement.tfFrame, GraphicsYio.borderThickness);
    }


    private void renderBlackout() {
        GraphicsYio.setBatchAlpha(batch, 0.15 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, nativeKeyboardElement.blackoutPosition);
    }


}
