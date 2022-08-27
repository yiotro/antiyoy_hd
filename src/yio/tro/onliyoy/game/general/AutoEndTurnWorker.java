package yio.tro.onliyoy.game.general;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.stuff.RepeatYio;

public class AutoEndTurnWorker implements IEventListener {

    ObjectsLayer objectsLayer;
    RepeatYio<AutoEndTurnWorker> repeatApply;
    boolean ready;


    public AutoEndTurnWorker(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        getViewableModel().eventsManager.addListener(this);
        ready = true;
        initRepeats();
    }


    private void initRepeats() {
        repeatApply = new RepeatYio<AutoEndTurnWorker>(this, 45) {
            @Override
            public void performAction() {
                parent.apply();
            }
        };
    }


    public void move() {
        repeatApply.move();
    }


    void apply() {
        if (!ready) return;
        if (DebugFlags.humanImitation) return;
        if (!SettingsManager.getInstance().automaticTurnEnd) return;
        EntitiesManager refEntitiesManager = getViewableModel().refModel.entitiesManager;
        if (!refEntitiesManager.getCurrentEntity().isHuman()) return;
        if (getViewableModel().diplomacyManager.enabled) return;
        if (!getViewableModel().isNetMatch()) return;
        NetRoot netRoot = objectsLayer.gameController.yioGdxGame.netRoot;
        long turnEndTime = netRoot.currentMatchData.turnEndTime;
        if (turnEndTime > 0 && turnEndTime - System.currentTimeMillis() < 4000) return;
        if (!Scenes.mechanicsOverlay.isCurrentlyVisible()) return;
        ButtonYio endTurnButton = Scenes.mechanicsOverlay.endTurnButton;
        if (endTurnButton.getFactor().isInDestroyState()) return;
        if (canAtLeastOneProvinceDoSomething()) return;
        ready = false;
        Scenes.notification.show("turd_was_ended_automatically");
        endTurnButton.pressArtificially(-1);
    }


    private boolean canAtLeastOneProvinceDoSomething() {
        HColor currentColor = getViewableModel().entitiesManager.getCurrentColor();
        for (Province province : getViewableModel().provincesManager.provinces) {
            if (province.getColor() != currentColor) continue;
            if (!canProvinceDoSomething(province)) continue;
            return true;
        }
        return false;
    }


    private boolean canProvinceDoSomething(Province province) {
        ViewableModel viewableModel = getViewableModel();
        if (province.getMoney() >= viewableModel.ruleset.getPrice(province, PieceType.peasant)) return true;
        for (Hex hex : province.getHexes()) {
            if (hex.hasUnit() && viewableModel.readinessManager.isReady(hex)) return true;
        }
        return false;
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        repeatApply.resetCountDown();
        switch (event.getType()) {
            default:
                break;
            case turn_end:
                ready = true;
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 4;
    }


    private ViewableModel getViewableModel() {
        return objectsLayer.viewableModel;
    }
}
