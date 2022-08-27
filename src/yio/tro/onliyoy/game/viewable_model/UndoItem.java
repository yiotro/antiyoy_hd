package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class UndoItem implements ReusableYio {

    UndoManager undoManager;
    public AbstractEvent event;
    private String levelCode;
    private final Object lockLevelCode;
    public Hex hookHex; // to restore selected province


    public UndoItem(UndoManager undoManager) {
        this.undoManager = undoManager;
        lockLevelCode = new Object();
        reset();
    }


    @Override
    public void reset() {
        event = null;
        synchronized (lockLevelCode) {
            levelCode = "";
        }
        hookHex = null;
    }


    public void setValues(AbstractEvent event, CoreModel coreModel) {
        this.event = event;
        // yes, I shouldn't use ExportParameters like singleton here
        // because it would be not thread safe
        ExportParameters parameters = new ExportParameters();
        parameters.setCoreModel(coreModel);
        synchronized (lockLevelCode) {
            levelCode = undoManager.exportManager.perform(parameters);
        }
        Province selectedProvince = coreModel.getSelectedProvince();
        if (selectedProvince != null) {
            hookHex = selectedProvince.getFirstHex();
        }
    }


    public String getLevelCode() {
        synchronized (lockLevelCode) {
            return levelCode;
        }
    }


    public void setLevelCode(String levelCode) {
        synchronized (lockLevelCode) {
            this.levelCode = levelCode;
        }
    }
}
