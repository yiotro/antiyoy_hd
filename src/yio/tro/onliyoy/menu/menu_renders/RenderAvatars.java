package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.IconTextYio;

import java.util.HashMap;

public class RenderAvatars extends RenderInterfaceElement{

    private HashMap<AvatarType, TextureRegion> map;
    private TextureRegion[] diceTextures;
    private TextureRegion[] faceTextures;


    @Override
    public void loadTextures() {
        map = new HashMap<>();
        for (AvatarType avatarType : AvatarType.values()) {
            if (!hasDirectTexture(avatarType)) continue;
            String path = "menu/avatar/avatar_" + avatarType + ".png";
            TextureRegion textureRegion = GraphicsYio.loadTextureRegion(path, true);
            map.put(avatarType, textureRegion);
        }
        diceTextures = new TextureRegion[6];
        loadTexturesArray(diceTextures, AvatarType.dice);
        faceTextures = new TextureRegion[4];
        loadTexturesArray(faceTextures, AvatarType.face);
    }


    private boolean hasDirectTexture(AvatarType avatarType) {
        switch (avatarType) {
            default:
                return true;
            case empty:
            case dice:
            case face:
                return false;
        }
    }


    private void loadTexturesArray(TextureRegion[] array, AvatarType avatarType) {
        for (int i = 0; i < array.length; i++) {
            String path = "menu/avatar/avatar_" + avatarType + (i + 1) + ".png";
            array[i] = GraphicsYio.loadTextureRegion(path, true);
        }
    }


    public TextureRegion getTexture(AvatarType avatarType, IconTextYio iconTextYio) {
        switch (avatarType) {
            default:
                return map.get(avatarType);
            case empty:
                return null;
            case dice:
                return diceTextures[iconTextYio.diceIndex - 1];
            case face:
                return faceTextures[iconTextYio.diceIndex - 1];
        }
    }


    @Override
    public void render(InterfaceElement element) {

    }
}
