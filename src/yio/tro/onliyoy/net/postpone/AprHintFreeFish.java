package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetExperienceManager;
import yio.tro.onliyoy.net.shared.NetValues;

public class AprHintFreeFish extends AbstractPostponedReaction{

    public AprHintFreeFish(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        if (OneTimeInfo.getInstance().hintFreeFish) return false;
        if (!Scenes.chooseGameMode.isCurrentlyVisible()) return false;
        if (Scenes.chooseGameMode.fishButton.getFactor().getValue() < 0.9) return false;
        long hoursOnline = root.initialStatisticsData.getHoursOnline();
        if (hoursOnline < 5) return false;
        return true;
    }


    @Override
    void apply() {
        Scenes.forefinger.create();
        Scenes.forefinger.forefinger.setTarget(Scenes.chooseGameMode.fishButton);
    }
}
