package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;

public class DirectionsManager {

    CoreModel coreModel;
    int adjCoordinate1;
    int adjCoordinate2;
    double[] angles;


    public DirectionsManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        initAngles();
    }


    private void initAngles() {
        angles = new double[6];
        double a = Math.PI / 2;
        double da = Math.PI / 3;
        for (int dir = 0; dir < 6; dir++) {
            angles[dir] = a;
            a -= da;
            if (a < 0) {
                a += 2 * Math.PI;
            }
        }
    }


    public double getAngle(int direction) {
        return angles[direction];
    }


    public Hex getAdjacentHex(Hex hex, int direction) {
        // this method should be only used outside core model
        // also it's not fast so it shouldn't be used frequently
        updateAdjacentCoordinates(hex, direction);
        return coreModel.getHex(adjCoordinate1, adjCoordinate2);
    }


    private void updateAdjacentCoordinates(Hex hex, int direction) {
        adjCoordinate1 = hex.coordinate1;
        adjCoordinate2 = hex.coordinate2;
        switch (direction) {
            default:
                System.out.println("DirectionsManager.updateAdjacentCoordinates: invalid direction");
                break;
            case 0:
                adjCoordinate1++;
                break;
            case 1:
                adjCoordinate2++;
                break;
            case 2:
                adjCoordinate1--;
                adjCoordinate2++;
                break;
            case 3:
                adjCoordinate1--;
                break;
            case 4:
                adjCoordinate2--;
                break;
            case 5:
                adjCoordinate1++;
                adjCoordinate2--;
                break;
        }
    }


    public void setCoreModel(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    public int getAdjCoordinate1() {
        return adjCoordinate1;
    }


    public int getAdjCoordinate2() {
        return adjCoordinate2;
    }
}
