package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetTimeSynchronizer;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.RepeatYio;

public class MatchPreparationViewElement extends InterfaceElement<MatchPreparationViewElement> {

    public long startTime;
    public RenderableTextYio title;
    RepeatYio<MatchPreparationViewElement> repeatUpdate;
    boolean ready;
    long srvCreationTime;


    public MatchPreparationViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        startTime = 0;
        initTitle();
        initRepeats();
    }


    private void initRepeats() {
        repeatUpdate = new RepeatYio<MatchPreparationViewElement>(this, 20) {
            @Override
            public void performAction() {
                parent.updateTitleString();
            }
        };
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        updateTitleString();
    }


    private void updateTitleString() {
        title.setString(getCurrentTitleString());
        title.updateMetrics();
    }


    private String getCurrentTitleString() {
        if (startTime == 0) {
            // preparation
            String prefix = LanguagesManager.getInstance().getString("preparation");
            long creationTimeMillis = NetTimeSynchronizer.getInstance().convertToClientTime(srvCreationTime);
            long deltaTimeMillis = System.currentTimeMillis() - creationTimeMillis;
            int deltaTimeFrames = Yio.convertMillisIntoFrames(deltaTimeMillis);
            String timeString = Yio.convertTimeToUnderstandableString(deltaTimeFrames);
            return prefix + " (" + timeString + ")";
        }
        if (System.currentTimeMillis() < startTime) {
            long deltaTimeInMillis = startTime - System.currentTimeMillis();
            int deltaTimeInFrames = Yio.convertMillisIntoFrames(deltaTimeInMillis);
            String timeString = Yio.convertTimeToUnderstandableString(deltaTimeInFrames);
            return LanguagesManager.getInstance().getString("starting_in") + " " + timeString;
        }
        return LanguagesManager.getInstance().getString("match_is_starting");
    }


    @Override
    protected MatchPreparationViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        repeatUpdate.move();
        updateTitlePosition();
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        startTime = 0;
        ready = true;
    }


    @Override
    public boolean checkToPerformAction() {
        if (ready) {
            if (startTime == 0) return false;
            if (System.currentTimeMillis() < startTime) return false;
            ready = false;
            if (!Scenes.matchLobby.isCurrentlyVisible()) return false;
            Scenes.waitMatchLaunching.create();
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    public void setStartTime(long startTime) {
        this.startTime = startTime;
        updateTitleString();
    }


    public void setCreationTime(long creationTime) {
        this.srvCreationTime = creationTime;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderMatchPreparationViewElement;
    }
}
