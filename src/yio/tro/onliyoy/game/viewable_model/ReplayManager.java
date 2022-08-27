package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesManager;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.*;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.stuff.PointYio;

import java.util.ArrayList;

public class ReplayManager implements IGameplayManager {

    ObjectsLayer objectsLayer;
    private ArrayList<AbstractEvent> events;
    private ArrayList<AbstractEvent> backupEvents;
    public boolean active;
    DifferenceDetector differenceDetector;
    public boolean fast;
    private CoreModel backupModel;
    private InvalidityConcealer invalidityConcealer;
    boolean readyToConceal;


    public ReplayManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        events = new ArrayList<>();
        differenceDetector = new DifferenceDetector();
        backupEvents = new ArrayList<>();
        backupModel = new CoreModel("backup");
        invalidityConcealer = new InvalidityConcealer(objectsLayer.viewableModel);
    }


    @Override
    public void defaultValues() {
        events.clear();
        active = false;
        fast = false;
        readyToConceal = true;
    }


    @Override
    public void onBasicStuffCreated() {
        if (!isInReplayMode()) return;
        differenceDetector.prepareTargetGraph(objectsLayer.viewableModel);
    }


    @Override
    public void moveActually() {

    }


    @Override
    public void moveVisually() {
        if (!active) return;
        if (!isInReplayMode()) return;
        ViewableModel viewableModel = objectsLayer.viewableModel;
        while (events.size() > 0) {
            AbstractEvent event = events.get(0);
            if (readyToConceal && !event.isValid()) {
                System.out.println();
                System.out.println("Detected invalid event during replay: " + event);
                invalidityConcealer.apply(event);
                readyToConceal = false; // to prevent repeating same try again and again
            }
            if (!isReadyToBeDisplayed(event)) break;
            events.remove(0);
            viewableModel.eventsManager.applyEvent(event);
            readyToConceal = true;
        }
    }


    public boolean isReadyToBeDisplayed(AbstractEvent event) {
        ViewableModel viewableModel = objectsLayer.viewableModel;
        if (fast && event.getType() == EventType.turn_end && !viewableModel.turnsManager.isTurnIndexInTheEndOfLap()) {
            return true;
        }
        if (!fast && viewableModel.isSomethingMovingCurrently()) return false;
        return viewableModel.isReadyToBeDisplayed(event);
    }


    private boolean isInReplayMode() {
        return objectsLayer.gameController.gameMode == GameMode.replay;
    }


    public void onStopButtonPressed() {
        setActive(false);
        setFast(false);
        applyRewind();
    }


    private void applyRewind() {
        ViewableModel viewableModel = objectsLayer.viewableModel;
        CoreModel startingPosition = objectsLayer.historyManager.startingPosition;
        viewableModel.clearUnitsManually();
        backupModel.buildSimilarGraph(startingPosition);
        backupModel.setBy(startingPosition);
        differenceDetector.setCurrentModel(viewableModel);
        differenceDetector.setTargetModelBy(startingPosition);
        ArrayList<AbstractEvent> changes = differenceDetector.apply();
        viewableModel.eventsManager.applyMultipleEvents(changes);
        viewableModel.onReplayRewindApplied(backupModel);
        startingPosition.setBy(backupModel);
        restoreBackupEvents();
    }


    public void setActive(boolean active) {
        if (this.active == active) return;
        this.active = active;
        onActiveStateChanged();
    }


    public void onFastForwardButtonPressed() {
        setFast(!fast);
    }


    public void setFast(boolean fast) {
        this.fast = fast;
        if (fast) {
            setActive(true);
        }
    }


    private void onActiveStateChanged() {
        if (Scenes.replayOverlay.isCurrentlyVisible()) {
            Scenes.replayOverlay.applySync();
        }
    }


    private void restoreBackupEvents() {
        events.clear();
        events.addAll(backupEvents);
    }


    private void doBackupEvents() {
        backupEvents.clear();
        backupEvents.addAll(events);
    }


    public void onAdvancedStuffCreated() {
        copyEventsFromHistoryManager();
        syncProvincesToStartingPosition();
        prepareCamera();
    }


    private void prepareCamera() {
        objectsLayer.gameController.cameraController.flyUp(true);
    }


    private void syncProvincesToStartingPosition() {
        CoreModel startingPosition = objectsLayer.historyManager.startingPosition;
        objectsLayer.viewableModel.provincesManager.setBy(startingPosition.provincesManager);
    }


    public ArrayList<AbstractEvent> getEvents() {
        return events;
    }


    public void copyEventsFromHistoryManager() {
        events.clear();
        events.addAll(objectsLayer.historyManager.eventsList);
        doBackupEvents();
    }
}
