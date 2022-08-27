package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.tests.*;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.CircleButtonYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.StoreLinksYio;

public class SceneDebugTests extends SceneYio {


    private CircleButtonYio backButton;
    CustomizableListYio customizableListYio;
    AbstractTest[] tests;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        createBackButton();
        createTestsArray();
        createCustomizableList();
    }


    private void createTestsArray() {
        tests = new AbstractTest[]{
                new TestRealSimulation(),
                new TestDiplomaticSimulation(),
                new TestReplayRewind(),
                createOpenSceneTest("Test screen", Scenes.testScreen),
                createForceExceptionTest(),
                new TestSimulateMatches(),
                createImportReplayTest(),
                createOpenQuickMapTest(),
                createOpenGoogleDriveTest(),
                createTestExperienceTest(),
                createCheckUtfTest(),
                createOpenSceneTest("Research factor behavior", Scenes.researchFactorBehavior),
                new TestReproduceFreeze(),
                createOpenSceneTest("Dueling maps", Scenes.chooseDuelingMap),
                new TestCrowdedTinyMap(),
        };
    }


    private AbstractTest createCheckUtfTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return "Check UTF";
            }


            @Override
            protected void execute() {
                Scenes.keyboard.create();
                Scenes.keyboard.setReaction(new AbstractKbReaction() {
                    @Override
                    public void onInputFromKeyboardReceived(String input) {
                        if (input.length() == 0) return;
                        netRoot.sendMessage(NmType.debug_check_utf, input);
                    }
                });
            }
        };
    }


    private AbstractTest createTestExperienceTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return "Test experience";
            }


            @Override
            protected void execute() {
                netRoot.sendMessage(NmType.test_experience, "");
            }
        };
    }


    private AbstractTest createOpenGoogleDriveTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return "Open Google Drive";
            }


            @Override
            protected void execute() {
                String link = StoreLinksYio.getInstance().getGoogleDriveLink();
                Gdx.net.openURI(link);
            }
        };
    }


    private AbstractTest createOpenQuickMapTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return "Open quick map";
            }


            @Override
            protected void execute() {
                Scenes.debugChooseQuickMap.create();
            }
        };
    }


    private AbstractTest createImportReplayTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return "Import replay";
            }


            @Override
            protected void execute() {
                performImportReplay();
            }
        };
    }


    public void performImportReplay() {
        Clipboard clipboard = Gdx.app.getClipboard();
        String levelCode = clipboard.getContents();
        if (!isLevelCodeValid(levelCode)) {
            levelCode = getDefaultReplayCode();
        }
        (new IwClientInit(yioGdxGame, LoadingType.replay_open)).perform(levelCode);
    }


    private boolean isLevelCodeValid(String levelCode) {
        if (levelCode == null) return false;
        if (levelCode.length() < 3) return false;
        if (!levelCode.contains("onliyoy_level_code")) return false;
        if (!levelCode.contains("core_init")) return false;
        if (!levelCode.contains("#hexes:")) return false;
        return true;
    }


    private String getDefaultReplayCode() {
        return "onliyoy_level_code#client_init:normal,-1#camera:1.24 2.03 0.86#core_init:1224.0 1958.40002 50.4#hexes:1 2 green,0 3 green,-1 3 green,-2 3 green farm -1,4 0 green,3 1 green,4 -1 green,2 2 green,1 3 green,0 4 green,-1 4 green,-2 4 green farm -1,-3 4 green farm -1,-4 4 green city -1,0 -4 green,5 0 green,4 1 green tower -1,5 -1 green,3 2 green,5 -2 green,2 3 green tower -1,1 4 green,0 5 green,-1 5 green,-3 5 green farm -1,-4 5 green farm -1,-5 5 green farm -1,-5 4 green farm -1,-1 -4 green,0 -5 green,1 -5 green,5 -5 green,5 1 green,6 -1 green,4 2 green,6 -2 green,3 3 green,6 -3 green,2 4 green,1 5 green,6 -5 green,0 6 green,-1 6 green,-2 6 green,-6 6 green farm -1,-6 5 green farm -1,-6 4 green farm -1,-6 3 green farm -1,-6 2 green farm -1,-2 -4 green,-1 -5 green,0 -6 green,1 -6 green,2 -6 green,3 -6 green,4 -6 green,5 -6 green,6 -6 green,6 1 green knight 154,5 2 green spearman 184,7 -2 green,4 3 green,7 -3 green,7 -4 green,2 5 green,7 -5 green,1 6 green,7 -6 green,-3 7 green,-7 7 green farm -1,-7 5 green farm -1,-7 4 green farm -1,-7 2 green farm -1,-7 1 green farm -1,-2 -5 green,4 -7 green,5 -7 green,6 -7 green,6 2 green knight 230,8 -2 green,5 3 green spearman 225,8 -3 green,4 4 green,8 -4 green,3 5 green,8 -5 green,2 6 green,8 -6 green,8 -7 green,-8 7 green farm -1,-8 6 green farm -1,-8 5 green farm -1,-8 4 green farm -1,-8 3 green farm -1,-8 2 green strong_tower -1,-8 1 green farm -1,-8 0 green farm -1,-7 -1 green farm -1,-3 -5 green,6 3 green,5 4 green,4 5 green,9 -5 green,3 6 green,9 -6 green,9 -7 green,-9 6 green farm -1,-9 5 green farm -1,-9 4 green farm -1,-9 2 green farm -1,-9 0 green farm -1,-7 -2 green farm -1,-6 -3 green farm -1,-4 -5 green,-3 -6 green,6 4 green peasant 171,5 5 green,10 -5 green,4 6 green,10 -6 green,10 -7 green,-10 6 green farm -1,-10 5 green farm -1,-8 -2 green farm -1,-7 -3 green farm -1,-6 -4 green farm -1,-5 -5 green,6 5 green,5 6 green knight 177,-11 7 green farm -1,-11 6 green farm -1,-11 5 green farm -1,-8 -3 green farm -1,-7 -4 green,-6 -5 green,-5 -6 green,7 5 yellow farm -1,6 6 green spearman 215,5 7 green peasant 223,-12 7 green farm -1,-12 6 green farm -1,-12 5 green farm -1,-7 -5 green,-6 -6 green,7 6 green spearman 238,6 7 green spearman 216,-13 7 green farm -1,#core_current_ids:239#player_entities:human green,ai_balancer blue,ai_balancer yellow,ai_balancer cyan,ai_balancer red,#provinces:19>-4 4 5 350 Betrovo,#ready:-#rules:def 1#turn:0 50#starting_hexes:1 2 gray,0 3 gray,-1 3 gray,-2 3 gray,4 0 gray palm -1,3 1 gray,4 -1 gray,2 2 gray,1 3 gray,0 4 gray,-1 4 gray,-2 4 gray grave -1,-3 4 gray,-4 4 green city -1,0 -4 gray,5 0 gray,4 1 gray,5 -1 gray,3 2 gray,5 -2 gray,2 3 gray pine -1,1 4 gray pine -1,0 5 gray,-1 5 gray,-3 5 gray,-4 5 green,-5 5 green,-5 4 green,-1 -4 gray,0 -5 gray,1 -5 gray,5 -5 gray,5 1 gray,6 -1 gray palm -1,4 2 gray,6 -2 cyan,3 3 gray palm -1,6 -3 gray,2 4 gray,1 5 gray pine -1,6 -5 gray,0 6 gray,-1 6 gray,-2 6 gray,-6 6 gray,-6 5 gray,-6 4 gray,-6 3 gray,-6 2 gray,-2 -4 gray,-1 -5 gray palm -1,0 -6 gray,1 -6 blue city -1,2 -6 blue,3 -6 gray,4 -6 gray,5 -6 gray,6 -6 gray,6 1 gray,5 2 gray grave -1,7 -2 cyan city -1,4 3 gray,7 -3 cyan,7 -4 gray,2 5 gray,7 -5 gray,1 6 gray,7 -6 gray,-3 7 gray tower -1,-7 7 gray,-7 5 gray,-7 4 gray,-7 2 gray,-7 1 gray grave -1,-2 -5 gray,4 -7 gray,5 -7 gray grave -1,6 -7 gray,6 2 gray,8 -2 cyan farm -1,5 3 gray,8 -3 cyan,4 4 gray,8 -4 gray,3 5 gray palm -1,8 -5 gray,2 6 gray palm -1,8 -6 gray,8 -7 gray,-8 7 gray,-8 6 gray,-8 5 gray,-8 4 gray,-8 3 gray,-8 2 gray,-8 1 gray,-8 0 gray,-7 -1 gray,-3 -5 gray,6 3 gray palm -1,5 4 gray,4 5 gray pine -1,9 -5 gray,3 6 gray palm -1,9 -6 gray,9 -7 gray palm -1,-9 6 gray,-9 5 gray,-9 4 gray,-9 2 gray,-9 0 gray,-7 -2 red,-6 -3 gray,-4 -5 gray,-3 -6 gray,6 4 gray,5 5 gray,10 -5 gray,4 6 gray,10 -6 gray,10 -7 gray,-10 6 gray,-10 5 gray,-8 -2 red city -1,-7 -3 gray,-6 -4 gray,-5 -5 gray,6 5 gray,5 6 gray palm -1,-11 7 gray,-11 6 gray,-11 5 gray grave -1,-8 -3 gray palm -1,-7 -4 gray,-6 -5 gray,-5 -6 gray,7 5 gray,6 6 yellow,5 7 gray palm -1,-12 7 gray,-12 6 gray,-12 5 gray,-7 -5 gray,-6 -6 gray,7 6 yellow,6 7 yellow city -1,-13 7 gray grave -1,#events_list:sm 5 10,sm 6 10,sm 7 10,sm 8 10,sm 9 10,pb -3 4 peasant 0 5,te -,pb 1 -5 peasant 1 7,te -,pb 5 7 peasant 2 9,te -,pb 6 -1 peasant 3 6,te -,pb -7 -3 peasant 4 8,te -,um -3 4 -2 4 t,te -,um 1 -5 0 -5 t,te -,um 5 7 5 6 t,te -,um 6 -1 5 -1 t,te -,um -7 -3 -8 -3 t,te -,um -2 4 -3 5 t,te -,um 0 -5 0 -6 t,te -,um 5 6 6 5 t,te -,um 5 -1 5 0 t,pb 8 -3 farm 5 6,te -,um -8 -3 -7 -4 t,te -,um -3 5 -2 3 t,pb -6 4 peasant 6 5,te -,um 0 -6 -1 -5 t,te -,um 6 5 5 6 t,pb 7 5 peasant 7 9,te -,um 5 0 4 0 t,pb 7 -3 farm 8 6,te -,um -7 -4 -6 -4 t,te -,um -2 3 -2 4 t,um -6 4 -6 3 t,te -,um -1 -5 -1 -4 t,pb 0 -4 peasant 9 7,te -,um 5 6 6 5 t,um 7 5 5 5 t,te -,um 4 0 4 -1 t,pb 6 -2 farm 10 6,te -,um -6 -4 -6 -3 t,pb -6 -5 peasant 11 8,te -,um -2 4 -2 3 t,um -6 3 -6 2 t,pb -5 4 farm 12 5,te -,um -1 -4 -1 -5 t,um 0 -4 -2 -4 t,te -,um 6 5 5 6 t,um 5 5 6 4 t,pb 5 7 farm 13 9,te -,um 4 -1 4 0 t,pb 6 -1 farm 14 6,te -,um -6 -3 -5 -5 t,um -6 -5 -7 -5 t,te -,um -2 3 -2 4 t,um -6 2 -7 2 t,pb -6 4 farm 15 5,te -,um -1 -5 -2 -5 t,um -2 -4 -3 -5 t,pb 3 -6 peasant 16 7,te -,um 5 6 4 6 t,um 6 4 5 4 t,te -,um 4 0 4 -1 t,pb 5 -1 farm 17 6,pb 5 0 peasant 18 6,te -,um -5 -5 -5 -6 t,um -7 -5 -6 -6 t,pb -7 -1 peasant 19 8,te -,um -7 2 -7 1 t,pb -8 1 peasant 20 5,te -,um -2 -5 -4 -5 t,um -3 -5 -3 -6 t,um 3 -6 4 -7 t,te -,um 4 6 4 5 t,um 5 4 4 4 t,pb 6 6 farm 21 9,te -,um 5 0 5 -2 t,um 4 -1 6 -3 t,pb 4 0 farm 22 6,pb 7 -4 peasant 23 6,te -,um -7 -1 -8 0 t,te -,um -8 1 -8 2 t,um -7 1 -9 2 t,pb -6 3 farm 24 5,um -2 4 -2 3 t,te -,um 4 -7 4 -6 t,pb -3 -5 tower 25 7,te -,um 4 5 4 6 t,um 4 4 6 4 t,pb 7 6 farm 26 9,pb 6 3 peasant 27 9,te -,um 5 -2 5 0 t,um 6 -3 8 -4 t,um 7 -4 8 -5 t,pb 5 -2 farm 28 6,pb 4 1 peasant 29 6,te -,um -8 0 -9 0 t,pb -6 -4 tower 30 8,te -,um -2 3 -2 4 t,um -8 2 -8 3 t,um -9 2 -9 4 t,pb -8 2 strong_tower 31 5,te -,um 4 -6 5 -7 t,pb 5 -6 peasant 32 7,te -,um 6 4 4 4 t,um 4 6 4 5 t,um 6 3 5 4 t,pb 6 5 farm 33 9,te -,um 5 0 7 -4 t,um 8 -4 9 -5 t,um 8 -5 7 -5 t,um 4 1 3 1 t,pb 4 -1 farm 34 6,pb 5 1 peasant 35 6,te -,te -,um -8 3 -8 4 t,um -9 4 -7 4 t,um -2 4 -2 3 t,pb -3 4 farm 36 5,te -,um 5 -7 6 -7 t,um 5 -6 6 -6 t,te -,um 5 4 6 3 t,um 4 5 4 6 t,um 4 4 6 4 t,pb 7 5 farm 37 9,pb 5 6 peasant 38 9,te -,um 7 -4 8 -6 t,um 9 -5 9 -6 t,um 7 -5 7 -6 t,um 3 1 4 1 t,um 5 1 4 2 t,pb 7 -5 tower 39 6,pb 7 -5 strong_tower 40 6,te -,pb -8 0 tower 41 8,te -,um -2 3 -1 3 t,um -8 4 -10 5 t,um -7 4 -8 5 t,te -,um 6 -7 5 -5 t,um 6 -6 6 -5 t,pb 5 -6 tower 42 7,um -4 -5 0 -5 t,um -3 -6 -4 -5 t,te -,um 6 3 4 4 t,um 6 4 5 4 t,um 4 6 4 5 t,um 5 6 5 5 t,pb 6 4 farm 43 9,pb 5 3 peasant 44 9,pb 4 3 peasant 45 9,te -,um 4 1 3 1 t,um 4 2 3 2 t,um 7 -6 8 -7 t,um 8 -6 9 -7 t,um 9 -6 10 -7 t,pb 4 1 tower 46 6,pb 6 -3 farm 47 6,te -,te -,um -10 5 -9 5 t,um -8 5 -7 5 t,pb -2 3 strong_tower 48 5,te -,pb 0 -6 farm 49 7,um 0 -5 4 -6 t,um -4 -5 0 -5 t,um 5 -5 6 -6 t,um 6 -5 3 -6 t,te -,um 4 4 3 5 t,um 5 4 3 6 t,um 4 5 2 6 t,um 5 5 5 2 t,um 5 3 6 2 t,um 4 3 6 1 t,pb 4 4 tower 50 9,pb 2 5 peasant 51 9,te -,um 3 1 2 2 t,um 3 2 2 3 t,um 8 -7 10 -6 t,um 9 -7 10 -5 t,pb 4 1 strong_tower 52 6,pb 3 3 peasant 53 6,um 10 -7 8 -5 t,te -,pb -7 -3 farm 54 8,te -,um -1 3 -2 4 t,um -9 5 -9 6 t,um -7 5 -8 6 t,pb -3 5 farm 55 5,te -,pb -1 -5 farm 56 7,um 0 -5 -4 -5 t,um 3 -6 0 -5 t,um 4 -6 6 -5 t,um 6 -6 2 -6 t,te -,um 3 5 1 6 t,um 3 6 1 5 t,um 2 6 2 4 t,um 2 5 1 4 t,pb 2 5 tower 57 9,pb 0 5 peasant 58 9,pb 0 6 peasant 59 9,um 5 2 4 3 t,um 6 2 5 3 t,um 6 1 5 2 t,te -,um 2 2 1 3 t,um 2 3 1 2 t,um 3 3 0 3 t,pb 2 2 tower 60 6,pb 2 2 strong_tower 61 6,um 8 -5 7 -4 t,um 10 -6 7 -6 t,um 10 -5 8 -4 t,te -,pb -7 -4 farm 62 8,te -,um -2 4 -4 5 t,pb -2 4 farm 63 5,um -9 6 -10 6 t,um -8 6 -8 7 t,te -,pb -1 -4 farm 64 7,um 0 -5 -3 -6 t,um 2 -6 -2 -5 t,um -4 -5 0 -5 t,um 6 -5 6 -6 t,te -,um 5 3 3 3 t,um 4 3 0 4 t,um 1 6 -1 6 t,um 1 5 -1 5 t,um 2 4 -1 4 t,pb 6 2 tower 65 9,um 5 2 6 1 t,um 1 4 4 3 t,um 0 5 1 4 t,um 0 6 3 5 t,te -,pb 5 0 farm 66 6,um 7 -4 8 -5 t,um 8 -4 7 -4 t,um 7 -6 8 -4 t,um 1 3 3 2 t,um 1 2 5 1 t,um 0 3 4 2 t,te -,te -,um -4 5 -6 5 t,um -8 7 -6 6 t,um -10 6 -11 7 t,pb -1 3 farm 67 5,te -,pb -3 -5 strong_tower 68 7,um 0 -5 4 -6 t,um -2 -5 -4 -5 t,um -3 -6 0 -5 t,um 6 -6 6 -5 t,te -,um 1 4 0 3 t,pb 1 4 tower 69 9,pb 4 4 strong_tower 70 9,um 4 3 2 4 t,um 3 5 0 5 t,um 6 1 4 3 t,um 3 3 5 2 t,um 0 4 3 3 t,um -1 6 1 5 t,um -1 5 3 5 t,um -1 4 1 6 t,te -,pb 3 1 farm 71 6,um 5 1 2 3 t,um 4 2 5 1 t,um 7 -4 7 -6 t,um 8 -4 8 -6 t,um 8 -5 7 -4 t,um 3 2 4 2 t,te -,pb -7 -2 farm 72 8,te -,um -6 6 -7 7 t,um -6 5 -10 5 t,um -11 7 -11 6 t,pb -7 4 farm 73 5,te -,um 0 -5 3 -6 t,um 4 -6 5 -5 t,um -4 -5 0 -5 t,um 6 -5 6 -6 t,te -,pb -1 5 tower 74 9,um 4 3 0 4 t,um 3 5 -1 6 t,um 5 2 4 3 t,um 1 6 5 3 t,um 1 5 -1 4 t,um 2 4 1 5 t,um 0 5 2 4 t,um 3 3 0 5 t,um 0 3 3 3 t,te -,pb 3 2 farm 75 6,um 5 1 1 2 t,um 4 2 5 1 t,um 7 -4 8 -5 t,um 7 -6 7 -4 t,um 8 -6 7 -6 t,um 2 3 4 2 t,te -,pb -7 -1 farm 76 8,te -,um -7 7 -10 6 t,um -11 6 -12 7 t,um -10 5 -11 5 t,pb -8 5 farm 77 5,pb -9 6 farm 78 5,te -,pb -2 -4 farm 79 7,um 0 -5 4 -6 t,um 3 -6 0 -5 t,um 6 -6 2 -6 t,um 5 -5 6 -5 t,te -,pb 6 2 strong_tower 80 9,um 5 3 1 6 t,um 4 3 5 3 t,um 1 5 4 3 t,um 2 4 0 3 t,um 0 5 2 4 t,um 3 3 0 5 t,um 0 4 3 3 t,um -1 6 0 4 t,um -1 4 1 5 t,te -,pb 8 -4 farm 81 6,pb 3 3 spearman 82 6,um 5 1 2 3 t,um 4 2 5 1 t,um 7 -4 8 -6 t,um 8 -5 7 -4 t,um 7 -6 8 -5 t,um 1 2 4 2 t,te -,pb -8 0 strong_tower 83 8,te -,um -10 6 -12 6 t,um -12 7 -13 7 t,um -11 5 -12 5 t,pb -10 6 farm 84 5,te -,pb -2 -5 farm 86 7,um 0 -5 3 -6 t,um 2 -6 0 -5 t,um 4 -6 6 -6 t,um 6 -5 5 -5 t,te -,pb 6 3 farm 87 9,um 5 3 5 2 t,um 4 3 3 5 t,um 1 6 4 3 t,um 1 5 -1 4 t,um 2 4 -1 6 t,um 0 5 2 4 t,um 0 4 2 6 t,um 0 3 0 4 t,te -,um 3 3 0 3 t,pb 3 3 farm 88 6,um 5 1 1 2 t,um 4 2 1 3 t,um 7 -4 9 -5 t,um 8 -5 7 -4 t,um 8 -6 7 -6 t,um 2 3 5 1 t,te -,te -,um -12 6 -8 3 t,um -13 7 -9 4 t,um -12 5 -8 4 t,pb -11 7 farm 89 5,pb -12 7 farm 90 5,te -,pb 5 -6 strong_tower 91 7,um 0 -5 -4 -5 t,um 3 -6 6 -5 t,um 6 -6 6 -7 t,um 5 -5 6 -6 t,te -,um 4 3 3 3 t,pb 5 4 farm 92 9,um 3 5 6 1 t,um 2 6 0 5 t,um 5 2 4 3 t,um 2 4 1 5 t,um 0 4 2 4 t,um -1 6 3 5 t,um -1 4 0 4 t,te -,um 0 3 3 3 t,pb 8 -5 farm 93 6,um 5 1 4 2 t,um 7 -4 8 -6 t,um 9 -5 7 -4 t,um 7 -6 8 -7 t,um 1 3 5 1 t,um 1 2 2 3 t,te -,pb -6 -3 farm 94 8,te -,um -8 4 -8 1 t,um -8 3 -7 1 t,um -9 4 -9 2 t,pb -13 7 farm 95 5,pb -12 6 farm 96 5,te -,um -4 -5 0 -5 t,um 6 -7 2 -6 t,um 6 -6 5 -5 t,um 6 -5 3 -6 t,te -,um 1 5 0 3 t,pb 2 5 strong_tower 97 9,um 4 3 1 5 t,um 3 5 0 6 t,um 6 1 4 3 t,um 2 4 5 3 t,um 0 5 2 4 t,um 0 4 1 6 t,te -,um 3 3 0 3 t,pb 9 -6 farm 98 6,pb 6 -5 spearman 99 6,um 5 1 3 3 t,um 4 2 5 1 t,um 7 -4 9 -5 t,um 8 -6 7 -4 t,um 8 -7 7 -6 t,um 2 3 4 2 t,te -,pb -7 -5 farm 100 8,te -,pb -12 5 farm 101 5,te -,pb -5 -5 baron 102 7,um 0 -5 4 -6 t,um 2 -6 6 -6 t,um 3 -6 0 -5 t,um 5 -5 6 -7 t,te -,pb 3 3 spearman 103 9,um 5 3 6 1 t,um 4 3 0 4 t,um 1 6 4 3 t,um 1 5 5 3 t,um 2 4 5 2 t,um 0 6 2 4 t,te -,pb 10 -7 farm 104 6,um 5 1 2 3 t,um 4 2 1 3 t,um 7 -4 8 -6 t,um 9 -5 7 -4 t,um 7 -6 8 -7 t,um 0 3 4 2 t,um 6 -5 7 -6 t,te -,pb -8 -3 farm 105 8,te -,pb -11 5 farm 106 5,pb -11 6 farm 107 5,te -,um -5 -5 -6 -4 t,um 0 -5 -4 -5 t,um 4 -6 0 -5 t,um 6 -7 2 -6 t,um 6 -6 5 -5 t,te -,um 3 3 0 3 t,m 5 3 5 2 108,m 4 3 2 4 109,um 5 2 1 5 t,um 6 1 3 3 t,um 2 4 6 1 t,um 0 4 2 4 t,te -,um 4 2 3 3 t,pb 4 2 farm 110 6,m 7 -4 8 -6 111,m 2 3 1 3 112,um 7 -6 7 -4 t,um 8 -6 7 -6 t,um 8 -7 8 -6 t,um 1 3 5 1 t,te -,pb -6 -5 tower 113 8,te -,pb -10 5 farm 114 5,pb -9 5 farm 115 5,te -,um -4 -5 -6 -3 t,um -6 -4 -6 -5 t,um 0 -5 -4 -5 t,um 2 -6 0 -5 t,um 5 -5 6 -6 t,te -,um 1 5 -1 6 t,pb 1 4 strong_tower 116 9,um 6 1 4 3 t,um 2 4 5 3 t,um 0 3 0 4 t,te -,pb 9 -5 farm 117 6,um 5 1 2 3 t,um 7 -4 6 -5 t,um 7 -6 7 -4 t,um 8 -6 7 -6 t,um 3 3 5 1 t,te -,pb -6 -3 spearman 118 8,te -,pb -9 4 farm 119 5,pb -8 4 farm 120 5,te -,um -4 -5 -7 -4 t,um -6 -5 -7 -5 t,um 0 -5 -4 -5 t,um 6 -6 2 -6 t,te -,um 5 3 2 4 t,um 4 3 6 1 t,um 0 4 -1 4 t,um -1 6 1 5 t,te -,pb 8 -6 farm 121 6,pb 0 3 baron 122 6,um 5 1 1 3 t,um 7 -4 8 -7 t,um 7 -6 9 -7 t,um 2 3 1 2 t,um 6 -5 7 -4 t,te -,um -6 -3 -6 -4 t,pb -6 -3 tower 123 8,te -,pb -8 6 farm 124 5,te -,um -7 -5 -7 -3 t,um 2 -6 0 -5 t,um -4 -5 -6 -5 t,um -7 -4 -3 -6 t,te -,um 6 1 3 3 t,um 1 5 0 4 t,um 2 4 4 3 t,um -1 4 2 4 t,te -,um 0 3 -1 4 t,pb -1 5 baron 125 6,um 7 -4 6 -5 t,um 8 -7 7 -6 t,um 9 -7 7 -4 t,um 1 3 2 3 t,um 1 2 0 3 t,te -,um -6 -4 -6 -5 t,um -6 -6 -5 -5 t,te -,pb -7 5 farm 126 5,pb -8 7 farm 127 5,te -,m 0 -5 -3 -6 128,um -3 -6 0 -5 t,te -,um 2 4 -1 6 t,pb 5 5 farm 129 9,um 4 3 0 5 t,um 0 4 1 5 t,um 3 3 5 2 t,te -,um 2 3 3 3 t,um -1 4 -2 6 t,um -1 5 -3 7 t,pb -1 4 tower 130 6,pb -1 4 strong_tower 131 6,um 7 -4 8 -7 t,um 7 -6 7 -4 t,um 6 -5 7 -6 t,um 0 3 2 3 t,te -,um -9 0 -7 -3 t,um -6 -5 -7 -4 t,pb -6 -5 tower 132 8,te -,pb -6 5 farm 133 5,pb -7 7 farm 134 5,te -,pb -5 -5 baron 135 7,um 0 -5 4 -6 t,te -,um 5 2 4 3 t,um 1 5 0 4 t,um 0 5 2 4 t,um -1 6 0 5 t,te -,um -2 6 -1 6 t,um -3 7 0 6 t,pb -2 6 tower 136 6,um 7 -4 6 -5 t,um 7 -6 7 -4 t,um 8 -7 7 -6 t,um 2 3 1 3 t,um 3 3 0 3 t,te -,um -7 -3 -7 -5 t,te -,pb -6 6 farm 137 5,pb -5 5 farm 138 5,te -,um -5 -5 -6 -5 t,pb -5 -6 peasant 139 7,um 4 -6 0 -5 t,te -,um 0 4 3 3 t,um 0 5 1 5 t,um 2 4 5 2 t,um 4 3 0 5 t,te -,pb 3 3 baron 140 6,um 0 3 2 3 t,um 1 3 0 3 t,um -1 6 -1 5 t,um 0 6 -1 6 t,um 7 -4 8 -7 t,um 6 -5 7 -4 t,um 7 -6 6 -5 t,te -,te -,pb -4 5 farm 141 5,pb -6 2 farm 142 5,te -,um -6 -5 -6 -4 t,um 0 -5 -4 -5 t,um -5 -6 -5 -5 t,te -,um 0 5 2 4 t,um 1 5 4 3 t,um 5 2 3 5 t,te -,um 0 3 1 2 t,um 2 3 1 3 t,um -1 5 2 3 t,um -1 6 0 3 t,um 7 -4 7 -6 t,um 6 -5 7 -4 t,um 8 -7 6 -5 t,um 3 3 5 1 t,te -,um -7 -4 -5 -6 t,pb -7 -4 tower 143 8,te -,pb -8 3 farm 144 5,te -,um -6 -4 -6 -3 t,um -4 -5 -6 -4 t,um -5 -5 -6 -5 t,te -,um 2 4 0 6 t,pb -1 6 baron 145 9,um 3 5 0 5 t,um 4 3 1 6 t,te -,mob 0 3 peasant 146 6,um 1 2 -1 5 t,um 0 3 3 3 t,um 1 3 0 3 t,um 2 3 1 3 t,um 5 1 2 3 t,um 7 -4 8 -7 t,um 6 -5 7 -4 t,um 7 -6 6 -5 t,te -,te -,pb -7 2 farm 147 5,m -9 2 -8 1 152,m -7 1 -8 1 153,mob -8 1 peasant 154 5,um -8 1 -8 0 t,pb -7 -1 peasant 155 5,te -,um -6 -4 -7 -2 t,um -6 -3 -7 -4 t,um -6 -5 -6 -3 t,te -,um -1 6 -2 6 t,um 0 6 -3 7 t,pb -1 6 tower 156 9,um 0 5 2 4 t,um 1 6 0 5 t,te -,te -,pb -7 -3 tower 157 8,te -,um -8 0 -7 -2 t,um -7 -1 -9 0 t,pb -6 -3 spearman 158 5,te -,um -7 -4 -7 -3 t,te -,um 0 5 3 3 t,um 2 4 4 3 t,um -2 6 2 4 t,um -3 7 1 5 t,te -,pb 0 3 peasant 159 6,pb -1 5 peasant 160 6,pb 7 -4 peasant 161 6,pb 6 -5 peasant 162 6,pb 8 -7 peasant 163 6,pb -1 6 baron 164 6,te -,te -,um -7 -2 -7 -3 t,um -6 -3 -8 -2 t,um -9 0 -7 -2 t,pb -8 -3 peasant 165 5,te -,pb -6 -4 tower 166 7,pb -5 -5 peasant 167 7,pb -7 -4 peasant 168 7,te -,um 1 5 5 3 t,um 2 4 0 6 t,um 3 3 5 2 t,um 4 3 3 3 t,te -,um 0 3 1 3 t,um -1 5 2 3 t,um -1 6 -2 6 t,pb 5 1 farm 169 6,um 7 -4 7 -6 t,um 6 -5 7 -4 t,um 8 -7 6 -5 t,te -,te -,um -7 -3 -6 -4 t,um -8 -2 -6 -5 t,m -7 -2 -8 -3 170,um -8 -3 -6 -6 t,pb -7 -5 peasant 171 5,te -,um -5 -5 -5 -6 t,pb -6 -6 baron 172 7,te -,um 0 6 0 4 t,um 3 3 0 6 t,um 5 2 4 3 t,um 5 3 2 4 t,te -,um -2 6 -3 7 t,pb -2 6 tower 173 6,pb -2 6 strong_tower 174 6,pb 0 6 spearman 175 6,um 1 3 -1 6 t,um 2 3 -1 5 t,um 7 -4 9 -7 t,um 6 -5 7 -4 t,um 7 -6 6 -5 t,te -,te -,um -6 -4 -5 -6 t,um -7 -5 -7 -4 t,um -6 -5 -5 -5 t,te -,te -,um 0 4 0 6 t,um 2 4 1 6 t,um 4 3 0 4 t,te -,pb 8 -7 farm 176 6,um -1 5 2 3 t,um -1 6 1 2 t,um 7 -4 7 -6 t,um 6 -5 7 -4 t,um 9 -7 6 -5 t,um -3 7 -1 5 t,te -,te -,um -7 -4 -6 -6 t,um -5 -6 -4 -5 t,mob -5 -5 spearman 177 5,um -5 -5 -3 -5 t,te -,pb 1 -5 farm 178 7,te -,mob 0 6 peasant 179 9,um 0 4 2 4 t,um 1 6 0 5 t,um 0 6 0 4 t,te -,um -1 5 0 6 t,pb 9 -7 farm 180 6,um 1 2 1 3 t,um 2 3 -1 5 t,um 7 -4 10 -5 t,um 6 -5 7 -4 t,um 7 -6 6 -5 t,te -,te -,um -6 -6 -3 -6 t,um -4 -5 -2 -5 t,um -3 -5 -1 -5 t,pb -2 -4 peasant 181 5,te -,pb 0 -4 tower 182 7,te -,te -,um 1 3 3 3 t,pb 7 -6 farm 183 6,um -1 5 0 3 t,um 7 -4 10 -6 t,um 6 -5 7 -4 t,um 10 -5 6 -5 t,um 0 6 -1 6 t,te -,te -,um -1 -5 0 -5 t,um -2 -5 0 -4 t,um -3 -6 -1 -4 t,mob -2 -4 peasant 184 5,um -2 -4 1 -6 t,pb -7 1 farm 185 5,pb -8 1 farm 186 5,pb -9 2 farm 187 5,pb -8 0 farm 188 5,pb -9 0 farm 189 5,pb -7 -1 farm 190 5,pb -7 -2 farm 191 5,te -,pb 2 -6 tower 192 7,te -,pb 2 4 peasant 193 9,te -,pb 10 -5 farm 194 6,mob -1 6 peasant 195 6,um 0 3 1 3 t,um -1 6 -1 5 t,um 3 3 2 3 t,te -,te -,um -1 -4 0 -6 t,um 0 -5 2 -6 t,um 1 -6 1 -5 t,um 0 -4 3 -6 t,te -,te -,um 2 4 0 4 t,pb 0 6 peasant 196 9,pb 3 3 spearman 197 9,te -,um -1 5 1 4 t,pb 0 4 spearman 198 6,um 1 3 0 3 t,um 2 3 1 2 t,te -,te -,um 0 -6 4 -7 t,um 2 -6 4 -6 t,um 3 -6 5 -6 t,um 1 -5 5 -7 t,te -,te -,um 0 6 0 5 t,m 3 3 0 5 199,te -,um 1 2 3 3 t,um 1 4 -1 3 t,pb 6 -6 spearman 200 6,pb 5 -5 spearman 201 6,um 0 3 -1 5 t,um 7 -4 6 -7 t,um 6 -5 7 -4 t,um 10 -6 6 -5 t,um 0 4 2 3 t,te -,te -,um 5 -6 6 -6 t,um 5 -7 6 -7 t,um 4 -6 6 -5 t,um 4 -7 5 -6 t,te -,te -,um 0 5 1 4 t,pb 0 5 tower 202 9,pb 0 5 strong_tower 203 9,te -,um -1 3 -2 3 t,pb -2 4 peasant 204 6,pb -3 5 peasant 205 6,um 2 3 -1 3 t,um -1 5 2 3 t,um 7 -4 10 -6 t,um 3 3 1 3 t,te -,te -,um 6 -5 7 -5 t,um 5 -6 5 -5 t,um 6 -7 7 -6 t,um 6 -6 8 -6 t,pb 8 -7 peasant 206 5,pb 9 -7 peasant 207 5,pb 8 -5 peasant 208 5,pb 8 -4 peasant 209 5,te -,te -,um 1 4 3 3 t,te -,um -1 3 -3 4 t,um -2 3 0 5 t,um -2 4 1 4 t,um 1 3 1 2 t,um 2 3 1 3 t,um -3 5 0 3 t,te -,te -,pb -6 -3 farm 210 5,pb -8 -2 farm 211 5,pb -7 -3 farm 212 5,pb -6 -4 farm 213 5,pb -8 -3 farm 214 5,um 7 -5 7 -3 t,um 8 -4 7 -4 t,um 5 -5 6 -3 t,um 7 -6 9 -6 t,m 8 -7 9 -7 215,um 9 -7 10 -6 t,um 8 -6 7 -2 t,mob 8 -5 peasant 216 5,um 8 -5 8 -2 t,pb 6 -2 peasant 217 5,pb 6 -1 peasant 218 5,pb 5 -2 peasant 219 5,pb 5 -1 peasant 220 5,te -,te -,mob 3 3 peasant 221 9,um 3 3 0 6 t,te -,te -,te -,um 9 -6 10 -7 t,um 10 -6 10 -5 t,um 7 -4 8 -3 t,um 8 -2 4 -1 t,um 7 -3 4 0 t,um 7 -2 4 1 t,um 6 -3 5 0 t,um 6 -2 5 1 t,um 5 -2 4 2 t,um 6 -1 3 3 t,te -,te -,um 0 6 -1 6 t,te -,te -,te -,um 10 -7 9 -5 t,um 4 0 3 1 t,um 4 1 2 2 t,um 5 -1 3 2 t,um 5 0 2 3 t,um 4 -1 1 3 t,um 5 1 1 2 t,um 4 2 1 4 t,pb 4 1 tower 222 5,um 10 -5 6 -2 t,um 8 -3 5 0 t,te -,te -,um -1 6 0 6 t,te -,te -,te -,pb -3 4 peasant 223 5,pb -3 5 peasant 224 5,um 3 1 0 3 t,um 2 2 -1 4 t,um 1 3 -1 3 t,m 1 4 1 2 225,um 1 2 -2 3 t,um 2 3 -2 4 t,pb 2 3 tower 226 5,m 3 3 3 2 227,um 3 2 0 4 t,um 6 -2 2 2 t,um 5 0 1 4 t,um 9 -5 5 -1 t,te -,te -,te -,te -,te -,um 2 2 3 3 t,um 0 4 0 5 t,um 0 3 -1 5 t,um -1 4 -2 6 t,um -1 3 -3 7 t,um -2 4 -1 6 t,um -2 3 1 3 t,um 1 4 0 6 t,um 5 -1 2 2 t,um -3 4 1 4 t,um -3 5 0 4 t,te -,te -,pb 2 4 peasant 228 9,te -,te -,te -,um -2 6 1 5 t,um -1 5 2 5 t,um 1 3 2 4 t,um -3 7 1 6 t,um -1 6 2 6 t,m 0 4 0 5 229,m 0 6 0 5 230,um 0 5 3 5 t,um 1 4 3 6 t,te -,te -,pb 4 6 tower 231 9,pb 4 6 strong_tower 232 9,te -,te -,te -,um 1 5 4 4 t,um 2 4 4 3 t,um 3 3 5 4 t,um 2 5 4 6 t,um 3 5 6 3 t,um 1 6 5 5 t,um 2 6 4 5 t,um 3 6 6 5 t,um 2 2 4 2 t,pb -3 4 farm 233 5,pb -3 5 farm 234 5,pb -2 3 farm 235 5,pb -2 4 farm 236 5,te -,te -,pb 5 6 tower 237 9,te -,te -,te -,um 6 3 6 2 t,um 4 2 5 2 t,um 4 3 5 3 t,um 4 4 6 1 t,um 4 6 5 6 t,um 4 5 6 4 t,um 5 4 6 6 t,um 5 5 6 7 t,um 6 5 5 7 t,pb 7 6 spearman 238 5,#starting_provinces:10>-4 4 5 10 Betrovo,6 -2 6 10 Botroi,1 -6 7 10 Oitraibe,-7 -2 8 10 Toippansk,6 6 9 10 Aipere,#";
    }


    private AbstractTest createImportSampleTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return "Import sample";
            }


            @Override
            protected void execute() {
                Scenes.loadingTraining.create();
                Scenes.loadingTraining.setLevelCode(getGameController().debugActionsController.getLevelCode());
            }
        };
    }


    private AbstractTest createOpenKeyboardTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return "Open keyboard";
            }


            @Override
            protected void execute() {
                Scenes.keyboard.create();
            }
        };
    }


    private AbstractTest createForceExceptionTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return "Force exception";
            }


            @Override
            protected void execute() {
                Yio.forceException();
            }
        };
    }


    private AbstractTest createOpenSceneTest(final String name, final SceneYio sceneYio) {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            protected String getName() {
                return name;
            }


            @Override
            protected void execute() {
                sceneYio.create();
            }
        };
    }


    private void createCustomizableList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle("Tests");
        customizableListYio.addItem(titleListItem);

        for (AbstractTest test : tests) {
            DtCustomItem dtCustomItem = new DtCustomItem();
            dtCustomItem.setTest(test);
            customizableListYio.addItem(dtCustomItem);
        }
    }


    private void createBackButton() {
        backButton = spawnBackButton(getOpenSceneReaction(Scenes.mainLobby));
    }

}
