#version 300 es 
precision highp float;

out vec4 fragmentColor;
in vec4 tex;
//LABTODO: world space inputs

//uniform struct{
//  //LABTODO: uniform for surface color (diffuse refleciton coeff kd)
//} material;

uniform struct{
  mat4 viewProjMatrix;
  //LABTODO: uniform for computing view direction
} camera;

//LABTODO: uniforms for light source data

void main(void) {
  fragmentColor = vec4(0, 1, 1, 1);
}
