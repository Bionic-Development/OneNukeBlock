#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform mat4 ProjMat;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    color *= vertexColor * ColorModulator;
    float fragmentDistance = -ProjMat[3].z / ((gl_FragCoord.z) * -2.0 + 1.0 - ProjMat[2].z);

    vec2 textureSize = textureSize(Sampler0, 0);
    ivec2 pixelCoord = ivec2(texCoord0 * textureSize);

    vec2 coord = vec2(pixelCoord) / textureSize;

    float hue = mod(- texCoord0.y * 0.2 + GameTime * 450, 1.0);
    vec3 rgb = clamp(abs(mod(hue * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);

    color.rgb = mix(color.rgb, rgb, color.a);

    fragColor = linear_fog(color, fragmentDistance, FogStart, FogEnd, FogColor);
}
