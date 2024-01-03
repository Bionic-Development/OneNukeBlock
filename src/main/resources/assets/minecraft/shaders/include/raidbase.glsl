#version 150

float rand(float time, vec2 co){
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233)) + time / 100) * 43758.5453) * 2.0 - 1.0;
}

float offset(float time, float blocks, vec2 uv) {
    return rand(time, vec2(time, floor(uv.y * blocks)));
}