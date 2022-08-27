package yio.tro.onliyoy.menu.elements.keyboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class NativeKeyboardElement extends InterfaceElement<NativeKeyboardElement> {


    public BitmapFont font;
    AbstractKbReaction reaction;
    public RectangleYio blackoutPosition;
    private TextField.TextFieldStyle textFieldStyle;
    private TextField textField;
    public RectangleYio tfPosition;
    public RectangleYio tfFrame;
    boolean touched;
    private float frameOffset;
    public FactorYio tfFactor;
    boolean readyToShowTextField;
    RepeatYio<NativeKeyboardElement> repeatCheckToShowTextField;
    String tfPreparedValue;


    public NativeKeyboardElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);

        position = new RectangleYio();
        viewPosition = new RectangleYio();
        appearFactor = new FactorYio();
        font = Fonts.gameFont;
        reaction = null;
        blackoutPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
        tfPosition = new RectangleYio();
        tfFrame = new RectangleYio();
        frameOffset = 0.02f * GraphicsYio.width;
        tfFactor = new FactorYio();
        tfPreparedValue = "";

        initTextFieldStyle();
        initRepeats();
    }


    private void initRepeats() {
        repeatCheckToShowTextField = new RepeatYio<NativeKeyboardElement>(this, 300) {
            @Override
            public void performAction() {
                parent.checkToShowTextField();
            }
        };
    }


    void checkToShowTextField() {
        if (!readyToShowTextField) return;

        readyToShowTextField = false;
        showTextField();
    }


    private void initTextFieldStyle() {
        textFieldStyle = new TextField.TextFieldStyle(
                Fonts.gameFont,
                Color.BLACK,
                new TextureRegionDrawable(menuControllerYio.yioGdxGame.gameView.blackPixel),
                new TextureRegionDrawable(GraphicsYio.loadTextureRegion("pixels/translucent.png", false)),
                new TextureRegionDrawable(GraphicsYio.loadTextureRegion("pixels/white.png", false))
        );
    }


    public void setReaction(AbstractKbReaction reaction) {
        this.reaction = reaction;
    }


    @Override
    protected NativeKeyboardElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        tfFactor.move();
        moveTfFrame();
        repeatCheckToShowTextField.move();
    }


    @Override
    public void onDestroy() {
        touched = false;
        Gdx.input.setOnscreenKeyboardVisible(false);
        textField.remove();
        tfFactor.reset();
    }


    @Override
    public void onAppear() {
        touched = false;
        updateTfPosition();
        Gdx.input.setOnscreenKeyboardVisible(true);
        tfFactor.appear(MovementType.approach, 8);

        repeatCheckToShowTextField.setCountDown(15);
        readyToShowTextField = true;
    }


    public void onPcKeyPressed(int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER:
                reaction.onInputFromKeyboardReceived(textField.getText());
                Scenes.keyboard.destroy();
                break;
            case Input.Keys.BACK:
            case Input.Keys.ESCAPE:
                Scenes.keyboard.destroy();
                break;
        }
    }


    private void updateTfPosition() {
        tfPosition.width = 0.8f * GraphicsYio.width;
        tfPosition.height = 0.05f * GraphicsYio.height;
        tfPosition.x = GraphicsYio.width / 2 - tfPosition.width / 2;
        tfPosition.y = 0.5f * GraphicsYio.height;
    }


    private void moveTfFrame() {
        tfFrame.setBy(tfPosition);
        tfFrame.increase(-tfPosition.height / 2);
        tfFrame.increase(tfFactor.getValue() * (tfPosition.height / 2 + frameOffset));
    }


    void showTextField() {
        textField = new TextField("", textFieldStyle);
        textField.setPosition(tfPosition.x, tfPosition.y);
        textField.setSize(tfPosition.width, tfPosition.height);

        getStage().addActor(textField);

        getStage().setKeyboardFocus(textField);
        textField.selectAll();

        if (tfPreparedValue.length() > 0) {
            textField.setText(tfPreparedValue);
            textField.selectAll();
        }
    }


    private Stage getStage() {
        return menuControllerYio.yioGdxGame.stage;
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean isTouchable() {
        return true;
    }


    @Override
    public boolean touchDown() {
        touched = !isTouchInsideRectangle(currentTouch, tfPosition);

        return true;
    }


    @Override
    public boolean touchDrag() {
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touched) return true;

        if (isClicked()) {
            onClick();
        }

        return true;
    }


    private void onClick() {
        if (reaction != null) {
            reaction.onInputCancelled();
        }
        Scenes.keyboard.destroy();
    }


    public void setValue(String value) {
        tfPreparedValue = value;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderNativeKeyboard;
    }

}
