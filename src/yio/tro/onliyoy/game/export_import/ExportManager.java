package yio.tro.onliyoy.game.export_import;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.events.HistoryManager;

public class ExportManager {


    private StringBuilder builder;
    private ExportParameters parameters;


    public ExportManager() {
        builder = new StringBuilder();
        parameters = null;
    }


    public String performToClipboard(ExportParameters parameters) {
        String result = perform(parameters);

        Clipboard clipboard = Gdx.app.getClipboard();
        clipboard.setContents(result);

        return result;
    }


    public synchronized String perform(ExportParameters parameters) {
        this.parameters = parameters;
        builder.setLength(0);

        addTitle();
        saveClientInitializationData();
        saveCameraPosition();
        saveCoreModel();
        saveHistory();
        saveEditor();
        saveCampaign();
        savePauseName();

        builder.append("#");
        return builder.toString();
    }


    private void savePauseName() {
        startSection("pause_name");
        builder.append(parameters.pauseName);
    }


    private void saveCampaign() {
        if (parameters.campaignLevelIndex == -1) return;
        startSection("campaign");
        builder.append(parameters.campaignLevelIndex);
    }


    private void saveEditor() {
        if (parameters.editorManager == null) return;
        startSection("editor");
        builder.append(parameters.editorManager.encode());
    }


    private void saveHistory() {
        HistoryManager historyManager = parameters.historyManager;
        if (historyManager == null) return;
        startSection("starting_hexes");
        builder.append(historyManager.encodeStartingHexes());
        startSection("events_list");
        builder.append(historyManager.encodeEventsList());
        startSection("starting_provinces");
        builder.append(historyManager.encodeStartingProvinces());
    }


    private void saveCoreModel() {
        CoreModel coreModel = parameters.coreModel;
        if (coreModel == null) return;
        startSection("core_init");
        builder.append(coreModel.encodeInitialization());
        startSection("hexes");
        builder.append(coreModel.encodeHexes());
        startSection("core_current_ids");
        builder.append(coreModel.encodeCurrentIds());
        startSection("player_entities");
        builder.append(coreModel.entitiesManager.encode());
        startSection("provinces");
        builder.append(coreModel.provincesManager.encode());
        startSection("ready");
        builder.append(coreModel.readinessManager.encode());
        startSection("rules");
        builder.append(coreModel.encodeRules());
        startSection("turn");
        builder.append(coreModel.turnsManager.encode());
        startSection("diplomacy");
        builder.append(coreModel.diplomacyManager.encode());
        startSection("mail_basket");
        builder.append(coreModel.lettersManager.encode());
        startSection("fog");
        builder.append(coreModel.fogOfWarManager.enabled);
    }


    private void addTitle() {
        builder.append("onliyoy_level_code");
    }


    private void saveCameraPosition() {
        if (parameters.cameraCode.length() < 2) return;
        startSection("camera");
        builder.append(parameters.cameraCode);
    }


    private void saveClientInitializationData() {
        if (parameters.initialLevelSize == null) return;
        startSection("client_init");
        builder.append(parameters.initialLevelSize);
        builder.append(",");
        builder.append(parameters.aiVersionCode);
    }


    private void startSection(String name) {
        builder.append("#").append(name).append(":");
    }
}
