#version 150

#moj_import <fog.glsl>
#moj_import <raidbase.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;
uniform float GlitchyStrength;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.1) {
        discard;
    }

    float strength = (GlitchyStrength > 1 ? 2 : GlitchyStrength);

    color.r = texture(Sampler0, texCoord0 + vec2(offset(GameTime, 16.0, texCoord0) * 0.03, 0.0) * strength).r;
    color.g = texture(Sampler0, texCoord0 + vec2(offset(GameTime, 8.0, texCoord0) * 0.03 * 0.16666666, 0.0) * strength).g;
    color.b = texture(Sampler0, texCoord0 + vec2(offset(GameTime, 8.0, texCoord0) * 0.03, 0.0) * strength).b;

    color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}