package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class ConstructionViewElement extends InterfaceElement<ConstructionViewElement> implements IConstructionView {

    public ArrayList<CveButton> buttons;
    private CveButton buildingsButton;
    private CveButton unitsButton;
    boolean touchedCurrently;
    public ArrayList<CveIndicator> indicators;
    ObjectPoolYio<CveIndicator> poolIndicators;
    RepeatYio<ConstructionViewElement> repeatRemoveDeadIndicators;


    public ConstructionViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        indicators = new ArrayList<>();
        buttons = new ArrayList<>();
        updateButtonsToMatchRules();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveDeadIndicators = new RepeatYio<ConstructionViewElement>(this, 120) {
            @Override
            public void performAction() {
                parent.removeDeadIndicators();
            }
        };
    }


    private void initPools() {
        poolIndicators = new ObjectPoolYio<CveIndicator>(indicators) {
            @Override
            public CveIndicator makeNewObject() {
                return new CveIndicator(ConstructionViewElement.this);
            }
        };
    }


    private void updateButtonsToMatchRules() {
        buildingsButton = new CveButton(this, "building", new PieceType[]{
                PieceType.farm,
                PieceType.tower,
                PieceType.strong_tower,
        });
        unitsButton = new CveButton(this, "unit", new PieceType[]{
                PieceType.peasant,
                PieceType.spearman,
                PieceType.baron,
                PieceType.knight,
        });
        buttons.clear();
        buttons.add(buildingsButton);
        buttons.add(unitsButton);
    }


    @Override
    public void onRulesDefined() {
        updateButtonsToMatchRules();
    }


    @Override
    protected ConstructionViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveButtons();
        moveIndicators();
        repeatRemoveDeadIndicators.move();
    }


    private void removeDeadIndicators() {
        for (int i = indicators.size() - 1; i >= 0; i--) {
            if (indicators.get(i).alive) continue;
            indicators.remove(i);
        }
    }


    private void moveIndicators() {
        for (CveIndicator indicator : indicators) {
            indicator.move();
        }
    }


    private void moveButtons() {
        for (CveButton cveButton : buttons) {
            cveButton.move();
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        prepareButtons();
        poolIndicators.clearExternalList();
    }


    private void prepareButtons() {
        float delta = 0.15f * GraphicsYio.width;
        buildingsButton.viewPosition.center.x = 0.5f * GraphicsYio.width - delta;
        unitsButton.viewPosition.center.x = 0.5f * GraphicsYio.width + delta;
        for (CveButton button : buttons) {
            button.resetPieceType();
        }
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    private CveButton getCurrentlyTouchedButton() {
        for (CveButton cveButton : buttons) {
            if (cveButton.isTouchedBy(currentTouch)) return cveButton;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = (getCurrentlyTouchedButton() != null);
        if (!touchedCurrently) return false;
        selectButtons();
        return true;
    }


    private void selectButtons() {
        CveButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return;
        currentlyTouchedButton.selectionEngineYio.applySelection();
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        if (isClicked()) {
            onClick(getCurrentlyTouchedButton());
        }
        return true;
    }


    private void onClick(CveButton cveButton) {
        if (cveButton == null) return;
        SoundManager.playSound(SoundType.button);
        hideAllIndicators();
        spawnIndicator(cveButton);
    }


    private void spawnIndicator(CveButton cveButton) {
        CveIndicator freshObject = poolIndicators.getFreshObject();
        freshObject.spawn(cveButton.pieceType);
        notifyEverythingAboutChosenPiece(freshObject.pieceType);
    }


    private void notifyEverythingAboutChosenPiece(PieceType pieceType) {
        for (CveButton button : buttons) {
            button.onIndicatorSpawned(pieceType);
        }
        getViewableModel().onPieceChosenInConstructionView(pieceType);
    }


    @Override
    public void onPcKeyPressed(int keycode) {
        if (getSelectedProvince() == null) return;
        if (!appearFactor.isInAppearState()) return;
        CveButton cveButton = null;
        switch (keycode) {
            default:
                break;
            case Input.Keys.NUM_1:
                cveButton = buttons.get(1);
                break;
            case Input.Keys.NUM_2:
                cveButton = buttons.get(0);
                break;
        }
        if (cveButton == null) return;
        cveButton.selectionEngineYio.applySelection();
        onClick(cveButton);
    }


    Province getSelectedProvince() {
        return getViewableModel().provinceSelectionManager.selectedProvince;
    }


    @Override
    public void onUnitSelected() {
        hideAllIndicators();
        for (CveIndicator cveIndicator : indicators) {
            cveIndicator.setMode(CveMode.down);
        }
    }


    @Override
    public PieceType getCurrentlyChosenPieceType() {
        CveIndicator lastAliveIndicator = getLastAliveIndicator();
        if (lastAliveIndicator == null) return null;
        return lastAliveIndicator.pieceType;
    }


    private CveIndicator getLastAliveIndicator() {
        CveIndicator result = null;
        for (CveIndicator cveIndicator : indicators) {
            if (!cveIndicator.alive) continue;
            if (!cveIndicator.appearFactor.isInAppearState()) continue;
            result = cveIndicator;
        }
        return result;
    }


    private CveButton getButton(String key) {
        for (CveButton cveButton : buttons) {
            if (cveButton.key.equals(key)) return cveButton;
        }
        return null;
    }


    @Override
    public PointYio getTagPosition(String argument) {
        CveButton cveButton = getButton(argument);
        if (cveButton != null) {
            return cveButton.viewPosition.center;
        }
        return super.getTagPosition(argument);
    }


    @Override
    public boolean isTagTouched(String argument, PointYio touchPoint) {
        CveButton cveButton = getButton(argument);
        if (cveButton != null) {
            return cveButton.isTouchedBy(touchPoint);
        }
        return super.isTagTouched(argument, touchPoint);
    }


    @Override
    public void onPieceBuiltByHand() {
        CveIndicator lastAliveIndicator = getLastAliveIndicator();
        lastAliveIndicator.setMode(CveMode.up);
        for (CveButton button : buttons) {
            button.resetPieceType();
        }
    }


    private void hideAllIndicators() {
        for (CveIndicator indicator : indicators) {
            indicator.hide();
        }
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderConstructionViewElement;
    }
}
