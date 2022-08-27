package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.resizable_element.*;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetMatchResults;
import yio.tro.onliyoy.net.shared.NetMatchStatisticsData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneNetMatchResults extends SceneYio {

    NetMatchResults netMatchResults;
    public ResizableViewElement rvElement;
    private ButtonYio saveReplayButton;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.mainLobby));
        createRvElement();
        createSaveReplayButton();
    }


    private void createSaveReplayButton() {
        double vOffset = 0.042;
        saveReplayButton = uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignRight(GraphicsYio.convertToWidth(vOffset))
                .alignTop(vOffset)
                .setTouchOffset(0.05)
                .loadCustomTexture("menu/icons/save.png")
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.up)
                .setSelectionTexture(getSelectionTexture())
                .setReaction(getSaveReplayReaction());
    }


    private Reaction getSaveReplayReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onSaveReplayButtonPressed();
            }
        };
    }


    private void onSaveReplayButtonPressed() {
        netRoot.sendMessage(NmType.request_replay, "" + netRoot.currentMatchData.matchId);
        saveReplayButton.destroy();
        Scenes.notification.show("requesting_replay");
    }


    public void onReplayCodeReceived(String replayCode) {
        if (!isLevelCodeValid(replayCode)) return;
        String name = "Net " + EditorManager.getDateString();
        getSavesManager().addItem(SaveType.replay, name, replayCode);
        (new IwClientInit(yioGdxGame, LoadingType.replay_open)).perform(replayCode);
    }


    private void createRvElement() {
        rvElement = uiFactory.getResizableViewElement()
                .setSize(0.8, 0.01)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .alignBottom(0.45);

        rvElement.addButton()
                .setSize(0.25, 0.05)
                .alignBottom(0.015)
                .alignRight(0.03)
                .setTitle("next")
                .setReaction(getNextReaction());

        rvElement.addButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignBottom(0.015)
                .alignLeft(0.03)
                .setIcon(GraphicsYio.loadTextureRegion("menu/icons/stats.png", true))
                .setKey("stats")
                .setReaction(getStatsReaction());
    }


    private Reaction getStatsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onStatisticsButtonPressed();
            }
        };
    }


    private void onStatisticsButtonPressed() {
        rvElement.deactivateButton("stats");
        netRoot.sendMessage(NmType.request_match_statistics, "" + netRoot.currentMatchData.matchId);
    }


    public void onStatisticsCodeReceived(String code) {
        rvElement.deactivateItem("def_blank");
        rvElement.addItem(new RveEmptyItem(0.03));

        RveTextItem titleItem = new RveTextItem();
        titleItem.setFont(Fonts.gameFont);
        titleItem.setTitle("statistics");
        titleItem.setHeight(0.04);
        rvElement.addItem(titleItem);

        NetMatchStatisticsData statisticsData = new NetMatchStatisticsData();
        statisticsData.decode(code);
        addRveTextItem(languagesManager.getString("turns_made") + ": " + Yio.getCompactValueString(statisticsData.turnsMade));
        addRveTextItem(languagesManager.getString("units_built") + ": " + Yio.getCompactValueString(statisticsData.unitsBuilt));
        addRveTextItem(languagesManager.getString("units_merged") + ": " + Yio.getCompactValueString(statisticsData.unitsMerged));
        addRveTextItem(languagesManager.getString("units_died") + ": " + Yio.getCompactValueString(statisticsData.unitsDied));
        addRveTextItem(languagesManager.getString("money_spent") + ": $" + Yio.getCompactValueString(statisticsData.moneySpent));
        addRveTextItem(languagesManager.getString("trees_felled") + ": " + Yio.getCompactValueString(statisticsData.treesFelled));
        addRveTextItem(languagesManager.getString("max_profit") + ": +$" + Yio.getCompactValueString(statisticsData.maxProfit));
        addRveTextItem(languagesManager.getString("first_attack") + ": " + Yio.getCompactValueString(statisticsData.firstAttackLap));

        rvElement.addItem(new RveEmptyItem(0.08));
    }


    private void addRveTextItem(String string) {
        RveTextItem rveTextItem = new RveTextItem();
        rveTextItem.setTitle(string);
        rveTextItem.setHeight(0.035);
        rvElement.addItem(rveTextItem);
    }


    private Reaction getNextReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                MenuSwitcher.getInstance().createChooseGameModeMenu();
            }
        };
    }


    public void setNetMatchResults(NetMatchResults netMatchResults) {
        this.netMatchResults = netMatchResults;
        updateRvElement();
    }


    private void updateRvElement() {
        rvElement.clearItems();

        RveWinnerItem rveWinnerItem = new RveWinnerItem();
        rveWinnerItem.setColor(netMatchResults.winnerColor);
        rveWinnerItem.setEntityType(netMatchResults.winnerEntityType);
        rveWinnerItem.setName(netMatchResults.winnerName);
        rvElement.addItem(rveWinnerItem);

        checkToAddCoinsElpItem();

        RveEmptyItem defBlankItem = new RveEmptyItem(0.1);
        defBlankItem.setKey("def_blank");
        rvElement.addItem(defBlankItem);
    }


    private void checkToAddCoinsElpItem() {
        int moneyDeltaValue = netMatchResults.moneyDeltaValue;
        int elpDeltaValue = netMatchResults.elpDeltaValue;
        if (moneyDeltaValue == 0 && elpDeltaValue == 0) return;
        RveCoinsElpItem rveCoinsElpItem = new RveCoinsElpItem();
        rveCoinsElpItem.setNetMatchResults(netMatchResults);
        rvElement.addItem(rveCoinsElpItem);
    }


    protected SavesManager getSavesManager() {
        return yioGdxGame.gameController.savesManager;
    }


    private boolean isLevelCodeValid(String levelCode) {
        if (levelCode == null) return false;
        if (levelCode.length() < 3) return false;
        if (!levelCode.contains("onliyoy_level_code")) return false;
        if (!levelCode.contains("core_init")) return false;
        if (!levelCode.contains("#hexes:")) return false;
        if (!levelCode.contains("#player_entities:")) return false;
        if (!levelCode.contains("#events_list:")) return false;
        return true;
    }
}
