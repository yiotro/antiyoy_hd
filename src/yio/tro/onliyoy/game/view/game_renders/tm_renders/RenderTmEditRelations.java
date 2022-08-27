package yio.tro.onliyoy.game.view.game_renders.tm_renders;

import yio.tro.onliyoy.game.touch_modes.TmDiplomacy;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.view.game_renders.GameRender;

public class RenderTmEditRelations extends RenderTmDiplomacy {

    @Override
    protected TmDiplomacy getTmDiplomacy() {
        return TouchMode.tmEditRelations;
    }
}
