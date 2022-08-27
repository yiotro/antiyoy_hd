package yio.tro.onliyoy.game.debug;

public class DebugFlags {

    // all this stuff has to be false at release

    public static boolean showFps = false;
    public static boolean debugEnabled = false;
    public static boolean testingModeEnabled = false;
    public static boolean superUserEnabled = false;
    public static boolean showPosMap = false;
    public static boolean showCoordinates = false;
    public static boolean debugLevelGenerator = false;
    public static boolean directRender = false;
    public static boolean showRefModel = false;
    public static boolean showVmCacheDebug = false;
    public static boolean transferReplay = false;
    public static boolean aiPerTurnMovement = false;
    public static boolean showMessagesFromServerInConsole = false;
    public static boolean treatNextServerEventAsInvalid = false;
    public static boolean invalidProvinceAccessDetected = false;
    public static boolean determinedRandom = false; // important: YioGdxGame.random has to be recreated manually
    public static boolean analyzeEventFlows = false;
    public static boolean compareClientVsServerEventLogs = false;
    public static boolean profileLoading = false;
    public static boolean profileRandomGeneration = false;
    public static boolean pcDebugLogin1 = false;
    public static boolean pcDebugLogin2 = false;
    public static boolean srvHyperMode = false;
    public static boolean invalidFrameBuffer = false;
    public static boolean detectedMoveZoneUpdateProblem = false;
    public static boolean humanImitation = false;
    public static boolean necAnalysis = false;
    public static boolean desyncInvestigation = false;
    public static boolean cacheInvestigation = false;
    public static boolean timedMassKick = false;
    public static boolean invalidEventInReplayDetected = false;
    public static boolean exportUserLevels = true;

}
