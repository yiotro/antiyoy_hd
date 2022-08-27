package yio.tro.onliyoy.game.general;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.net.shared.SkinType;
import yio.tro.onliyoy.stuff.AtlasLoader;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.HashMap;

public class SkinManager {

    private static SkinManager instance;
    private SkinType skinType;
    private HashMap<PieceType, Storage3xTexture> mapPieceTextures;
    private Storage3xTexture[] farmTextures;


    public SkinManager() {
        skinType = null;
    }


    private void loadTextures() {
        // should be only triggered internally
        AtlasLoader atlasLoader = new AtlasLoader(getFolderPath(), true);
        mapPieceTextures = new HashMap<>();
        for (PieceType pieceType : PieceType.values()) {
            if (pieceType == PieceType.farm) continue;
            mapPieceTextures.put(pieceType, new Storage3xTexture(atlasLoader, pieceType + ".png"));
        }
        farmTextures = new Storage3xTexture[3];
        for (int index = 0; index < farmTextures.length; index++) {
            farmTextures[index] = new Storage3xTexture(atlasLoader, "farm" + index + ".png");
        }
    }


    private String getFolderPath() {
        switch (skinType) {
            default:
                return "game/skins/" + skinType + "/";
            case def:
                return "game/atlas/";
        }
    }


    public Storage3xTexture getPiece3xTexture(Hex hex, PieceType pieceType) {
        if (pieceType == PieceType.farm) {
            return getFarm3xTexture(hex);
        }
        return getPiece3xTexture(pieceType);
    }


    public Storage3xTexture getPiece3xTexture(PieceType pieceType) {
        if (pieceType == PieceType.farm) {
            System.out.println("SkinManager.getPiece3xTexture: use another method to render farm");
            return null;
        }
        return mapPieceTextures.get(pieceType);
    }


    public Storage3xTexture getFarm3xTexture(Hex hex) {
        return farmTextures[hex.farmDiversityIndex];
    }


    public Storage3xTexture getFarm3xTexture(int farmDiversityIndex) {
        return farmTextures[farmDiversityIndex];
    }


    public static void initialize() {
        instance = null;
    }


    public void onAppStarted() {
        setSkinType(SkinType.valueOf(getPreferences().getString("skin_type", "def")));
    }


    public static SkinManager getInstance() {
        if (instance == null) {
            instance = new SkinManager();
        }
        return instance;
    }


    public SkinType getSkinType() {
        return skinType;
    }


    public void setSkinType(SkinType skinType) {
        if (this.skinType == skinType) return;
        this.skinType = skinType;
        save();
        loadTextures();
    }


    private void save() {
        Preferences preferences = getPreferences();
        preferences.putString("skin_type", "" + skinType);
        preferences.flush();
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("yio.tro.onliyoy.skin_local");
    }
}
