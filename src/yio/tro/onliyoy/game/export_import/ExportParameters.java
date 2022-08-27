package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.events.HistoryManager;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class ExportParameters implements ReusableYio {


    private static ExportParameters instance;
    public String cameraCode;
    public LevelSize initialLevelSize;
    public CoreModel coreModel;
    public HistoryManager historyManager;
    public int aiVersionCode;
    public EditorManager editorManager;
    public int campaignLevelIndex;
    public String pauseName;


    public ExportParameters() {
        reset();
    }


    @Override
    public void reset() {
        cameraCode = "-";
        initialLevelSize = null;
        coreModel = null;
        historyManager = null;
        aiVersionCode = -1;
        editorManager = null;
        campaignLevelIndex = -1;
        pauseName = "-";
    }


    public static void initialize() {
        instance = null;
    }


    public static ExportParameters getInstance() {
        if (instance == null) {
            instance = new ExportParameters();
        }
        instance.reset();
        return instance;
    }


    public void setCameraCode(String cameraCode) {
        this.cameraCode = cameraCode;
    }


    public void setInitialLevelSize(LevelSize initialLevelSize) {
        this.initialLevelSize = initialLevelSize;
    }


    public void setCoreModel(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    public void setHistoryManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


    public void setAiVersionCode(int aiVersionCode) {
        this.aiVersionCode = aiVersionCode;
    }


    public void setEditorManager(EditorManager editorManager) {
        this.editorManager = editorManager;
    }


    public void setCampaignLevelIndex(int campaignLevelIndex) {
        this.campaignLevelIndex = campaignLevelIndex;
    }


    public void setPauseName(String pauseName) {
        this.pauseName = pauseName;
    }
}
