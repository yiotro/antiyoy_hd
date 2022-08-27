package yio.tro.onliyoy.menu.elements.plot_view;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class PlotDescView implements ReusableYio {

    public RenderableTextYio renderableTextYio;
    public RectangleYio incBounds;
    public PlotColor color;
    boolean upwards;


    public PlotDescView() {
        renderableTextYio = new RenderableTextYio();
        renderableTextYio.setFont(Fonts.miniFont);
        incBounds = new RectangleYio();
    }


    @Override
    public void reset() {
        color = null;
        upwards = false;
    }


    void setText(String string) {
        renderableTextYio.setString(string);
        renderableTextYio.updateMetrics();
    }


    public void setColor(PlotColor color) {
        this.color = color;
    }


    public void setUpwards(boolean upwards) {
        this.upwards = upwards;
    }
}
