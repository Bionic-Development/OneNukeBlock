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
in vec4 position;

out vec4 fragColor;

void main() {
    vec4 c = texture(Sampler0, texCoord0);
    vec4 color = c * vertexColor * ColorModulator;
    if (c.a < 0.1) {
        discard;
    }

    if (c.x == c.y && c.y == c.z) {
        ivec2 resCoord = ivec2(position * 256);
        vec2 coord = vec2(resCoord) / 256;

        float hue = mod(-(-coord.x + coord.y) * 0.20 + GameTime * 450, 1.0);
        vec3 rgb = clamp(abs(mod(hue * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);

        rgb = mix(overlayColor.rgb, c.rgb * rgb, overlayColor.a);

        fragColor = linear_fog(vec4(rgb, color.a), vertexDistance, FogStart, FogEnd, FogColor);
    } else {
        fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
    }
}