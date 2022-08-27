package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.net.shared.NmbdItem;

public class AnirDebugRequestCurrentTurn extends AbstractNetInputReaction{

    @Override
    public void apply() {
        HColor currentColor = objectsLayer.viewableModel.entitiesManager.getCurrentColor();
        NmbdItem nmbdItem = root.currentMatchData.getItem(currentColor);
        String message = "null";
        if (nmbdItem != null) {
            message = nmbdItem.name + " has current turn";
        }
        root.sendMessage(NmType.debug_text, message);
    }
}
