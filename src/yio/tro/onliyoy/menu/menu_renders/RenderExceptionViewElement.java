package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.ExceptionViewElement;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.List;

public class RenderExceptionViewElement extends RenderInterfaceElement{

    private ExceptionViewElement exceptionViewElement;
    private TextureRegion backgroundTexture;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/background/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        exceptionViewElement = (ExceptionViewElement) element;

        if (exceptionViewElement.getFactor().getValue() < 0.05) return;

        GraphicsYio.drawByRectangle(batch, backgroundTexture, exceptionViewElement.getViewPosition());
        List<RenderableTextYio> viewList = exceptionViewElement.visualTextContainer.viewList;
        for (RenderableTextYio renderableTextYio : viewList) {
            if (renderableTextYio.bounds.y < exceptionViewElement.getViewPosition().y) continue;
            GraphicsYio.renderText(batch, renderableTextYio);
        }
    }


}
