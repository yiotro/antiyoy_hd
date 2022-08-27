package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class QmsElement extends InterfaceElement<QmsElement> {

    public RenderableTextYio title;
    public long startTime;
    boolean ready;
    private RepeatYio<QmsElement> repeatUpdate;
    long appearTime;
    public ArrayList<QmsItem> items;
    public RectangleYio incBounds;
    private int quantity;
    public ArrayList<QmsWave> waves;
    private ObjectPoolYio<QmsWave> poolWaves;
    private RepeatYio<QmsElement> repeatRemoveWaves;


    public QmsElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        startTime = 0;
        items = new ArrayList<>();
        incBounds = new RectangleYio();
        waves = new ArrayList<>();
        initTitle();
        initPools();
        initRepeats();
    }


    private void initPools() {
        poolWaves = new ObjectPoolYio<QmsWave>(waves) {
            @Override
            public QmsWave makeNewObject() {
                return new QmsWave(QmsElement.this);
            }
        };
    }


    private void initRepeats() {
        repeatRemoveWaves = new RepeatYio<QmsElement>(this, 60) {
            @Override
            public void performAction() {
                parent.checkToRemoveWaves();
            }
        };
        repeatUpdate = new RepeatYio<QmsElement>(this, 20) {
            @Override
            public void performAction() {
                parent.updateTitleString();
            }
        };
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        updateTitleString();
    }


    private void updateTitleString() {
        String string = getCurrentTitleString();
        title.setString(string);
        title.updateMetrics();
    }


    private String getCurrentTitleString() {
        if (startTime == 0) {
            if (quantity > 1) return LanguagesManager.getInstance().getString("rivals_found");
            long deltaTimeInMillis = System.currentTimeMillis() - appearTime;
            int deltaTimeInFrames = Yio.convertMillisIntoFrames(deltaTimeInMillis);
            String timeString = Yio.convertTime(deltaTimeInFrames);
            String searchingForRivals = LanguagesManager.getInstance().getString("searching_for_rivals");
            return searchingForRivals + " " + timeString;
        }
        if (System.currentTimeMillis() < startTime) {
            long deltaTimeInMillis = startTime - System.currentTimeMillis();
            int deltaTimeInFrames = Yio.convertMillisIntoFrames(deltaTimeInMillis);
            String timeString = Yio.convertTimeToUnderstandableString(deltaTimeInFrames);
            return LanguagesManager.getInstance().getString("starting_in") + " " + timeString;
        }
        return LanguagesManager.getInstance().getString("match_is_starting");
    }


    @Override
    protected QmsElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        repeatUpdate.move();
        repeatRemoveWaves.move();
        updateTitlePosition();
        moveWaves();
        moveItems();
        updateIncBounds();
    }


    private void checkToRemoveWaves() {
        for (int i = waves.size() - 1; i >= 0; i--) {
            QmsWave qmsWave = waves.get(i);
            if (!qmsWave.isReadyToBeRemoved()) continue;
            poolWaves.removeFromExternalList(qmsWave);
        }
    }


    private void moveWaves() {
        for (QmsWave qmsWave : waves) {
            qmsWave.move();
        }
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        for (QmsItem qmsItem : items) {
            CircleYio circle = qmsItem.viewPosition;
            float leftX = circle.center.x - circle.radius;
            if (leftX < incBounds.x) {
                float delta = incBounds.x - leftX;
                incBounds.x -= delta;
                incBounds.width += delta;
            }
            float rightX = circle.center.x + circle.radius;
            if (rightX > incBounds.x + incBounds.width) {
                float delta = rightX - (incBounds.x + incBounds.width);
                incBounds.width += delta;
            }
            float bottomY = circle.center.y - circle.radius;
            if (bottomY < incBounds.y) {
                float delta = incBounds.y - bottomY;
                incBounds.y -= delta;
                incBounds.height += delta;
            }
        }
        incBounds.increase(0.015f * GraphicsYio.width);
    }


    private void moveItems() {
        for (QmsItem qmsItem : items) {
            qmsItem.move();
        }
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.position.y = viewPosition.y + viewPosition.height - 0.01f * GraphicsYio.height;
        if (appearFactor.getValue() < 1) {
            title.position.y -= (1 - appearFactor.getValue()) * title.height;
        }
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        startTime = 0;
        ready = true;
        appearTime = System.currentTimeMillis();
        quantity = 0;
        poolWaves.clearExternalList();
    }


    @Override
    public boolean checkToPerformAction() {
        if (ready) {
            if (startTime == 0) return false;
            if (System.currentTimeMillis() < startTime) return false;
            ready = false;
            if (!Scenes.matchLobby.isCurrentlyVisible()) return false;
            Scenes.waitMatchLaunching.create();
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        if (isClicked()) {
            onClick();
        }
        return false;
    }


    private void onClick() {
        poolWaves.getFreshObject().launch(currentTouch);
    }


    public void onListCodeReceived(String listCode) {
        if (appearFactor.getValue() < 1) return; // too early
        quantity = extractQuantityFromListCode(listCode);
        for (int i = 0; i < items.size(); i++) {
            boolean indicate = i < quantity;
            items.get(i).setIndicate(indicate);
        }
    }


    private int extractQuantityFromListCode(String listCode) {
        int c = 0;
        for (String token : listCode.split(",")) {
            if (token.length() < 5) continue;
            c++;
        }
        return c;
    }


    public void setStartTime(long startTime) {
        this.startTime = startTime;
        updateTitleString();
    }


    public void initItems(int quantity) {
        items.clear();
        for (int i = 0; i < quantity; i++) {
            QmsItem qmsItem = new QmsItem(this);
            items.add(qmsItem);
        }
        updateItemDeltas();
        updateAppearTimes();
    }


    private void updateAppearTimes() {
        long time = System.currentTimeMillis() + 70;
        for (QmsItem qmsItem : items) {
            qmsItem.setAppearTime(time);
            time += 70;
        }
    }


    private void updateItemDeltas() {
        float radius = (position.height - title.height) / 2 - 0.01f * GraphicsYio.height;
        float internalHorizontalOffset = 1.0f * radius;
        float fullWidth = 2 * radius * items.size() + internalHorizontalOffset * (items.size() - 1);
        float x = -0.5f * fullWidth + radius;
        for (QmsItem qmsItem : items) {
            qmsItem.deltaX = x;
            qmsItem.setTargetRadius(radius);
            x += radius + internalHorizontalOffset + radius;
        }
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderQmsElement;
    }
}
