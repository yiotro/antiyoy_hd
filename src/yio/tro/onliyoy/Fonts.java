package yio.tro.onliyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import yio.tro.onliyoy.menu.CustomLanguageLoader;
import yio.tro.onliyoy.menu.LanguagesManager;

public class Fonts {

    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<^>∞";
    // йцукенгшщзхъёфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮіІїЇєЄ

    public static int FONT_SIZE;
    public static BitmapFont buttonFont;
    public static BitmapFont gameFont;
    public static BitmapFont miniFont;


    public static void init() {
        // language should be loaded before fonts are created
        // because of lang_chars
        CustomLanguageLoader.loadLanguage();

        FileHandle fontFile = Gdx.files.internal("font.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FONT_SIZE = (int) (0.041 * Gdx.graphics.getHeight());

        parameter.size = (int) (1.0f * FONT_SIZE);
        parameter.characters = getAllCharacters();
        parameter.flip = false;
        buttonFont = generator.generateFont(parameter);
        buttonFont.setColor(Color.BLACK);

        parameter.size = (int) (0.8f * FONT_SIZE);
        parameter.flip = false;
        gameFont = generator.generateFont(parameter);
        gameFont.setColor(Color.BLACK);

        parameter.size = (int) (0.65 * FONT_SIZE);
        parameter.flip = false;
        miniFont = generator.generateFont(parameter);
        miniFont.setColor(Color.BLACK);

        generator.dispose();
    }


    public static String getAllCharacters() {
        String langChars = LanguagesManager.getInstance().getString("lang_characters");
        return langChars + FONT_CHARACTERS;
    }
}
