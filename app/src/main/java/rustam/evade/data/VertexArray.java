package rustam.evade.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import rustam.evade.Constants;

/**
 * Created by RUSTAM on 2017-01-16.
 */

public class VertexArray {

    public FloatBuffer vertexDataBuffer;
    public FloatBuffer normalDataBuffer;
    public FloatBuffer textureDataBuffer;
    public IntBuffer indexDataBuffer;

    public int indexCount;
    public int buffersSize;

    public VertexArray(float[] vertices, float[] textures, float[] normals , int[] indexData) {
        if(vertices != null) {
            vertexDataBuffer = ByteBuffer.allocateDirect(vertices.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
            vertexDataBuffer.position(0);
        }
        if(normals != null) {
            normalDataBuffer = ByteBuffer.allocateDirect(normals.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(normals);
            normalDataBuffer.position(0);
        }
        if(textures != null) {
            textureDataBuffer = ByteBuffer.allocateDirect(textures.length * Constants.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().put(textures);
            textureDataBuffer.position(0);
        }
        if(indexData != null) {
            indexCount = indexData.length;
            buffersSize = (vertexDataBuffer.capacity() + normalDataBuffer.capacity() + textureDataBuffer.capacity()) * Constants.BYTES_PER_FLOAT;

            indexDataBuffer = ByteBuffer.allocateDirect(indexCount * Constants.BYTES_PER_INT).order(ByteOrder.nativeOrder()).asIntBuffer().put(indexData);
            indexDataBuffer.position(0);
        }
    }

    public void setVertexAttribPointer() {
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(0);

        GLES20.glVertexAttribPointer(1, 3, GLES20.GL_FLOAT, false, 0, vertexDataBuffer.capacity() * Constants.BYTES_PER_FLOAT);
        GLES20.glEnableVertexAttribArray(1);

        GLES20.glVertexAttribPointer(2, 2, GLES20.GL_FLOAT, false, 0, (vertexDataBuffer.capacity() + normalDataBuffer.capacity()) * Constants.BYTES_PER_FLOAT);
        GLES20.glEnableVertexAttribArray(2);
    }

    public int getIndexCount() {
        return indexCount;
    }
}
