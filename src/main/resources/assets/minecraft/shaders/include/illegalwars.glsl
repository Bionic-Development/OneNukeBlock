#version 150

vec4 getColor(vec4 color, vec4 overlay, float strength) {
    return vec4(mix(color, overlay, strength * 0.78f).rgb, color.a);
}