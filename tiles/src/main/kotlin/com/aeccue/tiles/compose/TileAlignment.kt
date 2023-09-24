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

package com.aeccue.tiles.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.*

internal class TileAlignment(
    val offsetPercent: Offset = Offset(x = 0.5f, y = 0.5f)
) : Alignment {

    constructor(x: Float, y: Float) : this(Offset(x = x, y = y))

    override fun align(
        size: IntSize,
        space: IntSize,
        layoutDirection: LayoutDirection
    ): IntOffset {
        val maxOffset = calculateMaxOffset(size = size, space = space)
        return Offset(
            x = offsetPercent.x * maxOffset.x,
            y = offsetPercent.y * maxOffset.y
        ).round()
    }

    internal fun toMutable() = MutableTileAlignment(
        initialOffsetPercent = offsetPercent
    )
}

internal class MutableTileAlignment(initialOffsetPercent: Offset) : Alignment {

    private var sizeCache by mutableStateOf(Size.Zero)
    private var maxOffset by mutableStateOf(Offset.Zero)
    private var offsetPercent by mutableStateOf(initialOffsetPercent)

    val readOnly get() = TileAlignment(offsetPercent = offsetPercent)

    override fun align(
        size: IntSize,
        space: IntSize,
        layoutDirection: LayoutDirection
    ): IntOffset {
        sizeCache = size.toSize()
        maxOffset = calculateMaxOffset(size = size, space = space)
        return if (size == space) {
            IntOffset.Zero
        } else {
            Offset(
                x = offsetPercent.x * maxOffset.x,
                y = offsetPercent.y * maxOffset.y
            ).round()
        }
    }

    fun onPan(change: Offset) {
        val x = (offsetPercent.x - change.x / sizeCache.width).coerceIn(0f, 1f)
        val y = (offsetPercent.y - change.y / sizeCache.height).coerceIn(0f, 1f)
        offsetPercent = Offset(x = x, y = y)
    }

    fun reset() {
        maxOffset = Offset.Zero
        offsetPercent = Offset(x = 0.5f, y = 0.5f)
    }
}

private fun calculateMaxOffset(size: IntSize, space: IntSize) = Offset(
    x = (space.width - size.width).toFloat(),
    y = (space.height - size.height).toFloat()
)
