package yio.tro.onliyoy.menu.menu_renders;

import yio.tro.onliyoy.game.touch_modes.TmDiplomacy;
import yio.tro.onliyoy.game.touch_modes.TouchMode;

public class RenderEditRelationsItems extends RenderDiplomaticItems{

    @Override
    protected TmDiplomacy getTmDiplomacy() {
        return TouchMode.tmEditRelations;
    }
}
