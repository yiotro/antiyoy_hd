package yio.tro.onliyoy.menu.scenes.info;

import com.badlogic.gdx.Gdx;
import yio.tro.onliyoy.*;
import yio.tro.onliyoy.menu.MenuParams;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneAboutGame extends AbstractInfoScene {


    private ButtonYio helpButton;
    private ButtonYio specialThanksButton;


    @Override
    protected void initInfoLabelPosition() {
        super.initInfoLabelPosition();
        infoLabelPosition.height = 0.7f;
        infoLabelPosition.y = 0.45f - infoLabelPosition.height / 2;
    }


    @Override
    public void initialize() {
        String articleKey = "about_game_article";
        if (YioGdxGame.platformType == PlatformType.ios) {
            articleKey = "antiyoy_hd";
        }
        createInfoMenu(articleKey, getBackReaction());
        createHelpButton();
        createPrivacyPolicyButton();
        createSpecialThanksButton();
        createDiscordButton();
        createRedditButton();
    }


    private void createRedditButton() {
        uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignLeft(previousElement, 0.02)
                .alignTop(0.02)
                .setTouchOffset(0.01)
                .loadTexture("menu/external/reddit.png")
                .setAnimation(AnimationYio.up)
                .setReaction(getRedditReaction());
    }


    private Reaction getRedditReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                String link = getRedditLink();
                Gdx.app.getClipboard().setContents(link);
                Scenes.notification.show("link_copied_to_clipboard");
                Gdx.net.openURI(link);
            }
        };
    }


    private void createDiscordButton() {
        uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignRight(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.01)
                .loadTexture("menu/external/discord.png")
                .setAnimation(AnimationYio.up)
                .setReaction(getDiscordReaction());
    }


    private Reaction getDiscordReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                String link = getDiscordLink();
                Gdx.app.getClipboard().setContents(link);
                Scenes.notification.show("link_copied_to_clipboard");
                Gdx.net.openURI(link);
            }
        };
    }


    private String getRedditLink() {
        return "https://www.reddit.com/r/yiotro_games/";
    }


    private String getDiscordLink() {
        return "https://discord.gg/Wx27T7znhn";
    }


    private void createPrivacyPolicyButton() {
        uiFactory.getButton()
                .setSize(0.7, 0.05)
                .setParent(infoPanel)
                .centerHorizontal()
                .alignBottom(0.018)
                .setFont(Fonts.miniFont)
                .setAllowedToAppear(getNotIosCondition())
                .applyText("privacy_policy")
                .setReaction(getPrivacyPolicyReaction());
    }


    private Reaction getPrivacyPolicyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                doShowPrivacyPolicy();
            }
        };
    }


    private void doShowPrivacyPolicy() {
        Scenes.privacyPolicy.create();
        Scenes.privacyPolicy.setBackReaction(getOpenSceneReaction(Scenes.aboutGame));
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onBackButtonPressed();
            }
        };
    }


    private void onBackButtonPressed() {
        yioGdxGame.setGamePaused(true);
        if (YioGdxGame.platformType == PlatformType.ios) {
            Scenes.mainLobby.create();
            return;
        }
        Scenes.settings.create();
    }


    private void createSpecialThanksButton() {
        if (!MenuParams.SPECIAL_THANKS) return;
        specialThanksButton = uiFactory.getButton()
                .setSize(0.7, 0.05)
                .setParent(infoPanel)
                .centerHorizontal()
                .setFont(Fonts.miniFont)
                .applyText("special_thanks_title")
                .setReaction(getOpenSceneReaction(Scenes.specialThanks));

        if (YioGdxGame.platformType != PlatformType.ios) {
            specialThanksButton.alignAbove(previousElement, 0.01);
        } else {
            specialThanksButton.alignBottom(0.018);
        }
    }


    private void createHelpButton() {
        if (!MenuParams.HELP_SECTION) return;
        helpButton = uiFactory.getButton()
                .setPosition(0.55, 0.9, 0.4, 0.07)
                .applyText("help")
                .setAnimation(AnimationYio.center)
                .setReaction(new Reaction() {
                    @Override
                    protected void apply() {

                    }
                });
    }

}
