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
    float alphar = 1.0;
    float alphag = 1.0;
    float step = 0.005;

    float THRESHOLD = 1.0;

    if (v_texCoords.x < 0.5) {
        for (float x = 0.5; x >= v_texCoords.x; x -= step) {
            vec4 sample = texture2D(u_texture, vec2(x, v_texCoords.y));

            if (sample.r < THRESHOLD) {
                alphar = 0.0;
            }
            if (sample.g < THRESHOLD) {
                alphag = 0.0;
            }
        }
    } else {
        for (float x = 0.5; x <= v_texCoords.x; x += step) {
            vec4 sample = texture2D(u_texture, vec2(x, v_texCoords.y));

            if (sample.r < THRESHOLD) {
                alphar = 0.0;
            }
            if (sample.g < THRESHOLD) {
                alphag = 0.0;
            }
        }
    }

    gl_FragColor = vec4(alphar, alphag, 0.0, 1.0);
}