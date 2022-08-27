package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.tutorial.TutorialManager;
import yio.tro.onliyoy.game.tutorial.TutorialType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.stuff.*;

public class TliInnerItem {

    TutorialListItem tutorialListItem;
    TutorialType tutorialType;
    public RenderableTextYio title;
    PointYio delta;
    public RectangleYio incBounds;
    public SelectionEngineYio selectionEngineYio;
    public RectangleYio touchPosition;
    public boolean completed;
    public CircleYio iconPosition;


    public TliInnerItem(TutorialListItem tutorialListItem, TutorialType tutorialType) {
        this.tutorialListItem = tutorialListItem;
        this.tutorialType = tutorialType;
        initTitle();
        delta = new PointYio();
        incBounds = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
        touchPosition = new RectangleYio();
        completed = false;
        iconPosition = new CircleYio();
        iconPosition.radius = 0.055f * GraphicsYio.width;
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        title.setString(LanguagesManager.getInstance().getString("" + tutorialType));
        title.updateMetrics();
    }


    void updateCompletedStatus() {
        CustomizableListYio customizableListYio = tutorialListItem.customizableListYio;
        YioGdxGame yioGdxGame = customizableListYio.menuControllerYio.yioGdxGame;
        TutorialManager tutorialManager = yioGdxGame.gameController.tutorialManager;
        completed = tutorialManager.isCompleted(tutorialType);
        if (completed) {
            // to achieve symmetry
            title.setString("M");
            title.updateMetrics();
        }
    }


    void move() {
        updateTitlePosition();
        updateIncBounds();
        moveSelection();
        updateTouchPosition();
        updateIconPosition();
    }


    private void updateIconPosition() {
        iconPosition.center.x = incBounds.x + incBounds.width / 2;
        iconPosition.center.y = incBounds.y + incBounds.height / 2;
    }


    private void updateTouchPosition() {
        touchPosition.height = Math.min(title.height + tutorialListItem.innerOffset, tutorialListItem.viewPosition.height);
        touchPosition.y = title.position.y - title.height / 2 - touchPosition.height / 2;
        touchPosition.width = title.width + tutorialListItem.innerOffset;
        touchPosition.x = title.position.x + title.width / 2 - touchPosition.width / 2;
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return touchPosition.isPointInside(touchPoint);
    }


    private void moveSelection() {
        if (tutorialListItem.customizableListYio.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(0.03f * GraphicsYio.width);
    }


    private void updateTitlePosition() {
        title.position.x = tutorialListItem.viewPosition.x + delta.x;
        title.position.y = tutorialListItem.viewPosition.y + delta.y;
        title.updateBounds();
    }
}
