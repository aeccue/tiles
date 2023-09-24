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

package com.aeccue.tiles.compose.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.aeccue.tiles.grid.tree.MutableTileGridLayoutTree
import com.aeccue.tiles.grid.tree.tileGridLayoutTree
import com.aeccue.tiles.model.MutableTileGridOrder
import com.aeccue.tiles.model.SelectionThumbnailConfiguration
import com.aeccue.tiles.model.ThumbnailConfiguration

@Composable
fun TileGrid(
    grid: TileGrid,
    tiles: List<TileOverview>,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    onTileClick: (tile: TileOverview) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(grid.padding.dp)
            .aspectRatio(grid.aspectRatio)
            .then(modifier)
    ) {
        TileGridLayout(
            tiles = remember(tiles) { tiles.associateBy { it.id } },
            order = grid.order,
            tree = remember(grid) {
                tileGridLayoutTree(
                    layout = grid.layout,
                    positions = null
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .clip(shape),
            isLayoutFlipped = grid.flipped,
            spacing = grid.spacing.dp,
            onTileClick = onTileClick
        )
    }
}

@Composable
fun TileGrid(
    layout: TileGrid.Layout,
    tree: MutableTileGridLayoutTree?,
    order: List<ThumbnailConfiguration>,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    isLayoutFlipped: Boolean,
    aspectRatio: Float = Theme.dimensions.tile.gridRatio,
    padding: Dp = Theme.dimensions.tile.gridSpacing,
    spacing: Dp = Theme.dimensions.tile.gridSpacing,
    isClickEnabled: (id: ThumbnailId) -> Boolean = { true },
    tileClickIcon: (@Composable (id: ThumbnailId) -> Unit)? = null,
    onTileClick: (id: ThumbnailId) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(padding)
            .aspectRatio(aspectRatio)
            .then(modifier)
    ) {
        TileGridLayout(
            order = order,
            tree = tree ?: layout.tree,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape),
            isClickEnabled = { orderIndex ->
                val thumbnailId = order[orderIndex].id
                isClickEnabled(thumbnailId)
            },
            isLayoutFlipped = isLayoutFlipped,
            spacing = spacing,
            tileOverlay = { orderIndex ->
                if (tileClickIcon != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(Theme.dimensions.button.defaultPadding)
                            .size(Theme.dimensions.icon.default)
                    ) {
                        val thumbnailId = order[orderIndex].id
                        tileClickIcon(thumbnailId)
                    }
                }
            },
            onTileClick = { orderIndex ->
                val thumbnailId = order[orderIndex].id
                onTileClick(thumbnailId)
            }
        )
    }
}

@Composable
fun SelectableTileGrid(
    layout: TileGrid.Layout,
    tree: MutableTileGridLayoutTree?,
    order: List<ThumbnailConfiguration>,
    modifier: Modifier = Modifier,
    isLayoutFlipped: Boolean,
    aspectRatio: Float = Theme.dimensions.tile.gridRatio,
    padding: Dp = Theme.dimensions.tile.gridSpacing,
    spacing: Dp = Theme.dimensions.tile.gridSpacing,
    isClickEnabled: (orderIndex: Int) -> Boolean,
    onTileToggle: (orderIndex: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(padding)
            .aspectRatio(aspectRatio)
            .then(modifier)
    ) {
        TileGridLayout(
            order = order,
            tree = tree ?: layout.tree,
            modifier = Modifier.fillMaxSize(),
            isLayoutFlipped = isLayoutFlipped,
            spacing = spacing,
            isClickEnabled = isClickEnabled,
            tileOverlay = { orderIndex ->
                when (val configuration = order[orderIndex]) {
                    is SelectionThumbnailConfiguration -> {
                        if (configuration.selected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White.copy(alpha = Theme.colors.secondaryAlpha))
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(Theme.dimensions.button.defaultPadding)
                                    .size(Theme.dimensions.icon.default)
                                    .clip(CircleShape)
                                    .background(Theme.colors.accent)
                            )
                        }
                    }
                    else -> {
                        // TODO: Different overlay?
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = Theme.colors.secondaryAlpha))
                        )
                    }
                }
            },
            onTileClick = onTileToggle
        )
    }
}

@Composable
fun TileGridPreview(
    layout: TileGrid.Layout,
    modifier: Modifier = Modifier,
    size: DpSize? = Theme.dimensions.tile.sampleGridSize,
    padding: Dp = Theme.dimensions.tile.gridSpacing,
    spacing: Dp = Theme.dimensions.tile.gridSpacing,
    colors: TileGridColors = TileGridColors.Default,
    clickable: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .applyIfNotNull(size) {
                size(it)
            }
            .applyIf(clickable) {
                clip(shape = RectangleShape)
                    .clickable(onClick = onClick)
            }
    ) {
        TileGridLayoutPreview(
            tree = layout.tree,
            modifier = Modifier.fillMaxSize(),
            colors = colors,
            padding = padding,
            spacing = spacing
        )
    }
}

@Composable
fun TileGridDraft(
    layout: TileGrid.Layout,
    tiles: Map<ThumbnailId, Thumbnail>,
    tree: MutableTileGridLayoutTree?,
    order: List<ThumbnailConfiguration>,
    modifier: Modifier = Modifier,
    isLayoutFlipped: Boolean,
    aspectRatio: Float = Theme.dimensions.tile.gridRatio,
    padding: Dp = Theme.dimensions.tile.gridSpacing,
    spacing: Dp = Theme.dimensions.tile.gridSpacing,
) {
    Box(
        modifier = Modifier
            .padding(padding)
            .aspectRatio(aspectRatio)
            .then(modifier)
    ) {
        TileGridLayoutDraft(
            tiles = tiles,
            order = order,
            tree = tree ?: layout.tree,
            modifier = Modifier.fillMaxSize(),
            isLayoutFlipped = isLayoutFlipped,
            spacing = spacing
        )
    }
}

@Composable
fun TileGridEditor(
    tree: MutableTileGridLayoutTree,
    order: MutableTileGridOrder,
    modifier: Modifier = Modifier,
    isLayoutFlipped: Boolean,
    padding: Dp,
    spacing: Dp
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(padding)
            .then(modifier)
    ) {
        TileGridLayoutEditor(
            tree = tree,
            order = order,
            modifier = Modifier.fillMaxSize(),
            isLayoutFlipped = isLayoutFlipped,
            containerBounds = constraints.bounds,
            spacing = spacing
        )
    }
}

private val TileGrid.Layout.tree: MutableTileGridLayoutTree
    get() = when (this) {
        is TileGrid.Layout.GridThree -> gridThreeLayoutTree(layout = this)
        is TileGrid.Layout.GridFour -> gridFourLayoutTree(layout = this)
        is TileGrid.Layout.GridFive -> gridFiveLayoutTree(layout = this)
        is TileGrid.Layout.GridSix -> gridSixLayoutTree(layout = this)
        is TileGrid.Layout.GridSeven -> gridSevenLayoutTree(layout = this)
        is TileGrid.Layout.GridEight -> gridEightLayoutTree(layout = this)
        is TileGrid.Layout.GridNine -> gridNineLayoutTree(layout = this)
    }
