#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

float avg(vec4 sample) {
    float s = 0.0;

    s += sample.r;
    s += sample.g;
    s += sample.b;
    s /= 3.0;

    return s;
}

void main()
{
    float u0 = v_texCoords.x * 2.0 - 1.0;
    float v0 = v_texCoords.y * 2.0 - 1.0;

    v0 = v0 * abs(u0);
    v0 = (v0 + 1.0) / 2.0;

    vec2 newCoords = vec2(v_texCoords.s, v0);

    float hor = 1.0 - avg(texture2D(u_texture, newCoords));
    float ver = 1.0 - avg(texture2D(u_texture, newCoords.yx));

    gl_FragColor =  vec4(hor, ver, 0.0, 1.0);
}