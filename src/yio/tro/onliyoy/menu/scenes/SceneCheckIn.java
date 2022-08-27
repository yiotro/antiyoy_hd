package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.resizable_element.*;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetRandomNicknameArguments;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.name_generator.NameGenerator;

import java.util.Random;

public class SceneCheckIn extends SceneYio{

    String nameFromServer;
    private AnnounceViewElement announceViewElement;
    private ResizableViewElement rvElement;
    long seed;
    private RveButton randomNicknameButton;
    NameGenerator nameGenerator;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        initNameGenerator();
        createRvElement();
    }


    private void initNameGenerator() {
        nameGenerator = new NameGenerator();
        NetRandomNicknameArguments nrmArguments = new NetRandomNicknameArguments();
        nameGenerator.setGroups(nrmArguments.groups);
        nameGenerator.setMasks(nrmArguments.masks);
        nameGenerator.setCapitalize(true);
    }


    private void addBlankItem(double h) {
        RveEmptyItem blankItem = new RveEmptyItem(h);
        blankItem.setKey("blank");
        rvElement.addItem(blankItem);
    }


    private void createRvElement() {
        rvElement = uiFactory.getResizableViewElement()
                .setSize(0.8, 0.01)
                .centerHorizontal()
                .setAnimation(AnimationYio.center)
                .alignBottom(0.45);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        seed = YioGdxGame.random.nextLong();
        loadValues();
    }


    private void loadValues() {
        rvElement.clearItems();
        addTitleItem();
        addBlankItem(0.03);

        double w = 0.65;
        double offset = (0.8 - w) / 2;
        double y = 0.09;
        double h = 0.07;

        addBlankItem(h);
        rvElement.addButton()
                .setSize(w, 0.05)
                .alignTop(y)
                .alignLeft(offset)
                .setTitle(nameFromServer)
                .setReaction(getDefaultNameReaction());
        y += h;

        if (nameFromServer.contains(" ")) {
            addBlankItem(h);
            rvElement.addButton()
                    .setSize(w, 0.05)
                    .alignTop(y)
                    .alignLeft(offset)
                    .setTitle(getShortName())
                    .setReaction(getShortNameReaction());
            y += h;
        }

        addBlankItem(h);
        randomNicknameButton = rvElement.addButton()
                .setSize(w - GraphicsYio.convertToWidth(h), 0.05)
                .alignTop(y)
                .alignLeft(offset)
                .setTitle("-")
                .setReaction(getChooseRandomNicknameReaction());
        rvElement.addButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignTop(y)
                .alignRight(offset)
                .setIcon(GraphicsYio.loadTextureRegion("menu/editor/dice.png", true))
                .setBackgroundEnabled(true)
                .setReaction(getRandomizeReaction());

        addBlankItem(0.03);
        applySeed();
    }


    private Reaction getShortNameReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.confirmCheckIn.prepare(getShortName(), "short");
                Scenes.confirmCheckIn.create();
            }
        };
    }


    private Reaction getDefaultNameReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.confirmCheckIn.prepare(nameFromServer, "def");
                Scenes.confirmCheckIn.create();
            }
        };
    }


    private Reaction getChooseRandomNicknameReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.confirmCheckIn.prepare(randomNicknameButton.title.string, "" + seed);
                Scenes.confirmCheckIn.create();
            }
        };
    }


    private Reaction getRandomizeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                seed = YioGdxGame.random.nextLong();
                applySeed();
            }
        };
    }


    private void applySeed() {
        Random random = new Random(seed);
        nameGenerator.setRandom(random);
        randomNicknameButton.setTitle(nameGenerator.generate());
    }


    private String getShortName() {
        String[] split = nameFromServer.split(" ");
        if (split.length < 2) return nameFromServer;
        return Yio.getCapitalizedString(split[0].substring(0, 1)) + ". " + split[1];
    }


    private void addTitleItem() {
        RveTextItem titleItem = new RveTextItem();
        titleItem.setCentered(true);
        titleItem.setFont(Fonts.gameFont);
        titleItem.setTitle(languagesManager.getString("choose_nickname"));
        titleItem.setHeight(0.04);
        rvElement.addItem(titleItem);
    }


    public void setNameFromServer(String nameFromServer) {
        this.nameFromServer = nameFromServer;
    }
}
