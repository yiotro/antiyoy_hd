package yio.tro.onliyoy.menu.elements.button;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.keyboard.CbButton;
import yio.tro.onliyoy.menu.elements.keyboard.CbPage;
import yio.tro.onliyoy.menu.elements.keyboard.CbType;
import yio.tro.onliyoy.menu.elements.keyboard.CustomKeyboardElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.HashMap;

public class CrKeyboard extends AbstractCacheRender{

    CbPage page;
    private TextureRegion backgroundTexture;
    HashMap<CbType, TextureRegion> mapIcons;
    private CustomKeyboardElement cbElement;
    RectangleYio tempRectangle;


    public CrKeyboard() {
        super();
        tempRectangle = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/round_shape/white.png", false);
        mapIcons = new HashMap<>();
        for (CbType cbType : CbType.values()) {
            if (cbType == CbType.normal) continue;
            mapIcons.put(cbType, loadIconTexture(cbType));
        }
    }


    private TextureRegion loadIconTexture(CbType cbType) {
        return GraphicsYio.loadTextureRegion("menu/keyboard/" + cbType + "_icon.png", true);
    }


    @Override
    public void setTarget(Object object) {
        page = (CbPage) object;
        cbElement = page.customKeyboardElement;
        framePosition.set(0, 0, GraphicsYio.width, cbElement.panelPosition.height);
    }


    @Override
    protected void render(SpriteBatch batch) {
        GraphicsYio.drawByRectangle(batch, backgroundTexture, framePosition);
        for (CbButton button : page.buttons) {
            tempRectangle.setBy(button.position);
            tempRectangle.x = framePosition.x + button.delta.x - button.position.width / 2;
            tempRectangle.y = framePosition.y + button.delta.y - button.position.height / 2;
            button.title.centerHorizontal(tempRectangle);
            button.title.centerVertical(tempRectangle);
            if (button.type == CbType.normal) {
                GraphicsYio.renderText(batch, button.title);
            } else {
                GraphicsYio.drawByRectangle(batch, mapIcons.get(button.type), tempRectangle);
            }
        }
    }
}
