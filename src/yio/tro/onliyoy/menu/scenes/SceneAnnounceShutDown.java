package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.TextFitParser;
import yio.tro.onliyoy.stuff.RepeatYio;

import java.util.ArrayList;

public class SceneAnnounceShutDown extends ModalSceneYio{

    private double height;
    RepeatYio<SceneAnnounceShutDown> repeatCheckToDestroy;
    RepeatYio<SceneAnnounceShutDown> repeatUpdate;
    long targetTime;


    @Override
    protected void initialize() {
        height = 0.25;
        createDefaultPanel(height);
        initRepeats();
    }


    private void initRepeats() {
        repeatCheckToDestroy = new RepeatYio<SceneAnnounceShutDown>(this, 20 * 60) {
            @Override
            public void performAction() {
                parent.checkToDestroy();
            }
        };
        repeatUpdate = new RepeatYio<SceneAnnounceShutDown>(this, 30) {
            @Override
            public void performAction() {
                parent.updateString();
            }
        };
    }


    public void setTargetTime(long targetTime) {
        this.targetTime = targetTime;
        updateString();
    }


    public void updateString() {
        repeatCheckToDestroy.resetCountDown(); // time can be received when scene is already visible
        repeatUpdate.resetCountDown();
        if (System.currentTimeMillis() > targetTime) return;
        long deltaTime = targetTime - System.currentTimeMillis();
        int timeInFrames = Yio.convertMillisIntoFrames(deltaTime);
        String source = languagesManager.getString("announce_shut_down");
        String parsedString = source.replace("[time]", Yio.convertTimeToUnderstandableString(timeInFrames));
        String restorationString = "";
        if (!yioGdxGame.gamePaused && yioGdxGame.gameController.objectsLayer.viewableModel.isNetMatch()) {
            restorationString = " " + languagesManager.getString("match_will_be_restored");
        }
        updateText(parsedString + restorationString);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        targetTime = 0;
        repeatCheckToDestroy.resetCountDown();
    }


    @Override
    public void move() {
        super.move();
        repeatCheckToDestroy.move();
        repeatUpdate.move();
    }


    void checkToDestroy() {
        if (!isCurrentlyVisible()) return;
        destroy();
    }


    public void updateText(String string) {
        ArrayList<String> strings = convertStringToArray(string);
        updateText(strings);
    }


    public void updateText(ArrayList<String> strings) {
        ArrayList<String> parsed = TextFitParser.getInstance().parseText(
                strings,
                Fonts.gameFont,
                0.92f * defaultPanel.getPosition().width,
                false
        );

        updateLabelHeight(parsed.size());
        defaultPanel.applyManyLines(parsed);
    }


    private void updateLabelHeight(int stringsQuantity) {
        double h = 0.05 + stringsQuantity * 0.055;
        h = Math.max(h, 0.2);
        defaultPanel.setSize(1, h);
    }
}
