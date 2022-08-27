package yio.tro.onliyoy.menu.elements.plot_view;

import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class PlotParameters {

    public String name;
    public MovementType movementType;
    public double speed;
    public boolean upwards;
    public PlotColor color;


    public PlotParameters() {
        name = "-";
        movementType = MovementType.approach;
        speed = 1;
        upwards = true;
        color = PlotColor.black;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }


    public void setSpeed(double speed) {
        this.speed = speed;
    }


    public void setUpwards(boolean upwards) {
        this.upwards = upwards;
    }


    public void setColor(PlotColor color) {
        this.color = color;
    }
}
