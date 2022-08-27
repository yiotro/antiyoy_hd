package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.view.game_renders.GameRendersList;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class TmVerification extends TouchMode{

    public ArrayList<TmvViewItem> viewItems;
    ObjectPoolYio<TmvViewItem> poolItems;


    public TmVerification(GameController gameController) {
        super(gameController);
        viewItems = new ArrayList<>();
        initPools();
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<TmvViewItem>(viewItems) {
            @Override
            public TmvViewItem makeNewObject() {
                return new TmvViewItem();
            }
        };
    }


    @Override
    public void onModeBegin() {

    }


    public void updateItems() {
        poolItems.clearExternalList();
        for (Province province : getViewableModel().provincesManager.provinces) {
            Hex capital = getCapital(province);
            if (capital == null) continue;
            TmvViewItem freshObject = poolItems.getFreshObject();
            freshObject.update(capital);
        }
    }


    private Hex getCapital(Province province) {
        ArrayList<Hex> hexes = province.getHexes();
        for (Hex hex : hexes) {
            if (hex.piece == PieceType.city) return hex;
        }
        if (hexes.size() > 0) return hexes.get(0);
        return null;
    }


    @Override
    public void onModeEnd() {

    }


    @Override
    public void move() {

    }


    @Override
    public boolean isCameraMovementEnabled() {
        return true;
    }


    @Override
    public void onTouchDown() {

    }


    @Override
    public void onTouchDrag() {

    }


    @Override
    public void onTouchUp() {

    }


    @Override
    public boolean onClick() {
        return false;
    }


    @Override
    public String getNameKey() {
        return null;
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmVerification;
    }
}
