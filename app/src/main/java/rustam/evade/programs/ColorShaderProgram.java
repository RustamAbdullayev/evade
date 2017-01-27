package rustam.evade.programs;

import android.content.Context;
import android.opengl.GLES20;

import rustam.evade.R;

/**
 * Created by RUSTAM on 2017-01-18.
 */

public class ColorShaderProgram extends ShaderProgram {

    // Uniform locations
    private final int uMatrixLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aNormalLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.color_vertex_shader, R.raw.color_fragment_shader, new String[] {A_POSITION, A_NORMAL, A_COLOR});

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aNormalLocation = GLES20.glGetAttribLocation(program, A_NORMAL);
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
    }

    public void setUniforms(float[] matrix) {
        // Pass the matrix into the shader program.
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }
    public int getColorAttributeLocation() {
        return aColorLocation;
    }

}
