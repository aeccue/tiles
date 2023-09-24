/*
 * Designed and developed by aeccue.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aeccue.tiles.grid.tree

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

internal fun MutableTileGridLayoutTree.toBitmapBounds(
    size: Size,
    flipVertical: Boolean,
    flipHorizontal: Boolean,
    spacing: Float
): List<Rect> = root.toBounds(
    bounds = RectBounds(size),
    flipVertical = flipVertical,
    flipHorizontal = flipHorizontal,
    lineMargin = spacing / 2
)

private fun Node.toBounds(
    bounds: RectBounds,
    flipVertical: Boolean,
    flipHorizontal: Boolean,
    lineMargin: Float
): List<Rect> = when (this) {
    is Node.Internal -> toBounds(
        bounds = bounds,
        flipVertical = flipVertical,
        flipHorizontal = flipHorizontal,
        lineMargin = lineMargin
    )
    is Node.Leaf -> listOf(bounds.toRect(lineMargin))
}

private fun Node.Internal.toBounds(
    bounds: RectBounds,
    flipVertical: Boolean,
    flipHorizontal: Boolean,
    lineMargin: Float
): List<Rect> {
    val boundsList = mutableListOf<Rect>()
    val flipVerticalActual = if (canFlipVertical) flipVertical else false
    val flipHorizontalActual = if (canFlipHorizontal) flipHorizontal else false
    when (orientation) {
        Orientation.Vertical -> {
            var boundCache = if (!flipVerticalActual) bounds.top else bounds.bottom
            nodes.forEachIndexed { index, node ->
                val childBounds = bounds.copy(
                    top = if (!flipVerticalActual) {
                        boundCache
                    } else {
                        when (index) {
                            nodes.lastIndex -> bounds.top
                            else -> RectBounds.Edge(
                                value = nodes[index + 1].position * bounds.height
                            )
                        }
                    },
                    bottom = if (!flipVerticalActual) {
                        when (index) {
                            nodes.lastIndex -> bounds.bottom
                            else -> RectBounds.Edge(
                                value = nodes[index + 1].position * bounds.height
                            )
                        }
                    } else {
                        boundCache
                    }
                )

                boundsList += node.toBounds(
                    bounds = childBounds,
                    flipVertical = flipVertical,
                    flipHorizontal = flipHorizontal,
                    lineMargin = lineMargin
                )

                boundCache = if (!flipVerticalActual) childBounds.bottom else childBounds.top
            }
        }
        Orientation.Horizontal -> {
            var boundCache = if (!flipHorizontalActual) bounds.left else bounds.right
            nodes.forEachIndexed { index, node ->
                val childBounds = bounds.copy(
                    left = if (!flipHorizontalActual) {
                        boundCache
                    } else {
                        when (index) {
                            nodes.lastIndex -> bounds.left
                            else -> RectBounds.Edge(
                                value = nodes[index + 1].position * bounds.width
                            )
                        }
                    },
                    right = if (!flipHorizontalActual) {
                        when (index) {
                            nodes.lastIndex -> bounds.right
                            else -> RectBounds.Edge(
                                value = nodes[index + 1].position * bounds.width
                            )
                        }
                    } else {
                        boundCache
                    }
                )

                boundsList += node.toBounds(
                    bounds = childBounds,
                    flipVertical = flipVertical,
                    flipHorizontal = flipHorizontal,
                    lineMargin = lineMargin
                )

                boundCache = if (!flipHorizontalActual) childBounds.right else childBounds.left
            }
        }
    }

    return boundsList
}

private fun RectBounds.toRect(lineMargin: Float): Rect = Rect(
    top = top.value + if (top.linked) lineMargin else 0f,
    bottom = bottom.value - if (bottom.linked) lineMargin else 0f,
    left = left.value + if (left.linked) lineMargin else 0f,
    right = right.value - if (right.linked) lineMargin else 0f,
)
