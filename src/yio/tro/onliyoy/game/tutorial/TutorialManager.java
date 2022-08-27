package yio.tro.onliyoy.game.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;

import java.util.ArrayList;

public class TutorialManager {

    GameController gameController;
    private static String PREFS = "yio.tro.onliyoy.tutorial";
    public TutorialType currentType;
    private ArrayList<TutorialType> completedTutorials;


    public TutorialManager(GameController gameController) {
        this.gameController = gameController;
        completedTutorials = new ArrayList<>();
        loadValues();
    }


    private void loadValues() {
        completedTutorials.clear();
        Preferences preferences = getPreferences();
        for (TutorialType tutorialType : TutorialType.values()) {
            if (!preferences.getBoolean("" + tutorialType, false)) continue;
            completedTutorials.add(tutorialType);
        }
    }


    public boolean isCompleted(TutorialType tutorialType) {
        return completedTutorials.contains(tutorialType);
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }


    public void onTutorialLevelCompleted() {
        if (completedTutorials.contains(currentType)) return;
        completedTutorials.add(currentType);
        saveValues();
    }


    private void saveValues() {
        Preferences preferences = getPreferences();
        for (TutorialType tutorialType : completedTutorials) {
            preferences.putBoolean("" + tutorialType, true);
        }
        preferences.flush();
    }


    public void move() {
        if (gameController.yioGdxGame.gamePaused) return;
        if (gameController.gameMode != GameMode.tutorial) return;

    }


    public void launch(TutorialType tutorialType) {
        currentType = tutorialType;
        String levelCode = getLevelCode(tutorialType);
        startInstantly(levelCode);
        createScripts(tutorialType);
    }


    private void createScripts(TutorialType tutorialType) {
        switch (tutorialType) {
            default:
                System.out.println("TutorialManager.createScripts: problem");
                break;
            case basics:
                createBasicScripts();
                break;
            case tactics:
                createTacticalScripts();
                break;
            case diplomacy:
                createDiplomaticScripts();
                break;
        }
    }


    private void createDiplomaticScripts() {
        gameController.objectsLayer.viewableModel.turnsManager.lap = 10;
        addScript(ScriptType.focus_lock_camera, "");
        addScript(ScriptType.message, "diplomacy_hello");
        addScript(ScriptType.message, "diplomacy_mode");
        addScript(ScriptType.point_at_ui_element, "flag");
        addScript(ScriptType.message, "diplomacy_you_can_mail");
        addScript(ScriptType.message, "diplomacy_lets_write");
        addScript(ScriptType.point_at_hex, "0 2");
        addScript(ScriptType.message, "diplomacy_propose_friendship");
        addScript(ScriptType.point_at_ui_tag, "letter add");
        addScript(ScriptType.point_at_ui_tag, "letter relation");
        addScript(ScriptType.highlight_ui_element, "setup_relation_panel diplomacy_setup_condition");
        addScript(ScriptType.point_at_position, "0.5 0.5");
        addScript(ScriptType.point_at_ui_tag, "letter send");
        addScript(ScriptType.wait, "7");
        addScript(ScriptType.message, "diplomacy_check_mail");
        addScript(ScriptType.point_at_ui_element, "mail");
        addScript(ScriptType.point_at_ui_tag, "mail_inbox letter");
        addScript(ScriptType.message, "diplomacy_agree");
        addScript(ScriptType.point_at_ui_tag, "read_letter agree");
        addScript(ScriptType.message, "diplomacy_lock");
        addScript(ScriptType.message, "diplomacy_alliance");
        addScript(ScriptType.message, "diplomacy_indicators");
        addScript(ScriptType.message, "diplomacy_bye");

        addScript(ScriptType.unlock_camera, "");
    }


    private void createTacticalScripts() {
        gameController.objectsLayer.viewableModel.turnsManager.lap = 10; // to apply economics, so dividing works
        addScript(ScriptType.focus_lock_camera, "");
        addScript(ScriptType.message, "tactics_hello");
        addScript(ScriptType.message, "tactics_economics");
        addScript(ScriptType.message, "tactics_divide");
        addScript(ScriptType.point_at_hex, "-2 1");
        addScript(ScriptType.point_at_hex, "-1 1");
        addScript(ScriptType.point_at_ui_element, "end_turn");
        addScript(ScriptType.message, "tactics_provinces");
        addScript(ScriptType.message, "tactics_separate_economics");
        addScript(ScriptType.point_at_hex, "-2 1");
        addScript(ScriptType.message, "tactics_hint1");
        addScript(ScriptType.point_at_hex, "-3 2");
        addScript(ScriptType.wait, "10");
        addScript(ScriptType.message, "tactics_hint2");
        addScript(ScriptType.point_at_ui_tag, "economics coin");
        addScript(ScriptType.message, "tactics_hint3");
        addScript(ScriptType.message, "tactics_bye");
        addScript(ScriptType.focus_lock_camera, "0.53 0.49 0.7");
        addScript(ScriptType.point_at_hex, "-4 1");
        addScript(ScriptType.point_at_hex, "-4 0");
        addScript(ScriptType.unlock_camera, "");
    }


    private void createBasicScripts() {
        addScript(ScriptType.focus_lock_camera, "");
        addScript(ScriptType.message, "basics_hello");
        addScript(ScriptType.message, "basics_select_kingdom");
        addScript(ScriptType.point_at_hex, "-1 -2");
        addScript(ScriptType.message, "basics_select_peasant");
        addScript(ScriptType.point_at_hex, "-2 -2");
        addScript(ScriptType.point_at_hex, "-2 0");
        addScript(ScriptType.message, "basics_unit_can_move_only_once");
        addScript(ScriptType.message, "basics_about_profit");
        addScript(ScriptType.highlight_ui_element, "economics basics_this_is_profit");
        addScript(ScriptType.message, "basics_farm");
        addScript(ScriptType.highlight_ui_element, "construction basics_these_are_construction_buttons");
        addScript(ScriptType.point_at_ui_tag, "construction building");
        addScript(ScriptType.point_at_hex, "-1 -2");
        addScript(ScriptType.message, "basics_farm_cost");
        addScript(ScriptType.message, "basics_towers");
        addScript(ScriptType.point_at_ui_tag, "construction building");
        addScript(ScriptType.point_at_ui_tag, "construction building");
        addScript(ScriptType.point_at_hex, "-1 -1");
        addScript(ScriptType.message, "basics_towers2");
        addScript(ScriptType.message, "basics_units");
        addScript(ScriptType.highlight_ui_element, "undo basics_undo");
        addScript(ScriptType.message, "basics_end_turn");
        addScript(ScriptType.point_at_ui_element, "end_turn");
        addScript(ScriptType.message, "basics_merge");
        addScript(ScriptType.point_at_hex, "-1 -2");
        addScript(ScriptType.point_at_ui_tag, "construction unit");
        addScript(ScriptType.point_at_hex, "-3 -2");
        addScript(ScriptType.point_at_hex, "-3 -2");
        addScript(ScriptType.point_at_hex, "-2 0");
        addScript(ScriptType.message, "basics_bye");
        addScript(ScriptType.unlock_camera, "");
    }


    private void addScript(ScriptType type, String argument) {
        gameController.scriptManager.addScript(type, argument);
    }


    private void startInstantly(String levelCode) {
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.add("level_code", levelCode);
        LoadingManager loadingManager = gameController.yioGdxGame.loadingManager;
        loadingManager.startInstantly(LoadingType.tutorial, loadingParameters);
    }


    private String getLevelCode(TutorialType tutorialType) {
        switch (tutorialType) {
            default:
                System.out.println("TutorialManager.getLevelCode: problem");
                return "-";
            case basics:
                return "onliyoy_level_code#client_init:tiny,-1#camera:0.46 0.7 0.7#core_init:420.0 672.0 29.4#hexes:0 1 gray pine -1,-1 1 gray,-1 0 gray,2 0 gray,1 1 gray,2 -1 gray,-1 2 gray,-2 2 gray,-2 1 gray,-2 0 gray,-1 -1 aqua,0 -2 aqua,3 0 red peasant 1,2 1 gray,3 -1 gray,1 2 gray,3 -2 gray,0 3 gray palm -1,-1 3 gray,-3 3 gray pine -1,-3 2 gray,-3 1 gray,-2 -1 aqua,-1 -2 aqua,2 -3 gray palm -1,3 -3 gray,4 -1 red peasant 0,2 2 gray,4 -2 gray palm -1,1 3 gray,-4 3 gray,-4 2 gray pine -1,-4 1 gray pine -1,-2 -2 aqua peasant 2,-1 -3 aqua city -1,4 1 gray palm -1,5 -1 red city -1,5 -3 gray palm -1,1 4 gray palm -1,-5 4 gray,-5 2 gray pine -1,-3 -2 aqua,4 2 gray tower -1,#core_current_ids:3#player_entities:human>aqua>Sodoro,ai_random>red>Yetreesk,#provinces:2>-1<-1<0<100<Sodoro,3<0<1<10<Yetreesk,#ready:-#rules:def 1#turn:0 0#diplomacy:off#mail_basket:0,#starting_hexes:0 1 gray pine -1,-1 1 gray,-1 0 gray,2 0 gray,1 1 gray,2 -1 gray,-1 2 gray,-2 2 gray,-2 1 gray,-2 0 gray,-1 -1 aqua,0 -2 aqua,3 0 red peasant 1,2 1 gray,3 -1 gray,1 2 gray,3 -2 gray,0 3 gray palm -1,-1 3 gray,-3 3 gray pine -1,-3 2 gray,-3 1 gray,-2 -1 aqua,-1 -2 aqua,2 -3 gray palm -1,3 -3 gray,4 -1 red peasant 0,2 2 gray,4 -2 gray palm -1,1 3 gray,-4 3 gray,-4 2 gray pine -1,-4 1 gray pine -1,-2 -2 aqua peasant 2,-1 -3 aqua city -1,4 1 gray palm -1,5 -1 red city -1,5 -3 gray palm -1,1 4 gray palm -1,-5 4 gray,-5 2 gray pine -1,-3 -2 aqua,4 2 gray tower -1,#events_list:#starting_provinces:2>-1<-1<0<100<Sodoro,3<0<1<10<Yetreesk,#editor:487000#";
            case tactics:
                return "onliyoy_level_code#client_init:tiny,-1#camera:0.54 0.77 0.9#core_init:420.0 672.0 29.4#hexes:1 0 gray,0 1 blue,-1 1 blue peasant 0,-1 0 blue peasant 1,0 -1 blue spearman 12,1 -1 gray,2 0 gray,1 1 blue,2 -1 gray palm -1,0 2 gray,-2 2 yellow palm -1,-2 1 yellow spearman 5,-2 0 yellow,-1 -1 blue baron 11,0 -2 blue,1 -2 gray pine -1,2 -2 brown pine -1,3 0 blue,2 1 blue farm -1,1 2 blue farm -1,3 -2 brown,0 3 gray,-3 2 yellow tower -1,-3 1 yellow pine -1,-3 0 yellow tower -1,-2 -1 yellow,-1 -2 gray,0 -3 green,1 -3 gray,2 -3 gray,3 -3 brown,3 1 blue palm -1,4 -1 gray,2 2 blue farm -1,4 -2 brown palm -1,1 3 blue farm -1,4 -3 brown farm -1,-1 4 gray palm -1,-2 4 gray palm -1,-4 3 yellow city -1,-4 2 yellow farm -1,-4 1 yellow peasant 6,-4 0 yellow palm -1,-3 -1 yellow pine -1,-2 -2 gray,-1 -3 gray palm -1,0 -4 green,1 -4 green,3 -4 gray palm -1,5 0 gray palm -1,5 -1 gray,2 3 blue city -1,5 -4 brown city -1,-5 3 yellow farm -1,-4 -1 gray,-3 -2 gray,-1 -4 green city -1,-4 -2 gray palm -1,-3 -3 gray,-2 -4 green palm -1,#core_current_ids:8#player_entities:human>yellow>Tonepe,ai_random>blue>Raasero,ai_random>green>Epeedoy,ai_random>brown>Yekonsk,#provinces:4>0<1<0<10<Raasero,-2<2<1<10<Tonepe,2<-2<2<10<Yekonsk,0<-3<3<10<Epeedoy,#ready:-#rules:def 1#turn:0 0#diplomacy:off#mail_basket:0,#starting_hexes:1 0 gray,0 1 blue,-1 1 blue peasant 0,-1 0 blue peasant 1,0 -1 blue spearman 12,1 -1 gray,2 0 gray,1 1 blue,2 -1 gray palm -1,0 2 gray,-2 2 yellow palm -1,-2 1 yellow spearman 5,-2 0 yellow,-1 -1 blue baron 11,0 -2 blue,1 -2 gray pine -1,2 -2 brown pine -1,3 0 blue,2 1 blue farm -1,1 2 blue farm -1,3 -2 brown,0 3 gray,-3 2 yellow tower -1,-3 1 yellow pine -1,-3 0 yellow tower -1,-2 -1 yellow,-1 -2 gray,0 -3 green,1 -3 gray,2 -3 gray,3 -3 brown,3 1 blue palm -1,4 -1 gray,2 2 blue farm -1,4 -2 brown palm -1,1 3 blue farm -1,4 -3 brown farm -1,-1 4 gray palm -1,-2 4 gray palm -1,-4 3 yellow city -1,-4 2 yellow farm -1,-4 1 yellow peasant 6,-4 0 yellow palm -1,-3 -1 yellow pine -1,-2 -2 gray,-1 -3 gray palm -1,0 -4 green,1 -4 green,3 -4 gray palm -1,5 0 gray palm -1,5 -1 gray,2 3 blue city -1,5 -4 brown city -1,-5 3 yellow farm -1,-4 -1 gray,-3 -2 gray,-1 -4 green city -1,-4 -2 gray palm -1,-3 -3 gray,-2 -4 green palm -1,#events_list:#starting_provinces:4>0<1<0<10<Raasero,-2<2<1<10<Tonepe,2<-2<2<10<Yekonsk,0<-3<3<10<Epeedoy,#editor:475000#";
            case diplomacy:
                return "onliyoy_level_code#client_init:tiny,-1#camera:0.54 0.73 0.9#core_init:420.0 672.0 29.4#hexes:5 -2 gray palm -1,4 0 gray palm -1,3 1 gray,2 2 gray,1 3 gray palm -1,0 1 yellow,1 0 purple,3 0 gray,2 1 gray,1 2 yellow peasant 1,0 2 yellow city -1,1 -1 purple,2 -2 purple city -1,3 -4 gray palm -1,-1 -1 cyan,0 -1 purple,0 0 purple,2 -1 purple farm -1,3 -1 purple peasant 0,3 -2 gray,4 -2 gray pine -1,-3 0 gray,-3 1 cyan pine -1,-4 2 cyan peasant 2,-3 3 cyan peasant 4,-2 3 yellow farm -1,-1 4 gray palm -1,-1 -2 cyan city -1,-1 -3 cyan farm -1,-3 -2 gray pine -1,-4 -1 gray palm -1,-4 0 gray tower -1,-5 1 gray,-5 2 cyan peasant 3,-5 3 gray,-4 -2 gray palm -1,-6 4 gray palm -1,-2 4 gray,-3 4 cyan,3 2 gray palm -1,3 3 gray palm -1,5 -3 gray palm -1,-1 2 yellow farm -1,-1 1 cyan,-2 0 cyan,-2 2 cyan,-1 0 purple,-2 -1 cyan farm -1,-3 2 cyan pine -1,2 -3 gray,1 -3 gray pine -1,#core_current_ids:0#player_entities:human>cyan>Nesiva,ai_random>purple>Senonsk,ai_random>yellow>Obyemsk,#provinces:3>0<1<0<10<Obyemsk,1<0<1<10<Senonsk,-1<-1<2<10<Nesiva,#ready:-#rules:def 1#turn:0 0#diplomacy:-#mail_basket:1,purple>cyan>0>change_relation@cyan@alliance@null@0@7@->give_lands@purple@null@null@0@0@-1%0%0%-1%>,#starting_hexes:0 0 purple,1 0 purple,0 1 yellow,-1 1 cyan,-1 0 cyan,0 -1 purple,1 -1 purple,2 -1 purple farm -1,0 2 yellow city -1,-1 2 yellow farm -1,-2 2 cyan,-2 0 cyan,-1 -1 cyan,2 -2 purple city -1,3 0 gray,2 1 gray,3 -1 purple peasant 0,1 2 yellow peasant 1,3 -2 gray,-2 3 yellow farm -1,-3 3 cyan peasant 4,-3 2 cyan pine -1,-3 1 cyan pine -1,-3 0 gray,-2 -1 cyan farm -1,-1 -2 cyan city -1,1 -3 gray pine -1,2 -3 gray,4 0 gray palm -1,3 1 gray,2 2 gray,4 -2 gray pine -1,1 3 gray palm -1,-1 4 gray palm -1,-2 4 gray,-3 4 cyan,-4 2 cyan peasant 2,-4 0 gray tower -1,-1 -3 cyan farm -1,3 -4 gray palm -1,3 2 gray palm -1,5 -2 gray palm -1,5 -3 gray palm -1,-5 3 gray,-5 2 cyan peasant 3,-5 1 gray,-4 -1 gray palm -1,-3 -2 gray pine -1,3 3 gray palm -1,-6 4 gray palm -1,-4 -2 gray palm -1,#events_list:#starting_provinces:3>0<1<0<10<Obyemsk,1<0<1<10<Senonsk,-1<-1<2<10<Nesiva,#editor:223000#";
        }
    }
}
