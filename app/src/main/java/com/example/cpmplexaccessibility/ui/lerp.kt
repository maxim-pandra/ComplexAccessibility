package com.example.cpmplexaccessibility.ui

fun lerp(value: Float, low1: Float, high1: Float, low2: Float, high2: Float): Float {
    return low2 + (value - low1) * (high2 - low2) / (high1 - low1)
}

/**
 * Linearly maps point [value] on line [from] to the point on line [to].
 *
 * Examples:
 * - from = (0, 100), value = 0, end = (50, 100) => result = 50
 * - from = (0, 100), value = 75, end = (2, 6) => result = 5
 * - from = (0, 100), value = 150, end = (0, 10) => result = 15
 * - from = (0, 100), value = 40, end = (200, 100) => result = 160
 */
@Suppress("NOTHING_TO_INLINE")
inline fun lerp(
    value: Float,
    from: ClosedFloatingPointRange<Float>,
    to: ClosedFloatingPointRange<Float>,
): Float {
    return lerp(value, from.start, from.endInclusive, to.start, to.endInclusive)
}