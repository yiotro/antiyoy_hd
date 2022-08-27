package yio.tro.onliyoy.menu.elements.plot_view;

import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;

public class PlotItem {

    PlotViewElement plotViewElement;
    PlotData plotData;
    public ArrayList<PointYio> points;
    public PlotColor color;


    public PlotItem(PlotViewElement plotViewElement) {
        this.plotViewElement = plotViewElement;
        plotData = null;
        points = new ArrayList<>();
        color = PlotColor.black;
    }


    void update() {
        prepareListSize();
        if (points.size() < 2) {
            System.out.println("PlotItem.update, problem: size=" + points.size());
            return;
        }
        RectangleYio pos = plotViewElement.getViewPosition();
        float x = pos.x;
        float dx = pos.width / (points.size() - 1);
        for (int i = 0; i < points.size(); i++) {
            PointYio pointYio = points.get(i);
            float value = plotData.values.get(i);
            pointYio.x = x;
            pointYio.y = pos.y + value * pos.height;
            x += dx;
        }
    }


    private void prepareListSize() {
        points.clear();
        for (int i = plotData.values.size(); i > 0; i--) {
            points.add(new PointYio());
        }
    }


    public void setPlotData(PlotData plotData) {
        this.plotData = plotData;
    }


    public void setColor(PlotColor color) {
        this.color = color;
    }
}
