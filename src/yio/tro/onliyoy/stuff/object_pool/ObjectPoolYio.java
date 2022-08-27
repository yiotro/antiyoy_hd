package yio.tro.onliyoy.stuff.object_pool;

import java.util.ArrayList;

public abstract class ObjectPoolYio<ObjectType extends ReusableYio> {


    private ArrayList<ObjectType> freeObjects;
    private ArrayList<ObjectType> externalList;
    private int holdSize;


    public ObjectPoolYio() {
        freeObjects = new ArrayList<>();
        externalList = null;
        holdSize = 0;
    }


    public ObjectPoolYio(ArrayList<ObjectType> externalList) {
        freeObjects = new ArrayList<>();
        setExternalList(externalList);
    }


    public abstract ObjectType makeNewObject();


    public void add(ObjectType object) {
        freeObjects.add(object);
    }


    public void clear() {
        freeObjects.clear();
    }


    public ObjectType getNext() {
        if (freeObjects.size() > holdSize) {
            ObjectType object = freeObjects.get(0);
            freeObjects.remove(object);
            object.reset();
            return object;
        }

        ObjectType object = makeNewObject();
        object.reset();
        return object;
    }


    public int getSize() {
        return freeObjects.size();
    }


    public void showInConsole() {
        System.out.println();
        if (freeObjects.size() == 0) {
            System.out.println("Empty pool");
        } else {
            String simpleName = freeObjects.get(0).getClass().getSimpleName();
            System.out.println("Pool" +
                    "(" + freeObjects.size() + ")" +
                    ": " + simpleName);
            if (hasDuplicates()) {
                System.out.println("Pool has duplicates");
            }
        }
    }


    public boolean hasDuplicates() {
        for (int i = 0; i < freeObjects.size(); i++) {
            for (int j = i + 1; j < freeObjects.size(); j++) {
                if (freeObjects.get(i) == freeObjects.get(j)) {
                    System.out.println("Found duplicate in pool");
                    return true;
                }
            }
        }

        return false;
    }


    public void removeFromExternalList(ObjectType object) {
        add(object);
        externalList.remove(object);
    }


    public void clearExternalList() {
        while (externalList.size() > 0) {
            removeFromExternalList(externalList.get(0));
        }
    }


    public ObjectType getFreshObject() {
        ObjectType next = getNext();
        externalList.add(next);
        return next;
    }


    private void setExternalList(ArrayList<ObjectType> externalList) {
        this.externalList = externalList;
    }


    public void setHoldSize(int holdSize) {
        this.holdSize = holdSize;
    }
}
