#version 300 es 
precision highp float;

out vec4 fragmentColor;
//LABTODO: world space inputs

//uniform struct{
//  //LABTODO: uniform for environment
//} material;

uniform struct{
  mat4 viewProjMatrix;
  //LABTODO: uniform for computing view direction
} camera;

//LABTODO: uniforms for light source data

void main(void) {
  fragmentColor = vec4(1, 0, 1, 1);
}
