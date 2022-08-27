package yio.tro.onliyoy.stuff.posmap;

import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;

public class PosMapYio {

    public int width, height;
    public float sectorSize;
    private ArrayList<AbstractPmObjectYio>[][] array;
    public RectangleYio position;


    public PosMapYio(RectangleYio position, double sectorSize) {
        this.position = position;
        this.sectorSize = (float) sectorSize;
        width = (int) (position.width / sectorSize) + 2;
        height = (int) (position.height / sectorSize) + 2;
        array = new ArrayList[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[i][j] = new ArrayList<>();
            }
        }
    }


    public void onObjectChangedPosition(AbstractPmObjectYio objectYio) {
        objectYio.updatePosMapPosition();
        updateCurrentIndex(objectYio);
        if (!hasObjectMovedToAnotherSector(objectYio)) return;
        removeObjectFromSector(objectYio, objectYio.previousIndex);
        addObjectToSector(objectYio, objectYio.currentIndex);
        objectYio.previousIndex.setBy(objectYio.currentIndex);
    }


    private boolean hasObjectMovedToAnotherSector(AbstractPmObjectYio objectYio) {
        if (objectYio.currentIndex.equals(objectYio.previousIndex)) return false;
        if (!isIndexPointInsideLevel(objectYio.currentIndex)) return false;
        return true;
    }


    private boolean isIndexPointInsideLevel(PmSectorIndex sectorIndex) {
        return isInside(sectorIndex.x, sectorIndex.y);
    }


    private void updatePreviousIndex(AbstractPmObjectYio objectYio) {
        objectYio.previousIndex.updateByPoint(this, objectYio.posMapPosition);
    }


    private void updateCurrentIndex(AbstractPmObjectYio objectYio) {
        objectYio.currentIndex.updateByPoint(this, objectYio.posMapPosition);
    }


    public void removeObject(AbstractPmObjectYio objectYio) {
        removeObjectFromSector(objectYio, objectYio.previousIndex);
    }


    private void removeObjectFromSector(AbstractPmObjectYio objectYio, PmSectorIndex sectorIndex) {
        array[sectorIndex.x][sectorIndex.y].remove(objectYio);
    }


    private void addObjectToSector(AbstractPmObjectYio objectYio, PmSectorIndex sectorIndex) {
        array[sectorIndex.x][sectorIndex.y].add(objectYio);
    }


    public void addObject(AbstractPmObjectYio object) {
        object.updatePosMapPosition();
        updatePreviousIndex(object);
        addObjectToSector(object, object.previousIndex);
    }


    private boolean isInside(int i, int j) {
        return i >= 0 && i < width && j >= 0 && j < height;
    }


    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[i][j].clear();
            }
        }
    }


    public void showInConsole() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                System.out.print(array[i][j].size() + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }


    public ArrayList<AbstractPmObjectYio> getSectorByPosition(PointYio pointYio) {
        int index_x = (int) ((pointYio.x - position.x) / sectorSize);
        int index_y = (int) ((pointYio.y - position.y) / sectorSize);
        return getSector(index_x, index_y);
    }


    public ArrayList<AbstractPmObjectYio> getSector(int i, int j) {
        if (!isInside(i, j)) return null;
        return array[i][j];
    }
}
