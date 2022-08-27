package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetCheckInData;

public class AnirPleaseCheckIn extends AbstractNetInputReaction{

    @Override
    public void apply() {
        Scenes.checkIn.setNameFromServer(value);
        Scenes.checkIn.create();
    }
}
