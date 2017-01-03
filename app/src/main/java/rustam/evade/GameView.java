package rustam.evade;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by RUSTAM on 2016-12-21.
 */

public class GameView extends GLSurfaceView {
    private static GameView instance;

    public static Context getCurrentContext() {
        return instance.getContext();
    }

    public GameView(Context context){
        super(context);
        instance = this;
        this.setEGLContextClientVersion(2);
        this.setRenderer(new GameRenderer());
    }
}
