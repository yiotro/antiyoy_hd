package yio.tro.onliyoy.game.core_model;

import java.util.ArrayList;

public abstract class CmWaveWorker {

    private ArrayList<Hex> propagationList;
    protected Hex startHex;


    public CmWaveWorker() {
        propagationList = new ArrayList<>();
    }


    public void apply(Hex startHex) {
        // important, this worker uses hex.flag
        // flags should be prepared externally at this point
        this.startHex = startHex;
        propagationList.clear();
        addToPropagationList(null, startHex);
        while (propagationList.size() > 0) {
            Hex hex = propagationList.get(0);
            propagationList.remove(hex);
            propagate(hex);
        }
    }


    private void propagate(Hex hex) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.flag) continue;
            if (!condition(hex, adjacentHex)) continue;
            addToPropagationList(hex, adjacentHex);
        }
    }


    protected abstract boolean condition(Hex parentHex, Hex hex);


    protected abstract void action(Hex parentHex, Hex hex);


    private void addToPropagationList(Hex parentHex, Hex hex) {
        hex.flag = true;
        propagationList.add(hex);
        action(parentHex, hex);
    }
}
