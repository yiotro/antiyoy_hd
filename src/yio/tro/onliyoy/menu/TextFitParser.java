package yio.tro.onliyoy.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.StringBuilder;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

import static yio.tro.onliyoy.Fonts.FONT_SIZE;

public class TextFitParser {

    private static TextFitParser instance = null;

    private static float horizontalOffset;
    ArrayList<Texture> texturesToDisposeInTheEnd;


    private TextFitParser() {
        texturesToDisposeInTheEnd = new ArrayList<>();
    }


    public static TextFitParser getInstance() {
        if (instance == null) {
            instance = new TextFitParser();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    private void resetHorizontalOffset(int FONT_SIZE) {
        horizontalOffset = (int) (0.3f * FONT_SIZE);
    }


    public void resetHorizontalOffset() {
        resetHorizontalOffset(FONT_SIZE);
    }


    public ArrayList<String> parseText(ArrayList<String> src, BitmapFont font, double maxWidth, boolean resetOffset) {
        if (resetOffset) {
            resetHorizontalOffset();
        }

        ArrayList<String> result = new ArrayList<>();
        double currentX, currentWidth;
        StringBuilder builder = new StringBuilder();
        for (String srcLine : src) {
            currentX = horizontalOffset;
            String[] split = srcLine.split(" ");
            for (String token : split) {
                currentWidth = GraphicsYio.getTextWidth(font, token + " ");
                if (currentX + currentWidth > maxWidth) {
                    result.add(builder.toString());
                    builder = new StringBuilder();
                    currentX = 0;
                }
                builder.append(token).append(" ");
                currentX += currentWidth;
            }
            result.add(builder.toString());
            builder = new StringBuilder();
        }
        return result;
    }


    public void disposeAllTextures() {
        for (Texture texture : texturesToDisposeInTheEnd) {
            texture.dispose();
        }

        texturesToDisposeInTheEnd.clear();
    }


    public void killInstance() {
        // this actually fixes stupid android bug
        // it's important and should not be deleted.

        // So here is the bug:
        // 1. Open app, go through menus, close app from main menu
        // 2. Open it again. Some buttons (with text) are now gray

        // It happens because android fucks up textures if they are in static objects
        // So I invalidate static instance of button renderer
        // to get them recreated after restart

        instance = null;
    }


}
