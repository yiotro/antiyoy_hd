package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.game.viewable_model.VrmIndicator;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderViewableRelations extends GameRender {

    HashMap<RelationType, TextureRegion> mapTextures;


    @Override
    protected void loadTextures() {
        mapTextures = new HashMap<>();
        for (RelationType relationType : RelationType.values()) {
            if (relationType == RelationType.neutral) continue;
            mapTextures.put(relationType, load3xTexture("relation_" + relationType, true).getNormal());
        }
    }


    @Override
    public void render() {
        ArrayList<VrmIndicator> indicators = getViewableModel().viewableRelationsManager.indicators;
        if (indicators.size() == 0) return;
        for (VrmIndicator indicator : indicators) {
            if (!indicator.isCurrentlyVisible()) continue;
            TextureRegion textureRegion = mapTextures.get(indicator.relationType);
            if (textureRegion == null) continue;
            GraphicsYio.drawRectangleRotatedByCenter(
                    batchMovable,
                    textureRegion,
                    indicator.viewPosition.center.x,
                    indicator.viewPosition.center.y,
                    indicator.viewSize.x,
                    indicator.viewSize.y,
                    indicator.viewPosition.angle
            );
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
