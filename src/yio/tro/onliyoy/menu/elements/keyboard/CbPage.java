package yio.tro.onliyoy.menu.elements.keyboard;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;
import java.util.Arrays;

public class CbPage {

    public CustomKeyboardElement customKeyboardElement;
    public RectangleYio position;
    public static final int ROWS_NUMBER = 5;
    private int currentRowIndex; // used only at initialization
    private int maxRowQuantity;
    public ArrayList<CbButton> buttons;
    public TextureRegion cacheTexture;
    int pageIndex;


    public CbPage(CustomKeyboardElement customKeyboardElement) {
        this.customKeyboardElement = customKeyboardElement;
        position = new RectangleYio();
        buttons = new ArrayList<>();
        cacheTexture = null;
        pageIndex = 0;
    }


    void move() {
        updatePosition();
        moveButtons();
    }


    private void moveButtons() {
        for (CbButton button : buttons) {
            button.move();
        }
    }


    void setLayout(String layoutString) {
        currentRowIndex = 0;
        updateMaxRowQuantity(layoutString);
        addRow(getNumeralRowString());
        for (String token : layoutString.split(";")) {
            addRow(token);
        }
        addRow("language space enter");
        adjustSpecialButton();
    }


    private void adjustSpecialButton() {
        float bWidth = GraphicsYio.width / maxRowQuantity;
        CbButton spaceButton = getButton(CbType.space);
        spaceButton.position.width *= 5;
        spaceButton.delta.x = GraphicsYio.width / 2;
        getButton(CbType.language).delta.x =  bWidth / 2;
        getButton(CbType.enter).delta.x = GraphicsYio.width - bWidth / 2;
        getButton(CbType.backspace).delta.x = GraphicsYio.width - bWidth / 2;
    }


    private void updateMaxRowQuantity(String layoutString) {
        maxRowQuantity = getNumeralRowString().split(" ").length;
        for (String token : layoutString.split(";")) {
            maxRowQuantity = Math.max(maxRowQuantity, token.split(" ").length);
        }
    }


    private ArrayList<String> parseIntoList(String source) {
        return new ArrayList<>(Arrays.asList(source.split(" ")));
    }


    CbButton getButton(CbType cbType) {
        for (CbButton cbButton : buttons) {
            if (cbButton.type == cbType) return cbButton;
        }
        return null;
    }


    private void addRow(String source) {
        ArrayList<String> list = parseIntoList(source);
        float rowHeight = customKeyboardElement.panelPosition.height / ROWS_NUMBER;
        float cy = customKeyboardElement.panelPosition.height - currentRowIndex * rowHeight - rowHeight / 2;
        float bWidth = GraphicsYio.width / maxRowQuantity;
        float currentRowLength = bWidth * list.size();
        float cx = GraphicsYio.width / 2 - currentRowLength / 2 + bWidth / 2;
        for (String key : list) {
            addButton(key, cx, cy, bWidth);
            cx += bWidth;
        }
        currentRowIndex++;
    }


    private void addButton(String key, float cx, float cy, float bSize) {
        CbButton cbButton = new CbButton(this);
        cbButton.type = detectButtonType(key);
        cbButton.delta.set(cx, cy);
        cbButton.position.width = bSize;
        cbButton.position.height = bSize;
        if (cbButton.type == CbType.normal) {
            cbButton.setTitle(key);
        }
        buttons.add(cbButton);
    }


    CbType detectButtonType(String key) {
        for (CbType cbType : CbType.values()) {
            if (key.equals("" + cbType)) return cbType;
        }
        return CbType.normal;
    }


    private void updatePosition() {
        position.setBy(customKeyboardElement.panelPosition);
        position.x += pageIndex * position.width + customKeyboardElement.simpleTabsEngineYio.getHook();
    }


    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }


    private String getNumeralRowString() {
        return "1 2 3 4 5 6 7 8 9 0 backspace";
    }
}
