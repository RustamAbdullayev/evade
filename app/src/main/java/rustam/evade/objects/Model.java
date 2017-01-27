package rustam.evade.objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import rustam.evade.Constants;
import rustam.evade.data.VertexArray;
import rustam.evade.programs.ColorShaderProgram;
import rustam.evade.programs.TextureShaderProgram;
import rustam.evade.utils.OBJLoader;

/**
 * Created by RUSTAM on 2017-01-16.
 */

public class Model {
    private final String TAG = "Model";
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COMPONENT_COUNT = 2;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int STRIDE = 0;//(POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private final VertexArray vertexArray;

    public int[] VBO = new int[1];
    public int[] IBO = new int[1];

    public Model(Context context, int resourceID) {
        vertexArray = OBJLoader.loadOBJModel(context, resourceID);

        GLES20.glGenBuffers(1, VBO, 0);
        GLES20.glGenBuffers(1, IBO, 0);

        if (VBO[0] > 0 && IBO[0] > 0) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[0]);
            GLES20.glBufferData(
                    GLES20.GL_ARRAY_BUFFER,
                    vertexArray.buffersSize,
                    null,
                    GLES20.GL_STATIC_DRAW);
            GLES20.glBufferSubData(
                    GLES20.GL_ARRAY_BUFFER,
                    0,
                    vertexArray.vertexDataBuffer.capacity() * Constants.BYTES_PER_FLOAT,
                    vertexArray.vertexDataBuffer);
            GLES20.glBufferSubData(
                    GLES20.GL_ARRAY_BUFFER,
                    vertexArray.vertexDataBuffer.capacity() * Constants.BYTES_PER_FLOAT,
                    vertexArray.normalDataBuffer.capacity() * Constants.BYTES_PER_FLOAT,
                    vertexArray.normalDataBuffer);
            GLES20.glBufferSubData(
                    GLES20.GL_ARRAY_BUFFER,
                    (vertexArray.vertexDataBuffer.capacity() + vertexArray.normalDataBuffer.capacity()) * Constants.BYTES_PER_FLOAT,
                    vertexArray.textureDataBuffer.capacity() * Constants.BYTES_PER_FLOAT,
                    vertexArray.textureDataBuffer);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, IBO[0]);
            GLES20.glBufferData(
                    GLES20.GL_ELEMENT_ARRAY_BUFFER,
                    vertexArray.indexDataBuffer.capacity() * Constants.BYTES_PER_INT,
                    vertexArray.indexDataBuffer,
                    GLES20.GL_STATIC_DRAW);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        } else {
            Log.e(TAG, "Error buffering Models into OpenGL buffers.");
        }
    }

    public void bindData(TextureShaderProgram textureProgram) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[0]);
        vertexArray.setVertexAttribPointer();
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }
    public void render() {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, IBO[0]);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, vertexArray.getIndexCount(), GLES20.GL_UNSIGNED_INT, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

   // public enum Direction {
   //     UP, DOWN, LEFT, RIGHT
   // }
//
   //   public float[] moveModel(Direction direction, float upf) {
   //       switch (direction) {
   //           case UP:
   //               Matrix.translateM(modelMatrix, 0, 0, upf, 0);
   //               System.out.println("MOVED UP!");
   //               break;
   //           case DOWN:
   //               Matrix.translateM(modelMatrix, 0, 0, -upf, 0);
   //               System.out.println("MOVED DOWN!");
   //               break;
   //           case LEFT:
   //               Matrix.translateM(modelMatrix, 0, upf, 0, 0);
   //               System.out.println("MOVED LEFT!");
   //               break;
   //           case RIGHT:
   //               Matrix.translateM(modelMatrix, 0, -upf, 0, 0);
   //               System.out.println("MOVED RIGHT!");
   //               break;
   //       }
   //       return modelMatrix;
   //   }

}
