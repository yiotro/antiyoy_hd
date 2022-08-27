package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.MlEntityItem;
import yio.tro.onliyoy.menu.elements.net.ChooseColorElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class SceneChooseColor extends ModalSceneYio{

    ArrayList<HColor> validColors;
    private ChooseColorElement chooseColorElement;
    IColorChoiceListener listener;


    public SceneChooseColor() {
        validColors = new ArrayList<>();
    }


    @Override
    protected void initialize() {
        createCloseButton();
        createChooseMlColorElement();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        listener = null;
    }


    private void createChooseMlColorElement() {
        chooseColorElement = uiFactory.getChooseMlColorElement()
                .setSize(0.96, GraphicsYio.convertToHeight(0.26))
                .centerHorizontal()
                .alignBottom(0.04)
                .setAnimation(AnimationYio.down)
                .setAppearParameters(MovementType.approach, 3)
                .setDestroyParameters(MovementType.lighty, 4.5);
    }


    public void loadValues(CoreModel coreModel) {
        updateValidColors(coreModel);
        chooseColorElement.loadValues(validColors);
    }


    private void updateValidColors(CoreModel coreModel) {
        validColors.clear();
        if (coreModel == null) return;
        for (Province province : coreModel.provincesManager.provinces) {
            HColor color = province.getColor();
            if (validColors.contains(color)) continue;
            validColors.add(color);
        }
    }


    public void loadValues(HColor currentColor) {
        updateValidColors(currentColor);
        chooseColorElement.loadValues(validColors);
    }


    public void onColorChosen(HColor color) {
        destroy();
        if (listener != null) {
            listener.onColorChosen(color);
        }
    }


    private void updateValidColors(HColor currentColor) {
        validColors.clear();
        for (PlayerEntity playerEntity : Scenes.matchLobby.netMatchLobbyData.entities) {
            HColor color = playerEntity.color;
            validColors.add(color);
        }
        for (AbstractCustomListItem item : Scenes.matchLobby.customizableListYio.items) {
            if (!(item instanceof MlEntityItem)) continue;
            MlEntityItem mlEntityItem = (MlEntityItem) item;
            HColor color = mlEntityItem.color;
            if (color == null) continue;
            if (color == currentColor) continue;
            validColors.remove(color);
        }
    }


    public void setListener(IColorChoiceListener listener) {
        this.listener = listener;
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
