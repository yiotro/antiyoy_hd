package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.MlEntityItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.net.shared.NmbdItem;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneMatchInfoPanel extends ModalSceneYio {


    private ButtonYio mainLabel;
    private CustomizableListYio customizableListYio;


    @Override
    protected void initialize() {
        createCloseButton();
        createMainLabel();
        createCustomizableList();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(languagesManager.getString("players"));
        customizableListYio.addItem(titleListItem);
        loadActivePlayers();
        loadSpectators();
    }


    private void loadSpectators() {
        for (NmbdItem nmbdItem : netRoot.currentMatchData.items) {
            if (nmbdItem.color != null) continue;
            MlEntityItem mlEntityItem = new MlEntityItem();
            mlEntityItem.setCalmAnimationMode(true);
            mlEntityItem.setClientId(nmbdItem.id);
            mlEntityItem.setValues(nmbdItem.name, nmbdItem.avatarType);
            mlEntityItem.setColor(null);
            mlEntityItem.setMatchId(netRoot.currentMatchData.matchId);
            customizableListYio.addItem(mlEntityItem);
        }
    }


    private void loadActivePlayers() {
        for (PlayerEntity entity : getViewableModel().entitiesManager.entities) {
            MlEntityItem mlEntityItem = new MlEntityItem();
            NmbdItem item = getItem(entity.color);
            mlEntityItem.setCalmAnimationMode(true);
            if (item != null) {
                mlEntityItem.setClientId(item.id);
                mlEntityItem.setValues(item.name, item.avatarType);
                mlEntityItem.setColor(item.color);
                mlEntityItem.setMatchId(netRoot.currentMatchData.matchId);
            } else {
                mlEntityItem.setClientId("-");
                mlEntityItem.setValues(languagesManager.getString("ai"), AvatarType.robot);
                mlEntityItem.setColor(entity.color);
                mlEntityItem.setClickAllowed(false);
            }
            customizableListYio.addItem(mlEntityItem);
        }
    }


    private NmbdItem getItem(HColor color) {
        for (NmbdItem item : netRoot.currentMatchData.items) {
            if (item.color == color) return item;
        }
        return null;
    }


    private void createCustomizableList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(mainLabel)
                .enableEmbeddedMode()
                .setSize(0.9, 0.4)
                .setCornerRadius(0)
                .centerHorizontal()
                .centerVertical();
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setSize(1.02, 0.4)
                .centerHorizontal()
                .alignTop(0)
                .setCornerRadius(0)
                .setAlphaEnabled(false)
                .setAnimation(AnimationYio.up)
                .setSilentReactionMode(true)
                .setAppearParameters(MovementType.inertia, 1.5)
                .setDestroyParameters(MovementType.inertia, 1.5);
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
