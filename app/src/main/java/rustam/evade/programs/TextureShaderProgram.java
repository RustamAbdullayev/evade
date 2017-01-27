package rustam.evade.programs;

import android.content.Context;
import android.opengl.GLES20;

import rustam.evade.R;

/**
 * Created by RUSTAM on 2017-01-18.
 */

public class TextureShaderProgram extends  ShaderProgram{

    // Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader, new String[] {A_POSITION, A_NORMAL, A_TEXTURE_COORDINATES});

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);
    }

    public void setUniforms(float[] matrix, int textureId) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by telling it to read from texture unit 0.
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }
}
