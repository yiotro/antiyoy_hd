package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetUserDossierData;

public class AnirUserDossier extends AbstractNetInputReaction{

    @Override
    public void apply() {
        NetUserDossierData netUserDossierData = new NetUserDossierData();
        netUserDossierData.decode(value);
        if (Scenes.mlUserInfo.isCurrentlyVisible()) {
            Scenes.mlUserInfo.onDossierReceived(netUserDossierData);
        }
        if (Scenes.userDossierByRating.isCurrentlyVisible()) {
            Scenes.userDossierByRating.onDossierReceived(netUserDossierData);
        }
    }
}
