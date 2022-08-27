package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.debug.DebugFlags;

public class AnirSoundAlert extends AbstractNetInputReaction{

    @Override
    public void apply() {
        SoundManager.playSound(SoundType.alert, true);
    }
}
