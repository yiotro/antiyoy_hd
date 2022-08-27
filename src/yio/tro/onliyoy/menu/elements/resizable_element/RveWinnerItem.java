package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RveWinnerItem extends AbstractRveItem{

    public EntityType entityType;
    public HColor color;
    public RenderableTextYio leftViewText;
    public RenderableTextYio rightViewText;
    public RectangleYio incBounds;
    float incOffset;
    float horOffset;
    String name;


    @Override
    protected void initialize() {
        entityType = null;
        color = null;
        initLeftViewText();
        rightViewText = new RenderableTextYio();
        rightViewText.setFont(Fonts.miniFont);
        incBounds = new RectangleYio();
        incOffset = 0.015f * GraphicsYio.width;
        horOffset = 0.03f * GraphicsYio.width;
        name = "";
    }


    private void initLeftViewText() {
        leftViewText = new RenderableTextYio();
        leftViewText.setFont(Fonts.miniFont);
        leftViewText.setString(LanguagesManager.getInstance().getString("winner"));
        leftViewText.updateMetrics();
    }


    @Override
    protected void onMove() {
        moveLeftViewText();
        moveRightViewText();
        updateIncBounds();
    }


    private void updateIncBounds() {
        incBounds.setBy(rightViewText.bounds);
        incBounds.increase(incOffset);
    }


    private void moveRightViewText() {
        rightViewText.centerVertical(position);
        rightViewText.position.x = position.x + position.width - horOffset - incOffset - rightViewText.width;
        rightViewText.updateBounds();
    }


    private void moveLeftViewText() {
        leftViewText.centerVertical(position);
        leftViewText.position.x = position.x + horOffset;
        leftViewText.updateBounds();
    }


    @Override
    protected float getHeight() {
        return 0.07f * GraphicsYio.height;
    }


    @Override
    public boolean isInsideDynamicPosition() {
        if (leftViewText.position.x + leftViewText.width > position.x + position.width) return false;
        if (rightViewText.position.x < position.x) return false;
        return super.isInsideDynamicPosition();
    }


    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
        updateRightViewText();
    }


    private void updateRightViewText() {
        String key = getCurrentEntityKey();
        String string = LanguagesManager.getInstance().getString(key);
        rightViewText.setString(string);
        rightViewText.updateMetrics();
    }


    private String getCurrentEntityKey() {
        if (name.length() > 0) {
            return name;
        }
        switch (entityType) {
            default:
                return "human";
            case ai_balancer:
            case ai_random:
                return "robot";
        }
    }


    public void setName(String name) {
        this.name = name;
        updateRightViewText();
    }


    public void setColor(HColor color) {
        this.color = color;
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveWinnerItem;
    }
}
