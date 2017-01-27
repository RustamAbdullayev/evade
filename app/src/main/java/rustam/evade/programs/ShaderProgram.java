package rustam.evade.programs;

import android.content.Context;
import android.opengl.GLES20;

import rustam.evade.utils.ShaderHelper;
import rustam.evade.utils.TextFileToString;

/**
 * Created by RUSTAM on 2017-01-18.
 */

public class ShaderProgram {

    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_NORMAL = "a_Normal";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // Shader program
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId, String[] attribs) {
        // Compile the shaders and link the program.
        program = ShaderHelper.createAndLinkProgram(
                vertexShaderResourceId,
                fragmentShaderResourceId,
                TextFileToString.readTextFileFromResource(context, vertexShaderResourceId),
                TextFileToString.readTextFileFromResource(context, fragmentShaderResourceId),
                attribs);
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program);
    }

}
