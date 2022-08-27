package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.Fonts;

import java.util.HashMap;

public class CharLocalizerYio {

    private static CharLocalizerYio instance;
    String replacementsString;
    HashMap<Character, String> map;
    private String renderableCharacters;


    public CharLocalizerYio() {
        map = new HashMap<>();
        replacementsString = "й i;ц c;у u;к k;е e;н n;г g;ш sh;щ sch;з z;х h;ъ i;ё yo;ф f;ы y;в v;а a;п p;р r;о o;л l;д d;ж j;э e;я ya;ч ch;с s;м m;и i;т t;ь i;б b;ю u;Й I;Ц C;У U;К K;Е E;Н N;Г G;Ш SH;Щ SCH;З Z;Х H;Ъ I;Ф F;Ы Y;В V;А A;П P;Р R;О O;Л L;Д D;Ж J;Э E;Я YA;Ч CH;С S;М M;И I;Т T;Ь I;Б B;Ю U;і i;І I;ї i;Ї I;є e;Є E;é e;à a;è e;ù u;â a;ê e;î i;ô o;û u;ç c;ë e;ï i;ü u;É E;À A;È E;Ù U;Â A;Ê E;Î I;Ô O;Û U;Ç C;Ë E;Ï I;Ü U;ä a;ö o;Ä A;Ö O;ß B;á a;ã a;í i;ó o;õ o;ú u;Á A;Ã A;Í I;Ó O;Õ O;Ú U;ğ g;Ğ G;ş s;Ş S;ı i;İ I;ě e;Ě E;ý y;Ý Y;ů u;Ů U;ž z;Ž Z;š s;Š S;č c;Č C;ř r;Ř R;ď d;Ď D;ť t;Ť T;ň n;Ň N;Å A;ø o;";
        initMap();
    }


    public static void initialize() {
        instance = null;
    }


    public static CharLocalizerYio getInstance() {
        if (instance == null) {
            instance = new CharLocalizerYio();
        }
        return instance;
    }


    private void updateRenderableCharacters() {
        renderableCharacters = Fonts.getAllCharacters();
    }


    private boolean doesStringContainNonRenderableCharacters(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (canBeRendered(string.charAt(i))) continue;
            return true;
        }
        return false;
    }


    private boolean canBeRendered(char c) {
        if (c == ' ') return true;
        return renderableCharacters.indexOf(c) != -1;
    }


    public String apply(String source) {
        updateRenderableCharacters();
        if (!doesStringContainNonRenderableCharacters(source)) return source;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String str = String.valueOf(c);
            if (!canBeRendered(c)) {
                if (map.containsKey(c)) {
                    str = map.get(c);
                } else {
                    str = "#";
                }
            }
            builder.append(str);
        }
        return builder.toString();
    }


    private void initMap() {
        map.clear();
        for (String token : replacementsString.split(";")) {
            String[] split = token.split(" ");
            if (split.length < 2) continue;
            char c = split[0].charAt(0);
            String value = split[1];
            map.put(c, value);
        }
    }
}
