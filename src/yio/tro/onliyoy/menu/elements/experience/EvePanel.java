package yio.tro.onliyoy.menu.elements.experience;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.net.NetExperienceManager;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class EvePanel {

    ExperienceViewElement experienceViewElement;
    public final int index;
    public RectangleYio viewPosition;
    public RenderableTextYio levelTitle;
    public EveStrip strip;
    public CircleYio fishPosition;
    float horOffset;
    int level;


    public EvePanel(ExperienceViewElement experienceViewElement, int index) {
        this.experienceViewElement = experienceViewElement;
        this.index = index;
        viewPosition = new RectangleYio();
        levelTitle = new RenderableTextYio();
        levelTitle.setFont(Fonts.buttonFont);
        strip = new EveStrip(this);
        horOffset = 0.06f * GraphicsYio.width;
        fishPosition = new CircleYio();
        fishPosition.radius = 0.02f * GraphicsYio.width;
        level = -1;
    }


    public void move() {
        updateViewPosition();
        updateLevelTitlePosition();
        strip.move();
        updateFishPosition();
    }


    private void updateFishPosition() {
        if (!hasToDisplayFish()) return;
        fishPosition.center.x = strip.position.x + strip.position.width - fishPosition.radius;
        fishPosition.center.y = levelTitle.position.y - levelTitle.height / 2;
    }


    private void updateViewPosition() {
        viewPosition.setBy(experienceViewElement.getViewPosition());
        if (index == 1) {
            viewPosition.x += viewPosition.width;
        }
        viewPosition.x -= experienceViewElement.flipFactor.getValue() * viewPosition.width;
    }


    private void updateLevelTitlePosition() {
        levelTitle.position.x = viewPosition.x + horOffset;
        levelTitle.position.y = viewPosition.y + viewPosition.height - 0.035f * GraphicsYio.height;
        levelTitle.updateBounds();
    }


    public void setLevel(int level) {
        this.level = level;
        levelTitle.setString(LanguagesManager.getInstance().getString("level") + " " + level);
        levelTitle.updateMetrics();
    }


    public boolean hasToDisplayFish() {
        return NetExperienceManager.shouldReceiveFish(level + 1);
    }


    public boolean isCurrentlyVisible() {
        if (index == 0 && experienceViewElement.flipFactor.getValue() < 1) return true;
        if (index == 1 && experienceViewElement.flipFactor.getValue() > 0) return true;
        return false;
    }

}
