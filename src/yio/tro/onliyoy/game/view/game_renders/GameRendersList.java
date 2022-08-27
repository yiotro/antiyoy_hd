package yio.tro.onliyoy.game.view.game_renders;

import yio.tro.onliyoy.game.view.GameView;
import yio.tro.onliyoy.game.view.game_renders.tm_renders.*;

import java.util.ArrayList;

public class GameRendersList {

    private static GameRendersList instance;
    ArrayList<GameRender> gameRenders = new ArrayList<>();

    public RenderTmDefault renderTmDefault;
    public RenderPosMap renderPosMap;
    public RenderUnits renderUnits;
    public RenderProvinceSelection renderProvinceSelection;
    public RenderUmSelector renderUmSelector;
    public RenderMoveZone renderMoveZone;
    public RenderDefenseIndicators renderDefenseIndicators;
    public RenderModelDirectly renderModelDirectly;
    public RenderCacheableStuff renderCacheableStuff;
    public RenderVmCache renderVmCache;
    public RenderBackground renderBackground;
    public RenderStaticPiecesInTransition renderStaticPiecesInTransition;
    public RenderHexesInTransition renderHexesInTransition;
    public RenderEditorStuff renderEditorStuff;
    public RenderTmDiplomacy renderTmDiplomacy;
    public RenderViewableRelations renderViewableRelations;
    public RenderTmChooseLands renderTmChooseLands;
    public RenderPigeons renderPigeons;
    public RenderExclamations renderExclamations;
    public RenderTmVerification renderTmVerification;
    public RenderQuickInfo renderQuickInfo;
    public RenderTmEditRelations renderTmEditRelations;
    public RenderFogOfWar renderFogOfWar;
    // initialize them lower


    public GameRendersList() {
        //
    }


    public static GameRendersList getInstance() {
        if (instance == null) {
            instance = new GameRendersList();
            instance.createAllRenders();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    public void updateGameRenders(GameView gameView) {
        for (GameRender gameRender : gameRenders) {
            gameRender.update(gameView);
        }
    }


    private void createAllRenders() {
        renderTmDefault = new RenderTmDefault();
        renderPosMap = new RenderPosMap();
        renderUnits = new RenderUnits();
        renderProvinceSelection = new RenderProvinceSelection();
        renderUmSelector = new RenderUmSelector();
        renderMoveZone = new RenderMoveZone();
        renderDefenseIndicators = new RenderDefenseIndicators();
        renderModelDirectly = new RenderModelDirectly();
        renderCacheableStuff = new RenderCacheableStuff();
        renderVmCache = new RenderVmCache();
        renderBackground = new RenderBackground();
        renderStaticPiecesInTransition = new RenderStaticPiecesInTransition();
        renderHexesInTransition = new RenderHexesInTransition();
        renderEditorStuff = new RenderEditorStuff();
        renderTmDiplomacy = new RenderTmDiplomacy();
        renderViewableRelations = new RenderViewableRelations();
        renderTmChooseLands = new RenderTmChooseLands();
        renderPigeons = new RenderPigeons();
        renderExclamations = new RenderExclamations();
        renderTmVerification = new RenderTmVerification();
        renderQuickInfo = new RenderQuickInfo();
        renderTmEditRelations = new RenderTmEditRelations();
        renderFogOfWar = new RenderFogOfWar();
    }
}
