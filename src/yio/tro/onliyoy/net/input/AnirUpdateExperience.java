package yio.tro.onliyoy.net.input;

public class AnirUpdateExperience extends AbstractNetInputReaction{

    @Override
    public void apply() {
        long exp = Long.valueOf(value);
        yioGdxGame.netRoot.netExperienceManager.onExperienceChanged(exp);
    }
}
