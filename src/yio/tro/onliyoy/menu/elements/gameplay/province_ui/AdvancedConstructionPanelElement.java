package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.AbstractRuleset;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.view.GameView;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class AdvancedConstructionPanelElement extends InterfaceElement<AdvancedConstructionPanelElement> implements IConstructionView {

    public RectangleYio sideShadowPosition;
    boolean touchedCurrently;
    public ArrayList<AcpButton> buttons;
    private ObjectPoolYio<AcpButton> poolButtons;
    boolean updateRequired;
    private PieceType[] leftPieces;
    private PieceType[] rightPieces;
    public ArrayList<CveIndicator> indicators;
    ObjectPoolYio<CveIndicator> poolIndicators;
    RepeatYio<AdvancedConstructionPanelElement> repeatRemoveDeadIndicators;


    public AdvancedConstructionPanelElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        sideShadowPosition = new RectangleYio();
        buttons = new ArrayList<>();
        updateRequired = true;
        indicators = new ArrayList<>();
        initPieces();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveDeadIndicators = new RepeatYio<AdvancedConstructionPanelElement>(this, 120) {
            @Override
            public void performAction() {
                parent.removeDeadIndicators();
            }
        };
    }


    private void initPieces() {
        leftPieces = new PieceType[]{
                PieceType.strong_tower,
                PieceType.tower,
                PieceType.farm,
        };
        rightPieces = new PieceType[]{
                PieceType.knight,
                PieceType.baron,
                PieceType.spearman,
                PieceType.peasant,
        };
    }


    private void initPools() {
        poolButtons = new ObjectPoolYio<AcpButton>(buttons) {
            @Override
            public AcpButton makeNewObject() {
                return new AcpButton(AdvancedConstructionPanelElement.this);
            }
        };
        poolIndicators = new ObjectPoolYio<CveIndicator>(indicators) {
            @Override
            public CveIndicator makeNewObject() {
                return new CveIndicator(AdvancedConstructionPanelElement.this);
            }
        };
    }


    @Override
    protected AdvancedConstructionPanelElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateSideShadowPosition();
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
        for (AcpButton acpButton : buttons) {
            acpButton.move();
        }
    }


    private void updateSideShadowPosition() {
        sideShadowPosition.set(
                0, viewPosition.y + viewPosition.height - 0.03f * GraphicsYio.height,
                GraphicsYio.width, 0.045f * GraphicsYio.height
        );
    }


    @Override
    public void onDestroy() {
        touchedCurrently = false;
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        loadValues();
        poolIndicators.clearExternalList();
    }


    private void loadValues() {
        if (!updateRequired) return;
        updateRequired = false;
        poolButtons.clearExternalList();
        AbstractRuleset ruleset = getViewableModel().ruleset;
        float delta = 0;
        float step = (1f / 8f) * GraphicsYio.width;
        for (PieceType pieceType : leftPieces) {
            if (!ruleset.isBuildable(pieceType)) continue;
            addButton(pieceType, delta);
            delta += step;
        }
        delta = GraphicsYio.width - step;
        for (PieceType pieceType : rightPieces) {
            if (!ruleset.isBuildable(pieceType)) continue;
            addButton(pieceType, delta);
            delta -= step;
        }
    }


    private void addButton(PieceType pieceType, float delta) {
        AcpButton freshObject = poolButtons.getFreshObject();
        freshObject.pieceType = pieceType;
        freshObject.delta = delta;
    }


    @Override
    public void onRulesDefined() {
        updateRequired = true;
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


    @Override
    public void onPieceBuiltByHand() {
        CveIndicator lastAliveIndicator = getLastAliveIndicator();
        lastAliveIndicator.setMode(CveMode.up);
    }


    @Override
    public void onPcKeyPressed(int keycode) {
        if (getSelectedProvince() == null) return;
        if (!appearFactor.isInAppearState()) return;
        AcpButton acpButton = null;
        switch (keycode) {
            default:
                break;
            case Input.Keys.NUM_1:
                acpButton = getButton(PieceType.peasant);
                break;
            case Input.Keys.NUM_2:
                acpButton = getButton(PieceType.farm);
                break;
        }
        if (acpButton == null) return;
        acpButton.selectionEngineYio.applySelection();
        SoundManager.playSound(SoundType.button);
        onClick(acpButton);
    }


    private AcpButton getButton(PieceType pieceType) {
        for (AcpButton acpButton : buttons) {
            if (acpButton.pieceType == pieceType) return acpButton;
        }
        return null;
    }


    @Override
    public float getAlpha() {
        GameView gameView = menuControllerYio.yioGdxGame.gameView;
        return gameView.appearFactor.getValue();
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    private AcpButton getCurrentlyTouchedButton() {
        for (AcpButton acpButton : buttons) {
            if (acpButton.isTouchedBy(currentTouch)) return acpButton;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = viewPosition.isPointInside(currentTouch);
        if (!touchedCurrently) return false;
        AcpButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton != null) {
            currentlyTouchedButton.selectionEngineYio.applySelection();
        }
        return true;
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


    private void onClick(AcpButton acpButton) {
        if (acpButton == null) return;
        SoundManager.playSound(SoundType.button);
        hideAllIndicators();
        spawnIndicator(acpButton.pieceType);
    }


    private void spawnIndicator(PieceType pieceType) {
        CveIndicator freshObject = poolIndicators.getFreshObject();
        freshObject.spawn(pieceType);
        freshObject.setBottomDelta(position.height + 0.035f * GraphicsYio.height);
        getViewableModel().onPieceChosenInConstructionView(pieceType);
    }


    Province getSelectedProvince() {
        return getViewableModel().provinceSelectionManager.selectedProvince;
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


    private void hideAllIndicators() {
        for (CveIndicator indicator : indicators) {
            indicator.hide();
        }
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderAdvancedConstructionPanelElement;
    }
}
