package rustam.evade;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by RUSTAM on 2016-12-21.
 */

public class GameView extends GLSurfaceView {

    public GameView(Context context){
        super(context);
        this.setEGLContextClientVersion(2);
        this.setRenderer(new GameRenderer());
    }

}
