package org.chyavorec.app.ui.theme

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

/**
 * AGSL шейдърите на „Живата шевица“ (API 33+). Пазят се тук като текст, за да
 * ги проверяват тестовете (униформите, сигнатурата). Шарката (`stitchKind`) има
 * едно Kotlin копие — [ShevitsaPattern]; осветлението на бода — едно копие на
 * процесора, само за плочката на неподвижния вариант до API 32 (ShevitsaStill в
 * LivingShevitsa.kt). ShevitsaParityTest изпълнява AGSL и сверява и двете.
 */
object ShevitsaShaders {
    /**
     * Шевица на кръстат бод: ромбова мрежа, осморъки (осмолъчни звезди) и „ромб с
     * ромб и точка“ ([ShevitsaMotif.RHOMBUS]) в ромбовете. Всяка клетка е един
     * кръстат бод (две нишки с обла светлина и усукване), между тях — канава с
     * дупчици. Сноп златна светлина минава по нишките ([uTime], период
     * [SWEEP_SECONDS] s) и златните бодове леко проблясват.
     *
     * Изходът е premultiplied; [uFabricAlpha] = 0 оставя фона прозрачен (само нишките).
     */
    const val WEAVE: String = """
uniform float2 uResolution;
uniform float2 uOrigin;
uniform float uTime;
uniform float uCell;
uniform float uPeriod;
uniform float uRibbon;
uniform float uIntensity;
uniform float uFabricAlpha;
uniform float uThreadAlpha;
uniform float uClear;
layout(color) uniform half4 uFabric;
layout(color) uniform half4 uRed;
layout(color) uniform half4 uInk;
layout(color) uniform half4 uGold;
layout(color) uniform half4 uGlow;

float hash(float2 p) {
    return fract(sin(dot(p, float2(127.1, 311.7))) * 43758.5453);
}

// 0 = празно, 1 = червено, 2 = мастило, 3 = злато.
float stitchKind(float2 c) {
    float P = uPeriod;
    float R = P * 0.5;
    bool ribbon = uRibbon > 0.5;
    float u = c.x + c.y;
    float v = c.x - c.y;
    float mu = mod(u, P);
    float mv = mod(v, P);
    float du = min(mu, P - mu);
    float dv = min(mv, P - mv);
    bool node = max(du, dv) < 1.5;
    float ay = abs(c.y);
    if (ribbon && ay > R + 0.5) {
        if (ay < R + 1.5) { return node ? 3.0 : 0.0; }
        if (ay < R + 2.5) { return 1.0; }
        return 0.0;
    }
    if (node) { return (du + dv < 0.5) ? 2.0 : 3.0; }
    if (du < 0.5 || dv < 0.5) { return 1.0; }
    float i = floor(u / P);
    float j = floor(v / P);
    if (ribbon && abs(i - j) > 0.5) { return 0.0; }
    float lu = mu - R;
    float lv = mv - R;
    float manh = max(abs(lu), abs(lv));
    float sel = ribbon ? mod(i, 2.0) : mod(i + j, 2.0);
    float S = ribbon ? R : R * 0.72;
    if (sel < 0.5) {
        if (manh < 0.5) { return 3.0; }
        if (manh < 1.5) { return 2.0; }
        float ax = abs(lu + lv) * 0.5;
        float by = abs(lu - lv) * 0.5;
        float s = (ax + by) * 0.5 - S * 0.375;
        float d = abs(ax - by) * 0.5;
        if (abs(s) / (S * 0.275) + d / (S * 0.1) <= 1.0) { return 3.0; }
        float ma = max(ax, by);
        float mi = min(ax, by);
        if (abs(ma - S * 0.5) / (S * 0.4) + mi / (S * 0.2) <= 1.0) { return 1.0; }
        return 0.0;
    }
    float b = floor(S * 0.75 + 0.01);
    if (abs(manh - b) < 0.1) { return 1.0; }
    if (abs(manh - floor(b * 0.5)) < 0.1) { return 3.0; }
    if (manh < 0.1) { return 2.0; }
    return 0.0;
}

half4 main(float2 fragCoord) {
    float2 g = fragCoord / uCell;
    float2 c = floor(g) - uOrigin;
    float2 q = fract(g);
    float k = stitchKind(c);
    float aa = 1.0 / uCell;
    float w = 0.21;
    float dA = abs(q.x - q.y) * 0.7071;
    float tA = (q.x + q.y) * 0.5;
    float dB = abs(q.x + q.y - 1.0) * 0.7071;
    float tB = (q.x - q.y) * 0.5 + 0.5;
    float endA = smoothstep(0.03, 0.1, tA) * (1.0 - smoothstep(0.9, 0.97, tA));
    float endB = smoothstep(0.03, 0.1, tB) * (1.0 - smoothstep(0.9, 0.97, tB));
    float covA = (1.0 - smoothstep(w - aa, w + aa, dA)) * endA;
    float covB = (1.0 - smoothstep(w - aa, w + aa, dB)) * endB;
    float nA = dA / w;
    float nB = dB / w;
    float shA = sqrt(clamp(1.0 - nA * nA, 0.0, 1.0));
    float shB = sqrt(clamp(1.0 - nB * nB, 0.0, 1.0));
    float twA = 0.9 + 0.1 * sin((tA * 7.0 + dA * 9.0) * 6.2832);
    float twB = 0.9 + 0.1 * sin((tB * 7.0 + dB * 9.0) * 6.2832);
    float shd = 1.0 - 0.35 * (1.0 - smoothstep(w, w + 0.12, dB)) * endB;
    float litA = (0.5 + 0.62 * shA) * twA * shd;
    float litB = (0.5 + 0.62 * shB) * twB;
    float lit = litB * covB + litA * (1.0 - covB);
    float shade = shB * covB + shA * (1.0 - covB);
    float cov = (k > 0.5) ? clamp(covA + covB - covA * covB, 0.0, 1.0) : 0.0;

    float3 thread = (k < 1.5) ? float3(uRed.rgb) : ((k < 2.5) ? float3(uInk.rgb) : float3(uGold.rgb));
    float3 glow = float3(uGlow.rgb);
    float2 dir = float2(0.944, 0.330);
    float coord = dot(fragCoord, dir) / dot(uResolution, dir);
    float e = (coord - (fract(uTime / 8.0) * 1.6 - 0.3)) / 0.11;
    float band = exp(-(e * e)) * uIntensity;
    float tw = (k > 2.5) ? pow(0.5 + 0.5 * sin(uTime * 1.7 + hash(c) * 6.2832), 14.0) * uIntensity : 0.0;
    float spec = shade * shade * shade;
    spec = spec * spec;
    float3 col = thread * lit;
    col = col * (1.0 - 0.6 * band) + glow * (lit * 0.6 * band);
    col = clamp(col + glow * ((band * 0.45 + tw * 0.8) * spec), 0.0, 1.0);

    float2 m = min(q, 1.0 - q);
    float hole = 1.0 - smoothstep(0.05, 0.2, length(m));
    float weave = 0.5 + 0.5 * sin(q.x * 12.566) * sin(q.y * 12.566);
    float3 fabric = clamp(float3(uFabric.rgb) * (0.97 + 0.04 * weave - 0.1 * hole) + glow * (band * 0.05), 0.0, 1.0);
    float ta = uThreadAlpha;
    if (uClear > 0.0) {
        float r = length(fragCoord - uResolution * 0.5) / min(uResolution.x, uResolution.y);
        ta = ta * smoothstep(uClear * 0.55, uClear, r);
    }
    float ca = cov * ta;
    float fa = uFabricAlpha * (1.0 - ca);
    return half4(half3(col * ca + fabric * fa), half(ca + fa));
}
"""

    /** Униформите на [WEAVE] — всички се задават от кода (тестът сверява с текста). */
    val WEAVE_UNIFORMS = listOf(
        "uResolution", "uOrigin", "uTime", "uCell", "uPeriod", "uRibbon", "uIntensity",
        "uFabricAlpha", "uThreadAlpha", "uClear", "uFabric", "uRed", "uInk", "uGold", "uGlow",
    )

    /**
     * Холографско фолио на читателската карта: тънък филм (дъга, примесена със
     * злато), чийто тон се мести с наклона ([uTilt], [uHue]), фини дифракционни
     * линии, по-силна ивица „блясък“ ([uSheen]) и мек отблясък ([uGlint]).
     * Рисува се с BlendMode.Screen под съдържанието на картата (не върху баркода).
     */
    const val FOIL: String = """
uniform float2 uResolution;
uniform float2 uTilt;
uniform float uHue;
uniform float uSheen;
uniform float2 uGlint;
uniform float uGlintStrength;
uniform float uStrength;
layout(color) uniform half4 uGold;

half4 main(float2 p) {
    float2 uv = p / uResolution;
    float aspect = uResolution.x / uResolution.y;
    float th = 0.55 * uv.x + 0.35 * uv.y + 0.08 * sin(uv.x * 9.0 + uv.y * 5.0) + 0.05 * sin(uv.y * 17.0 - uv.x * 7.0);
    float phase = th * 1.6 + uHue + dot(uv - 0.5, uTilt) * 0.6;
    float3 film = 0.5 + 0.5 * cos(6.2832 * (phase + float3(0.0, 0.33, 0.67)));
    film = mix(film, float3(uGold.rgb), 0.3);
    float luma = dot(film, float3(0.299, 0.587, 0.114));
    film = mix(float3(luma), film, 0.8);
    float grating = 0.82 + 0.18 * sin((p.x + p.y) * 0.9);
    float sc = uv.x * 0.7 + uv.y * 0.3;
    float e = (sc - uSheen) / 0.16;
    float sheen = exp(-(e * e));
    float a = uStrength * (0.3 + 0.7 * sheen) * grating;
    float2 gd = (uv - uGlint) * float2(aspect, 1.0);
    float glint = exp(-dot(gd, gd) / 0.008) * uGlintStrength;
    float3 col = film * a + float3(1.0, 0.96, 0.88) * glint;
    float alpha = clamp(a + glint, 0.0, 1.0);
    return half4(half3(clamp(col, 0.0, alpha)), half(alpha));
}
"""

    val FOIL_UNIFORMS = listOf("uResolution", "uTilt", "uHue", "uSheen", "uGlint", "uGlintStrength", "uStrength", "uGold")

    /** Един сноп светлина минава за толкова секунди „време на тъкане“. */
    const val SWEEP_SECONDS = 8f
}

/**
 * Kotlin копие на шарката от [ShevitsaShaders.WEAVE] (stitchKind) — за плочката на
 * неподвижния вариант без шейдър и за тестовете. Промяна тук = промяна и в шейдъра:
 * ShevitsaParityTest рисува шейдъра и сверява вида на всяка клетка с [kind].
 * Шарката е периодична — поле през [period] бода по двете оси, лента през 2·period.
 */
object ShevitsaPattern {
    const val EMPTY = 0
    const val RED = 1
    const val INK = 2
    const val GOLD = 3

    /** Редовете бодове на лентата (ромбове + кант отгоре и отдолу) при период [period]. */
    fun ribbonRows(period: Int): Int = period + 5

    private fun mod(x: Float, y: Float) = x - y * floor(x / y)

    /** Видът бод в клетка ([cx], [cy]) спрямо центъра на шарката. */
    fun kind(cx: Float, cy: Float, period: Float, ribbon: Boolean): Int {
        val p = period
        val r = p * 0.5f
        val u = cx + cy
        val v = cx - cy
        val mu = mod(u, p)
        val mv = mod(v, p)
        val du = min(mu, p - mu)
        val dv = min(mv, p - mv)
        val node = max(du, dv) < 1.5f
        val ay = abs(cy)
        if (ribbon && ay > r + 0.5f) {
            if (ay < r + 1.5f) return if (node) GOLD else EMPTY
            if (ay < r + 2.5f) return RED
            return EMPTY
        }
        if (node) return if (du + dv < 0.5f) INK else GOLD
        if (du < 0.5f || dv < 0.5f) return RED
        val i = floor(u / p)
        val j = floor(v / p)
        if (ribbon && abs(i - j) > 0.5f) return EMPTY
        val lu = mu - r
        val lv = mv - r
        val manh = max(abs(lu), abs(lv))
        val sel = if (ribbon) mod(i, 2f) else mod(i + j, 2f)
        val s = if (ribbon) r else r * 0.72f
        if (sel < 0.5f) {
            if (manh < 0.5f) return GOLD
            if (manh < 1.5f) return INK
            val ax = abs(lu + lv) * 0.5f
            val by = abs(lu - lv) * 0.5f
            val along = (ax + by) * 0.5f - s * 0.375f
            val across = abs(ax - by) * 0.5f
            if (abs(along) / (s * 0.275f) + across / (s * 0.1f) <= 1f) return GOLD
            val ma = max(ax, by)
            val mi = min(ax, by)
            if (abs(ma - s * 0.5f) / (s * 0.4f) + mi / (s * 0.2f) <= 1f) return RED
            return EMPTY
        }
        val b = floor(s * 0.75f + 0.01f)
        if (abs(manh - b) < 0.1f) return RED
        if (abs(manh - floor(b * 0.5f)) < 0.1f) return GOLD
        if (manh < 0.1f) return INK
        return EMPTY
    }
}
