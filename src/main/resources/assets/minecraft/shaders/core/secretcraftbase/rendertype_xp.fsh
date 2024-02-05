#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;
in vec2 UV0;

out vec4 fragColor;

void main() {
    vec4 c = texture(Sampler0, texCoord0);
    vec4 color = c * vertexColor * ColorModulator;
    if (c.a < 0.1) {
        discard;
    }

    if (c.x == c.y && c.y == c.z) {
        float r = (GameTime * 12000);
        float s = ((sin(r + 0.0f) + 1.0f) * 0.5f);
        float t = 1f;
        float u = ((sin(r + 4.1887903f) + 1.0f) * 0.1f);

        vec3 rgb = mix(overlayColor.rgb, c.rgb *  vec3(s, t, u), overlayColor.a);

        fragColor = linear_fog(vec4(rgb, color.a), vertexDistance, FogStart, FogEnd, FogColor);
    } else {
        fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
    }
}