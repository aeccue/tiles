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

package com.aeccue.tiles.model

import androidx.compose.runtime.toMutableStateList

internal class MutableTileGridOrder(initialOrder: List<ThumbnailConfiguration>) {

    private val order: MutableList<MutableThumbnailConfiguration> =
        initialOrder.map { config ->
            config.toMutable()
        }.toMutableStateList()

    fun toReadOnly(): List<ThumbnailConfiguration> = order.map { config ->
        config.toReadOnly()
    }

    val size get() = order.size

    internal operator fun get(index: Int): MutableThumbnailConfiguration = order[index]

    internal operator fun iterator(): Iterator<MutableThumbnailConfiguration> = order.iterator()

    internal fun swap(first: Int, second: Int) {
        if (first == second) return
        val secondTile = order[second]
        order[second] = order[first]
        order[first] = secondTile
    }

    fun reset() {
        order.forEach { config ->
            config.reset()
        }
    }
}
