package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.AdvancedConstructionPanelElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.ConstructionViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.EconomicsViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.IConstructionView;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneProvinceManagement extends ModalSceneYio {


    private EconomicsViewElement economicsViewElement;
    private ConstructionViewElement cvElement;
    private AdvancedConstructionPanelElement acpElement;
    public IConstructionView constructionView;


    @Override
    protected void initialize() {
        economicsViewElement = uiFactory.getEconomicsViewElement()
                .setSize(1, 0.1)
                .centerHorizontal()
                .alignTop(0)
                .setAnimation(AnimationYio.up)
                .setKey("economics")
                .setAppearParameters(MovementType.approach, 8)
                .setDestroyParameters(MovementType.lighty, 7);

        cvElement = uiFactory.getConstructionViewElement()
                .setSize(1, 0.1)
                .centerHorizontal()
                .alignBottom(0)
                .setAnimation(AnimationYio.down)
                .setKey("construction")
                .setAllowedToAppear(getDefaultConstructionViewCondition())
                .setAppearParameters(MovementType.approach, 8)
                .setDestroyParameters(MovementType.lighty, 7);

        double acpHeight = GraphicsYio.convertToHeight(1d / 8d);
        acpElement = uiFactory.getAdvancedConstructionPanelElement()
                .setSize(1, acpHeight)
                .alignLeft(0)
                .alignBottom(0)
                .setAnimation(AnimationYio.down)
                .setAllowedToAppear(getAcpCondition())
                .setAppearParameters(MovementType.approach, 8)
                .setDestroyParameters(MovementType.lighty, 7);
    }


    private ConditionYio getAcpCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return SettingsManager.getInstance().constructionPanel;
            }
        };
    }


    private ConditionYio getDefaultConstructionViewCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return !SettingsManager.getInstance().constructionPanel;
            }
        };
    }


    @Override
    protected void onEndCreation() {
        super.onEndCreation();
        Scenes.gameOverlay.forceElementsToTop();
        Scenes.mechanicsOverlay.forceElementsToTop();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateConstructionViewReference();
        Scenes.phraseButton.destroy();
    }


    private void updateConstructionViewReference() {
        if (SettingsManager.getInstance().constructionPanel) {
            constructionView = acpElement;
            return;
        }
        constructionView = cvElement;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Scenes.phraseButton.create();
    }


    public void syncEconomicsView() {
        if (economicsViewElement != null) {
            economicsViewElement.syncWithSelectedProvince();
        }
    }


    public void onRulesDefined() {
        if (constructionView == null) return;
        constructionView.onRulesDefined();
    }


    public void onUnitSelected() {
        if (constructionView == null) return;
        constructionView.onUnitSelected();
    }


    public PieceType getCurrentlyChosenPieceType() {
        if (constructionView == null) return null;
        return constructionView.getCurrentlyChosenPieceType();
    }


    public void onPieceBuiltByHand() {
        if (constructionView == null) return;
        constructionView.onPieceBuiltByHand();
    }


    public void onPcKeyPressed(int keycode) {
        if (constructionView == null) return;
        constructionView.onPcKeyPressed(keycode);
    }
}
