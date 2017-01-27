package rustam.evade.utils;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rustam.evade.GameRenderer;
import rustam.evade.GameView;

/**
 * Created by RUSTAM on 2017-01-17.
 */

public class ShaderHelper {
    final static String TAG = "Shader Helper";

    private static int compileShader(final int shaderType, final String shaderSource) {
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

    public static int createAndLinkProgram(int vertexShaderID, int fragmentShaderID, String vertexShaderCode, String fragmentShaderCode, final String[] attributes) {
        int programID = GLES20.glCreateProgram();

        if(programID != 0){
            GLES20.glAttachShader(programID, compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode));
            GLES20.glAttachShader(programID, compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode));

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
