#version 150

#moj_import <fog.glsl>
#moj_import <raidbase.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord1;
in vec4 normal;
in vec4 position;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);

    fragColor.r = texture(Sampler0, texCoord0 + vec2(offset(GameTime, 16.0, texCoord0) * 0.03, 0.0)).r;
    fragColor.g = texture(Sampler0, texCoord0 + vec2(offset(GameTime, 8.0, texCoord0) * 0.03 * 0.16666666, 0.0)).g;
    fragColor.b = texture(Sampler0, texCoord0 + vec2(offset(GameTime, 8.0, texCoord0) * 0.03, 0.0)).b;
}
