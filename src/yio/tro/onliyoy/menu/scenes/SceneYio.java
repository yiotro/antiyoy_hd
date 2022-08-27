package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.MenuViewYio;
import yio.tro.onliyoy.menu.UiFactory;
import yio.tro.onliyoy.menu.elements.*;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.ArrayList;
import java.util.StringTokenizer;

public abstract class SceneYio {

    public static ArrayList<SceneYio> sceneList;

    boolean initialized;
    protected YioGdxGame yioGdxGame;
    public MenuControllerYio menuControllerYio;
    protected UiFactory uiFactory;
    protected LanguagesManager languagesManager;
    private ArrayList<InterfaceElement> localElementsList;
    protected InterfaceElement previousElement, currentAddedElement;
    protected CircleButtonYio backButton;
    protected NetRoot netRoot;


    public SceneYio() {
        initialized = false;
        if (sceneList == null) {
            sceneList = new ArrayList<>();
        }

        sceneList.add(0, this);

        localElementsList = new ArrayList<>();
        previousElement = null;
        currentAddedElement = null;
    }


    public static void onGeneralInitialization() {
        sceneList = null;
    }


    public void addLocalElement(InterfaceElement interfaceElement) {
        localElementsList.add(interfaceElement);
        interfaceElement.setSceneOwner(this);

        checkToApplyButtonBackground(interfaceElement);

        previousElement = currentAddedElement;
        currentAddedElement = interfaceElement;
    }


    private void checkToApplyButtonBackground(InterfaceElement interfaceElement) {
        if (!(interfaceElement instanceof ButtonYio)) return;
        ((ButtonYio) interfaceElement).setBackground(getButtonBackground());
    }


    private void checkToInitialize() {
        if (initialized) return;
        initialized = true;

        initialize();

        endInitialization();
    }


    protected void endInitialization() {

    }


    public void create() {
        beginCreation();

        checkToInitialize();
        appear();
        applyBackground();

        endCreation();
    }


    protected void applyBackground() {
        BackgroundYio backgroundValue = getBackgroundValue();
        if (backgroundValue == null) return;
        yioGdxGame.applyBackground(backgroundValue);
    }


    protected void beginCreation() {
        menuControllerYio.setFocusScene(this);
        destroyAllVisibleElements();
        menuControllerYio.checkToRemoveInvisibleElements();
    }


    private void endCreation() {
        for (int i = localElementsList.size() - 1; i >= 0; i--) {
            InterfaceElement element = localElementsList.get(i);
            if (element instanceof ScrollableAreaYio) {
                ((ScrollableAreaYio) element).forceToTop();
            }
            element.onSceneEndCreation();
        }

        onEndCreation();
    }


    protected void onEndCreation() {
        //
    }


    public void move() {

    }


    protected void onAppear() {

    }


    protected final void appear() {
        onAppear();
        for (InterfaceElement interfaceElement : localElementsList) {
            if (!interfaceElement.isAllowedToAppear()) continue;
            appearElement(interfaceElement);
        }
    }


    private void appearElement(InterfaceElement interfaceElement) {
        // do not override this method
        // instead use InterfaceElement.setAllowedToAppear()
        interfaceElement.appear();
        menuControllerYio.addVisibleElement(interfaceElement);
    }


    public void destroy() {
        onDestroy();
        for (InterfaceElement interfaceElement : localElementsList) {
            interfaceElement.destroy();
        }
    }


    protected void onDestroy() {
        // nothing by default
    }


    public static void updateAllScenes(MenuControllerYio menuControllerYio) {
        for (SceneYio sceneYio : sceneList) {
            sceneYio.updateLinks(menuControllerYio);
        }
    }


    public void updateLinks(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
        yioGdxGame = menuControllerYio.yioGdxGame;
        uiFactory = new UiFactory(this);
        languagesManager = menuControllerYio.languagesManager;
        netRoot = yioGdxGame.netRoot;;
    }


    protected CircleButtonYio spawnBackButton(Reaction reaction) {
        backButton = uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignLeft(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.05)
                .loadTexture("menu/back_icon.png")
                .setAnimation(AnimationYio.up)
                .setHotkeyKeycode(Input.Keys.BACK)
                .setReaction(reaction);
        return backButton;
    }


    protected void destroyAllVisibleElements() {
        if (!isDisruptive()) return;
        for (InterfaceElement interfaceElement : menuControllerYio.getInterfaceElements()) {
            if (!interfaceElement.isVisible()) continue;
            if (interfaceElement.isResistantToAutoDestroy()) continue;
            interfaceElement.destroy();
        }
    }


    protected boolean isDisruptive() {
        return true;
    }


    public abstract BackgroundYio getBackgroundValue();


    public BackgroundYio getButtonBackground() {
        return BackgroundYio.white;
    }


    protected abstract void initialize();


    public void forceElementsToTop() {
        for (InterfaceElement interfaceElement : localElementsList) {
            forceElementToTop(interfaceElement);
        }
    }


    public void forceElementToTop(InterfaceElement interfaceElement) {
        if (interfaceElement == null) {
            System.out.println("SceneYio.forceElementToTop, warning: element is null");
        }
        menuControllerYio.removeVisibleElement(interfaceElement);
        menuControllerYio.addVisibleElement(interfaceElement);
    }


    public static ArrayList<String> convertStringToArray(String src) {
        ArrayList<String> list = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(src, "#");
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }


    public void setBackground(int backgroundIndex) {
        System.out.println("SceneYio.setBackground");
    }


    public ArrayList<InterfaceElement> getLocalElementsList() {
        return localElementsList;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


    public void tagAsNotInitialized() {
        initialized = false;
    }


    protected MenuViewYio getMenuView() {
        return menuControllerYio.yioGdxGame.menuViewYio;
    }


    protected TextureRegion getTextureFromAtlas(String name) {
        return (new Storage3xTexture(getMenuView().menuAtlas, name + ".png")).getNormal();
    }


    protected TextureRegion getSelectionTexture() {
        return GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    protected GameController getGameController() {
        return menuControllerYio.yioGdxGame.gameController;
    }


    protected ViewableModel getViewableModel() {
        return getGameController().objectsLayer.viewableModel;
    }


    public boolean isCurrentlyVisible() {
        for (InterfaceElement interfaceElement : localElementsList) {
            if (interfaceElement.getFactor().isInAppearState()) return true;
        }
        return false;
    }


    protected Reaction getOpenSceneReaction(final SceneYio sceneYio) {
        return new Reaction() {
            @Override
            protected void apply() {
                sceneYio.create();
            }
        };
    }


    public boolean isInFocus() {
        return menuControllerYio.getFocusScene() == this;
    }


    public boolean isOnlineTargeted() {
        return false;
    }


    protected ConditionYio getNotIosCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return YioGdxGame.platformType != PlatformType.ios;
            }
        };
    }


    protected ObjectsLayer getObjectsLayer() {
        return getGameController().objectsLayer;
    }
}
