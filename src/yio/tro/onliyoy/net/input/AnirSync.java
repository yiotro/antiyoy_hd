package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.net.shared.NmType;

public class AnirSync extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (DebugFlags.humanImitation) {
            SoundManager.playSound(SoundType.coin, true);
            showCurrentLevelCodeInConsole();
        }
        objectsLayer.syncManager.apply(value);
    }


    private void showCurrentLevelCodeInConsole() {
        if (!DebugFlags.desyncInvestigation) return;
        ExportParameters parameters = new ExportParameters();
        ObjectsLayer objectsLayer = viewableModel.objectsLayer;
        parameters.setInitialLevelSize(objectsLayer.gameController.sizeManager.initialLevelSize);
        parameters.setCoreModel(viewableModel);
        String levelCode = objectsLayer.exportManager.perform(parameters);
        root.sendMessage(NmType.debug_text, levelCode);
    }
}
