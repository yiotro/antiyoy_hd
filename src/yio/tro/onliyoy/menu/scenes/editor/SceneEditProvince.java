package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.viewable_model.ProvinceSelectionManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneEditProvince extends ModalSceneYio {

    public static final int NAME_LIMIT = 12;
    private ButtonYio cityNameButton;
    private int[] moneyValues;
    private SliderElement moneySlider;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.275);
        createCityNameButton();
        createMoneySlider();
        createRelationsButton();
    }


    private void createRelationsButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.55, 0.05)
                .centerHorizontal()
                .alignBottom(0.005)
                .setBackground(BackgroundYio.gray)
                .applyText("relations")
                .setAllowedToAppear(getRelationsCondition())
                .setReaction(getRelationsReaction());
    }


    private Reaction getRelationsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onRelationsButtonPressed();
            }
        };
    }


    private void onRelationsButtonPressed() {
        ViewableModel viewableModel = getObjectsLayer().viewableModel;
        ProvinceSelectionManager provinceSelectionManager = viewableModel.provinceSelectionManager;
        Province selectedProvince = provinceSelectionManager.selectedProvince;
        if (selectedProvince == null) return;
        if (viewableModel.entitiesManager.getEntity(selectedProvince.getColor()) == null) return;
        destroy();
        provinceSelectionManager.changeSelectionExternally(selectedProvince, false);
        provinceSelectionManager.forceAppearance();
        getGameController().setTouchMode(TouchMode.tmEditRelations);
    }


    private ConditionYio getRelationsCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return getViewableModel().diplomacyManager.enabled;
            }
        };
    }


    private void createMoneySlider() {
        moneyValues = new int[]{0, 5, 7, 9, 10, 12, 15, 20, 25, 50, 100, 250, 500, 1000, 2000, 5000, 10000, 25000, 100000, 1000000};
        String[] compactValues = new String[moneyValues.length];
        for (int i = 0; i < compactValues.length; i++) {
            compactValues[i] = Yio.getCompactValueString(moneyValues[i]);
        }
        moneySlider = uiFactory.getSlider()
                .setParent(defaultPanel)
                .setWidth(0.8)
                .centerHorizontal()
                .alignUnder(previousElement, 0.03)
                .setTitle("money")
                .setPossibleValues(compactValues)
                .setValueChangeReaction(getMoneySliderReaction());
    }


    private Reaction getMoneySliderReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (getSelectedProvince() == null) return;
                int valueIndex = moneySlider.getValueIndex();
                int moneyValue = moneyValues[valueIndex];
                getSelectedProvince().setMoney(moneyValue);
            }
        };
    }


    private void createCityNameButton() {
        cityNameButton = uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.7, 0.06)
                .alignTop(0.02)
                .centerHorizontal()
                .setBackground(BackgroundYio.gray)
                .applyText("-")
                .setReaction(getCityNameReaction());
    }


    private Reaction getCityNameReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onCityNameButtonPressed();
            }
        };
    }


    private void onCityNameButtonPressed() {
        if (getSelectedProvince() == null) return;
        Scenes.keyboard.create();
        Scenes.keyboard.setValue(getSelectedProvince().getCityName());
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                if (input.length() > NAME_LIMIT) {
                    input = input.substring(0, NAME_LIMIT);
                }
                getSelectedProvince().setCityName(input);
                loadValues();
            }
        });
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getObjectsLayer().viewableModel.provinceSelectionManager.changeSelectionExternally(null);
    }


    private void loadValues() {
        if (getSelectedProvince() == null) return;
        cityNameButton.applyText(getSelectedProvince().getCityName());
        moneySlider.setValueIndex(convertMoneyIntoSliderIndex(getSelectedProvince().getMoney()));
    }


    private int convertMoneyIntoSliderIndex(int money) {
        for (int i = 0; i < moneyValues.length; i++) {
            if (money <= moneyValues[i]) return i;
        }
        return 0;
    }


    private Province getSelectedProvince() {
        ViewableModel viewableModel = getGameController().objectsLayer.viewableModel;
        return viewableModel.provinceSelectionManager.selectedProvince;
    }
}
