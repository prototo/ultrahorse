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
    vec3 col = vec3(1.0, 0.0, 1.0);
    float a = 0.0;

    float u0 = v_texCoords.x * 2.0 - 1.0;
    float v0 = v_texCoords.y * 2.0 - 1.0;

    float ver = 0.0;
    if (abs(u0) < abs(v0)) {
        ver = 1.0;
    }

    if (ver == 0.0) {
        v0 = v0 / abs(u0);
        v0 = (v0 + 1.0) / 2.0;

        vec2 newCoords = vec2(v_texCoords.x, v0);

        vec4 sample = texture2D(u_texture, newCoords);
        a = sample.r;
    } else {
        u0 = u0 / abs(v0);
        u0 = (u0 + 1.0) / 2.0;

        // don't know why this works, don't question it
        vec2 newCoords = vec2(v_texCoords.y, u0);

        vec4 sample = texture2D(u_texture, newCoords);
        a = sample.g;
    }

    gl_FragColor = vec4(col, a);
}