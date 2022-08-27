package yio.tro.onliyoy.menu.elements.plot_view;

import java.util.ArrayList;

public class PlotData {

    String name;
    ArrayList<Float> values;
    String description;
    boolean upwards;


    public PlotData() {
        name = "-";
        description = "-";
        values = new ArrayList<>();
        upwards = false;
    }
}
