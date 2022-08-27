package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.core_model.AbstractRuleset;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.EconomyListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneProfitReport extends ModalSceneYio {

    Province province;
    private ButtonYio mainLabel;
    private double h;
    private CustomizableListYio customizableListYio;
    private boolean darken;


    @Override
    protected void initialize() {
        createCloseButton();
        createMainLabel();
        createList();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        addTitleItem();
        if (province == null) return;
        darken = true;
        addEconomyListItem("lands", calculateLands());
        addEconomyListItem("farms", calculateFarms());
        addEconomyListItem("units", calculateUnits());
        addEconomyListItem("towers", calculateTowers());
        addEconomyListItem("trees", calculateTrees());
    }


    private int calculateTrees() {
        int pines = province.countPieces(PieceType.pine);
        int palms = province.countPieces(PieceType.palm);
        int trees = palms + pines;
        return -trees;
    }


    private int calculateTowers() {
        int sum = 0;
        for (Hex hex : province.getHexes()) {
            if (!hex.hasTower()) continue;
            sum += getRuleset().getConsumption(hex.piece);
        }
        return -sum;
    }


    private int calculateUnits() {
        int sum = 0;
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            sum += getRuleset().getConsumption(hex.piece);
        }
        return -sum;
    }


    private int calculateFarms() {
        int quantity = province.countPieces(PieceType.farm);
        int farmAdditionalIncome = getRuleset().getHexIncome(PieceType.farm) - 1;
        return quantity * farmAdditionalIncome;
    }


    private AbstractRuleset getRuleset() {
        return getViewableModel().ruleset;
    }


    private int calculateLands() {
        return province.getHexes().size();
    }


    private void addEconomyListItem(String name, int deltaValue) {
        EconomyListItem economyListItem = new EconomyListItem();
        economyListItem.setValues(name, deltaValue);
        economyListItem.setDarken(darken);
        darken = !darken;
        customizableListYio.addItem(economyListItem);
    }


    private void addTitleItem() {
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(languagesManager.getString("economy"));
        customizableListYio.addItem(titleListItem);
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(mainLabel)
                .setCornerRadius(0)
                .setSize(0.95, h - 0.02)
                .centerHorizontal()
                .enableEmbeddedMode()
                .setScrollingEnabled(false)
                .alignTop(0.01);
    }


    public void setProvince(Province province) {
        this.province = province;
    }


    private void createMainLabel() {
        h = 0.435;
        mainLabel = uiFactory.getButton()
                .setSize(1.02, h)
                .centerHorizontal()
                .alignTop(-0.001)
                .setCornerRadius(0)
                .setAlphaEnabled(false)
                .setAnimation(AnimationYio.up)
                .setSilentReactionMode(true)
                .setAppearParameters(MovementType.inertia, 1.5)
                .setDestroyParameters(MovementType.inertia, 1.5);
    }
}
