#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>
#moj_import <raidbase.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 ChunkOffset;
uniform int FogShape;
uniform float GameTime;
uniform vec3 CameraPos;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;
out vec4 position;

void main() {
    vec3 pos = Position + ChunkOffset;
    position = vec4(pos + CameraPos, 1.0);

    float animation = GameTime * 16000.0;
    float xs = sin(pos.x + animation);
    float zs = cos(pos.z + animation);
    vec2 offset = vec2(rand(GameTime, gl_Position.xy), rand(GameTime, gl_Position.xy * 2.0)) * 0.12;
    vec3 newPosition = pos + vec3(offset, 0.0);
    gl_Position = ProjMat * ModelViewMat * (vec4(newPosition, 1.0) + vec4(xs / 16.0, 0.0, zs / 16.0, 0.0));

    vertexDistance = fog_distance(ModelViewMat, pos, FogShape);
    vertexColor = Color * minecraft_sample_lightmap(Sampler2, UV2);
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
