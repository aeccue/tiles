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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import kotlin.math.max

@JvmInline
internal value class TileContentScale(
    val scale: Float = 1f
) : ContentScale {

    override fun computeScaleFactor(srcSize: Size, dstSize: Size): ScaleFactor {
        val widthScale = dstSize.width / srcSize.width
        val heightScale = dstSize.height / srcSize.height
        val scale = max(widthScale, heightScale) * scale

        return ScaleFactor(scale, scale)
    }

    internal fun toMutable() = MutableTileContentScale(initialScale = scale)
}

internal class MutableTileContentScale(initialScale: Float) : ContentScale {

    private val scale: MutableState<Float> = mutableStateOf(initialScale)
    val readOnly: TileContentScale
        get() = TileContentScale(scale = scale.value)

    override fun computeScaleFactor(srcSize: Size, dstSize: Size): ScaleFactor {
        val widthScale = dstSize.width / srcSize.width
        val heightScale = dstSize.height / srcSize.height
        val scale = max(widthScale, heightScale) * scale.value

        return ScaleFactor(scale, scale)
    }

    fun onZoom(change: Float) {
        scale.value = (scale.value * change).coerceAtLeast(1f)
    }

    fun reset() {
        scale.value = 1f
    }
}
