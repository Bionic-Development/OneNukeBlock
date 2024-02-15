#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

uniform float BubbleGumStrength;

vec4 getColor(vec4 color, vec4 overlay, float strength) {
    return vec4(mix(color, overlay, strength * 0.71f).rgb, color.a);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.01) {
        discard;
    }

    color *= vertexColor * ColorModulator;
    color = getColor(color, vec4(0.91764705882, 0.10980392156, 0.81568627451, 1), BubbleGumStrength);

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
