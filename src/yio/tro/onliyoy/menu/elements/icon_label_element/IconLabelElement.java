package yio.tro.onliyoy.menu.elements.icon_label_element;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class IconLabelElement extends InterfaceElement<IconLabelElement> {

    public ArrayList<RenderableTextYio> texts;
    ObjectPoolYio<RenderableTextYio> poolTexts;
    public ArrayList<IleIcon> icons;
    ObjectPoolYio<IleIcon> poolIcons;
    private ArrayList<String> tempStringList;
    ArrayList<Object> orderedList;
    BitmapFont font;
    float iOffset;
    public RectangleYio incBounds;
    public boolean backgroundEnabled;
    public RectangleYio touchArea;
    public SelectionEngineYio selectionEngineYio;
    boolean currentlyTouched;
    Reaction reaction;
    boolean needToPerformAction;
    long timeToPerformAction;
    PointYio tempPoint;


    public IconLabelElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        texts = new ArrayList<>();
        icons = new ArrayList<>();
        tempStringList = new ArrayList<>();
        orderedList = new ArrayList<>();
        font = Fonts.miniFont;
        iOffset = GraphicsYio.borderThickness;
        incBounds = new RectangleYio();
        backgroundEnabled = false;
        touchArea = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
        reaction = null;
        needToPerformAction = false;
        tempPoint = new PointYio();
        initPools();
    }


    private void initPools() {
        poolTexts = new ObjectPoolYio<RenderableTextYio>(texts) {
            @Override
            public RenderableTextYio makeNewObject() {
                return new RenderableTextYio();
            }
        };
        poolIcons = new ObjectPoolYio<IleIcon>(icons) {
            @Override
            public IleIcon makeNewObject() {
                return new IleIcon(IconLabelElement.this);
            }
        };
    }


    @Override
    protected IconLabelElement getThis() {
        return this;
    }


    private void moveSelection() {
        if (currentlyTouched) return;
        selectionEngineYio.move();
    }


    @Override
    public void onMove() {
        updateViewPosition();
        moveSelection();
        moveTexts();
        moveIcons();
        updateIncBounds();
        updateTouchArea();
    }


    private void updateTouchArea() {
        touchArea.setBy(incBounds);
        touchArea.increase(0.05f * GraphicsYio.dim);
    }


    private void updateIncBounds() {
        if (orderedList.size() == 0) return;
        Object firstObject = orderedList.get(0);
        if (firstObject instanceof RenderableTextYio) {
            incBounds.setBy(((RenderableTextYio) firstObject).bounds);
        }
        if (firstObject instanceof IleIcon) {
            incBounds.setBy(((IleIcon) firstObject).viewPosition);
        }

        for (int i = 1; i < orderedList.size(); i++) {
            Object object = orderedList.get(i);
            if (object instanceof RenderableTextYio) {
                incBounds.width += ((RenderableTextYio) object).width;
            }
            if (object instanceof IleIcon) {
                incBounds.width += ((IleIcon) object).viewPosition.width + iOffset;
            }
        }

        incBounds.increase(0.015f * GraphicsYio.dim);
    }


    private void moveIcons() {
        for (IleIcon icon : icons) {
            icon.move();
        }
    }


    private void moveTexts() {
        for (RenderableTextYio text : texts) {
            text.position.x = viewPosition.x + text.delta.x;
            text.position.y = viewPosition.y + text.delta.y;
            text.updateBounds();
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        currentlyTouched = false;
        needToPerformAction = false;
    }


    public IconLabelElement applyText(String string) {
        poolTexts.clearExternalList();
        poolIcons.clearExternalList();
        orderedList.clear();
        updateTempStringList(string);
        processTempStringList();
        fixVerticalIconDeltas();
        return this;
    }


    private void fixVerticalIconDeltas() {
        if (texts.size() == 0) return;
        if (icons.size() == 0) return;
        RenderableTextYio firstText = texts.get(0);
        float minY = firstText.delta.y - firstText.height;
        float maxY = firstText.delta.y;
        for (RenderableTextYio text : texts) {
            minY = Math.min(minY, text.delta.y - text.height);
            maxY = Math.max(maxY, text.delta.y);
        }

        float average = (minY + maxY) / 2;
        for (IleIcon icon : icons) {
            icon.delta.y = average - icon.viewPosition.height / 2;
        }
    }


    private void processTempStringList() {
        float x = 0;
        for (String string : tempStringList) {
            if (string.length() == 0) continue;
            boolean isIcon = string.contains("[");
            if (isIcon) {
                x += addIcon(string, x);
                continue;
            }
            x += addText(string, x);
        }
    }


    private float addIcon(String source, float x) {
        IleIcon freshObject = poolIcons.getFreshObject();
        source = source.substring(1, source.length() - 1); // remove braces
        freshObject.textureType = IleTextureType.valueOf(source);
        float size = 1.4f * GraphicsYio.getTextHeight(font, "H");
        freshObject.viewPosition.height = size;
        freshObject.viewPosition.width = size;
        freshObject.delta.x = x;
        freshObject.delta.y = position.height - size;
        orderedList.add(freshObject);
        return iOffset + size;
    }


    private float addText(String string, float x) {
        RenderableTextYio freshObject = poolTexts.getFreshObject();
        freshObject.setFont(font);
        freshObject.setString(string);
        freshObject.updateMetrics();
        freshObject.delta.x = x;
        freshObject.delta.y = position.height;
        orderedList.add(freshObject);
        return freshObject.width;
    }


    public IconLabelElement alignTextToTheRight() {
        float x = position.width;
        for (int i = orderedList.size() - 1; i >= 0; i--) {
            Object object = orderedList.get(i);
            if (object instanceof RenderableTextYio) {
                RenderableTextYio renderableTextYio = (RenderableTextYio) object;
                x -= renderableTextYio.width;
                renderableTextYio.delta.x = x;
                continue;
            }
            if (object instanceof IleIcon) {
                IleIcon ileIcon = (IleIcon) object;
                x -= iOffset + ileIcon.viewPosition.width;
                ileIcon.delta.x = x;
            }
        }
        return this;
    }


    public IconLabelElement alignTextToTheMiddle() {
        float sumLength = getSumLength();
        float x = position.width / 2 - sumLength / 2;
        for (Object object : orderedList) {
            if (object instanceof RenderableTextYio) {
                RenderableTextYio renderableTextYio = (RenderableTextYio) object;
                renderableTextYio.delta.x = x;
                x += renderableTextYio.width;
                continue;
            }
            if (object instanceof IleIcon) {
                IleIcon ileIcon = (IleIcon) object;
                ileIcon.delta.x = x;
                x += ileIcon.viewPosition.width + iOffset;
            }
        }
        return this;
    }


    private float getSumLength() {
        float sum = 0;
        for (RenderableTextYio text : texts) {
            sum += text.width;
        }
        for (IleIcon icon : icons) {
            sum += iOffset + icon.viewPosition.width;
        }
        return sum;
    }


    private void updateTempStringList(String source) {
        tempStringList.clear();
        while (true) {
            int preIndex = source.indexOf("[");
            if (preIndex == -1) {
                tempStringList.add(source);
                break;
            }
            if (preIndex > 0) {
                tempStringList.add(source.substring(0, preIndex));
            }
            int postIndex = source.indexOf("]");
            if (postIndex == -1) break; // error
            if (postIndex <= preIndex) break;
            tempStringList.add(source.substring(preIndex, postIndex + 1));
            source = source.substring(postIndex + 1);
            if (source.length() == 0) break;
        }
    }


    public IconLabelElement setFont(BitmapFont font) {
        this.font = font;
        return this;
    }


    public IconLabelElement setBackgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }


    @Override
    public boolean isTouchedBy(PointYio touchPoint) {
        return touchArea.isPointInside(touchPoint);
    }


    @Override
    public boolean checkToPerformAction() {
        if (needToPerformAction && System.currentTimeMillis() > timeToPerformAction) {
            needToPerformAction = false;
            if (appearFactor.isInAppearState()) {
                reaction.perform(menuControllerYio);
            }
            return true;
        }
        return false;
    }


    @Override
    public void pressArtificially(int keycode) {
        super.pressArtificially(keycode);
        selectionEngineYio.applySelection();
        onClick();
    }


    @Override
    public boolean touchDown() {
        if (appearFactor.getValue() < 0.25) return false;

        if (isTouchedBy(currentTouch) && reaction != null) {
            currentlyTouched = true;
            selectionEngineYio.applySelection();
            return true;
        }

        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        if (currentlyTouched) {
            currentlyTouched = false;
            if (isClicked()) {
                onClick();
            }
            return true;
        }

        return false;
    }


    @Override
    public IconLabelElement clone(InterfaceElement src) {
        IconLabelElement result = super.clone(src);

        IconLabelElement srcLabel = (IconLabelElement) src;
        setFont(srcLabel.font);

        return result;
    }


    private void onClick() {
        if (reaction == null) return;
        if (appearFactor.isInDestroyState()) return;

        currentlyTouched = false;
        SoundManager.playSound(SoundType.button);
        menuControllerYio.yioGdxGame.render();
        needToPerformAction = true;
        timeToPerformAction = System.currentTimeMillis() + 100;
    }


    @Override
    public PointYio getTagPosition(String argument) {
        tempPoint.x = incBounds.x + incBounds.width / 2;
        tempPoint.y = incBounds.y + incBounds.height / 2;
        return tempPoint;
    }


    @Override
    public boolean isTagTouched(String argument, PointYio touchPoint) {
        return isTouchedBy(touchPoint);
    }


    public IconLabelElement setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderIconLabelElement;
    }
}
