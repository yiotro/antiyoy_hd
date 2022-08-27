package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirNextFishIn extends AbstractNetInputReaction{

    @Override
    public void apply() {
        long deltaTimeMillis = Long.valueOf(value);
        String sourceString = LanguagesManager.getInstance().getString("next_fish_in");
        int deltaTimeFrames = Yio.convertMillisIntoFrames(deltaTimeMillis);
        String timeString = Yio.convertTimeToUnderstandableString(deltaTimeFrames);
        String message = sourceString.replace("[time]", timeString);
        Scenes.notification.show(message);
    }
}
