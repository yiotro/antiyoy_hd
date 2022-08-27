package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneCaptainUI extends ModalSceneYio{


    @Override
    protected void initialize() {
        createLaunchButton();
        createCancelButton();
    }


    private void createCancelButton() {
        uiFactory.getButton()
                .setSize(0.3, 0.055)
                .centerHorizontal()
                .alignBottom(0.02)
                .setBackground(BackgroundYio.red)
                .applyText("cancel")
                .setReaction(getCancelReaction())
                .setAllowedToAppear(getCancelCondition())
                .setAnimation(AnimationYio.down);
    }


    private ConditionYio getCancelCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return !isInWaitingForPlayersMode();
            }
        };
    }


    private Reaction getCancelReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                netRoot.sendMessage(NmType.captain_cancel, Scenes.matchLobby.netMatchLobbyData.matchId);
            }
        };
    }


    private void createLaunchButton() {
        uiFactory.getButton()
                .setSize(0.45, 0.07)
                .alignRight(0.05)
                .alignTop(0.03)
                .setBackground(BackgroundYio.green)
                .applyText("launch")
                .setReaction(getLaunchReaction())
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setAllowedToAppear(getLaunchCondition())
                .setAnimation(AnimationYio.up);
    }


    private ConditionYio getLaunchCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return isInWaitingForPlayersMode();
            }
        };
    }


    private Reaction getLaunchReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                netRoot.sendMessage(NmType.captain_launch, Scenes.matchLobby.netMatchLobbyData.matchId);
            }
        };
    }


    private boolean isInWaitingForPlayersMode() {
        return Scenes.matchLobby.matchPreparationViewElement.startTime == 0;
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
