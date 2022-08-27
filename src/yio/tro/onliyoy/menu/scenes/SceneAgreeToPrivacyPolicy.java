package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneAgreeToPrivacyPolicy extends ModalSceneYio{

    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.4);
        createLabel();
        createPrivacyPolicyButton();
        createAgreeButton();
    }


    private void createAgreeButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.5, 0.06)
                .alignRight(0.04)
                .alignBottom(0.02)
                .setShadow(true)
                .applyText("i_agree")
                .setReaction(getAgreeReaction());
    }


    private Reaction getAgreeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                OneTimeInfo.getInstance().privacyPolicy = true;
                OneTimeInfo.getInstance().save();
                Scenes.entry.googleLoginProcessActive = true;
                yioGdxGame.signInManager.apply(netRoot.signInData);
                destroy();
            }
        };
    }


    private void createPrivacyPolicyButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.8, 0.055)
                .centerHorizontal()
                .alignTop(0.18)
                .setBackground(BackgroundYio.gray)
                .applyText("privacy_policy")
                .setReaction(getPrivacyPolicyReaction());
    }


    private Reaction getPrivacyPolicyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onPrivacyPolicyButtonPressed();
            }
        };
    }


    private void onPrivacyPolicyButtonPressed() {
        Scenes.privacyPolicy.create();
        Scenes.privacyPolicy.setBackReaction(new Reaction() {
            @Override
            protected void apply() {
                Scenes.entry.create();
                Scenes.agreeToPrivacyPolicy.create();
            }
        });
    }


    private void createLabel() {
        uiFactory.getAdvancedLabelElement()
                .setParent(defaultPanel)
                .setSize(0.9, 0.01)
                .centerHorizontal()
                .alignTop(0.03)
                .setFont(Fonts.miniFont)
                .applyText(getLabelText());
    }


    private String getLabelText() {
        return languagesManager.getString("privacy_policy_tldr") + " " + languagesManager.getString("do_you_agree");
    }


    @Override
    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                System.out.println("SceneAgreeToPrivacyPolicy.reaction: start authorizing");
            }
        };
    }
}
