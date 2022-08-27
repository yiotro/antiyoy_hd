package yio.tro.onliyoy.stuff;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class FrameBufferYio extends FrameBuffer implements ReusableYio {

    public float f;

    public static FrameBufferYio getInstance(Pixmap.Format format, int width, int height, boolean hasDepth) {
        try {
            return new FrameBufferYio(format, width, height, hasDepth, 1);
        } catch (Exception e) {
            try {
                return new FrameBufferYio(Pixmap.Format.RGBA8888, width, height, hasDepth, 1);
            } catch (Exception e2) {
                try {
                    return new FrameBufferYio(Pixmap.Format.RGB565, width, height, true, 1);
                } catch (Exception e3) {
                    try {
                        return new FrameBufferYio(format, width / 2, height / 2, hasDepth, 0.5f);
                    } catch (Exception e4) {
                        System.out.println("Fake FrameBuffer created");
                        boolean bckp = DebugFlags.invalidFrameBuffer;
                        DebugFlags.invalidFrameBuffer = false;
                        FakeFbYio fakeFbYio = new FakeFbYio();
                        DebugFlags.invalidFrameBuffer = bckp;
                        return fakeFbYio;
                    }
                }
            }
        }
    }


    public FrameBufferYio(Pixmap.Format format, int width, int height, boolean hasDepth, float f) {
        super(format, width, height, hasDepth);
        if (DebugFlags.invalidFrameBuffer) {
            Yio.forceException();
        }
        this.f = f;
    }


    @Override
    public void reset() {
        // these objects shouldn't be reused actually
        // they implement reusable to be added to object pool once
    }
}
