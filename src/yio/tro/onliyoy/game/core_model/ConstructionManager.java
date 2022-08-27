package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.EventMergeOnBuild;
import yio.tro.onliyoy.game.core_model.events.EventPieceBuild;
import yio.tro.onliyoy.game.core_model.events.EventsManager;

public class ConstructionManager {

    CoreModel coreModel;


    public ConstructionManager(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    public boolean onEntityRequestedConstruction(Province parentProvince, Hex hex, PieceType pieceType) {
        if (parentProvince == null) return false;
        if (hex == null) return false;
        if (pieceType == null) return false;
        if (!parentProvince.isOwnedByCurrentEntity()) return false;
        Province province = hex.getProvince();
        boolean isUnit = Core.isUnit(pieceType);
        if (!isUnit && province != parentProvince) return false;
        if (isUnit) {
            int strength = Core.getStrength(pieceType);
            coreModel.ruleset.updateMoveZoneForUnitConstruction(parentProvince, strength);
            if (!coreModel.moveZoneManager.contains(hex)) return false;
        }
        if (pieceType == PieceType.farm) {
            coreModel.moveZoneManager.updateForFarm(parentProvince);
            if (!coreModel.moveZoneManager.contains(hex)) return false;
        }
        EventsManager eventsManager = coreModel.eventsManager;
        PlayerEntity author = coreModel.entitiesManager.getEntity(parentProvince.getColor());
        if (isUnit && hex.hasUnit() && hex.color == parentProvince.getColor()) {
            EventMergeOnBuild event = eventsManager.factory.createEventMergeOnBuild(hex, pieceType, coreModel.getIdForNewUnit(), parentProvince.getId());
            event.setAuthor(author);
            eventsManager.applyEvent(event);
            return true;
        }
        EventPieceBuild event = eventsManager.factory.createBuildPieceEvent(hex, pieceType, coreModel.getIdForNewUnit(), parentProvince.getId());
        event.setAuthor(author);
        eventsManager.applyEvent(event);
        return true;
    }

}
