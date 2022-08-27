package yio.tro.onliyoy.menu.elements.editor;

import com.badlogic.gdx.Gdx;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class EditorPanelElement extends InterfaceElement<EditorPanelElement> {

    public RectangleYio renderPosition;
    public RectangleYio sideShadowPosition;
    public ArrayList<EpeButton> buttons;
    public static final float hDelta = 0.02f;
    public static final int rowQuantity = 9;
    public static final float bRadius = ((1 - 2 * hDelta) / rowQuantity) * Gdx.graphics.getWidth() * 0.5f;
    boolean touchedCurrently;
    boolean readyToProcessClick;
    EpeButton clickButton;


    public EditorPanelElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        renderPosition = new RectangleYio();
        sideShadowPosition = new RectangleYio();
        buttons = new ArrayList<>();
        setAppearParameters(MovementType.approach, 3);
        setDestroyParameters(MovementType.lighty, 4.5);
    }


    @Override
    protected EditorPanelElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveRenderPosition();
        updateSideShadowPosition();
        moveButtons();
    }


    private void moveButtons() {
        for (EpeButton epeButton : buttons) {
            epeButton.move();
        }
    }


    private void updateSideShadowPosition() {
        sideShadowPosition.set(
                0, viewPosition.y + viewPosition.height - 0.03f * GraphicsYio.height,
                GraphicsYio.width, 0.045f * GraphicsYio.height
        );
    }


    private void moveRenderPosition() {
        renderPosition.setBy(viewPosition);
        renderPosition.increase(GraphicsYio.borderThickness);
        renderPosition.height -= GraphicsYio.borderThickness;

        if (renderPosition.y > 0) {
            renderPosition.height += renderPosition.y;
            renderPosition.y = 0;
        }
    }


    @Override
    public void onDestroy() {
        getEditorManager().setChosenType(null);
    }


    private EditorManager getEditorManager() {
        return getGameController().objectsLayer.editorManager;
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        readyToProcessClick = false;
        clickButton = null;

        // update button position before first render
        updateViewPosition();
        moveButtons();
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToProcessClick) {
            readyToProcessClick = false;
            applyReaction(clickButton.type);
            return true;
        }
        return false;
    }


    public void applyReaction(EpbType epbType) {
        switch (epbType) {
            default:
                EpbType currentType = getEditorManager().chosenType;
                if (currentType == epbType) {
                    epbType = null; // toggle
                }
                getEditorManager().setChosenType(epbType);
                break;
            case back:
                sceneOwner.destroy();
                break;
        }
    }


    EpeButton getCurrentlyTouchedButton() {
        for (EpeButton epeButton : buttons) {
            if (!epeButton.isTouchedBy(currentTouch)) continue;
            return epeButton;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = renderPosition.isPointInside(currentTouch);
        if (!touchedCurrently) return false;
        applySelection();
        return true;
    }


    private void applySelection() {
        EpeButton currentlyTouchedButton = getCurrentlyTouchedButton();
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
            onClick();
        }
        return true;
    }


    private void onClick() {
        EpeButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return;
        SoundManager.playSound(SoundType.button);
        readyToProcessClick = true;
        clickButton = currentlyTouchedButton;
    }


    public void initPiecesButtons() {
        buttons.clear();
        addBackButton();
        float y = position.height - bRadius;
        float x = position.width - hDelta - bRadius;
        EpbType[] types = new EpbType[]{
                EpbType.piece_eraser,
                EpbType.pine,
                EpbType.palm,
                EpbType.farm,
                EpbType.tower,
                EpbType.spearman,
                EpbType.peasant,
        };
        for (EpbType type : types) {
            if (!isPieceTypeAllowed(type)) continue;
            addButton(x, y, type);
            x -= 2 * bRadius;
        }
    }


    private boolean isPieceTypeAllowed(EpbType epbType) {
        if (epbType == EpbType.piece_eraser) return true;
        if (epbType == EpbType.pine) return true;
        if (epbType == EpbType.palm) return true;
        PieceType pieceType = PieceType.valueOf("" + epbType);
        ObjectsLayer objectsLayer = getGameController().objectsLayer;
        return objectsLayer.viewableModel.ruleset.isBuildable(pieceType);
    }


    private void addButton(float dx, float dy, EpbType epbType) {
        EpeButton epeButton = new EpeButton(this);
        epeButton.position.setRadius(bRadius);
        epeButton.type = epbType;
        epeButton.delta.set(dx, dy);
        buttons.add(epeButton);
    }


    public void initLandscapeButtons() {
        addBackButton();
        float defX = position.width - hDelta * GraphicsYio.width - bRadius;
        float x = defX;
        float y = position.height - bRadius;
        int counter = 0;
        for (HColor color : HColor.values()) {
            if (isColorAllowed(color)) {
                addButton(x, y, EpbType.valueOf("" + color));
            }
            counter++;
            x -= 2 * bRadius;
            if (counter % 6 == 0) {
                x = defX;
                y -= 2 * bRadius;
            }
            if (counter == 12) {
                x -= 2 * bRadius; // last line should leave a place for eraser
            }
        }
        addButton(defX, y, EpbType.hex_eraser);
    }


    private boolean isColorAllowed(HColor color) {
        ViewableModel viewableModel = getGameController().objectsLayer.viewableModel;
        switch (viewableModel.ruleset.getRulesType()) {
            default:
            case def:
            case experimental:
            case duel:
                return true;
            case classic:
                return color != HColor.gray;
        }
    }


    public void addBackButton() {
        addButton(
                hDelta + bRadius,
                position.height - bRadius,
                EpbType.back
        );
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderEditorPanelElement;
    }
}
