package yio.tro.onliyoy.game.core_model.events;

public class EventKeys {


    public static String convertTypeToKey(EventType eventType) {
        switch (eventType) {
            default:
                System.out.println("EventsKeys.convertTypeToKey: problem");
                return "-";
            case hex_change_color:
                return "hcc";
            case set_money:
                return "sm";
            case turn_end:
                return "te";
            case graph_created:
                return "gc";
            case match_started:
                return "mc";
            case unit_move:
                return "um";
            case piece_build:
                return "pb";
            case piece_delete:
                return "pd";
            case piece_add:
                return "pa";
            case merge_on_build:
                return "mob";
            case merge:
                return "m";
            case set_relation_softly:
                return "srs";
            case send_letter:
                return "sl";
            case indicate_undo_letter:
                return "iul";
            case decline_letter:
                return "dl";
            case apply_letter:
                return "al";
            case give_money:
                return "gm";
            case subtract_money:
                return "sbm";
            case set_ready:
                return "sr";
        }
    }


    public static EventType convertKeyToType(String key) {
        switch (key) {
            default:
                System.out.println("EventsKeys.convertKeyToType: problem");
                return null;
            case "hcc":
                return EventType.hex_change_color;
            case "sm":
                return EventType.set_money;
            case "te":
                return EventType.turn_end;
            case "gc":
                return EventType.graph_created;
            case "mc":
                return EventType.match_started;
            case "um":
                return EventType.unit_move;
            case "pb":
                return EventType.piece_build;
            case "pd":
                return EventType.piece_delete;
            case "pa":
                return EventType.piece_add;
            case "mob":
                return EventType.merge_on_build;
            case "m":
                return EventType.merge;
            case "srs":
                return EventType.set_relation_softly;
            case "sl":
                return EventType.send_letter;
            case "iul":
                return EventType.indicate_undo_letter;
            case "dl":
                return EventType.decline_letter;
            case "al":
                return EventType.apply_letter;
            case "gm":
                return EventType.give_money;
            case "sbm":
                return EventType.subtract_money;
            case "sr":
                return EventType.set_ready;
        }
    }
}
