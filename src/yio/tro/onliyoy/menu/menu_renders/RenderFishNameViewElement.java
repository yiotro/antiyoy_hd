package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.net.FishNameViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderFishNameViewElement extends RenderInterfaceElement{


    private TextureRegion fishTexture;
    private FishNameViewElement fnvElement;


    @Override
    public void loadTextures() {
        fishTexture = GraphicsYio.loadTextureRegion("menu/shop/fish.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        fnvElement = (FishNameViewElement) element;

        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, fnvElement.name, alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, fnvElement.value, alpha);
        GraphicsYio.drawByCircle(batch, fishTexture, fnvElement.iconPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
