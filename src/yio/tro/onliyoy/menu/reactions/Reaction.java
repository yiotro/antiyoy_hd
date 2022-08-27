package yio.tro.onliyoy.menu.reactions;

import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.YioGdxGame;

public abstract class Reaction {

    public static RbNothing rbNothing;
    protected GameController gameController;
    protected MenuControllerYio menuControllerYio;
    protected YioGdxGame yioGdxGame;


    public static void initialize() {
        rbNothing = new RbNothing();
    }


    protected abstract void apply();


    public void perform(MenuControllerYio menuControllerYio) {
        if (menuControllerYio != null) {
            this.menuControllerYio = menuControllerYio;
            yioGdxGame = menuControllerYio.yioGdxGame;
            gameController = yioGdxGame.gameController;
        }
        apply();
    }


}
