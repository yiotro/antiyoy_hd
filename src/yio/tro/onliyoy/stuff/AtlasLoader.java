package yio.tro.onliyoy.stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AtlasLoader {

    String srcName, txtFileName;
    public TextureRegion atlasRegion;
    boolean antialias;
    HashMap<String, RectangleYio> mapMetrics;
    int rows;


    public AtlasLoader(String folderPath, boolean antialias) {
        this(folderPath + "atlas_texture.png", folderPath + "atlas_structure.txt", antialias);
    }


    public AtlasLoader(String srcName, String txtFileName, boolean antialias) {
        this.srcName = srcName;
        this.txtFileName = txtFileName;
        this.antialias = antialias;
        mapMetrics = new HashMap<>();
        loadEverything();
    }


    protected void loadEverything() {
        atlasRegion = GraphicsYio.loadTextureRegion(srcName, antialias);
        FileHandle fileHandle = Gdx.files.internal(txtFileName);
        String atlasStructure = fileHandle.readString();
        ArrayList<String> lines = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(atlasStructure, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (token.contains("rows=")) {
                String s = token.substring(5, token.length() - 1);
                rows = Integer.parseInt(s);
            }
            if (token.length() > 5 && !token.contains("compression=") && !token.contains("rows=")) {
                lines.add(token);
            }
        }
        mapMetrics = new HashMap<>();
        for (String line : lines) {
            applyLine(line);
        }
    }


    private void applyLine(String line) {
        int charPos = line.indexOf("#");
        String fileName = line.substring(0, charPos);
        String sizeString = line.substring(charPos + 1, line.length() - 1);
        String[] split = sizeString.split(" ");
        int x = Integer.valueOf(split[0]);
        int y = Integer.valueOf(split[1]);
        int width = Integer.valueOf(split[2]);
        int height = Integer.valueOf(split[3]);
        RectangleYio rect = new RectangleYio(x, y, width, height);
        mapMetrics.put(fileName, rect);
    }


    public TextureRegion getTexture(String fileName) {
        // atlas image size should be 2^n to avoid graphic bugs on android
        RectangleYio metrics = mapMetrics.get(fileName);
        return new TextureRegion(
                atlasRegion,
                (int) metrics.x,
                (int) metrics.y,
                (int) metrics.width,
                (int) metrics.height);
    }


    public void dispose() {
        atlasRegion.getTexture().dispose();
    }

}
