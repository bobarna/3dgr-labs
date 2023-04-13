#version 300 es

in vec4 vertexPosition;
in vec3 vertexNormal;
in vec4 vertexTexCoord;

uniform struct{
	mat4 modelMatrix;
	//LABTODO: uniform for transforming normals
} gameObject;

uniform struct{
	mat4 viewProjMatrix;
	//LABTODO: uniform for computing view direction
} camera;

out vec4 tex;
//LABTODO: world space outputs

void main(void) {
  gl_Position = vertexPosition * gameObject.modelMatrix * camera.viewProjMatrix;
  tex = vertexTexCoord;
}