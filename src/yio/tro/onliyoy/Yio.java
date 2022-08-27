package yio.tro.onliyoy;

import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RepeatYio;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class Yio {

    private static String slowSayMessage = null;
    private static RepeatYio<Yio> repeatCheckSlowSay = new RepeatYio<Yio>(null, 120, 1) {
        @Override
        public void performAction() {
            checkToSlowSay();
        }
    };


    public static double angle(double x1, double y1, double x2, double y2) {
        if (x1 == x2) {
            if (y2 > y1) return 0.5 * Math.PI;
            if (y2 < y1) return 1.5 * Math.PI;
            return 0;
        }

        if (x2 >= x1) {
            return Math.atan((y2 - y1) / (x2 - x1));
        } else {
            return Math.PI + Math.atan((y2 - y1) / (x2 - x1));
        }
    }


    public static double roundUp(double value, int length) {
        double d = Math.pow(10, length);
        value = value * d;
        int i = (int) (value + 0.45);
        return (double) i / d;
    }


    public static double roundUpDownwards(double value, int length) {
        double d = Math.pow(10, length);
        value = value * d;
        int i = (int) (value);
        return (double) i / d;
    }


    public static boolean removeByIterator(ArrayList<?> list, Object object) {
        ListIterator iterator = list.listIterator();

        while (iterator.hasNext()) {
            if (iterator.next() == object) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }


    public static void addByIterator(ArrayList<?> list, Object object) {
        ListIterator iterator = list.listIterator();
        iterator.add(object);
    }


    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }


    public static double distanceBetweenAngles(double a1, double a2) {
        double result = a2 - a1;

        while (result > Math.PI) {
            result -= 2 * Math.PI;
        }

        while (result < -Math.PI) {
            result += 2 * Math.PI;
        }

        return Math.abs(result);
    }


    public static float radianToDegree(double angle) {
        return (float) (180 / Math.PI * angle);
    }


    public static void syncSay(String message) {
        synchronized (System.out) {
            safeSay(message);
        }
    }


    public static void timeSay(String message) {
        long millis = System.currentTimeMillis() - YioGdxGame.appLaunchTime;
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        String time = String.format("%02d:%02d:%03d", minute, second, millis % 1000);
        safeSay(time + ": " + message);
    }


    public static void slowSay(String message) {
        slowSayMessage = message;
        repeatCheckSlowSay.move();
    }


    private static void checkToSlowSay() {
        safeSay(slowSayMessage);
    }


    public static void safeSay(String message) {
        System.out.println(message);
    }


    public static void printStackTrace() {
        try {
            forceException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static StackTraceElement[] getStackTrace() {
        try {
            forceException();
        } catch (Exception e) {
            return e.getStackTrace();
        }

        return null;
    }


    public static void forceException() {
        PointYio p = null;
        p.x = 0; // should fail here
    }


    public static String getUniqueCode(Object object) {
        String s = object.toString();
        return s.substring(s.indexOf("@"));
    }


    public static double getRandomAngle() {
        return 2d * Math.PI * YioGdxGame.random.nextDouble();
    }


    public static ArrayList<String> createArrayListFromString(String src) {
        ArrayList<String> list = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(src, "#");
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }


    public static String convertTime(long frames) {
        long currentCountDown = frames;
        if (currentCountDown < 0) {
            currentCountDown = 0;
        }
        currentCountDown /= 60; // seconds
        int min = 0;
        while (currentCountDown >= 60) {
            min++;
            currentCountDown -= 60;
        }
        String zero = "";
        if (currentCountDown < 10) zero = "0";
        return min + ":" + zero + currentCountDown;
    }


    public static String convertTimeToUnderstandableString(long frames) {
        long seconds = frames / 60;
        int minutes = 0;
        while (seconds >= 60) {
            minutes++;
            seconds -= 60;
        }
        int hours = 0;
        while (minutes >= 60) {
            hours++;
            minutes -= 60;
        }

        String hString = "";
        if (hours > 0) {
            hString = hours + getTimeAbbreviation("hours_abbreviation", "h") + " ";
        }
        String mString = "";
        if (minutes > 0 || hours > 0) {
            String prefix = "" + minutes;
            if (hours > 0) {
                prefix = convertToTwoDigitString(minutes);
            }
            mString = prefix + getTimeAbbreviation("minutes_abbreviation", "m") + " ";
        }
        String sString = convertToTwoDigitString(seconds) + getTimeAbbreviation("seconds_abbreviation", "s");

        return hString + mString + sString;
    }


    public static String convertTimeToTurnDurationString(long frames) {
        long seconds = frames / 60;
        if (seconds < 120) return convertTime(frames);
        String string = convertTimeToUnderstandableString(frames);
        if (string.contains(" ")) {
            string = string.substring(0, string.indexOf(" "));
        }
        return string;
    }


    private static String getTimeAbbreviation(String key, String defValue) {
        if (SettingsManager.getInstance().localizeTime) {
            return LanguagesManager.getInstance().getString(key);
        }
        return defValue;
    }


    public static String convertToTwoDigitString(long value) {
        if (value < 10) {
            return "0" + value;
        }
        return "" + value;
    }


    public static int convertMillisIntoFrames(long millis) {
        return (int) (60 * (millis / 1000));
    }


    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }


    public static String getCompactValueString(int srcValue) {
        String prefix = "";
        if (srcValue < 0) {
            prefix = "-";
        }
        srcValue = Math.abs(srcValue);

        if (srcValue < 1000) {
            return prefix + srcValue;
        }

        float v;
        int iv;

        if (srcValue < 10000) {
            v = srcValue;
            v /= 1000;
            v = (float) Yio.roundUpDownwards(v, 1);
            return prefix + v + "k";
        }

        if (srcValue < 1000000) {
            iv = srcValue;
            iv /= 1000;
            return prefix + iv + "k";
        }

        if (srcValue < 10000000) {
            v = srcValue;
            v /= 1000000;
            v = (float) Yio.roundUpDownwards(v, 1);
            return prefix + v + "m";
        }

        iv = srcValue;
        iv /= 1000000;
        return prefix + iv + "m";
    }


    public static String getRandomlyCutText(String source, int maxWords) {
        String[] split = source.split(" ");
        int targetLength = YioGdxGame.random.nextInt(maxWords);
        targetLength = Math.max(4, targetLength);
        targetLength = Math.min(targetLength, split.length);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < targetLength; i++) {
            builder.append(split[i]).append(" ");
        }
        return builder.toString();
    }


    public static String getCapitalizedString(String string) {
        if (string == null || string.length() == 0) return "";
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }


    public static String getLimitedString(String source, int limit) {
        if (source.length() < limit) {
            return source;
        }
        return source.substring(0, limit) + "...";
    }


    public static String convertBooleanToShortString(boolean value) {
        if (value) return "t";
        return "f";
    }


    public static boolean convertShortStringToBoolean(String string) {
        if (string.equals("t")) return true;
        if (string.equals("f")) return false;
        System.out.println("Yio.convertShortStringToBoolean: problem");
        return false;
    }


    public static String getLoremIpsum() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eu eros eu ligula tempor mattis. Donec suscipit ante a ex placerat ullamcorper. Praesent vel mattis eros, vel volutpat ante # #Ut urna magna, gravida in aliquet a, mattis a justo. Proin sodales lectus enim, vel cursus mi fringilla nec. Cras rutrum feugiat nibh, a ornare quam venenatis blandit. Nunc massa ante, scelerisque vel ligula quis, consequat porttitor ex. Maecenas et ante nibh. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent mollis mattis magna, vel feugiat purus scelerisque non. Donec neque lorem, dignissim et commodo sed, vestibulum quis enim";
    }
}
