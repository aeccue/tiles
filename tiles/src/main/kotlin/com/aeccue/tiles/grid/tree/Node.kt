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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.constraintlayout.compose.ConstrainedLayoutReference

private const val MIN_TILE_SIZE_PERCENT = 0.15f

internal sealed interface Node {

    val id: Any
    val orientation: Orientation
    val bounds: Bounds
    val position: Float

    val lineReference: ConstrainedLayoutReference?

    fun updatePositionBy(
        percent: Float,
        flipVertical: Boolean,
        flipHorizontal: Boolean
    )

    class Internal(
        override val id: Any,
        override val orientation: Orientation,
        override val bounds: Bounds,
        val nodes: List<Node>,
        val canFlipVertical: Boolean,
        val canFlipHorizontal: Boolean,
        private val positionState: MutableState<Float>
    ) : Node {

        override val position by positionState

        override val lineReference =
            if (bounds.hasLine) ConstrainedLayoutReference("$id[line]") else null

        override fun updatePositionBy(
            percent: Float,
            flipVertical: Boolean,
            flipHorizontal: Boolean
        ) {
            positionState.value = when (orientation) {
                Orientation.Vertical -> {
                    val min = (bounds.horizontalStart.prev?.position ?: 0f) + MIN_TILE_SIZE_PERCENT
                    val max = (bounds.horizontalStart.next?.position ?: 1f) - MIN_TILE_SIZE_PERCENT
                    if (canFlipHorizontal && flipHorizontal) {
                        (position - percent).coerceIn(min, max)
                    } else {
                        (position + percent).coerceIn(min, max)
                    }
                }
                Orientation.Horizontal -> {
                    val min = (bounds.verticalStart.prev?.position ?: 0f) + MIN_TILE_SIZE_PERCENT
                    val max = (bounds.verticalStart.next?.position ?: 1f) - MIN_TILE_SIZE_PERCENT
                    if (canFlipVertical && flipVertical) {
                        (position - percent).coerceIn(min, max)
                    } else {
                        (position + percent).coerceIn(min, max)
                    }
                }
            }
        }
    }

    class Leaf(
        override val id: Any,
        override val orientation: Orientation,
        override val bounds: Bounds,
        val index: Int,
        val canFlipVertical: Boolean,
        val canFlipHorizontal: Boolean,
        private val positionState: MutableState<Float>,
    ) : Node {

        override val position by positionState

        val reference = ConstrainedLayoutReference(id)
        val gestureReference = ConstrainedLayoutReference("$id[gesture]")
        override val lineReference =
            if (bounds.hasLine) ConstrainedLayoutReference("$id[line]") else null

        override fun updatePositionBy(
            percent: Float,
            flipVertical: Boolean,
            flipHorizontal: Boolean
        ) {
            positionState.value = when (orientation) {
                Orientation.Vertical -> {
                    val min =
                        (bounds.verticalStart.prev?.position ?: 0f) + MIN_TILE_SIZE_PERCENT
                    val max =
                        (bounds.verticalStart.next?.position ?: 1f) - MIN_TILE_SIZE_PERCENT
                    if (canFlipVertical && flipVertical) {
                        (position - percent).coerceIn(min, max)
                    } else {
                        (position + percent).coerceIn(min, max)
                    }
                }
                Orientation.Horizontal -> {
                    val min =
                        (bounds.horizontalStart.prev?.position ?: 0f) + MIN_TILE_SIZE_PERCENT
                    val max =
                        (bounds.horizontalStart.next?.position ?: 1f) - MIN_TILE_SIZE_PERCENT
                    if (canFlipHorizontal && flipHorizontal) {
                        (position - percent).coerceIn(min, max)
                    } else {
                        (position + percent).coerceIn(min, max)
                    }
                }
            }
        }
    }
}
