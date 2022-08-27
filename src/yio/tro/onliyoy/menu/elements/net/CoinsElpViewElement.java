package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NetMatchResults;
import yio.tro.onliyoy.net.shared.NetUserData;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

import java.util.ArrayList;

public class CoinsElpViewElement extends InterfaceElement<CoinsElpViewElement> {

    public ArrayList<CeItem> items;
    Reaction reaction;
    public RectangleYio overallSize;
    public RectangleYio touchPosition;
    public SelectionEngineYio selectionEngineYio;
    boolean touchedCurrently;
    boolean readyToProcessClick;


    public CoinsElpViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        reaction = null;
        touchPosition = new RectangleYio();
        overallSize = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
        initItems();
    }


    private void initItems() {
        items = new ArrayList<>();
//        items.add(new CevItem(this, CevType.money));
        items.add(new CeItem(this, CevType.text));
        items.add(new CeItem(this, CevType.elp));
        items.add(new CeItem(this, CevType.text));
    }


    @Override
    protected CoinsElpViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveItems();
        updateTouchPosition();
        moveSelection();
    }


    private void moveSelection() {
        if (touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateTouchPosition() {
        touchPosition.setBy(overallSize);
        touchPosition.x = viewPosition.x;
        touchPosition.y = viewPosition.y + viewPosition.height - touchPosition.height;
        touchPosition.increase(0.03f * GraphicsYio.width);
    }


    private void moveItems() {
        for (CeItem ceItem : items) {
            ceItem.move();
        }
    }


    @Override
    public void onDestroy() {
        touchedCurrently = false;
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        readyToProcessClick = false;
        update();
    }


    public void update() {
        loadTextValues();
        updateRadius();
        updateDeltas();
        updateOverallSize();
    }


    public void update(NetMatchResults netMatchResults) {
        loadTextValues(netMatchResults);
        updateRadius();
        updateDeltas();
        updateOverallSize();
    }


    private void loadTextValues(NetMatchResults netMatchResults) {
        RenderableTextYio moneyViewText = items.get(0).title;
        RenderableTextYio elpViewText = items.get(2).title;
        String moneyPrefix = "+$";
        if (netMatchResults.moneyDeltaValue < 0) {
            moneyPrefix = "-$";
        }
        String elpPrefix = "+";
        if (netMatchResults.elpDeltaValue < 0) {
            elpPrefix = "-";
        }
        moneyViewText.setString(moneyPrefix + Math.abs(netMatchResults.moneyDeltaValue) + "   " + elpPrefix);
        moneyViewText.updateMetrics();
        elpViewText.setString("" + Math.abs(netMatchResults.elpDeltaValue));
        elpViewText.updateMetrics();
    }


    private void updateOverallSize() {
        CeItem lastItem = items.get(items.size() - 1);
        overallSize.width = lastItem.delta.x + lastItem.position.radius;
        overallSize.height = getAverageTextHeight();
    }


    private void updateDeltas() {
        float x = 0;
        float y = getAverageTextHeight() / 2;
        float innerOffset = 0.005f * GraphicsYio.width;
        for (CeItem ceItem : items) {
            x += ceItem.position.radius;
            ceItem.delta.set(x, y);
            x += ceItem.position.radius + innerOffset;
        }
    }


    private float getAverageTextHeight() {
        int n = 0;
        float sum = 0;
        for (CeItem ceItem : items) {
            if (ceItem.type != CevType.text) continue;
            n++;
            sum += ceItem.title.height;
        }
        return sum / n;
    }


    private void updateRadius() {
        for (CeItem ceItem : items) {
            switch (ceItem.type) {
                default:
                case elp:
                case money:
                    ceItem.position.setRadius(0.011f * GraphicsYio.height);
                    break;
                case text:
                    ceItem.position.setRadius(ceItem.title.width / 2);
                    break;
            }
        }
    }


    private void loadTextValues() {
        NetRoot netRoot = menuControllerYio.yioGdxGame.netRoot;
        NetUserData userData = netRoot.userData;
        RenderableTextYio moneyViewText = items.get(0).title;
        RenderableTextYio elpViewText = items.get(2).title;
        moneyViewText.setString("$" + Yio.getCompactValueString(userData.money) + "   ");
        moneyViewText.updateMetrics();
        elpViewText.setString("" + userData.elp);
        elpViewText.updateMetrics();
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToProcessClick) {
            readyToProcessClick = false;
            reaction.perform(menuControllerYio);
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDown() {
        if (reaction == null) return false;
        touchedCurrently = touchPosition.isPointInside(currentTouch, 0.04f * GraphicsYio.height);
        if (!touchedCurrently) return false;
        selectionEngineYio.applySelection();
        return true;
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;
        return true;
    }


    @Override
    public boolean touchUp() {
        if (reaction == null) return false;
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    private void onClick() {
        SoundManager.playSound(SoundType.button);
        readyToProcessClick = true;
    }


    public CoinsElpViewElement setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCoinsElpViewElement;
    }
}
