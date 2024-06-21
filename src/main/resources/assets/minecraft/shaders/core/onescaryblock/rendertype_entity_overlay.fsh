#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in vec4 texProj0;
in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;
uniform float BloodOverlayProgress;

out vec4 fragColor;

float getGrayColor(vec4 color, float brightness) {
    return clamp(dot(color.rgb, vec3(0.2126, 0.7152, 0.0722)) * brightness, 0, 1);
}

vec3 getGrayColor(vec4 color) {
    float gray = getGrayColor(color, 1.2f);
    return vec3(gray, gray, gray);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.1) {
        discard;
    }

    float alpha = BloodOverlayProgress;

    color *= vertexColor * ColorModulator;
    color.rgb = mix(getGrayColor(color), color.rgb, (1f - alpha) * 0.5f);
    color *= vec4(0.50980392156f, 0.03921568627f, 0.03921568627f, alpha);
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
