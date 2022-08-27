package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirUpdateCustomizationData extends AbstractNetInputReaction{

    @Override
    public void apply() {
        root.customizationData.decode(value);
        SkinManager.getInstance().setSkinType(root.customizationData.skinType);
        if (Scenes.shop.isCurrentlyVisible()) {
            Scenes.shop.onCustomizationDataUpdated();
        }
    }
}
