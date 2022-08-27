package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventPieceDelete;
import yio.tro.onliyoy.game.core_model.events.EventType;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.export_import.Encodeable;

import java.util.ArrayList;

public class ReadinessManager implements Encodeable, IEventListener {

    CoreModel coreModel;
    private ArrayList<Hex> hexes;
    StringBuilder stringBuilder;


    public ReadinessManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        hexes = new ArrayList<>();
        stringBuilder = new StringBuilder();
    }


    public void setBy(ReadinessManager source) {
        hexes.clear();
        for (Hex srcHex : source.hexes) {
            Hex hex = coreModel.getHexWithSameCoordinates(srcHex);
            hexes.add(hex);
        }
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case turn_end:
                update();
                break;
            case graph_created:
                update();
                break;
            case piece_delete:
                // example: unit turns into grave
                EventPieceDelete eventPieceDelete = (EventPieceDelete) event;
                hexes.remove(eventPieceDelete.hex);
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 8;
    }


    public void update() {
        hexes.clear();
        for (Province province : coreModel.provincesManager.provinces) {
            if (!province.isOwnedByCurrentEntity()) continue;
            for (Hex hex : province.getHexes()) {
                if (!hex.hasUnit()) continue;
                hexes.add(hex);
            }
        }
    }


    public void onUnitMoved(Hex start) {
        hexes.remove(start);
    }


    public boolean isReady(Hex hex) {
        return hexes.contains(hex);
    }


    public void setReady(Hex hex, boolean value) {
        boolean contains = hexes.contains(hex);
        if (value && !contains) {
            hexes.add(hex);
            return;
        }
        if (!value && contains) {
            hexes.remove(hex);
        }
    }


    public void updateFlags(boolean value) {
        for (Hex hex : hexes) {
            hex.flag = value;
        }
    }


    public void reset() {
        // this method shouldn't be used in actual match
        hexes.clear();
    }


    public void decode(String source) {
        hexes.clear();
        for (String token : source.split(",")) {
            String[] split = token.split(" ");
            if (split.length < 2) continue;
            int c1 = Integer.valueOf(split[0]);
            int c2 = Integer.valueOf(split[1]);
            Hex hex = coreModel.getHex(c1, c2);
            hexes.add(hex);
        }
    }


    @Override
    public String encode() {
        if (hexes.size() == 0) return "-";
        stringBuilder.setLength(0);
        for (Hex hex : hexes) {
            stringBuilder
                    .append(hex.coordinate1)
                    .append(" ")
                    .append(hex.coordinate2)
                    .append(",");
        }
        return stringBuilder.toString();
    }
}
