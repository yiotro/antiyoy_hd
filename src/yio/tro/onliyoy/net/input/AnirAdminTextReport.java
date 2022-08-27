package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirAdminTextReport extends AbstractNetInputReaction{

    @Override
    public void apply() {
        Scenes.adminTextReport.setMessage(value);
        Scenes.adminTextReport.create();
    }
}
