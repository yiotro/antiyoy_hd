package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.calendar.CalendarViewElement;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneCalendar extends SceneYio{

    public CalendarViewElement calendarViewElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        createCalendarView();
        spawnBackButton(getBackReaction());
    }


    private void createCalendarView() {
        calendarViewElement = uiFactory.getCalendarViewElement()
                .setAnimation(AnimationYio.from_touch)
                .setSize(1, 1);
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
