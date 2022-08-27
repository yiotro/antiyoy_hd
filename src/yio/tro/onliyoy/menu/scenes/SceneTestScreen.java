package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.HorSampleItem;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SpectateMatchItem;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.multi_button.MultiButtonElement;
import yio.tro.onliyoy.menu.elements.resizable_element.*;
import yio.tro.onliyoy.menu.elements.rules_picker.RulesPickerElement;
import yio.tro.onliyoy.menu.elements.setup_entities.CondensedEntitiesViewElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetMatchSpectateData;
import yio.tro.onliyoy.net.shared.NetRandomNicknameArguments;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.name_generator.NameGenerator;

import java.util.ArrayList;
import java.util.Random;

public class SceneTestScreen extends SceneYio {


    private LabelElement labelElement;
    int counter;
    RepeatYio<SceneTestScreen> repeatCount;
    private ResizableViewElement resizableViewElement;
    RepeatYio<SceneTestScreen> repeatRandomizeRvElement;
    private AnnounceViewElement mainLabel;
    private RulesPickerElement rulesPickerElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected void initialize() {
        createInternals();
        createBackButton();
        initRepeats();
    }


    private void createInternals() {
        createMainLabel();
        createCondensedEntitiesView();
    }


    private void createCondensedEntitiesView() {
        CondensedEntitiesViewElement condensedEntitiesViewElement;
        condensedEntitiesViewElement = uiFactory.getCondensedEntitiesViewElement()
                .setParent(mainLabel)
                .setSize(0.85, 0.3)
                .centerHorizontal()
                .setReadOnly(false)
                .alignBottom(0.03);
        mainLabel.setAnimation(AnimationYio.down); // yeah, it's strange but cev element won't be resized anyway
        condensedEntitiesViewElement.loadValues(generateEntityArray());
    }


    private PlayerEntity[] generateEntityArray() {
        return new PlayerEntity[]{
               new PlayerEntity(null, EntityType.human, HColor.aqua),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.red),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.yellow),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.cyan),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.blue),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.lavender),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.algae),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.whiskey),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.mint),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.ice),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.rose),
               new PlayerEntity(null, EntityType.ai_balancer, HColor.brown),
        };
    }


    private void createRulesPickerElement() {
        uiFactory.getSlider()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignTop(0.07)
                .setTitle("Slider")
                .setPossibleValues(RulesType.class);

        rulesPickerElement = uiFactory.getRulesPickerElement()
                .setParent(mainLabel)
                .alignUnder(previousElement, 0)
                .centerHorizontal();

        for (RulesType rulesType : RulesType.values()) {
            uiFactory.getCheckButton()
                    .setParent(mainLabel)
                    .centerHorizontal()
                    .alignUnder(previousElement, 0)
                    .setName(Yio.getCapitalizedString(languagesManager.getString("" + rulesType)));
        }
    }


    private void createMainLabel() {
        double h = 0.5;
        mainLabel = uiFactory.getAnnounceViewElement()
                .setSize(0.9, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.from_touch)
                .setTitle("Test screen")
                .setText(" ")
                .setTouchable(false);
    }


    private void createFakeSpectateList() {
        CustomizableListYio customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);

        for (int i = 0; i < 10; i++) {
            NetMatchSpectateData netMatchSpectateData = new NetMatchSpectateData();
            randomizeSpectateData(netMatchSpectateData);
            SpectateMatchItem spectateMatchItem = new SpectateMatchItem();
            customizableListYio.addItem(spectateMatchItem);
            spectateMatchItem.setNetMatchSpectateData(netMatchSpectateData);
        }
    }


    private void randomizeSpectateData(NetMatchSpectateData netMatchSpectateData) {
        netMatchSpectateData.name = "Match name";
        netMatchSpectateData.levelSize = LevelSize.tiny;
        netMatchSpectateData.rulesType = RulesType.def;
        netMatchSpectateData.diplomacy = false;
        netMatchSpectateData.hasPassword = false;
        netMatchSpectateData.matchId = "0";
        ArrayList<String> participants = netMatchSpectateData.participants;
        int quantity = 3 + (new Random()).nextInt(7);
        NameGenerator nameGenerator = new NameGenerator();
        NetRandomNicknameArguments nrmArguments = new NetRandomNicknameArguments();
        nameGenerator.setGroups(nrmArguments.groups);
        nameGenerator.setMasks(nrmArguments.masks);
        nameGenerator.setCapitalize(true);
        for (int i = 0; i < quantity; i++) {
            participants.add(nameGenerator.generate());
        }
    }


    private void createSliderTest() {
        ButtonYio buttonYio = uiFactory.getButton()
                .setSize(0.9, 0.4)
                .centerVertical()
                .centerHorizontal()
                .applyText(" ")
                .setTouchable(false)
                .setAnimation(AnimationYio.from_touch);

        uiFactory.getSlider()
                .setParent(buttonYio)
                .setTitle("Slider 1")
                .centerHorizontal()
                .alignTop(0.05)
                .setPossibleValues(new int[]{0, 1});

        uiFactory.getSlider()
                .setParent(buttonYio)
                .setTitle("Slider 2")
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setPossibleValues(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});

        uiFactory.getSlider()
                .setParent(buttonYio)
                .setTitle("Slider 3")
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setPossibleValues(NmType.class);
    }


    private void createResizableViewElement() {
        resizableViewElement = uiFactory.getResizableViewElement()
                .setSize(0.8, 0.01)
                .centerHorizontal()
                .alignBottom(0.45);

        for (int i = 0; i < 3; i++) {
            RveTextItem rveTextItem = new RveTextItem();
            rveTextItem.setTitle("item " + (i + 1));
            resizableViewElement.addItem(rveTextItem);
        }
        RveEmptyItem emptyItem = new RveEmptyItem(0.07);
        emptyItem.setKey("blank");
        resizableViewElement.addItem(emptyItem);

        resizableViewElement.addButton()
                .setSize(0.2, 0.045)
                .alignBottom(0.015)
                .alignRight(0.03)
                .setTitle("Ok")
                .setReaction(new Reaction() {
                    @Override
                    protected void apply() {
                        MenuSwitcher.getInstance().createChooseGameModeMenu();
                    }
                });

        resizableViewElement.addButton()
                .setSize(GraphicsYio.convertToWidth(0.045))
                .alignBottom(0.015)
                .alignLeft(0.03)
                .setIcon(GraphicsYio.loadTextureRegion("menu/info_icon.png", true))
                .setKey("expand")
                .setReaction(new Reaction() {
                    @Override
                    protected void apply() {
                        expandRvElement();
                    }
                });
    }


    private void initRepeats() {
        repeatCount = new RepeatYio<SceneTestScreen>(this, 6) {
            @Override
            public void performAction() {
                parent.count();
            }
        };
        repeatRandomizeRvElement = new RepeatYio<SceneTestScreen>(this, 90) {
            @Override
            public void performAction() {
                parent.expandRvElement();
            }
        };
    }


    private void createColoredList() {
        CustomizableListYio customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.7, 0.7)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .alignBottom(0.1);

        for (int i = 0; i < 16; i++) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setHeight(0.08f * GraphicsYio.height);
            scrollListItem.setTitle("Item " + (i + 1));
            customizableListYio.addItem(scrollListItem);
        }
    }


    private void createIconLabelElement() {
        uiFactory.getIconLabelElement()
                .setSize(0.4, 0.06)
                .alignTop(0.05)
                .alignRight(0.07)
                .setBackgroundEnabled(true)
                .setAnimation(AnimationYio.up)
                .applyText("Icon label")
                .alignTextToTheRight();
    }


    private void createMultiButtonCheck() {
        double h = 6 * 0.08;
        MultiButtonElement multiButtonElement = uiFactory.getMultiButtonElement()
                .setSize(0.7, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.from_touch);

        BackgroundYio[] array = new BackgroundYio[]{
                BackgroundYio.green,
                BackgroundYio.yellow,
                BackgroundYio.orange,
                BackgroundYio.cyan,
                BackgroundYio.magenta,
                BackgroundYio.red
        };

        for (int i = 0; i < array.length; i++) {
            addColoredLocalButton(multiButtonElement, array[i]);
        }
    }


    private void addColoredLocalButton(MultiButtonElement multiButtonElement, BackgroundYio backgroundYio) {
        final String capitalizedString = Yio.getCapitalizedString("" + backgroundYio);
        multiButtonElement.addLocalButton(capitalizedString, backgroundYio, new Reaction() {
            @Override
            protected void apply() {
                Scenes.keyboard.create();
                Scenes.keyboard.setValue(capitalizedString);
                Scenes.keyboard.setReaction(new AbstractKbReaction() {
                    @Override
                    public void onInputFromKeyboardReceived(String input) {
                        Scenes.notification.show(input);
                    }
                });
            }
        });
    }


    @Override
    public void move() {
        super.move();
    }


    private void expandRvElement() {
        resizableViewElement.deactivateItem("blank");
        resizableViewElement.deactivateButton("expand");
        for (int i = 0; i < 4; i++) {
            RveTextItem rveTextItem = new RveTextItem();
            rveTextItem.setTitle("additional item " + (i + 1));
            resizableViewElement.addItem(rveTextItem);
        }
    }


    private void count() {
        if (labelElement == null) return;
        counter++;
        System.out.println("counter = " + counter);
        labelElement.setString("" + counter);
    }


    private void createHorizontalList() {
        CustomizableListYio customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, GraphicsYio.convertToHeight(0.9))
                .centerHorizontal()
                .centerVertical()
                .setHorizontalMode(true)
                .setAnimation(AnimationYio.center);

        for (int i = 0; i < 5; i++) {
            HorSampleItem horSampleItem = new HorSampleItem();
            customizableListYio.addItem(horSampleItem);
        }
    }


    private void createBackButton() {
        spawnBackButton(getOpenSceneReaction(Scenes.secretScreen));
    }


}
