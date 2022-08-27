package yio.tro.onliyoy.menu.elements.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NmbdItem;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class NetTurnViewElement extends InterfaceElement<NetTurnViewElement> {

    public IconTextYio iconTextYio;
    public RectangleYio incBounds;
    private float tOffset;
    public HColor currentColor;
    public HColor previousColor;
    public FactorYio slideFactor;
    public FactorYio colorFactor;
    RepeatYio<NetTurnViewElement> repeatUpdate;
    public SelectionEngineYio selectionEngineYio;
    boolean touchedCurrently;
    public RectangleYio touchPosition;
    RepeatYio<NetTurnViewElement> repeatListen;
    public AvatarType avatarType;


    public NetTurnViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        iconTextYio = new IconTextYio();
        iconTextYio.setFont(Fonts.miniFont);
        iconTextYio.setOffset(0.01f * GraphicsYio.width);
        incBounds = new RectangleYio();
        tOffset = 0.025f * GraphicsYio.height;
        currentColor = null;
        previousColor = null;
        slideFactor = new FactorYio();
        colorFactor = new FactorYio();
        selectionEngineYio = new SelectionEngineYio();
        touchPosition = new RectangleYio();
        initRepeats();
    }


    private void initRepeats() {
        repeatUpdate = new RepeatYio<NetTurnViewElement>(this, 30) {
            @Override
            public void performAction() {
                parent.updateTitleTime();
            }
        };
        repeatListen = new RepeatYio<NetTurnViewElement>(this, 2) {
            @Override
            public void performAction() {
                parent.doListen();
            }
        };
    }


    @Override
    protected NetTurnViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        slideFactor.move();
        repeatUpdate.move();
        repeatListen.move();
        updateIconTextPosition();
        updateIncBounds();
        updateTouchPosition();
        colorFactor.move();
        moveSelection();
        iconTextYio.move();
    }


    private void updateTouchPosition() {
        touchPosition.setBy(incBounds);
        touchPosition.increase(0.015f * GraphicsYio.width);
    }


    private void moveSelection() {
        if (touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void doListen() {
        boolean slideTarget = getSlideTarget();
        if (slideTarget && !slideFactor.isInAppearState()) {
            doSlideDown();
            return;
        }
        if (!slideTarget && !slideFactor.isInDestroyState()) {
            doSlideUp();
        }
    }


    private boolean getSlideTarget() {
        if (Scenes.provinceManagement.isCurrentlyVisible()) return true;
        if (getGameController().touchMode == TouchMode.tmDiplomacy) return true;
        return false;
    }


    private void updateTitleTime() {
        EntitiesManager entitiesManager = getEntitiesManager();
        PlayerEntity currentEntity = entitiesManager.getCurrentEntity();
        if (!currentEntity.isHuman()) return;
        String prefix = LanguagesManager.getInstance().getString("your_turn");
        String timeString = "";
        NetRoot netRoot = menuControllerYio.yioGdxGame.netRoot;
        long turnEndTime = netRoot.currentMatchData.turnEndTime;
        if (turnEndTime > 0 && System.currentTimeMillis() < turnEndTime) {
            long deltaTimeMillis = turnEndTime - System.currentTimeMillis();
            long deltaTimeSeconds = deltaTimeMillis / 1000;
            timeString = " " + deltaTimeSeconds;
            if (deltaTimeSeconds > 120) {
                timeString = " " + Yio.convertTimeToUnderstandableString(60 * deltaTimeSeconds);
            }
        }
        setValues(prefix + timeString, AvatarType.empty);
    }


    private EntitiesManager getEntitiesManager() {
        // I need to use ref model because it's closer to what player actually sees
        return getViewableModel().refModel.entitiesManager;
    }


    private void doSlideDown() {
        slideFactor.appear(MovementType.approach, 5);
    }


    private void doSlideUp() {
        slideFactor.destroy(MovementType.lighty, 5);
    }


    private void updateIncBounds() {
        incBounds.setBy(iconTextYio.bounds);
        incBounds.increase(0.015f * GraphicsYio.width);
    }


    private void updateIconTextPosition() {
        iconTextYio.centerHorizontal(viewPosition);
        iconTextYio.position.y = position.y + position.height - tOffset;
        iconTextYio.position.y += (1 - appearFactor.getValue()) * (2 * tOffset + iconTextYio.getHeight());
        iconTextYio.position.y -= slideFactor.getValue() * 3 * iconTextYio.getHeight();
        iconTextYio.onPositionChanged();
    }


    public void update() {
        EntitiesManager entitiesManager = getEntitiesManager();
        PlayerEntity currentEntity = entitiesManager.getCurrentEntity();
        if (currentEntity.isHuman()) {
            setColor(null);
            setValues(LanguagesManager.getInstance().getString("your_turn"), AvatarType.empty);
            updateTitleTime();
            updateIconTextPosition();
            updateIncBounds();
            repeatUpdate.resetCountDown();
            return;
        }
        setColor(currentEntity.color);
        String name = currentEntity.name;
        NetRoot netRoot = menuControllerYio.yioGdxGame.netRoot;
        NmbdItem nmbdItem = netRoot.currentMatchData.getItem(currentEntity.color);
        AvatarType avatarType = AvatarType.robot;
        if (nmbdItem != null) {
            name = CharLocalizerYio.getInstance().apply(nmbdItem.name);
            avatarType = nmbdItem.avatarType;
        }
        setValues(name, avatarType);
    }


    private void setValues(String string, AvatarType avatarType) {
        this.avatarType = avatarType;
        iconTextYio.setString(string);
        iconTextYio.updateMetrics();
        iconTextYio.setupByAvatarType(avatarType);
    }


    @Override
    public void onDestroy() {
        touchedCurrently = false;
    }


    @Override
    public void onAppear() {
        slideFactor.setValue(1);
        slideFactor.stop();
        avatarType = AvatarType.empty;
        update();
        colorFactor.setValue(1);
        touchedCurrently = false;
    }


    public void setColor(HColor color) {
        if (currentColor == color) return;
        previousColor = currentColor;
        currentColor = color;
        onColorChanged();
    }


    private void onColorChanged() {
        colorFactor.reset();
        colorFactor.appear(MovementType.inertia, 1.8);
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = touchPosition.isPointInside(currentTouch);
        if (!touchedCurrently) return false;
        selectionEngineYio.applySelection();
        return true;
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
        SoundManager.playSound(SoundType.button);
        Scenes.matchInfoPanel.create();
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderNetTurnViewElement;
    }
}
