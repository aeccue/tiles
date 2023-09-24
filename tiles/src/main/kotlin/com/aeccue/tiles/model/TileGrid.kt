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

import com.aeccue.tiles.compose.TileAlignment
import com.aeccue.tiles.compose.TileContentScale

inline val TileGrid.Tile.alignment
    get() = TileAlignment(
        x = alignmentX,
        y = alignmentY
    )

inline val TileGrid.Tile.contentScale
    get() = TileContentScale(scale)
