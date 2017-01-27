package rustam.evade;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rustam.evade.objects.Model;
import rustam.evade.programs.ColorShaderProgram;
import rustam.evade.programs.TextureShaderProgram;
import rustam.evade.utils.TextureHelper;

/**
 * Created by RUSTAM on 2016-12-21.
 */

public class GameRenderer implements GLSurfaceView.Renderer {
    final static String TAG = "Main Activity";

    public final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] mvMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];

    // Actors
    private Model model;

    // Shader programs
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int texture;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        model = new Model(context, R.raw.stall);

        textureProgram = new TextureShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.mipmap.earth_bitmap);

        colorProgram = new ColorShaderProgram(context);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setLookAtM(viewMatrix, 0,
                0, 0, 1,        //position of the eye
                0, 0, 0,        //center of the view
                0, 1, 0);       //up vector
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int height, int width) {
        GLES20.glViewport(0, 0, height, width);

        float ratio = (float) height / width;
      //  Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 0.1f, 10000.0f);
        Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1, 1, -1, 1000);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //Multiply matrices
        Matrix.rotateM(modelMatrix, 0, 5, 1, 0, 0);
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);

        // Render the model player
        textureProgram.useProgram();
        textureProgram.setUniforms(mvpMatrix, texture);
        model.bindData(textureProgram);
        model.render();
    }

    public GameRenderer(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
