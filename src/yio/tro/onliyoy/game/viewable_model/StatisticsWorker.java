package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.net.shared.NetMatchStatisticsData;

public class StatisticsWorker implements IEventListener {

    CoreModel coreModel;
    CoreModel statsModel;
    HistoryManager historyManager;
    private NetMatchStatisticsData netMatchStatisticsData;
    private InvalidityConcealer invalidityConcealer;
    private HColor winnerColor;


    public StatisticsWorker(CoreModel coreModel, HistoryManager historyManager) {
        this.coreModel = coreModel;
        this.historyManager = historyManager;
    }


    public void apply(NetMatchStatisticsData netMatchStatisticsData, HColor winnerColor) {
        this.netMatchStatisticsData = netMatchStatisticsData;
        this.winnerColor = winnerColor;
        prepare();
        for (AbstractEvent event : historyManager.eventsList) {
            processEvent(event);
        }
        finish();
    }


    private void prepare() {
        initStatsModel();
        invalidityConcealer = new InvalidityConcealer(statsModel);
        EventFlowAnalyzer.getInstance().onReproducedModelCreated(statsModel);
    }


    private void finish() {
        netMatchStatisticsData.turnsMade = statsModel.turnsManager.lap;
        if (statsModel.turnsManager.turnIndex > getWinnerTurnIndex()) {
            netMatchStatisticsData.turnsMade++;
        }
    }


    private int getWinnerTurnIndex() {
        PlayerEntity[] entities = statsModel.entitiesManager.entities;
        for (int i = 0; i < entities.length; i++) {
            if (entities[i].color == winnerColor) return i;
        }
        return -1;
    }


    private void processEvent(AbstractEvent event) {
        AbstractEvent statsEvent = statsModel.eventsManager.factory.createCopy(event);
        if (!statsEvent.isValid()) {
            System.out.println("StatisticsWorker.processEvent: invalid event detected " + statsEvent);
            invalidityConcealer.apply(statsEvent);
        }
        statsModel.eventsManager.applyEvent(statsEvent);
    }


    @Override
    public void onEventValidated(AbstractEvent statsEvent) {
        Province province;
        int price;
        switch (statsEvent.getType()) {
            default:
                break;
            case turn_end:
                updateMaxProfit();
                break;
            case piece_delete:
                EventPieceDelete eventPieceDelete = (EventPieceDelete) statsEvent;
                if (eventPieceDelete.hex.hasUnit()) {
                    netMatchStatisticsData.unitsDied++;
                }
                break;
            case piece_build:
                EventPieceBuild eventPieceBuild = (EventPieceBuild) statsEvent;
                province = statsModel.provincesManager.getProvince(eventPieceBuild.provinceId);
                price = statsModel.ruleset.getPrice(province, eventPieceBuild.pieceType);
                netMatchStatisticsData.moneySpent += price;
                if (Core.isUnit(eventPieceBuild.pieceType)) {
                    netMatchStatisticsData.unitsBuilt++;
                }
                if (eventPieceBuild.hex.hasUnit()) {
                    netMatchStatisticsData.unitsDied++;
                }
                if (eventPieceBuild.hex.hasTree()) {
                    netMatchStatisticsData.treesFelled++;
                }
                if (isAttackDetected(province.getColor(), eventPieceBuild.hex)) {
                    checkToNoticeFirstAttackTurn();
                }
                break;
            case unit_move:
                EventUnitMove eventUnitMove = (EventUnitMove) statsEvent;
                if (eventUnitMove.finish.hasUnit()) {
                    netMatchStatisticsData.unitsDied++;
                }
                if (eventUnitMove.finish.hasTree()) {
                    netMatchStatisticsData.treesFelled++;
                }
                if (isAttackDetected(eventUnitMove.start.color, eventUnitMove.finish)) {
                    checkToNoticeFirstAttackTurn();
                }
                break;
            case merge:
                netMatchStatisticsData.unitsMerged++;
                break;
            case merge_on_build:
                EventMergeOnBuild eventMergeOnBuild = (EventMergeOnBuild) statsEvent;
                netMatchStatisticsData.unitsMerged++;
                province = statsModel.provincesManager.getProvince(eventMergeOnBuild.provinceId);
                price = statsModel.ruleset.getPrice(province, eventMergeOnBuild.pieceType);
                netMatchStatisticsData.moneySpent += price;
                break;
        }
    }


    private void checkToNoticeFirstAttackTurn() {
        if (netMatchStatisticsData.firstAttackLap != -1) return;
        netMatchStatisticsData.firstAttackLap = statsModel.turnsManager.lap;
    }


    private boolean isAttackDetected(HColor attackerColor, Hex victimHex) {
        if (victimHex.isNeutral()) return false;
        if (attackerColor == victimHex.color) return false;
        if (victimHex.getProvince() == null) return false;
        return true;
    }


    private void updateMaxProfit() {
        int currentProfit = getCurrentProfit();
        if (currentProfit <= netMatchStatisticsData.maxProfit) return;
        netMatchStatisticsData.maxProfit = currentProfit;
    }


    private int getCurrentProfit() {
        HColor currentColor = statsModel.entitiesManager.getCurrentColor();
        int profit = 0;
        for (Province province : statsModel.provincesManager.provinces) {
            if (province.getColor() != currentColor) continue;
            profit += statsModel.economicsManager.calculateProvinceProfit(province);
        }
        return profit;
    }


    @Override
    public void onEventApplied(AbstractEvent statsEvent) {

    }


    @Override
    public int getListenPriority() {
        return 5;
    }


    private void initStatsModel() {
        statsModel = new CoreModel("stats");
        statsModel.buildSimilarGraph(coreModel);
        statsModel.entitiesManager.setBy(coreModel.entitiesManager);
        statsModel.setRulesBy(coreModel);
        statsModel.setBy(historyManager.startingPosition);
        statsModel.eventsManager.addListener(this);
    }
}
