package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NetOptionsData;
import yio.tro.onliyoy.net.shared.NmType;

public class AnirWelcome extends AbstractNetInputReaction{

    @Override
    public void apply() {
        String[] split = value.split("#");
        root.userData.decode(split[0]);
        if (split.length > 1) {
            root.customizationData.decode(split[1]);
        }
        root.netExperienceManager.experience = root.userData.experience;
        if (split.length > 2) {
            root.initialStatisticsData.decode(split[2]);
        }
        Scenes.entry.onWelcomeMessageReceived();
        checkForSignedInStuff();
    }


    private void checkForSignedInStuff() {
        if (root.userData.role.ordinal() < NetRole.normal.ordinal()) return;
        SkinManager.getInstance().setSkinType(root.customizationData.skinType);
        SettingsManager.getInstance().autoLogin = true;
        SettingsManager.getInstance().saveValues();
        PostponedReactionsManager.aprHintProfile.launch();
        checkToSendOptions();
    }


    private void checkToSendOptions() {
        if (!hasToSendOptions()) return;
        NetOptionsData netOptionsData = new NetOptionsData();
        netOptionsData.setBy(SettingsManager.getInstance());
        root.sendMessage(NmType.options, netOptionsData.encode());
    }


    private boolean hasToSendOptions() {
        if (SettingsManager.getInstance().hideStatistics) return true;
        return false;
    }
}
