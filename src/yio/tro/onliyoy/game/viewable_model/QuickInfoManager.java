package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NmbdItem;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class QuickInfoManager {

    ViewableModel viewableModel;
    public ArrayList<QimItem> items;
    ObjectPoolYio<QimItem> poolItems;
    RepeatYio<QuickInfoManager> repeatRemoveDead;


    public QuickInfoManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        items = new ArrayList<>();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveDead = new RepeatYio<QuickInfoManager>(this, 120) {
            @Override
            public void performAction() {
                parent.removeDeadItems();
            }
        };
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<QimItem>(items) {
            @Override
            public QimItem makeNewObject() {
                return new QimItem(QuickInfoManager.this);
            }
        };
    }


    public void onHexClicked(Hex hex) {
        if (!viewableModel.isNetMatch()) return;
        if (viewableModel.entitiesManager.getCurrentEntity().isHuman()) return;
        Province province = hex.getProvince();
        if (province == null) return;
        NetRoot netRoot = viewableModel.objectsLayer.gameController.yioGdxGame.netRoot;
        if (netRoot.currentMatchData == null) return;
        NmbdItem item = netRoot.currentMatchData.getItem(netRoot.userData.id);
        if (item == null) return;
        if (province.getColor() != item.color) return;
        poolItems.getFreshObject().launch(province);
    }


    public void move() {
        moveItems();
        repeatRemoveDead.move();
    }


    private void removeDeadItems() {
        for (int i = items.size() - 1; i >= 0; i--) {
            FactorYio appearFactor = items.get(i).appearFactor;
            if (!appearFactor.isInDestroyState()) continue;
            if (appearFactor.getValue() > 0) continue;
            items.remove(i);
        }
    }


    private void moveItems() {
        for (QimItem qimItem : items) {
            qimItem.move();
        }
    }
}
