package yio.tro.onliyoy.menu.elements.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.*;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class NetChatViewElement extends InterfaceElement<NetChatViewElement> {

    public ArrayList<NcvItem> items;
    ObjectPoolYio<NcvItem> poolItems;
    RepeatYio<NetChatViewElement> repeatKill;
    private StringBuilder stringBuilder;
    private RenderableTextYio tempRenderableText;


    public NetChatViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        items = new ArrayList<>();
        stringBuilder = new StringBuilder();
        tempRenderableText = new RenderableTextYio();
        tempRenderableText.setFont(Fonts.miniFont);
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatKill = new RepeatYio<NetChatViewElement>(this, 6) {
            @Override
            public void performAction() {
                parent.killOldItems();
            }
        };
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<NcvItem>(items) {
            @Override
            public NcvItem makeNewObject() {
                return new NcvItem(NetChatViewElement.this);
            }
        };
    }


    @Override
    protected NetChatViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        repeatKill.move();
        moveItems();
    }


    private void moveItems() {
        for (NcvItem ncvItem : items) {
            ncvItem.move();
        }
    }


    void killOldItems() {
        if (items.size() == 0) return;
        boolean killedSomething = false;
        for (int i = items.size() - 1; i >= 0; i--) {
            NcvItem ncvItem = items.get(i);
            if (System.currentTimeMillis() < ncvItem.deathTime) continue;
            poolItems.removeFromExternalList(ncvItem);
            killedSomething = true;
        }
        if (killedSomething) {
            updateDeltas();
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    public void onPhraseReceived(NetPhraseData netPhraseData) {
        NetMatchBattleData currentMatchData = getNetRoot().currentMatchData;
        NmbdItem nmbdItem = currentMatchData.getItem(netPhraseData.speakerId);
        String name = CharLocalizerYio.getInstance().apply(nmbdItem.name);
        String valueString = generateValueString(netPhraseData);
        String generatedString = name + ": " + valueString;
        float limit = 0.95f * GraphicsYio.width;
        String[] split = generatedString.split(" ");
        stringBuilder.setLength(0);
        tempRenderableText.setString("");
        String previousText = "";
        for (String token : split) {
            previousText = stringBuilder.toString();
            stringBuilder.append(token).append(" ");
            tempRenderableText.setString(stringBuilder.toString());
            tempRenderableText.updateMetrics();
            if (tempRenderableText.width < limit) continue;
            addMessage(previousText, 5000);
            stringBuilder.setLength(0);
            stringBuilder.append(token).append(" ");
        }
        String leftOutString = stringBuilder.toString();
        if (leftOutString.length() > 0) {
            addMessage(leftOutString, 5000);
        }
    }


    public static String generateValueString(NetPhraseData netPhraseData) {
        int elp;
        String prefix;
        switch (netPhraseData.phraseType) {
            default:
                return LanguagesManager.getInstance().getString("" + netPhraseData.phraseType);
            case check_my_play_time:
                long millis = Long.valueOf(netPhraseData.argument);
                int frames = Yio.convertMillisIntoFrames(millis);
                String time = Yio.convertTimeToUnderstandableString(frames);
                return LanguagesManager.getInstance().getString("my_play_time") + " - " + time;
            case check_my_elp:
                elp = Integer.valueOf(netPhraseData.argument);
                return LanguagesManager.getInstance().getString("my_elp") + " - " + elp;
            case check_my_win_rate:
                double ratio = Float.valueOf(netPhraseData.argument);
                double percent = Yio.roundUp(ratio * 100, 2);
                return LanguagesManager.getInstance().getString("my_win_rate") + " - " + percent + "%";
            case check_my_rank:
                String[] split = netPhraseData.argument.split(" ");
                if (split.length < 2) return "-";
                RankType rankType = RankType.valueOf(split[0]);
                elp = Integer.valueOf(split[1]);
                prefix = LanguagesManager.getInstance().getString("i_am") + " ";
                return prefix + RankWorker.apply(rankType, elp);
            case check_out_my_nickname:
                prefix = LanguagesManager.getInstance().getString("i_am") + " ";
                String nickname = CharLocalizerYio.getInstance().apply(netPhraseData.argument);
                return prefix + nickname.toLowerCase();
        }
    }


    public void addMessage(String value) {
        NcvItem freshObject = poolItems.getFreshObject();
        freshObject.setTitle(value);
        updateDeltas();
    }


    public void addMessage(String value, long delay) {
        NcvItem freshObject = poolItems.getFreshObject();
        freshObject.setTitle(value);
        freshObject.setDeathTime(System.currentTimeMillis() + delay);
        updateDeltas();
    }


    private void updateDeltas() {
        float y = position.height;
        for (NcvItem ncvItem : items) {
            ncvItem.delta.x = 0;
            ncvItem.delta.y = y;
            y -= ncvItem.title.height + 0.031f * GraphicsYio.width;
        }
    }


    @Override
    public boolean checkToPerformAction() {
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


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderNetChatViewElement;
    }
}
