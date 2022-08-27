package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.LetterListItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderLetterListItem extends AbstractRenderCustomListItem {


    private TextureRegion letterTexture;
    private LetterListItem letterListItem;


    @Override
    public void loadTextures() {
        letterTexture = GraphicsYio.loadTextureRegion("game/atlas/letter.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        letterListItem = (LetterListItem) item;
        renderColoredBackground();
        renderIcon();
        renderText();
        renderDefaultSelection(item);
    }


    private void renderColoredBackground() {
        HColor senderColor = letterListItem.letter.senderColor;
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderUiColors.map.get(senderColor),
                letterListItem.viewPosition
        );
    }


    private void renderText() {
        renderTextOptimized(letterListItem.renderableTextYio, 1);
    }


    private void renderIcon() {
        GraphicsYio.drawByCircle(batch, letterTexture, letterListItem.iconPosition);
    }
}
