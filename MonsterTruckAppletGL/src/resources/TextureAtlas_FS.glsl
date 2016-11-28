#version 120

varying vec2 fragmentUV;

uniform sampler2D myTexture;

uniform float offset;

void main()
{
    //gl_FragColor = vec4(1.0f, 0.0f, 0.0f, 0.1f);

    gl_FragColor = texture2D(myTexture, vec2(fragmentUV.x + offset, fragmentUV.y ));
}