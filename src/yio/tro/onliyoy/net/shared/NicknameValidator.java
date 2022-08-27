package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.Fonts;

import java.util.HashMap;
import java.util.Map;

public class NicknameValidator {

    private HashMap<String, String> obscenityMap;


    public NicknameValidator() {
        obscenityMap = new HashMap<>();
        obscenityMap.put("nigga", "mega");
        obscenityMap.put("nigger", "mega");
        obscenityMap.put("niger", "mega");
        obscenityMap.put("pidor", "peter");
        obscenityMap.put("anal", "omal");
        obscenityMap.put("anus", "omus");
        obscenityMap.put("pussy", "kitty");
        obscenityMap.put("whore", "where");
        obscenityMap.put("cumshot", "sho");
        obscenityMap.put("faggot", "carrot");
        obscenityMap.put("fuck", "lov");
        obscenityMap.put("hitler", "stalin");
        obscenityMap.put("gitler", "stalin");
        obscenityMap.put("nazi", "evil");
        obscenityMap.put("гитлер", "сталин");
    }


    public String ensureValidSymbols(String sourceName) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sourceName.length(); i++) {
            char c = sourceName.charAt(i);
            if (isCharForbidden(c)) continue;
            builder.append(c);
        }
        String name = builder.toString();

        if (name.length() > NetValues.NAME_LENGTH_LIMIT) {
            name = name.substring(0, NetValues.NAME_LENGTH_LIMIT);
        }

        return name;
    }


    private boolean isCharForbidden(char c) {
        switch (c) {
            default:
                return false;
            case '#':
            case ',':
            case '>':
            case '<':
            case '$':
            case '!':
            case '@':
            case '^':
            case ':':
            case ';':
            case '[':
            case ']':
                return true;
        }
    }


    public String applyBadWordsFilter(String sourceName) {
        int c = 10;
        String name = sourceName;
        while (c > 0) {
            c--;
            String badWord = findBadWord(name);
            if (badWord.length() == 0) return name;
            String badSubstring = detectCorrespondingSubstring(name, badWord);
            String replacement = obscenityMap.get(badWord);
            name = name.replace(badSubstring, replacement);
        }
        return "Someone";
    }


    private String detectCorrespondingSubstring(String string, String word) {
        while (true) {
            String substring = string.substring(1);
            if (!suppress(substring).contains(word)) break;
            string = substring;
        }
        while (true) {
            String substring = string.substring(0, string.length() - 1);
            if (!suppress(substring).contains(word)) break;
            string = substring;
        }
        return string;
    }


    private String findBadWord(String string) {
        String suppressedString = suppress(string);
        for (String badWord : obscenityMap.keySet()) {
            if (suppressedString.contains(badWord)) return badWord;
        }
        return "";
    }


    public String suppress(String source) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            String symbol = source.substring(i, i + 1);
            if (symbol.equals(" ")) continue;
            builder.append(suppressSingleSymbol(symbol));
        }
        return builder.toString();
    }


    private String suppressSingleSymbol(String symbol) {
        symbol = symbol.toLowerCase();
        if (symbol.equals("0")) return "o";
        if (symbol.equals("1")) return "i";
        return symbol;
    }
}
