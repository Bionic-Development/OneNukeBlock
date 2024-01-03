#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>
#moj_import <raidbase.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in vec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat3 IViewRotMat;
uniform int FogShape;
uniform float GameTime;

uniform vec3 Light0_Direction;
uniform vec3 Light1_Direction;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec2 texCoord1;
out vec2 texCoord2;
out vec4 normal;
out vec4 position;

void main() {
    float animation = GameTime * 16000.0;
    float xs = sin(Position.x + animation);
    float zs = cos(Position.z + animation);
    vec2 offset = vec2(rand(GameTime, gl_Position.xy), rand(GameTime, gl_Position.xy * 2.0)) * 0.12;
    vec3 newPosition = Position + vec3(offset, 0.0);
    gl_Position = ProjMat * ModelViewMat * (vec4(newPosition, 1.0) + vec4(xs / 16.0, 0.0, zs / 16.0, 0.0));

    vertexDistance = fog_distance(ModelViewMat, IViewRotMat * Position, FogShape);
    vertexColor = minecraft_mix_light(Light0_Direction, Light1_Direction, Normal, Color) * texelFetch(Sampler2, UV2 / 16, 0);
    texCoord0 = UV0;
    texCoord1 = UV1;
    texCoord2 = UV2;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
