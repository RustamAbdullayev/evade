package rustam.evade;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by RUSTAM on 2016-12-21.
 */

public class GameView extends GLSurfaceView {
    private GameRenderer gameRenderer;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    public GameView(Context context){
        super(context);
        this.setEGLContextClientVersion(2);
        gameRenderer = new GameRenderer(context);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.setRenderer(gameRenderer);
    }

  //  @Override
  //  public boolean onTouchEvent(MotionEvent e){
  //      float x = e.getX();
  //      float y = e.getY();
//
  //      switch (e.getAction()) {
  //          case MotionEvent.ACTION_DOWN:
  //              if (y > getHeight() / 2) {
  //                  gameRenderer.playerMove(GameRenderer.Direction.DOWN, 0.1f);
  //              }
  //              if (y < getHeight() / 2) {
  //                  gameRenderer.playerMove(GameRenderer.Direction.UP, 0.1f);
  //              }
  //              if (x < getWidth() / 2) {
  //                  gameRenderer.playerMove(GameRenderer.Direction.LEFT, 0.1f);
  //              }
  //              if (x > getWidth() / 2) {
  //                  gameRenderer.playerMove(GameRenderer.Direction.RIGHT, 0.1f);
  //              }
  //          case MotionEvent.ACTION_UP:
//
  //      }
  //      return true;
  //  }

}
