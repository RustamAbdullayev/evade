package rustam.evade;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RUSTAM on 2016-12-26.
 */

public class OBJLoader {

    public static Model loadOBJModel(Context ctx, int fileID) {
        InputStream inputStream = ctx.getResources().openRawResource(fileID);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = null;

        // Temporary vertex attribute data storage for easier management
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();

        // Final destination of vertex attribute data, that will be used as output and into a Model instance
        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;

        //
        try {
            while (true) {
                line = bufferedReader.readLine();
                String[] currentLine = line.split(" ");

                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                    System.out.println("v: " + vertex.x + " " + vertex.y + " " + vertex.z);

                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                    System.out.println("vt: " + currentLine[1]+ " " + currentLine[2]);

                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                    System.out.println("vn: " + currentLine[1]+ " " + currentLine[2] + " " + currentLine[3]);

                } else if (line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 4];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            } System.out.println("Status: Finished transfer of data to ArrayLists");

            System.out.println("Status: Beginning to process vertices");
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray); System.out.println("first vertex processed");
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray); System.out.println("second vertex processed");
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray); System.out.println("third vertex processed");

                line = bufferedReader.readLine();
            } System.out.println("Status: Finished processing vertices");

            bufferedReader.close();
            inputStream.close();
            inputStreamReader.close();

        } catch (IOException e) {
            System.err.println("File Not Found.");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("NullPointer");
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return new Model(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals,
                                      float[] texturesArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

       // Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);// TODO Model instance is not receiving color/texture data - fix
       // texturesArray[currentVertexPointer * 2] = currentTexture.x;
       // texturesArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }

}

class Vector3f {
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x;
    public float y;
    public float z;
}

class Vector2f {
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x;
    public float y;
}


