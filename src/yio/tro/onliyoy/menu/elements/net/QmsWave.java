package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;
import java.util.Collections;

public class QmsWave implements ReusableYio {

    QmsElement qmsElement;
    long nextTickleTime;
    long delay;
    private ArrayList<QmsItem> targetItems;


    public QmsWave(QmsElement qmsElement) {
        this.qmsElement = qmsElement;
        delay = 80;
        targetItems = new ArrayList<>();
    }


    @Override
    public void reset() {
        nextTickleTime = 0;
        targetItems.clear();
    }


    void launch(PointYio startPoint) {
        targetItems.clear();
        targetItems.addAll(qmsElement.items);
        for (QmsItem qmsItem : targetItems) {
            qmsItem.sortValue = (int) qmsItem.viewPosition.center.distanceTo(startPoint);
        }
        Collections.sort(targetItems);
        nextTickleTime = System.currentTimeMillis() + delay / 2;
    }


    void move() {
        if (System.currentTimeMillis() < nextTickleTime) return;
        nextTickleTime = System.currentTimeMillis() + delay;
        if (targetItems.size() == 0) return;
        QmsItem firstItem = targetItems.get(0);
        firstItem.tickle();
        targetItems.remove(firstItem);
    }


    boolean isReadyToBeRemoved() {
        return targetItems.size() == 0;
    }
}
