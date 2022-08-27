package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.LightBottomPanelElement;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public abstract class ModalSceneYio extends SceneYio {


    protected LightBottomPanelElement defaultPanel;
    protected ButtonYio closeButton;
    private SceneYio previousFocusScene;


    public ModalSceneYio() {
        super();
        defaultPanel = null;
        previousFocusScene = null;
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return null;
    }


    @Override
    protected void applyBackground() {

    }


    @Override
    public void create() {
        if (isCurrentlyVisible()) return;
        super.create();
    }


    @Override
    public void addLocalElement(InterfaceElement interfaceElement) {
        interfaceElement.setFakeDyingStatusEnabled(true);
        super.addLocalElement(interfaceElement);
    }


    @Override
    protected void beginCreation() {
        updatePreviousFocusScene();
        if (!shouldNotTouchFocusScene()) {
            menuControllerYio.setFocusScene(this);
        }
        menuControllerYio.checkToRemoveInvisibleElements();
    }


    private void updatePreviousFocusScene() {
        if (shouldNotTouchFocusScene()) return;
        previousFocusScene = menuControllerYio.getFocusScene();
        if (previousFocusScene == this) return;
        while (previousFocusScene != null && previousFocusScene instanceof ModalSceneYio) {
            SceneYio temp = ((ModalSceneYio) previousFocusScene).previousFocusScene;
            if (temp == previousFocusScene) break;
            previousFocusScene = temp;
        }
    }


    protected boolean shouldNotTouchFocusScene() {
        return false;
    }


    @Override
    protected void endInitialization() {
        super.endInitialization();

        for (InterfaceElement element : getLocalElementsList()) {
            element.setOnTopOfGameView(true);
        }
    }


    protected void createDefaultPanel(double height) {
        defaultPanel = uiFactory.getLightBottomPanelElement()
                .setSize(1, height)
                .centerHorizontal()
                .alignBottom(0.01)
                .setAnimation(AnimationYio.down)
                .setAppearParameters(MovementType.approach, 2.3)
                .setDestroyParameters(MovementType.lighty, 3.5);
    }


    protected void createCloseButton() {
        closeButton = uiFactory.getButton()
                .setSize(1, 1)
                .centerHorizontal()
                .centerVertical()
                .setHidden(true)
                .setHotkeyKeycode(Input.Keys.BACK)
                .setDebugName("invisible_close_button")
                .setReaction(getCloseReaction());
    }


    protected void createDarken() {
        uiFactory.getDarkenElement().setSize(1, 1);
    }


    @Override
    public void destroy() {
        if (previousFocusScene != null) {
            menuControllerYio.setFocusScene(previousFocusScene);
        }
        super.destroy();
    }


    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
            }
        };
    }

}
