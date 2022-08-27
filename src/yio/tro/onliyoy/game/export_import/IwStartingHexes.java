package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.events.HistoryManager;

public class IwStartingHexes extends IwCoreHexes{

    HistoryManager historyManager;


    public IwStartingHexes(HistoryManager historyManager) {
        super(historyManager.startingPosition);
        this.historyManager = historyManager;
    }


    @Override
    protected String getDefaultSectionName() {
        return "starting_hexes";
    }


    @Override
    protected void apply() {
        historyManager.clearAll();
        historyManager.buildStartingGraph();
        restoreHexes();
    }
}
