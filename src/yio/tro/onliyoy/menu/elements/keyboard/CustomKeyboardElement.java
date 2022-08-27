package yio.tro.onliyoy.menu.elements.keyboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;

import java.util.ArrayList;

public class CustomKeyboardElement extends InterfaceElement<CustomKeyboardElement> {

    public RectangleYio panelPosition;
    public RectangleYio bottomEmptyZone;
    float animDistance;
    public ArrayList<CbPage> pages;
    AbstractKbReaction reaction;
    public RectangleYio sideShadowPosition;
    boolean touchedCurrently;
    boolean readyToDestroy;
    public SimpleTabsEngineYio simpleTabsEngineYio;
    public CbTextField textField;
    boolean deletionMode;
    private long timeToDelete;
    boolean capitalizeMode;
    public boolean blackoutEnabled;
    public RectangleYio blackoutPosition;
    CbButton lastPressedButton;
    LongTapDetector longTapDetector;


    public CustomKeyboardElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        panelPosition = new RectangleYio();
        panelPosition.set(0, -GraphicsYio.height, GraphicsYio.width, 0.3f * GraphicsYio.height);
        animDistance = 0.4f * GraphicsYio.height;
        bottomEmptyZone = new RectangleYio();
        bottomEmptyZone.set(0, -GraphicsYio.height, GraphicsYio.width, 0.02f * GraphicsYio.height);
        sideShadowPosition = new RectangleYio();
        simpleTabsEngineYio = new SimpleTabsEngineYio();
        simpleTabsEngineYio.setTabWidth(GraphicsYio.width);
        pages = new ArrayList<>();
        textField = new CbTextField(this);
        reaction = null;
        capitalizeMode = true;
        blackoutEnabled = false;
        blackoutPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
        initLongTapDetector();
        initPages();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                CustomKeyboardElement.this.onLongTapDetected();
            }
        };
    }


    public void initPages() {
        pages.clear();
        addPage(LanguagesManager.getInstance().getString("keyboard_layout"));
        addPage(getEnglishLayoutString());
        simpleTabsEngineYio.setLimit(pages.size());
    }


    private void addPage(String layout) {
        if (layout.length() < 5) return;
        CbPage cbPage = new CbPage(this);
        cbPage.setLayout(layout);
        cbPage.setPageIndex(pages.size());
        pages.add(cbPage);
    }


    private String getEnglishLayoutString() {
        return "q w e r t y u i o p;a s d f g h j k l;z x c v b n m";
    }


    @Override
    protected CustomKeyboardElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateBottomEmptyZone();
        updatePanelPosition();
        updateSideShadowPosition();
        simpleTabsEngineYio.move();
        movePages();
        textField.move();
        longTapDetector.move();
        checkToEnableDeletionMode();
        moveDeletionMode();
    }


    private void moveDeletionMode() {
        if (!deletionMode) return;
        if (!touchedCurrently && !Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            deactivateDeletionMode();
        }
        if (System.currentTimeMillis() < timeToDelete) return;
        SoundManager.playSound(SoundType.kb_press);
        textField.deleteLastCharacter();
        timeToDelete = System.currentTimeMillis() + 17;
    }


    private void deactivateDeletionMode() {
        deletionMode = false;
        CbPage currentPage = getCurrentPage();
        if (currentPage == null) return;
        CbButton backspaceButton = currentPage.getButton(CbType.backspace);
        if (backspaceButton == null) return;
        backspaceButton.selectionEngineYio.factorYio.reset();
    }


    private void checkToEnableDeletionMode() {
        if (deletionMode) return;
        CbPage currentPage = getCurrentPage();
        if (currentPage == null) return;
        CbButton backspaceButton = currentPage.getButton(CbType.backspace);
        if (backspaceButton == null) return;
        if (!backspaceButton.selectionEngineYio.isSelected()) return;
        if (System.currentTimeMillis() < backspaceButton.selectionTime + 500) return;
        if (getCurrentlyTouchedButton() != backspaceButton) return;
        deletionMode = true;
    }


    private void updateBottomEmptyZone() {
        bottomEmptyZone.y = -(1 - appearFactor.getValue()) * animDistance;
    }


    private void updateSideShadowPosition() {
        sideShadowPosition.set(
                0, panelPosition.y + panelPosition.height - 0.035f * GraphicsYio.height,
                GraphicsYio.width, 0.045f * GraphicsYio.height
        );
    }


    private void movePages() {
        for (CbPage cbPage : pages) {
            cbPage.move();
        }
    }


    private void updatePanelPosition() {
        panelPosition.y = bottomEmptyZone.height - (1 - appearFactor.getValue()) * animDistance;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        reaction = null;
        touchedCurrently = false;
        readyToDestroy = false;
        simpleTabsEngineYio.onAppear();
        textField.onAppear();
        deletionMode = false;
        blackoutEnabled = true;
        lastPressedButton = null;
        checkToUpdateCache();
    }


    private void checkToUpdateCache() {
        for (CbPage cbPage : pages) {
            if (cbPage.cacheTexture != null) continue;
            cbPage.cacheTexture = menuControllerYio.cacheTexturesManager.perform(cbPage);
        }
    }


    public void setReaction(AbstractKbReaction reaction) {
        this.reaction = reaction;
    }


    public void setValue(String value) {
        textField.setString(value);
        textField.applyHighlight();
    }


    private CbButton getButton(String key) {
        if (key.equals("delete")) {
            key = "" + CbType.backspace;
        }
        CbType cbType = pages.get(0).detectButtonType(key);
        if (cbType != CbType.normal) {
            return getCurrentPage().getButton(cbType);
        }
        for (CbPage cbPage : pages) {
            for (CbButton button : cbPage.buttons) {
                if (button.type != CbType.normal) continue;
                if (button.title.string.equals(key)) return button;
            }
        }
        return null;
    }


    public int getOtherTabIndex(int tabIndex) {
        if (tabIndex == 0) return 1;
        return 0;
    }


    private CbPage getCurrentPage() {
        return pages.get(simpleTabsEngineYio.getCurrentTabIndex());
    }


    public void onPcKeyPressed(int keycode) {
        switch (keycode) {
            default:
                String key = Input.Keys.toString(keycode);
                if (key == null) break;
                CbButton cbButton = getButton(key.toLowerCase());
                if (cbButton == null) break;
                simpleTabsEngineYio.goTo(1); // auto switch to english
                currentTouch.set(
                        cbButton.position.x + cbButton.position.width / 2,
                        cbButton.position.y + cbButton.position.height / 2
                );
                selectButton(cbButton);
                break;
            case Input.Keys.BACK:
            case Input.Keys.ESCAPE:
                Scenes.keyboard.destroy();
                break;
            case Input.Keys.RIGHT:
                textField.hideHighlight();
                break;
            case Input.Keys.MINUS:
                textField.addText("-");
                SoundManager.playSound(SoundType.kb_press);
                break;
        }
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToDestroy) {
            readyToDestroy = false;
            Scenes.keyboard.destroy();
            return true;
        }
        return false;
    }


    private CbButton getCurrentlyTouchedButton() {
        CbButton cbButton = getCurrentlyTouchedButton(0);
        if (cbButton != null) return cbButton;
        return getCurrentlyTouchedButton(0.03f * GraphicsYio.width);
    }


    private CbButton getCurrentlyTouchedButton(float offset) {
        for (CbPage page : pages) {
            for (CbButton button : page.buttons) {
                if (!button.isTouchedBy(currentTouch, offset)) continue;
                return button;
            }
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = true;
        lastPressedButton = null;
        longTapDetector.onTouchDown(currentTouch);
        selectButton(getCurrentlyTouchedButton());
        return true;
    }


    private void selectButton(CbButton cbButton) {
        if (cbButton == null) return;
        SoundManager.playSound(SoundType.kb_press);
        cbButton.applySelection();
        onCbButtonPressed(cbButton);
    }


    private void onCbButtonPressed(CbButton cbButton) {
        lastPressedButton = cbButton;
        switch (cbButton.type) {
            default:
                System.out.println("CustomKeyboardElement.onPressedButton");
                break;
            case normal:
                textField.addText(cbButton.title.string);
                break;
            case enter:
                onEnterPressed();
                break;
            case space:
                textField.addText(" ");
                break;
            case language:
                applyLanguageSwitch();
                break;
            case backspace:
                textField.deleteLastCharacter();
                break;
        }
    }


    private void onEnterPressed() {
        Scenes.keyboard.destroy();
        if (reaction == null) return;

        String string = textField.title.string;
        if (string == null) {
            string = "";
        }
        reaction.onInputFromKeyboardReceived(string);
    }


    @Override
    public void onAppPause() {
        resetCacheTextures();
    }


    private void resetCacheTextures() {
        for (CbPage page : pages) {
            page.cacheTexture = null;
        }
    }


    @Override
    public void onAppResume() {
        super.onAppResume();
        checkToUpdateCache();
    }


    private void applyLanguageSwitch() {
        if (simpleTabsEngineYio.getCurrentTabIndex() == 0) {
            simpleTabsEngineYio.goTo(1);
        } else {
            simpleTabsEngineYio.goTo(0);
        }
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;
        longTapDetector.onTouchDrag(currentTouch);
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        deletionMode = false;
        longTapDetector.onTouchUp(currentTouch);
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    private void onClick() {
        if (isCurrentTouchOutsideActiveZone()) {
            readyToDestroy = true;
            if (reaction != null) {
                reaction.onInputCancelled();
            }
            return;
        }
        if (textField.viewPosition.isPointInside(currentTouch)) {
            textField.hideHighlight();
        }
    }


    void onLongTapDetected() {
        if (lastPressedButton == null) return;
        switch (lastPressedButton.type) {
            default:
                break;
            case normal:
                textField.deleteLastCharacter();
                textField.addText(lastPressedButton.title.string.toUpperCase());
                break;
            case space:
                textField.deleteLastCharacter();
                textField.addText(". ");
                break;
        }
    }


    public CustomKeyboardElement setHint(String string) {
        textField.hintViewText.setString(string);
        textField.hintViewText.updateMetrics();
        return this;
    }


    public CustomKeyboardElement setCapitalizeMode(boolean capitalizeMode) {
        this.capitalizeMode = capitalizeMode;
        return this;
    }


    public CustomKeyboardElement setBlackoutEnabled(boolean blackoutEnabled) {
        this.blackoutEnabled = blackoutEnabled;
        return this;
    }


    private boolean isCurrentTouchOutsideActiveZone() {
        if (panelPosition.isPointInside(currentTouch, 0.02f * GraphicsYio.width)) return false;
        if (bottomEmptyZone.isPointInside(currentTouch, 0.02f * GraphicsYio.width)) return false;
        if (textField.viewPosition.isPointInside(currentTouch, 0.02f * GraphicsYio.width)) return false;
        return true;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCustomKeyboardElement;
    }
}
