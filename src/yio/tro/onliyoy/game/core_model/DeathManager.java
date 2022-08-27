package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class DeathManager implements IEventListener {

    CoreModel coreModel;
    boolean aggressiveEventValidated;


    public DeathManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        aggressiveEventValidated = false;
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case unit_move:
                EventUnitMove eventUnitMove = (EventUnitMove) event;
                if (!eventUnitMove.areColorTransferConditionsSatisfied()) break;
                aggressiveEventValidated = true;
                break;
            case piece_build:
                EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
                if (!Core.isUnit(eventPieceBuild.pieceType)) break;
                Province province = coreModel.provincesManager.getProvince(eventPieceBuild.provinceId);
                if (province == null) break;
                if (province.getColor() == eventPieceBuild.hex.color) break;
                if (eventPieceBuild.hex.isNeutral()) break;
                aggressiveEventValidated = true;
                break;
            case apply_letter:
                aggressiveEventValidated = true;
                break;
        }
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                if (aggressiveEventValidated) {
                    aggressiveEventValidated = false;
                    killLonelyUnits();
                }
                break;
            case turn_end:
                onTurnEndEventApplied();
                break;
        }
    }


    private void onTurnEndEventApplied() {
        checkToResetMoneyByKillingUnits();
        killLonelyUnits();
    }


    private void killLonelyUnits() {
        for (Hex hex : coreModel.hexes) {
            if (hex.isNeutral()) continue;
            if (hex.getProvince() != null) continue;
            if (hex.isEmpty()) continue;
            if (!Core.isUnit(hex.piece)) continue;
            spawnGrave(hex);
        }
    }


    private void checkToResetMoneyByKillingUnits() {
        for (Province province : coreModel.provincesManager.provinces) {
            if (!province.isOwnedByCurrentEntity()) continue;
            if (province.getMoney() >= 0) continue;
            province.setMoney(0);
            killUnits(province);
        }
    }


    private void killUnits(Province province) {
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            spawnGrave(hex);
        }
    }


    private void spawnGrave(Hex hex) {
        EventsManager eventsManager = coreModel.eventsManager;
        EventsRefrigerator eventsRefrigerator = coreModel.eventsRefrigerator;
        eventsManager.applyEvent(eventsRefrigerator.getDeletePieceEvent(hex));
        eventsManager.applyEvent(eventsRefrigerator.getAddPieceEvent(hex, PieceType.grave));
    }


    @Override
    public int getListenPriority() {
        return 6;
    }

}
