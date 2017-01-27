uniform mat4 u_Matrix;

attribute vec4 a_Position;
attribute vec3 a_Normal;
attribute vec4 a_Color;

varying vec4 v_Color;

void main() {
    v_Color = a_Position * u_Matrix;
    gl_Position = u_Matrix * a_Position;
}
