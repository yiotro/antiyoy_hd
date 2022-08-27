package yio.tro.onliyoy.menu.elements.choose_game_mode;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.CornerEngineYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class CgmElement extends InterfaceElement<CgmElement> {

    public CornerEngineYio cornerEngineYio;
    public ArrayList<CgmGroup> groups;
    boolean touchedCurrently;
    private float touchOffset;
    Reaction targetReaction; // perform immediately if not null
    public RectangleYio dynamicPosition;
    private FactorYio dynamicMagnetFactor;


    public CgmElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        cornerEngineYio = new CornerEngineYio();
        groups = new ArrayList<>();
        touchOffset = 0.05f * GraphicsYio.width;
        targetReaction = null;
        dynamicPosition = new RectangleYio();
        dynamicMagnetFactor = new FactorYio();
    }


    @Override
    protected CgmElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveGroupExpansions();
        updateDynamicPosition();
        moveDynamicMagnetFactor();
        cornerEngineYio.move(dynamicPosition, appearFactor);
        moveGroups();
    }


    private void moveGroupExpansions() {
        for (CgmGroup cgmGroup : groups) {
            cgmGroup.moveExpansion();
        }
    }


    private void moveDynamicMagnetFactor() {
        dynamicMagnetFactor.move();
        if (!dynamicMagnetFactor.isInAppearState() && appearFactor.getValue() == 1) {
            launchMagnet();
        }
    }


    private void launchMagnet() {
        dynamicMagnetFactor.appear(MovementType.approach, 5);
    }


    private void updateDynamicPosition() {
        dynamicPosition.setBy(viewPosition);
        float sumGroupsHeight = calculateSumGroupsHeight();
        float hDelta = dynamicMagnetFactor.getValue() * (sumGroupsHeight - dynamicPosition.height);
        if (hDelta > 0) {
            dynamicPosition.height += hDelta;
            dynamicPosition.y -= hDelta / 2;
        }
    }


    private float calculateSumGroupsHeight() {
        float s = 0;
        for (CgmGroup cgmGroup : groups) {
            s += cgmGroup.targetHeight;
        }
        return s;
    }


    private void moveGroups() {
        for (CgmGroup cgmGroup : groups) {
            cgmGroup.move();
        }
    }


    @Override
    public void onDestroy() {
        doCancelAllExpansions();
        doCancelAllShrinks();
        dynamicMagnetFactor.destroy(MovementType.lighty, 9);
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        targetReaction = null;
        dynamicPosition.reset();
        dynamicMagnetFactor.reset();
        for (CgmGroup cgmGroup : groups) {
            cgmGroup.onElementAppear();
        }
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        initGroups();
    }


    private void initGroups() {
        addGroup("online_match", BackgroundYio.magenta)
                .addSubButton("duel", getOpenSceneReaction(Scenes.searchingForDuel))
                .addSubButton("quick_match", getOpenSceneReaction(Scenes.quickMatchSearching))
                .addSubButton("custom_match", getOpenSceneReaction(Scenes.customMatches));
        addSimpleGroup("user_levels", BackgroundYio.yellow, getOpenSceneReaction(Scenes.userLevels));
        addGroup("content", BackgroundYio.cyan)
                .addSubButton("calendar", getOpenSceneReaction(Scenes.calendar))
                .addSubButton("campaign", getOpenSceneReaction(Scenes.campaign));
        addSimpleGroup("editor", BackgroundYio.orange, getOpenSceneReaction(Scenes.editorLobby));
        addGroup("training", BackgroundYio.green)
                .addSubButton("skirmish", getOpenSceneReaction(Scenes.setupSkirmish))
                .addSubButton("spectate", getOpenSceneReaction(Scenes.chooseMatchToSpectate))
                .addSubButton("load", getOpenSceneReaction(Scenes.loadFromSlot));
    }


    private CgmGroup addGroup(String key, BackgroundYio backgroundYio) {
        CgmGroup cgmGroup = new CgmGroup(this);
        cgmGroup.setBackgroundYio(backgroundYio);
        cgmGroup.setTitle(key);
        cgmGroup.setSimple(false);
        groups.add(cgmGroup);
        onGroupAdded();
        return cgmGroup;
    }


    private void addSimpleGroup(String key, BackgroundYio backgroundYio, Reaction reaction) {
        CgmGroup cgmGroup = new CgmGroup(this);
        cgmGroup.setBackgroundYio(backgroundYio);
        cgmGroup.setTitle(key);
        cgmGroup.setReaction(reaction);
        groups.add(cgmGroup);
        onGroupAdded();
    }


    private void onGroupAdded() {
        float targetHeight = position.height / groups.size();
        for (int i = 0; i < groups.size(); i++) {
            CgmGroup cgmGroup = groups.get(i);
            cgmGroup.setIndex(i);
            cgmGroup.setDefaultHeight(targetHeight);
        }
    }


    @Override
    public boolean checkToPerformAction() {
        if (targetReaction != null) {
            targetReaction.perform(menuControllerYio);
            targetReaction = null;
            return true;
        }
        return false;
    }


    @Override
    public boolean acceptsKeycode(int keycode) {
        return super.acceptsKeycode(keycode) || keycode == Input.Keys.ENTER;
    }


    private CgmGroup getCurrentlyTouchedGroup() {
        CgmGroup currentlyTouchedGroup = getCurrentlyTouchedGroup(0);
        if (currentlyTouchedGroup != null) return currentlyTouchedGroup;
        return getCurrentlyTouchedGroup(touchOffset);
    }


    private CgmGroup getCurrentlyTouchedGroup(float tOffset) {
        for (CgmGroup cgmGroup : groups) {
            if (cgmGroup.isTouchedBy(currentTouch, tOffset)) return cgmGroup;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = true;
        if (!touchedCurrently) return false;
        selectGroup();
        return true;
    }


    private void selectGroup() {
        CgmGroup currentlyTouchedGroup = getCurrentlyTouchedGroup();
        if (currentlyTouchedGroup == null) return;
        currentlyTouchedGroup.select(currentTouch);
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    private void onClick() {
        CgmGroup currentlyTouchedGroup = getCurrentlyTouchedGroup();
        if (currentlyTouchedGroup == null) {
            doCancelAllExpansions();
            doCancelAllShrinks();
            return;
        }
        onClickedOnGroup(currentlyTouchedGroup);
        SoundManager.playSound(SoundType.button);
    }


    private void onClickedOnGroup(CgmGroup cgmGroup) {
        if (cgmGroup.simple) {
            targetReaction = cgmGroup.reaction;
            return;
        }
        if (cgmGroup.expansionFactor.isInAppearState()) {
            CgmSubButton touchedButton = cgmGroup.getTouchedButton(currentTouch);
            if (touchedButton == null) return;
            targetReaction = touchedButton.reaction;
        } else {
            checkToExpandGroup(cgmGroup);
        }
    }


    private void checkToExpandGroup(CgmGroup currentlyTouchedGroup) {
        if (currentlyTouchedGroup.expansionFactor.isInAppearState()) return;
        doCancelAllExpansions();
        currentlyTouchedGroup.doExpand();
        doShrinkAllGroups();
        currentlyTouchedGroup.shrinkFactor.destroy(MovementType.inertia, 4);
        launchMagnet();
        checkToShowOnlineDisclaimer(currentlyTouchedGroup);
    }


    private void checkToShowOnlineDisclaimer(CgmGroup currentlyTouchedGroup) {
        if (!currentlyTouchedGroup.key.equals("online_match")) return;
        if (OneTimeInfo.getInstance().onlineDisclaimer) return;
        if (getNetRoot().initialStatisticsData.getHoursOnline() > 3) return;
        OneTimeInfo.getInstance().onlineDisclaimer = true;
        OneTimeInfo.getInstance().save();
        Scenes.onlineDisclaimer.create();
    }


    void doCancelAllExpansions() {
        for (CgmGroup cgmGroup : groups) {
            cgmGroup.doCancelExpansion();
        }
    }


    void doShrinkAllGroups() {
        for (CgmGroup cgmGroup : groups) {
            cgmGroup.shrinkFactor.appear(MovementType.inertia, 3.5);
        }
    }


    void doCancelAllShrinks() {
        for (CgmGroup cgmGroup : groups) {
            cgmGroup.shrinkFactor.destroy(MovementType.inertia, 4);
        }
    }


    public void pressQuickMatch() {
        if (groups.size() == 0) return;
        CgmGroup firstGroup = groups.get(0);
        CgmSubButton firstSubButton = firstGroup.subButtons.get(0);
        RectangleYio pos = firstSubButton.viewPosition;
        menuControllerYio.currentTouchPoint.set(pos.x + pos.width / 2, pos.y + pos.height / 2);
        currentTouch.setBy(menuControllerYio.currentTouchPoint);
        onClick();
    }


    @Override
    public void pressArtificially(int keycode) {
        if (groups.size() == 0) return;
        super.pressArtificially(keycode);
        int index = 0;
        if (keycode == Input.Keys.BACK) {
            index = groups.size() - 1;
        }
        CgmGroup firstGroup = groups.get(index);
        RectangleYio pos = firstGroup.viewPosition;
        menuControllerYio.currentTouchPoint.set(pos.x + pos.width / 2, pos.y + pos.height / 2);
        currentTouch.setBy(menuControllerYio.currentTouchPoint);
        selectGroup();
        onClick();
    }


    protected Reaction getOpenSceneReaction(final SceneYio sceneYio) {
        return new Reaction() {
            @Override
            protected void apply() {
                sceneYio.create();
            }
        };
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCgmElement;
    }
}
