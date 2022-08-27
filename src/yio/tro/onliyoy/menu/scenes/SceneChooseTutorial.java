package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.game.tutorial.TutorialType;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneChooseTutorial extends SceneYio {


    private AnnounceViewElement mainView;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getBackReaction());
        createMainView();
        createButtons();
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(mainView)
                .setSize(0.6, 0.05)
                .centerHorizontal()
                .alignTop(0.08)
                .applyText("basics")
                .setReaction(getTutorialReaction(TutorialType.basics));

        uiFactory.getButton()
                .clone(previousElement)
                .centerHorizontal()
                .alignUnder(previousElement, 0.015)
                .applyText("tactics")
                .setReaction(getTutorialReaction(TutorialType.tactics));

        uiFactory.getButton()
                .clone(previousElement)
                .centerHorizontal()
                .alignUnder(previousElement, 0.015)
                .applyText("diplomacy")
                .setReaction(getTutorialReaction(TutorialType.diplomacy));
    }


    private Reaction getTutorialReaction(final TutorialType tutorialType) {
        return new Reaction() {
            @Override
            protected void apply() {
                gameController.tutorialManager.launch(tutorialType);
            }
        };
    }


    private void createMainView() {
        double h = 0.28;
        mainView = uiFactory.getAnnounceViewElement()
                .setSize(0.8, h)
                .alignBottom(0.45 - h / 2)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .setTitle("tutorial")
                .setText(" ")
                .setTouchable(false);
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                MenuSwitcher.getInstance().createChooseGameModeMenu();
            }
        };
    }
}
