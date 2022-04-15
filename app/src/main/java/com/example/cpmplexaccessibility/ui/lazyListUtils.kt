package com.example.cpmplexaccessibility.ui

import androidx.compose.foundation.lazy.LazyListState

/**
 * Returns the scroll offset of the first visible item or 0 if item is not visible
 */
fun LazyListState.firstItemOffset(): Int {
    if (firstVisibleItemIndex != 0) return 0
    return firstVisibleItemScrollOffset
}

/**
 * Returns the amount of visibility of the first item: `0f` if it's fully visible, and `1f` if it's no longer visible
 */
fun LazyListState.firstItemVisibilityFraction(): Float {
    val firstItemInfo = layoutInfo.visibleItemsInfo.firstOrNull() ?: return 0f
    if (firstItemInfo.index != 0) return 1f
    return -firstItemInfo.offset.toFloat() / firstItemInfo.size
}

/**
 * Returns bottom Y coordinate of first item relative to LazyList
 */
fun LazyListState.firstItemBottomY(): Int {
    val firstItemInfo = layoutInfo.visibleItemsInfo.firstOrNull() ?: return Int.MAX_VALUE
    if (firstItemInfo.index != 0) return 0
    return firstItemInfo.size + firstItemInfo.offset
}