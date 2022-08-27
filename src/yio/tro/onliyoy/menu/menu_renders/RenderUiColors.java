package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderUiColors extends RenderInterfaceElement{

    public HashMap<HColor, TextureRegion> map;


    @Override
    public void loadTextures() {
        map = new HashMap<>();
        for (HColor color : HColor.values()) {
            map.put(color, GraphicsYio.loadTextureRegion("menu/setup_entities/" + color + ".png", true));
        }
    }


    @Override
    public void render(InterfaceElement element) {
        // this class is just a storage for colors map
    }
}
