package yio.tro.onliyoy.stuff;

import yio.tro.onliyoy.Yio;

public class TimeMeasureYio {

    private static long startTime;


    public static void begin() {
        // I should not leave this method in various places around the project
        startTime = System.currentTimeMillis();
    }


    public static long apply(String message) {
        long resultTime = getCurrentResult();
        Yio.safeSay(message + ": " + resultTime);
        begin();
        return resultTime;
    }


    public static long apply() {
        return apply("Time taken");
    }


    public static long getCurrentResult() {
        return System.currentTimeMillis() - startTime;
    }

}
