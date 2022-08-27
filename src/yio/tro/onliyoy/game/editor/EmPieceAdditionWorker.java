package yio.tro.onliyoy.game.editor;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.CmWaveWorker;
import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.events.EventPieceAdd;
import yio.tro.onliyoy.game.core_model.events.EventPieceDelete;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class EmPieceAdditionWorker {

    EditorManager editorManager;
    CmWaveWorker waveWorker;
    boolean cityDetected;


    public EmPieceAdditionWorker(EditorManager editorManager) {
        this.editorManager = editorManager;
        initWaveWorker();
    }


    private void initWaveWorker() {
        waveWorker = new CmWaveWorker() {
            @Override
            protected boolean condition(Hex parentHex, Hex hex) {
                return parentHex.color == hex.color && !cityDetected;
            }


            @Override
            protected void action(Hex parentHex, Hex hex) {
                if (hex.piece != PieceType.city) return;
                cityDetected = true;
            }
        };
    }


    public void onHexClicked(Hex hex) {
        if (editorManager.chosenType == null) return;
        switch (editorManager.chosenType) {
            default:
                break;
            case pine:
                if (hex.piece == PieceType.pine) {
                    ensurePiece(hex, PieceType.grave);
                    break;
                }
                ensurePiece(hex, PieceType.pine);
                break;
            case palm:
                if (hex.piece == PieceType.palm) {
                    ensurePiece(hex, PieceType.grave);
                    break;
                }
                ensurePiece(hex, PieceType.palm);
                break;
            case tower:
                if (hex.piece == PieceType.tower) {
                    ensurePiece(hex, PieceType.strong_tower);
                    break;
                }
                ensurePiece(hex, PieceType.tower);
                break;
            case farm:
                updateCityDetected(hex);
                if (cityDetected) {
                    ensurePiece(hex, PieceType.farm);
                } else {
                    ensurePiece(hex, PieceType.city);
                }
                break;
            case peasant:
                onUnitRequested(hex, 1);
                break;
            case spearman:
                onUnitRequested(hex, 2);
                break;
        }
    }


    private void onUnitRequested(Hex hex, int deltaStrength) {
        if (hex.isEmpty() || !Core.isUnit(hex.piece)) {
            ensurePiece(hex, Core.getUnitByStrength(deltaStrength));
            return;
        }
        int strength = Core.getStrength(hex.piece);
        strength += deltaStrength;
        if (strength > 4) {
            strength -= 4;
        }
        ensurePiece(hex, Core.getUnitByStrength(strength));
    }


    private void updateCityDetected(Hex startHex) {
        for (Hex hex : getViewableModel().hexes) {
            hex.flag = false;
        }
        cityDetected = false;
        waveWorker.apply(startHex);
    }


    private void ensurePiece(Hex hex, PieceType pieceType) {
        hex.setPiece(pieceType);
        if (Core.isUnit(pieceType)) {
            hex.setUnitId(getMaximumUnitId() + 1);
        }
        if (!editorManager.isSomethingMoving()) {
            getViewableModel().cacheManager.applyUpdate(true);
        }
        SoundManager.playSound(SoundType.tick);
    }


    private ViewableModel getViewableModel() {
        return editorManager.getViewableModel();
    }


    private int getMaximumUnitId() {
        int maxId = -1;
        for (Hex hex : getViewableModel().hexes) {
            if (hex.unitId > maxId) {
                maxId = hex.unitId;
            }
        }
        return maxId;
    }
}
