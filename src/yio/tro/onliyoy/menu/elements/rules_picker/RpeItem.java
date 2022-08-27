package yio.tro.onliyoy.menu.elements.rules_picker;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RpeItem {

    RulesPickerElement rulesPickerElement;
    public RulesType type;
    public RenderableTextYio title;
    String key;
    float delta;


    public RpeItem(RulesPickerElement rulesPickerElement, RulesType type) {
        this.rulesPickerElement = rulesPickerElement;
        this.type = type;
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        key = getKey();
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
        delta = 0;
    }


    private String getKey() {
        switch (type) {
            default:
                return type + "_rules";
            case def:
                return "normal_rules";
        }
    }


    public void setDelta(float delta) {
        this.delta = delta;
    }


    void move() {
        updatePosition();
    }


    public boolean isCurrentlyVisible() {
        return title.bounds.intersects(rulesPickerElement.maskPosition);
    }


    private void updatePosition() {
        RectangleYio srcPos = rulesPickerElement.getViewPosition();
        title.centerVertical(srcPos);
        title.position.x = srcPos.x + srcPos.width / 2 + delta - title.width / 2 + rulesPickerElement.hook;
        title.updateBounds();
    }
}
