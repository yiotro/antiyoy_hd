package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.shop.*;
import yio.tro.onliyoy.net.shared.SkinType;
import yio.tro.onliyoy.stuff.AtlasLoader;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;
import yio.tro.onliyoy.stuff.Storage3xTexture;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

import java.util.HashMap;

public class RenderShopViewElement extends RenderInterfaceElement{

    private ShopViewElement svElement;
    private TextureRegion xTexture;
    private TextureRegion coinTexture;
    private TextureRegion fishTexture;
    private HashMap<CveColorYio, TextureRegion> mapColors;
    private TextureRegion darkenTexture;
    public HashMap<String, TextureRegion> mapSkinTextures;


    @Override
    public void loadTextures() {
        mapColors = new HashMap<>();
        for (CveColorYio cveColorYio : CveColorYio.values()) {
            mapColors.put(cveColorYio, GraphicsYio.loadTextureRegion("menu/calendar/" + cveColorYio + ".png", false));
        }
        xTexture = GraphicsYio.loadTextureRegion("game/stuff/x.png", false);
        coinTexture = GraphicsYio.loadTextureRegion("menu/icon_label/coin.png", true);
        fishTexture = GraphicsYio.loadTextureRegion("menu/shop/fish.png", true);
        darkenTexture = GraphicsYio.loadTextureRegion("pixels/artificial_darken.png", false);
        initMapIconTextures();
    }


    private void initMapIconTextures() {
        mapSkinTextures = new HashMap<>();
        AtlasLoader atlasLoader = new AtlasLoader("game/skins/preview/", true);
        PieceType[] array = IslSkinItem.getPieceTypes();
        for (SkinType skinType : SkinType.values()) {
            for (PieceType pieceType : array) {
                String key = skinType + "_" + pieceType;
                TextureRegion textureRegion = (new Storage3xTexture(atlasLoader, key + ".png")).getNormal();
                mapSkinTextures.put(key, textureRegion);
            }
        }
    }


    @Override
    public void render(InterfaceElement element) {
        svElement = (ShopViewElement) element;

        GraphicsYio.setBatchAlpha(batch, alpha);
        renderPages();
        renderAccent();
        renderTabsArea();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderAccent() {
        if (svElement.swipeFactor.getValue() == 1) {
            GraphicsYio.setBatchAlpha(batch, alpha);
            GraphicsYio.drawByRectangle(batch, mapColors.get(svElement.currentColor), svElement.accentPosition);
            return;
        }
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, mapColors.get(svElement.previousColor), svElement.accentPosition);
        GraphicsYio.setBatchAlpha(batch, alpha * svElement.swipeFactor.getValue());
        GraphicsYio.drawByRectangle(batch, mapColors.get(svElement.currentColor), svElement.accentPosition);
    }


    private void renderTabsArea() {
        for (AbstractShopPage page : svElement.pageList) {
            if (!page.isNameVisible()) continue;
            renderSingleName(page);
        }
        GraphicsYio.setBatchAlpha(batch, alpha);
    }


    private void renderPages() {
        for (AbstractShopPage page : svElement.pageList) {
            if (!page.isCurrentlyVisible()) continue;
            if (svElement.getFactor().getValue() == 1 && !page.isMaskingAlwaysRequired()) {
                renderSinglePage(page);
                continue;
            }
            batch.end();
            Masking.begin();

            prepareShapeRenderer();
            drawRoundRectShape(page.maskPosition, svElement.cornerEngineYio.getCurrentRadius());
            shapeRenderer.end();

            batch.begin();
            Masking.continueAfterBatchBegin();
            renderSinglePage(page);
            Masking.end(batch);
        }
    }


    private void renderSinglePage(AbstractShopPage page) {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderRoundShape.getBackgroundTexture(BackgroundYio.white),
                page.viewPosition
        );
        switch (page.getType()) {
            default:
                System.out.println("RenderShopViewElement.renderSinglePage");
                break;
            case empty:
                renderSpEmpty(page);
                break;
            case exchange:
                renderSpExchange(page);
                break;
            case sample_list:
            case phrases:
            case skins:
            case ranks:
            case avatars:
                renderShopListPage((AbstractShopListPage) page);
                break;
        }
    }


    private void renderShopListPage(AbstractShopListPage abstractShopListPage) {
        renderInternalShopList(abstractShopListPage.internalShopList);
    }


    private void renderInternalShopList(InternalShopList internalShopList) {
        for (AbstractIslItem islItem : internalShopList.items) {
            if (!islItem.isCurrentlyVisible()) continue;
            renderSingleIslItem(islItem);
        }
    }


    private void renderSingleIslItem(AbstractIslItem item) {
        if (item.darken) {
            GraphicsYio.drawByRectangle(batch, darkenTexture, item.viewPosition);
        }

        GraphicsYio.renderTextOptimized(batch, blackPixel, item.nameViewText, alpha * item.selfScrollWorkerYio.getAlpha());
        renderSpecificItemStuff(item);
        GraphicsYio.drawByRectangle(batch, getCoverTexture(item), item.coverPosition);

        if (item.statusValue != IsliStatus.unknown) {
            GraphicsYio.renderTextOptimized(batch, blackPixel, item.priceViewText, alpha);
            if (item.statusValue == IsliStatus.available) {
                GraphicsYio.drawByCircle(batch, fishTexture, item.fishPosition);
            }
        }

        if (item.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, alpha * item.selectionEngineYio.getAlpha());
            GraphicsYio.drawByRectangle(batch, blackPixel, item.viewPosition);
            GraphicsYio.setBatchAlpha(batch, alpha);
        }
    }


    private void renderSpecificItemStuff(AbstractIslItem item) {
        if (item instanceof IslSkinItem) {
            GraphicsYio.setBatchAlpha(batch, alpha);
            for (IslPieceIcon icon : ((IslSkinItem) item).icons) {
                GraphicsYio.drawByCircle(batch, mapSkinTextures.get(icon.key), icon.position);
            }
            GraphicsYio.setBatchAlpha(batch, 1);
        }
        if (item instanceof IslAvatarItem) {
            GraphicsYio.setBatchAlpha(batch, alpha);
            IslAvatarItem islAvatarItem = (IslAvatarItem) item;
            GraphicsYio.renderItyOptimized(
                    batch,
                    blackPixel,
                    MenuRenders.renderAvatars.getTexture(islAvatarItem.avatarType, islAvatarItem.iconTextYio),
                    islAvatarItem.iconTextYio,
                    alpha
            );
            GraphicsYio.setBatchAlpha(batch, 1);
        }
    }


    private TextureRegion getCoverTexture(AbstractIslItem islItem) {
        if (islItem.darken) return darkenTexture;
        return MenuRenders.renderRoundShape.getBackgroundTexture(BackgroundYio.white);
    }


    private void renderSpExchange(AbstractShopPage page) {
        SpExchange spExchange = (SpExchange) page;
        GraphicsYio.renderTextOptimized(batch, blackPixel, spExchange.title, alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, spExchange.fishViewText, alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, spExchange.coinViewText, alpha);
        GraphicsYio.drawByCircle(batch, coinTexture, spExchange.coinIconPosition);
        GraphicsYio.drawByCircle(batch, fishTexture, spExchange.fishIconPosition);
        renderSpeLocalButton(spExchange.exchangeButton);
        renderSpeLocalButton(spExchange.buyFishButton);
    }


    private void renderSpeLocalButton(SpeLocalButton speLocalButton) {
        GraphicsYio.setBatchAlpha(batch, alpha);
        MenuRenders.renderRoundShape.renderRoundShape(speLocalButton.viewPosition, BackgroundYio.gray);
        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.renderTextOptimized(batch, blackPixel, speLocalButton.title, alpha);
        if (speLocalButton.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, alpha * speLocalButton.selectionEngineYio.getAlpha());
            MenuRenders.renderRoundShape.renderRoundShape(speLocalButton.viewPosition, BackgroundYio.black);
            GraphicsYio.setBatchAlpha(batch, alpha);
        }
    }


    private void renderSpEmpty(AbstractShopPage page) {
        SpEmpty spEmpty = (SpEmpty) page;
        GraphicsYio.setBatchAlpha(batch, 0.1 * alpha);
        GraphicsYio.drawByRectangle(batch, xTexture, spEmpty.viewPosition);
        GraphicsYio.setBatchAlpha(batch, alpha);
    }


    private void renderSingleName(AbstractShopPage page) {
        GraphicsYio.renderTextOptimized(batch, blackPixel, page.nameViewText, alpha);
        if (page.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, alpha * page.selectionEngineYio.getAlpha());
            GraphicsYio.drawByRectangle(batch, blackPixel, page.selectionViewBounds);
            GraphicsYio.setBatchAlpha(batch, alpha);
        }
    }
}
