package rustam.evade;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by RUSTAM on 2016-12-25.
 */

public class Model {

    // VBOs
    private int[] vbo = new int[1];
    private int[] ibo = new int[1];

    // Vertex Attribute DataBuffers
    FloatBuffer vertexDataBuffer;
    FloatBuffer normalDataBuffer;
    FloatBuffer colorDataBuffer;
    IntBuffer indexDataBuffer;

    int indexCount;

    int buffersSize;

    public Model(float[] vertices, float[] colors, float[] normals , int[] indexData) {

        if(vertices!=null && normals!=null && colors!=null && indexData!=null) {
            vertexDataBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            vertexDataBuffer.put(vertices).position(0);

            normalDataBuffer = ByteBuffer.allocateDirect(normals.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            normalDataBuffer.put(normals).position(0);

            colorDataBuffer = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            colorDataBuffer.put(colors).position(0);

            indexCount = indexData.length;
            buffersSize = (vertexDataBuffer.capacity() + normalDataBuffer.capacity() + colorDataBuffer.capacity()) * 4;

            indexDataBuffer = ByteBuffer.allocateDirect(indexCount * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
            indexDataBuffer.put(indexData).position(0);

            GLES20.glGenBuffers(1, vbo, 0);
            GLES20.glGenBuffers(1, ibo, 0);

            System.out.println("Status: Beginning alocation of data into buffers");
            if (vbo[0] > 0 && ibo[0] > 0) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffersSize, null, GLES20.GL_STATIC_DRAW);
                // Positions
                GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, vertexDataBuffer.capacity()*4, vertexDataBuffer);
                // Colors
               // GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, vertexDataBuffer.capacity()*4, colorDataBuffer.capacity()*4, colorDataBuffer);
                // Normals
             //   GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, (vertexDataBuffer.capacity()+colorDataBuffer.capacity())*4, normalDataBuffer.capacity()*4, normalDataBuffer);

                GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, 0);
             //   GLES20.glVertexAttribPointer(1, , GLES20.GL_FLOAT, false, 0, 0);
             //   GLES20.glVertexAttribPointer(2, 3, GLES20.GL_FLOAT, false, 0, 0);

                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
                GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexDataBuffer.capacity()*4, indexDataBuffer, GLES20.GL_STATIC_DRAW);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            }System.out.println("Status: Finished alocation of data into buffers");
        }
    }

    // Pass in vertex attributes Position, Normal, Color
    public void render() {
        if (vbo[0]>0 && ibo[0]>0) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);

            GLES20.glEnableVertexAttribArray(0);
            GLES20.glEnableVertexAttribArray(1);
            GLES20.glEnableVertexAttribArray(2);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_INT, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }
}
