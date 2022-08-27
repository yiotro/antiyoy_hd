package yio.tro.onliyoy.net;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetValues;
import yio.tro.onliyoy.stuff.RepeatYio;

public class NetExperienceManager {

    NetRoot netRoot;
    public long experience;
    boolean readyToShowExperienceView;
    RepeatYio<NetExperienceManager> repeatShowExperienceView;
    long viewStartExp;
    long viewEndExp;


    public NetExperienceManager(NetRoot netRoot) {
        this.netRoot = netRoot;
        experience = 0;
        readyToShowExperienceView = false;
        initRepeats();
    }


    private void initRepeats() {
        repeatShowExperienceView = new RepeatYio<NetExperienceManager>(this, 6) {
            @Override
            public void performAction() {
                checkToShowExperienceView();
            }
        };
    }


    public static int convertExperienceToLevel(long experience) {
        return 1 + (int) (experience / NetValues.EXPERIENCE_PER_LEVEL);
    }


    public static boolean shouldReceiveFish(int level) {
        return false;
    }


    public void onExperienceChanged(long newValue) {
        if (newValue <= experience) return;
        viewStartExp = experience;
        viewEndExp = newValue;
        experience = newValue;
        readyToShowExperienceView = true;
    }


    public void move() {
        repeatShowExperienceView.move();
    }


    private void checkToShowExperienceView() {
        if (!readyToShowExperienceView) return;
        if (Scenes.experienceView.isCurrentlyVisible()) return;
        if (netRoot.userData.role == NetRole.guest && !netRoot.isInLocalMode()) return;
        if (netRoot.yioGdxGame.gameView.appearFactor.getValue() > 0) return;
        readyToShowExperienceView = false;
        Scenes.experienceView.create();
        Scenes.experienceView.experienceViewElement.setValues(viewStartExp, viewEndExp);
    }
}
