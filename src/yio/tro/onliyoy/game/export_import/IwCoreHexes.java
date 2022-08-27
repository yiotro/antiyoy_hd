package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventsFactory;
import yio.tro.onliyoy.game.core_model.events.EventsManager;

public class IwCoreHexes extends AbstractImportWorker {

    CoreModel coreModel;


    public IwCoreHexes(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "hexes";
    }


    @Override
    protected void apply() {
        restoreHexes();
    }


    protected void restoreHexes() {
        // all hexes have to be empty at this point
        // this worker doesn't change graph
        for (String token : source.split(",")) {
            String[] split = token.split(" ");
            if (split.length < 3) continue;
            int c1 = Integer.valueOf(split[0]);
            int c2 = Integer.valueOf(split[1]);
            Hex hex = coreModel.getHex(c1, c2);
            HColor color = HColor.valueOf(split[2]);
            hex.setColor(color);
            if (split.length > 3) {
                // has piece
                PieceType pieceType = PieceType.valueOf(split[3]);
                int unitId = Integer.valueOf(split[4]);
                EventsManager eventsManager = coreModel.eventsManager;
                EventsFactory factory = eventsManager.factory;
                AbstractEvent addPieceEvent = factory.createAddPieceEvent(hex, pieceType, unitId);
                eventsManager.applyEvent(addPieceEvent);
            }
        }
    }
}
