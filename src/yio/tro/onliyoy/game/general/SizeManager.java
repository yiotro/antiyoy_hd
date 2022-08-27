package yio.tro.onliyoy.game.general;

import yio.tro.onliyoy.stuff.RectangleYio;

public class SizeManager {

    public LevelSize initialLevelSize;
    public float boundWidth;
    public float boundHeight;
    public RectangleYio position;
    public float defaultWidth;


    public SizeManager(float defaultWidth) {
        this.defaultWidth = defaultWidth;
        position = new RectangleYio();
    }


    public void initLevelSize(LevelSize levelSize) {
        initialLevelSize = levelSize;
        switch (levelSize) {
            case tiny:
                setBounds(1);
                break;
            case small:
                setBounds(1.3);
                break;
            case normal:
                setBounds(1.7);
                break;
            case big:
                setBounds(2);
                break;
            case large:
                setBounds(2.9);
                break;
            case giant:
                setBounds(3.9);
                break;
            case giant_landscape:
                setLandscapeBounds(4);
                break;
        }
    }


    void setLandscapeBounds(double multiplier) {
        boundHeight = (float) (multiplier * defaultWidth);
        boundWidth = 1.6f * boundHeight;
        position.set(0, 0, boundWidth, boundHeight);
    }


    void setBounds(double multiplier) {
        boundWidth = (float) (multiplier * defaultWidth);
        boundHeight = 1.6f * boundWidth;
        position.set(0, 0, boundWidth, boundHeight);
    }

}