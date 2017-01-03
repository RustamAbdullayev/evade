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
    /*
    Persistent Data: Buffer Data; generate and fill buffers once per instance

    Dynamic Data: Vertex Attributes

    Uniforms are dynamic, but do not belong to a Model, they are shared between all Models in the GameRenderer class
     */

    // VBOs
    private int[] vbo = new int[1];
    private int[] ibo = new int[1];

    // Vertex Attribute DataBuffers
    FloatBuffer vertexDataBuffer;
    FloatBuffer normalDataBuffer;
    FloatBuffer colorDataBuffer;

    int indexCount;

    public Model(float[] vertices, float[] colors, float[] normals , int[] indexData) {

        indexCount = indexData.length;

        if(vertices!=null && normals!=null && colors!=null) {
            vertexDataBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            vertexDataBuffer.put(vertices).position(0);

            normalDataBuffer = ByteBuffer.allocateDirect(normals.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            normalDataBuffer.put(normals).position(0);

            colorDataBuffer = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            colorDataBuffer.put(colors).position(0);

            int buffersCap = vertexDataBuffer.capacity() * 4 + normalDataBuffer.capacity() * 4 + colorDataBuffer.capacity() * 4;

            final IntBuffer indexDataBuffer = ByteBuffer.allocateDirect(indexData.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
            indexDataBuffer.put(indexData).position(0);

            GLES20.glGenBuffers(1, vbo, 0);
            GLES20.glGenBuffers(1, ibo, 0);

            System.out.println("Status: Beginning alocation of data into buffers");
            if (vbo[0] > 0 && ibo[0] > 0) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, buffersCap, null, GLES20.GL_STATIC_DRAW);

                // Fill buffers with data(vertex, normal, color)
                GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, vertexDataBuffer.capacity() * 4, vertexDataBuffer);
                GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * 4, normalDataBuffer.capacity() * 4, normalDataBuffer);
                GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * 4 + normalDataBuffer.capacity() * 4, colorDataBuffer.capacity() * 4, colorDataBuffer);

                // Fill index buffer
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
                GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexDataBuffer.capacity() * 4, indexDataBuffer, GLES20.GL_STATIC_DRAW);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            }System.out.println("Status: Finished alocation of data into buffers");
        }
    }

    // Pass in vertex attributes Position, Normal, Color
    public void render(int posAttribID, int normAttribID, int colorAttribID) {
        if (vbo[0]>0 && ibo[0]>0) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);

            System.out.println("Status: Creating pointers to buffered data");
            // Vertex positions
            GLES20.glVertexAttribPointer(posAttribID, 3, GLES20.GL_FLOAT, false, 0, 0); // TODO fix this
            GLES20.glEnableVertexAttribArray(posAttribID);

            // Vertex normals
            GLES20.glVertexAttribPointer(normAttribID, 3, GLES20.GL_FLOAT, false, 0, vertexDataBuffer.capacity()*4);
            GLES20.glEnableVertexAttribArray(normAttribID);

            // Vertex color
            GLES20.glVertexAttribPointer(colorAttribID, 4, GLES20.GL_FLOAT, false, 0, vertexDataBuffer.capacity()*4 + normalDataBuffer.capacity()*4);
            GLES20.glEnableVertexAttribArray(colorAttribID);
            System.out.println("Status: Finished creating pointers to buffered data");

            //TODO possible spot for optimization - idea: call draw only if uniforms have changed, without touching attribpointers

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, indexCount, GLES20.GL_UNSIGNED_INT, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }
}
