#version 150

#moj_import <matrix.glsl>
#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform sampler2D Sampler3;
uniform sampler2D Sampler4;

uniform float GameTime;
uniform int EndPortalLayers;

in vec4 texProj0;
in vec2 texCoord0;
in vec2 texCoord2;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec4 normal;

out vec4 fragColor;

void main() {
    vec4 c = texture(Sampler0, texCoord0);
    if (texture(Sampler3, texCoord0).a > 0.1) {
        vec4 color = texture(Sampler3, texCoord0);

        color *= vertexColor * ColorModulator;
        color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
        color *= lightMapColor;
        fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
    } else {
        if (c.a < 0.1) {
            discard;
        }

        c *= vertexColor * ColorModulator;
        c.rgb = mix(overlayColor.rgb, c.rgb, overlayColor.a);
        c *= lightMapColor;
        fragColor = linear_fog(c, vertexDistance, FogStart, FogEnd, FogColor);
    }
}
