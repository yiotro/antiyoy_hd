package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.Letter;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventIndicateUndoLetter;
import yio.tro.onliyoy.game.core_model.events.EventSendLetter;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class PigeonsManager implements IEventListener {

    ViewableModel viewableModel;
    public ArrayList<Pigeon> pigeons;
    ObjectPoolYio<Pigeon> poolPigeons;
    RepeatYio<PigeonsManager> repeatRemove;
    public double alphaModifier;


    public PigeonsManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        pigeons = new ArrayList<>();
        initRepeats();
        initPools();
    }


    private void initPools() {
        poolPigeons = new ObjectPoolYio<Pigeon>(pigeons) {
            @Override
            public Pigeon makeNewObject() {
                return new Pigeon(PigeonsManager.this);
            }
        };
    }


    private void initRepeats() {
        repeatRemove = new RepeatYio<PigeonsManager>(this, 120) {
            @Override
            public void performAction() {
                parent.checkToRemovePigeons();
            }
        };
    }


    public void move() {
        movePigeons();
        repeatRemove.move();
    }


    private void movePigeons() {
        for (Pigeon pigeon : pigeons) {
            pigeon.move();
        }
    }


    private void checkToRemovePigeons() {
        for (int i = pigeons.size() - 1; i >= 0; i--) {
            Pigeon pigeon = pigeons.get(i);
            if (pigeon.isRelocating()) continue;
            poolPigeons.removeFromExternalList(pigeon);
        }
    }


    public boolean isSomethingMovingCurrently() {
        for (Pigeon pigeon : pigeons) {
            if (pigeon.isRelocating()) return true;
        }
        return false;
    }


    void sendPigeon(Hex start, Hex finish, boolean quick) {
        if (start == null) return;
        if (finish == null) return;
        poolPigeons.getFreshObject().launch(start, finish, quick);
    }


    void sendPigeon(Letter letter, boolean quick) {
        Hex start = getLargestCapital(letter.senderColor);
        Hex finish = getLargestCapital(letter.recipientColor);
        if (finish == null) return;
        sendPigeon(start, finish, quick);
    }


    void sendPigeonBackwards(HColor senderColor, HColor recipientColor) {
        Hex start = getLargestCapital(recipientColor);
        Hex finish = getLargestCapital(senderColor);
        sendPigeon(start, finish, true);
    }


    private Hex getLargestCapital(HColor color) {
        Province largestProvince = viewableModel.provincesManager.getLargestProvince(color);
        if (largestProvince == null) return null;
        for (Hex hex : largestProvince.getHexes()) {
            if (hex.piece == PieceType.city) return hex;
        }
        return null;
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case send_letter:
                EventSendLetter eventSendLetter = (EventSendLetter) event;
                sendPigeon(eventSendLetter.letter, event.isQuick());
                break;
            case indicate_undo_letter:
                EventIndicateUndoLetter eventIndicateUndoLetter = (EventIndicateUndoLetter) event;
                sendPigeonBackwards(eventIndicateUndoLetter.senderColor, eventIndicateUndoLetter.recipientColor);
                break;
            case decline_letter:
            case apply_letter:
                if (!viewableModel.refModel.entitiesManager.isHumanTurnCurrently()) break;
                Scenes.mechanicsOverlay.updateMailTexture();
                break;
            case match_started:
                updateAlphaModifier();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 4;
    }


    private void updateAlphaModifier() {
        alphaModifier = 0.66;
        if (viewableModel.entitiesManager.isInAiOnlyMode()) return;
        if (viewableModel.objectsLayer.replayManager.active) return;
        alphaModifier = 1;
    }
}
