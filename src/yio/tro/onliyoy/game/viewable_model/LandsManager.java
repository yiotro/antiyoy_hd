package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesManager;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class LandsManager implements IEventListener{

    // this manager tracks color changes and static pieces
    ViewableModel viewableModel;
    public ArrayList<ViewableHex> viewableHexes;
    public ArrayList<ViewableHex> currentlyAnimatedHexes;
    ObjectPoolYio<ViewableHex> poolViewableHexes;
    HColor provinceColor;
    RepeatYio<LandsManager> repeatRemoveAnimatedHexes;


    public LandsManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        viewableHexes = new ArrayList<>();
        currentlyAnimatedHexes = new ArrayList<>();
        provinceColor = null;
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveAnimatedHexes = new RepeatYio<LandsManager>(this, 30) {
            @Override
            public void performAction() {
                parent.checkToRemoveAnimatedHexes();
            }
        };
    }


    private void initPools() {
        poolViewableHexes = new ObjectPoolYio<ViewableHex>(viewableHexes) {
            @Override
            public ViewableHex makeNewObject() {
                return new ViewableHex(LandsManager.this);
            }
        };
    }


    void move() {
        moveAnimatedHexes();
        repeatRemoveAnimatedHexes.move();
    }


    void checkToRemoveAnimatedHexes() {
        for (int i = currentlyAnimatedHexes.size() - 1; i >= 0; i--) {
            ViewableHex vHex = currentlyAnimatedHexes.get(i);
            if (vHex.isPieceFactorInMovementMode()) continue;
            if (vHex.isColorFactorInMovementMode()) continue;
            if (vHex.inTransition) continue;
            currentlyAnimatedHexes.remove(vHex);
        }
    }


    public void onHexBecameAnimated(ViewableHex vHex) {
        if (currentlyAnimatedHexes.contains(vHex)) return;
        currentlyAnimatedHexes.add(vHex);
    }


    private void moveAnimatedHexes() {
        for (ViewableHex viewableHex : currentlyAnimatedHexes) {
            viewableHex.move();
        }
    }


    public void sync() {
        poolViewableHexes.clearExternalList();
        for (Hex hex : viewableModel.hexes) {
            ViewableHex freshObject = poolViewableHexes.getFreshObject();
            freshObject.setHex(hex);
        }
    }


    public ViewableHex getViewableHex(Hex hex) {
        for (ViewableHex viewableHex : viewableHexes) {
            if (viewableHex.hex == hex) return viewableHex;
        }
        return null;
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case piece_build:
                EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
                if (Core.isUnit(eventPieceBuild.pieceType)) {
                    ProvincesManager refProvincesManager = viewableModel.refModel.provincesManager;
                    Province refProvince = refProvincesManager.getProvince(eventPieceBuild.provinceId);
                    if (refProvince == null) {
                        System.out.println("LandsManager.onEventValidated: ref province is null, id = " + eventPieceBuild.provinceId);
                    }
                    provinceColor = refProvince.getColor();
                }
                break;
        }
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case match_started:
                sync();
                currentlyAnimatedHexes.clear();
                break;
            case hex_change_color:
                onEventHexChangeColor(event);
                break;
            case unit_move:
                onEventUnitMove(event);
                break;
            case piece_build:
                onEventPieceBuild(event);
                break;
            case piece_delete:
                onEventPieceDelete(event);
                break;
            case piece_add:
                onEventPieceAdd(event);
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 5;
    }


    private void onEventPieceAdd(AbstractEvent event) {
        EventPieceAdd eventPieceAdd = (EventPieceAdd) event;
        ViewableHex viewableHex = getViewableHex(eventPieceAdd.hex);
        if (viewableHex == null) return;
        viewableHex.setPieceType(eventPieceAdd.pieceType);
    }


    private void onEventPieceDelete(AbstractEvent event) {
        EventPieceDelete eventPieceDelete = (EventPieceDelete) event;
        ViewableHex viewableHex = getViewableHex(eventPieceDelete.hex);
        if (viewableHex == null) return;
        viewableHex.setPieceType(null);
    }


    private void onEventPieceBuild(AbstractEvent event) {
        EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
        ViewableHex viewableHex = getViewableHex(eventPieceBuild.hex);
        if (viewableHex == null) return;
        viewableHex.setPieceType(eventPieceBuild.pieceType, VhSpawnType.constructed);
        if (Core.isUnit(eventPieceBuild.pieceType)) {
            // province may be removed at this point (because event was applied)
            // so its color was backed up before event was applied
            if (provinceColor != viewableHex.currentColor) {
                viewableHex.applyColorChange(provinceColor);
            }
        }
    }


    private void onEventUnitMove(AbstractEvent event) {
        EventUnitMove eventUnitMove = (EventUnitMove) event;
        ViewableHex viewableHex = getViewableHex(eventUnitMove.finish);
        if (viewableHex == null) return;
        viewableHex.setPieceType(null); // piece gets smashed by unit
        Hex refFinish = viewableModel.refModel.getHexWithSameCoordinates(eventUnitMove.finish);
        viewableHex.applyColorChange(refFinish.color);
    }


    private void onEventHexChangeColor(AbstractEvent event) {
        EventHexChangeColor eventHexChangeColor = (EventHexChangeColor) event;
        ViewableHex viewableHex = getViewableHex(eventHexChangeColor.hex);
        if (viewableHex == null) return;
        viewableHex.applyColorChange(eventHexChangeColor.color);
    }
}
