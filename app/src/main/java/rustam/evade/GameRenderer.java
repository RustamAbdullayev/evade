package rustam.evade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by RUSTAM on 2016-12-21.
 */

public class GameRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "MainActivity";

    // Models
    Model bob;

    // Matrices for client side computations
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] MVMatrix = new float[16];

    // additional matrices TODO review
    private final float[] accumulatedRotation = new float[16];
    private final float[] currentRotation = new float[16];
    private final float[] lightModelMatrix = new float[16];
    private final float[] temporaryMatrix = new float[16];

    // Handles for matrices in OpenGL program
    private int mModelViewMatrixID;
    private int mProjectionMatrixID;

    // Handles for vertex attributes
    private int aPosition;
    private int aNormal;
    private int aColor;

    // Handles for OpenGL programs
    private int programID;

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

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we
        // holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        //Create OpenGL program
        int vertexShaderID = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShaderID = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        programID = createAndLinkProgram(vertexShaderID, fragmentShaderID, new String[] {"aPosition", "aNormal", "aColor"});

        bob = new OBJLoader().loadOBJModel(GameView.getCurrentContext() , R.raw.suzanne);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int height, int width) {
        GLES20.glViewport(0, 0, height, width);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 1000.0f;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(programID);

        // Set-up uniform handles
        mModelViewMatrixID = GLES20.glGetUniformLocation(programID, "mModelView");
        mProjectionMatrixID = GLES20.glGetUniformLocation(programID, "mProjection");
        // Set-up attribute handles
        aPosition = GLES20.glGetAttribLocation(programID, "aPosition");
        aNormal = GLES20.glGetAttribLocation(programID, "aNormal");
        aColor = GLES20.glGetAttribLocation(programID, "aColor");

        //TODO add input-dependent Model matrix calculations
        Matrix.setIdentityM(modelMatrix, 0);

        // Calculate Model and View matrices
        Matrix.multiplyMM(MVMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        // Pass MVMatrix data to mModelView uniform
        GLES20.glUniformMatrix4fv(mModelViewMatrixID, 1, false, MVMatrix, 0);

        // Pass Projection data mProjection uniform
        GLES20.glUniformMatrix4fv(mProjectionMatrixID, 1, false, projectionMatrix, 0);

        bob.render(aPosition, aNormal, aColor);
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

    private int createAndLinkProgram(int vertexShaderID, int fragmentShaderID, final String[] attributes) {
        int programID = GLES20.glCreateProgram();

        if(programID != 0){
            GLES20.glAttachShader(programID, vertexShaderID);
            GLES20.glAttachShader(programID, fragmentShaderID);

            // Bind attributes
            if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    GLES20.glBindAttribLocation(programID, i, attributes[i]);
                }
            }

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
