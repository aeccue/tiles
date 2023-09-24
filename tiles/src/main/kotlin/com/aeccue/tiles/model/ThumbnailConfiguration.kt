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

import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import com.aeccue.tiles.compose.MutableTileAlignment
import com.aeccue.tiles.compose.MutableTileContentScale
import com.aeccue.tiles.compose.TileAlignment
import com.aeccue.tiles.compose.TileContentScale

interface ThumbnailConfiguration {

    companion object {

        operator fun invoke(
            id: ThumbnailId
        ): ThumbnailConfiguration = object : ThumbnailConfiguration {

            override val id: ThumbnailId = id
            override val alignment: TileAlignment = TileAlignment()
            override val contentScale: TileContentScale = TileContentScale()
        }

        operator fun invoke(
            id: ThumbnailId,
            alignment: TileAlignment,
            contentScale: TileContentScale
        ): ThumbnailConfiguration = object : ThumbnailConfiguration {

            override val id: ThumbnailId = id
            override val alignment: TileAlignment = alignment
            override val contentScale: TileContentScale = contentScale
        }
    }

    val id: ThumbnailId
    val alignment: Alignment
    val contentScale: ContentScale
}

interface SelectionThumbnailConfiguration : ThumbnailConfiguration {

    val selected: Boolean
}


internal fun ThumbnailConfiguration.toMutable() = MutableThumbnailConfiguration(
    id = id,
    alignment = (alignment as TileAlignment).toMutable(),
    contentScale = (contentScale as TileContentScale).toMutable()
)

internal data class MutableThumbnailConfiguration(
    override val id: ThumbnailId,
    override val alignment: MutableTileAlignment,
    override val contentScale: MutableTileContentScale,
) : ThumbnailConfiguration {

    fun toReadOnly(): ThumbnailConfiguration = ThumbnailConfiguration(
        id = id,
        alignment = alignment.readOnly,
        contentScale = contentScale.readOnly
    )

    fun reset() {
        alignment.reset()
        contentScale.reset()
    }
}
