package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.net.shared.NetMatchResults;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.ArrayList;

public class RveCoinsElpItem extends AbstractRveItem{

    public ArrayList<RceiInnerItem> items;
    NetMatchResults netMatchResults;


    @Override
    protected void initialize() {
        items = new ArrayList<>();
        items.add(new RceiInnerItem(this, RceiInnerType.text));
        items.add(new RceiInnerItem(this, RceiInnerType.elp));
        items.add(new RceiInnerItem(this, RceiInnerType.text));
    }


    @Override
    protected void onMove() {
        for (RceiInnerItem rceiInnerItem : items) {
            rceiInnerItem.move();
        }
    }


    public void setNetMatchResults(NetMatchResults netMatchResults) {
        this.netMatchResults = netMatchResults;
        update();
    }


    public void update() {
        loadTextValues();
        updateRadius();
        updateDeltas();
    }


    private void updateDeltas() {
        float x = 0.03f * GraphicsYio.width;
        float y = getAverageTextHeight() / 2;
        float innerOffset = 0.005f * GraphicsYio.width;
        for (RceiInnerItem rceiInnerItem : items) {
            x += rceiInnerItem.position.radius;
            rceiInnerItem.delta.set(x, y);
            x += rceiInnerItem.position.radius + innerOffset;
        }
    }


    private float getAverageTextHeight() {
        int n = 0;
        float sum = 0;
        for (RceiInnerItem rceiInnerItem : items) {
            if (rceiInnerItem.type != RceiInnerType.text) continue;
            n++;
            sum += rceiInnerItem.title.height;
        }
        return sum / n;
    }


    private void updateRadius() {
        for (RceiInnerItem rceiInnerItem : items) {
            switch (rceiInnerItem.type) {
                default:
                case elp:
                    rceiInnerItem.position.setRadius(0.011f * GraphicsYio.height);
                    break;
                case text:
                    rceiInnerItem.position.setRadius(rceiInnerItem.title.width / 2);
                    break;
            }
        }
    }


    private void loadTextValues() {
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


    @Override
    protected float getHeight() {
        return 0.04f * GraphicsYio.height;
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveCoinsElpItem;
    }
}
