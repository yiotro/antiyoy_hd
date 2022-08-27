package yio.tro.onliyoy.net.input;

public class AnirUpdateMatchBattleData extends AbstractNetInputReaction{

    @Override
    public void apply() {
        root.currentMatchData.decode(value);
    }
}
