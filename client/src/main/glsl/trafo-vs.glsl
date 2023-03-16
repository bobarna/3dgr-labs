#version 300 es
in vec4 vertexPosition;
in vec4 vertexTexCoord;
out vec4 tex;
out vec4 modelPosition;

uniform struct {
	mat4 modelMatrix;
} gameObject;

//LABTODO: camera

void main(void) {
	tex = vertexTexCoord;
  modelPosition = vertexPosition;
  gl_Position = vertexPosition * gameObject.modelMatrix;
  //LABTODO: camera trafo
}
