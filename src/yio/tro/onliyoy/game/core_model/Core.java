package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.events.EventType;

public class Core {

    public static boolean isUnit(PieceType pieceType) {
        switch (pieceType) {
            default:
                return false;
            case peasant:
            case spearman:
            case baron:
            case knight:
                return true;
        }
    }


    public static int getStrength(PieceType pieceType) {
        if (pieceType == null) return -1;
        switch (pieceType) {
            default:
                return -1;
            case peasant:
                return 1;
            case spearman:
                return 2;
            case baron:
                return 3;
            case knight:
                return 4;
        }
    }


    public static PieceType getUnitByStrength(int strength) {
        switch (strength) {
            default:
                return null;
            case 1:
                return PieceType.peasant;
            case 2:
                return PieceType.spearman;
            case 3:
                return PieceType.baron;
            case 4:
                return PieceType.knight;
        }
    }


    public static PieceType getMergeResult(PieceType piece1, PieceType piece2) {
        int strength1 = getStrength(piece1);
        if (strength1 == -1) return null;
        int strength2 = getStrength(piece2);
        if (strength2 == -1) return null;
        return getUnitByStrength(strength1 + strength2);
    }


    public static boolean canBeUndone(EventType eventType) {
        switch (eventType) {
            default:
                return false;
            case unit_move:
            case piece_build:
            case merge:
            case merge_on_build:
            case send_letter:
            case decline_letter:
            case apply_letter:
                return true;
        }
    }
}
