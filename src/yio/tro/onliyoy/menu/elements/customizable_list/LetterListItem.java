package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.Condition;
import yio.tro.onliyoy.game.core_model.Letter;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.ArrayList;

public class LetterListItem extends AbstractCustomListItem{

    public Letter letter;
    public CircleYio iconPosition;
    public RenderableTextYio renderableTextYio;


    @Override
    protected void initialize() {
        iconPosition = new CircleYio();
        iconPosition.radius = (float) (0.3f * getHeight());
        renderableTextYio = new RenderableTextYio();
        renderableTextYio.setFont(Fonts.miniFont);
        renderableTextYio.setString("-");
        renderableTextYio.updateMetrics();
    }


    @Override
    protected void move() {
        updateIconPosition();
        moveText();
    }


    private void moveText() {
        renderableTextYio.position.x = viewPosition.x + 0.06f * GraphicsYio.width + 2 * iconPosition.radius;
        renderableTextYio.position.y = viewPosition.y + viewPosition.height / 2 + renderableTextYio.height / 2;
        renderableTextYio.updateBounds();
    }


    private void updateIconPosition() {
        iconPosition.radius = 0.3f * viewPosition.height;
        iconPosition.center.y = viewPosition.y + viewPosition.height / 2;
        iconPosition.center.x = viewPosition.x + 0.03f * GraphicsYio.width + iconPosition.radius;
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.06f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {
        Scenes.inbox.destroy();
        Scenes.readLetter.create();
        Scenes.readLetter.loadValues(letter);
    }


    @Override
    protected void onLongTapped() {

    }


    public void setLetter(Letter letter) {
        this.letter = letter;
        updateText();
    }


    private void updateText() {
        ViewableModel viewableModel = customizableListYio.getViewableModel();
        PlayerEntity sender = viewableModel.entitiesManager.getEntity(letter.senderColor);
        StringBuilder builder = new StringBuilder();
        builder.append(sender.name).append(" (");
        ArrayList<Condition> conditions = letter.conditions;
        for (int i = 0; i < conditions.size(); i++) {
            Condition condition = conditions.get(i);
            String typeString = LanguagesManager.getInstance().getString("" + condition.type);
            builder.append(typeString.toLowerCase());
            if (i < conditions.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(")");
        renderableTextYio.setString(builder.toString());
        renderableTextYio.updateMetrics();
    }


    @Override
    public String getKey() {
        return "letter";
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderLetterListItem;
    }
}
