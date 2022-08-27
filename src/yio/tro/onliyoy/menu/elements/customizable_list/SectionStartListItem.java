package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.LineYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SectionStartListItem extends AbstractCustomListItem{

    public LineYio left, right;
    float offset = 0.1f * GraphicsYio.width;
    public RenderableTextYio title;
    public Difficulty difficulty;
    private float height;
    RectangleYio refPosition;
    public FactorYio transitionFactor;


    @Override
    protected void initialize() {
        left = new LineYio();
        right = new LineYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        refPosition = new RectangleYio();
        transitionFactor = new FactorYio();
    }


    @Override
    protected void move() {
        updateLeftLine();
        updateRightLine();
        updateTitlePosition();
        moveTransitionFactor();
    }


    @Override
    public void onAppear() {
        super.onAppear();
        transitionFactor.reset();
        transitionFactor.appear(MovementType.approach, 1.8);
    }


    private void moveTransitionFactor() {
        if (customizableListYio.getFactor().getValue() < 0.98) return;
        transitionFactor.move();
    }


    @Override
    protected RectangleYio getReferencePosition() {
        refPosition.setBy(customizableListYio.maskPosition);
        refPosition.x = customizableListYio.getPosition().x;
        return refPosition;
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.position.y = viewPosition.y + title.height + 0.005f * GraphicsYio.height;
        title.updateBounds();
    }


    public void setValues(Difficulty difficulty, boolean empty, float height) {
        this.difficulty = difficulty;
        String string = LanguagesManager.getInstance().getString("" + difficulty);
        this.height = height;
        if (empty) {
            string = "";
        }
        title.setString(string);
        title.updateMetrics();
    }


    private void updateRightLine() {
        right.start.x = viewPosition.x + viewPosition.width / 2 + title.width / 2 + offset / 2;
        right.start.y = viewPosition.y + viewPosition.height / 2;
        right.finish.x = viewPosition.x + viewPosition.width - offset;
        right.finish.y = viewPosition.y + viewPosition.height / 2;
    }


    private void updateLeftLine() {
        left.start.x = viewPosition.x + offset;
        left.start.y = viewPosition.y + viewPosition.height / 2;
        left.finish.x = viewPosition.x + viewPosition.width / 2 - title.width / 2 - offset / 2;
        left.finish.y = viewPosition.y + viewPosition.height / 2;
    }


    @Override
    protected double getWidth() {
        return customizableListYio.getPosition().width;
    }


    @Override
    protected double getHeight() {
        return height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {

    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderSectionStartListItem;
    }
}