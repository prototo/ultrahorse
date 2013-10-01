#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main()
{
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
    vec4 col = gl_FragColor;
    float avg = col.r;
    avg = avg + col.g;
    avg = avg + col.b;
    avg = avg / 3.0;

    float low = 0.45;
    col.r = avg;
    col.g = avg;
    col.b = avg;

    gl_FragColor = col;
}