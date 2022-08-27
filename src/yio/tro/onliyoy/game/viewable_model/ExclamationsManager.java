package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class ExclamationsManager implements IEventListener {

    ViewableModel viewableModel;
    public ArrayList<Exclamation> exclamations;
    ObjectPoolYio<Exclamation> poolExclamations;
    RepeatYio<ExclamationsManager> repeatRemoveDead;
    RepeatYio<ExclamationsManager> repeatKillOutdated;


    public ExclamationsManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        viewableModel.eventsManager.addListener(this);
        exclamations = new ArrayList<>();
        initRepeats();
        initPools();
    }


    private void initRepeats() {
        repeatRemoveDead = new RepeatYio<ExclamationsManager>(this, 60) {
            @Override
            public void performAction() {
                parent.removeDeadExclamations();
            }
        };
        repeatKillOutdated = new RepeatYio<ExclamationsManager>(this, 15) {
            @Override
            public void performAction() {
                parent.killOutdatedExclamations();
            }
        };
    }


    private void initPools() {
        poolExclamations = new ObjectPoolYio<Exclamation>(exclamations) {
            @Override
            public Exclamation makeNewObject() {
                return new Exclamation();
            }
        };
    }


    public void move() {
        moveExclamations();
        repeatRemoveDead.move();
        repeatKillOutdated.move();
    }


    private void killOutdatedExclamations() {
        // situation: there are two provinces with exclamations
        // player selects one and unites it with second province
        // capital of second province gets removed making its exclamation outdated
        for (int i = exclamations.size() - 1; i >= 0; i--) {
            Exclamation exclamation = exclamations.get(i);
            if (exclamation.appearFactor.isInDestroyState()) continue;
            if (exclamation.hex.piece == PieceType.city) continue;
            exclamation.kill();
        }
    }


    private void removeDeadExclamations() {
        for (int i = exclamations.size() - 1; i >= 0; i--) {
            Exclamation exclamation = exclamations.get(i);
            if (exclamation.appearFactor.getValue() > 0) continue;
            if (!exclamation.appearFactor.isInDestroyState()) continue;
            poolExclamations.removeFromExternalList(exclamation);
        }
    }


    private void moveExclamations() {
        for (Exclamation exclamation : exclamations) {
            exclamation.move();
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
                checkToIndicate();
                break;
            case match_started:
                checkToIndicate();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 5;
    }


    private void indicateProvince(Province province) {
        Hex capital = getCapital(province);
        if (capital == null) return;
        Exclamation freshObject = poolExclamations.getFreshObject();
        freshObject.setHex(capital);
    }


    public void onProvinceSelected(Province province) {
        Exclamation exclamation = getExclamation(province);
        if (exclamation == null) return;
        exclamation.kill();
    }


    private Exclamation getExclamation(Province province) {
        Hex capital = getCapital(province);
        if (capital == null) return null;
        for (Exclamation exclamation : exclamations) {
            if (exclamation.hex == capital) return exclamation;
        }
        return null;
    }


    private Hex getCapital(Province province) {
        for (Hex hex : province.getHexes()) {
            if (hex.piece == PieceType.city) return hex;
        }
        return null;
    }


    private void checkToIndicate() {
        poolExclamations.clearExternalList();
        PlayerEntity currentEntity = viewableModel.entitiesManager.getCurrentEntity();
        if (!currentEntity.isHuman()) return;
        for (Province province : viewableModel.provincesManager.provinces) {
            if (province.getColor() != currentEntity.color) continue;
            indicateProvince(province);
        }
    }
}
