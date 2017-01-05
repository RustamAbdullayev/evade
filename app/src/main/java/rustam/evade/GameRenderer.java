package rustam.evade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by RUSTAM on 2016-12-21.
 */

public class GameRenderer implements GLSurfaceView.Renderer {
    final String TAG = "Main Activity";

    final String vertexShaderCode =
            "attribute vec4 aPosition;" +
            "attribute vec3 aNormal;" +
            "attribute vec4 aColor;" +
            "uniform mat4 mModelView;" +
            "uniform mat4 mProjection;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "vColor = aColor;" +
            "gl_Position = mProjection * mModelView * aPosition;" +
            "}";

    final String fragmentShaderCode =
            "precision mediump float;" +
            "varying vec4 vColor;" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "void main() {" +
            "gl_FragColor = vColor;" +
            "}";

    Model jk;
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        jk = OBJLoader.loadOBJModel(GameView.getCurrentContext(), R.raw.suzanne);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int height, int width) {
       // GLES20.glViewport(0, 0, height, width);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        jk.render();
    }

    public int loadTexture(Context context, int resourceID) {
        int[] textureID = new int[1];
        GLES20.glGenTextures(1, textureID, 0);

        if (textureID[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID, options);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
            // Filters
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            // Load texture to bound tuexture buffer
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            bitmap.recycle();
        }
        if (textureID[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        return textureID[0];
    }

    //Returns int shaderID of selected shader types and binds attributes
    private int compileShader(final int shaderType, final String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    private int createAndLinkProgram(int vertexShaderID, int fragmentShaderID/*, final String[] attributes*/) {
        int programID = GLES20.glCreateProgram();

        if(programID != 0){
            GLES20.glAttachShader(programID, vertexShaderID);
            GLES20.glAttachShader(programID, fragmentShaderID);

            // Bind attributes
         /*   if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    GLES20.glBindAttribLocation(programID, i, attributes[i]);
                }
            } */

            GLES20.glLinkProgram(programID);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programID, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programID));
                GLES20.glDeleteProgram(programID);
                programID = 0;
            }
        }
        return programID;
    }
}
