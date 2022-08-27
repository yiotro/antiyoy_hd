package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public abstract class AbstractChoiceItem extends AbstractCustomListItem{

    public RenderableTextYio title;
    public boolean active;
    public CircleYio iconPosition;
    Reaction clickReaction;


    @Override
    protected void initialize() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        active = false;
        iconPosition = new CircleYio();
        clickReaction = null;
    }


    public void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    @Override
    protected void move() {
        moveRenderableTextByDefault(title);
        updateIconPosition();
    }


    private void updateIconPosition() {
        iconPosition.setRadius(0.2f * getHeight());
        iconPosition.center.x = viewPosition.x + viewPosition.width - viewPosition.height / 2;
        iconPosition.center.y = viewPosition.y + viewPosition.height / 2;
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.08f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {
        title.delta.x = 0.04f * GraphicsYio.width;
        title.delta.y = (float) (getHeight() / 2 + title.height / 2);
    }


    @Override
    protected void onClicked() {
        clickReaction.perform(customizableListYio.menuControllerYio);
    }


    @Override
    protected void onLongTapped() {

    }


    public void setClickReaction(Reaction clickReaction) {
        this.clickReaction = clickReaction;
    }


    public void setActive(boolean active) {
        this.active = active;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderChoiceListItem;
    }
}
