#version 120

uniform mat4 model;

attribute vec4 position;
attribute vec2 vertexUV;

varying vec2 fragmentUV;

void main()
{
    gl_Position =  model * position;
    
    fragmentUV = vertexUV;
}