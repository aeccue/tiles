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

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.aeccue.tiles.compose.component.TileOverview
import com.aeccue.tiles.model.MutableTileGridOrder
import com.aeccue.tiles.compose.MutableTileContentScale
import com.aeccue.tiles.grid.tree.MutableTileGridLayoutTree
import com.aeccue.tiles.grid.tree.Node
import com.aeccue.tiles.grid.tree.toConstraintSet
import com.aeccue.tiles.grid.tree.toEditorConstraintSet
import com.aeccue.tiles.grid.tree.toPreviewConstraintSet
import com.aeccue.tiles.model.ThumbnailConfiguration

data class TileGridColors(
    val tile: Color,
    val line: Color
) {

    companion object {

        val Default
            @Composable get() = TileGridColors(
                tile = LocalContentColor.current,
                line = Color.Transparent
            )
    }
}

@Composable
internal fun TileGridLayout(
    tiles: Map<Id, TileOverview>,
    order: List<TileGrid.Tile>,
    tree: MutableTileGridLayoutTree,
    modifier: Modifier = Modifier,
    isLayoutFlipped: Boolean,
    spacing: Dp,
    onTileClick: (tile: TileOverview) -> Unit
) {
    val constraintSet = tree.toConstraintSet(
        flipVertical = isLayoutFlipped,
        flipHorizontal = isLayoutFlipped,
        spacing = spacing
    )

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
    ) {
        TileTree(
            tiles = tiles,
            order = order,
            tree = tree.root,
            onTileClick = onTileClick
        )
    }
}


@Composable
private fun TileTree(
    tiles: Map<Id, TileOverview>,
    order: List<TileGrid.Tile>,
    tree: Node.Internal,
    onTileClick: (tile: TileOverview) -> Unit
) {
    tree.nodes.forEach { node ->
        when (node) {
            is Node.Internal -> TileTree(
                tiles = tiles,
                order = order,
                tree = node,
                onTileClick = onTileClick
            )

            is Node.Leaf -> {
                val config = order[node.index]
                val tile = requireNotNull(tiles[config.id])
                TileOverview(
                    tile = tile,
                    config = config,
                    modifier = Modifier.layoutId(node.reference.id)
                ) {
                    onTileClick(tile)
                }
            }
        }
    }
}

@Composable
internal fun TileGridLayout(
    order: List<ThumbnailConfiguration>,
    tree: MutableTileGridLayoutTree,
    modifier: Modifier = Modifier,
    isLayoutFlipped: Boolean,
    spacing: Dp,
    isClickEnabled: (orderIndex: Int) -> Boolean,
    tileOverlay: @Composable BoxScope.(orderIndex: Int) -> Unit,
    onTileClick: (orderIndex: Int) -> Unit
) {
    val constraintSet = tree.toConstraintSet(
        flipVertical = isLayoutFlipped,
        flipHorizontal = isLayoutFlipped,
        spacing = spacing
    )

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
    ) {
        TileTree(
            order = order,
            tree = tree.root,
            isClickEnabled = isClickEnabled,
            tileOverlay = tileOverlay,
            onTileClick = onTileClick
        )
    }
}

@Composable
private fun TileTree(
    order: List<ThumbnailConfiguration>,
    tree: Node.Internal,
    isClickEnabled: (orderIndex: Int) -> Boolean,
    tileOverlay: @Composable BoxScope.(orderIndex: Int) -> Unit,
    onTileClick: (orderIndex: Int) -> Unit
) {
    tree.nodes.forEach { node ->
        when (node) {
            is Node.Internal -> TileTree(
                order = order,
                tree = node,
                isClickEnabled = isClickEnabled,
                tileOverlay = tileOverlay,
                onTileClick = onTileClick
            )

            is Node.Leaf -> {
                val config = order[node.index]
                Tile(
                    uri = config.id.uri,
                    modifier = Modifier.layoutId(node.reference.id),
                    isClickEnabled = isClickEnabled(node.index),
                    alignment = config.alignment,
                    contentScale = config.contentScale,
                    overlay = { tileOverlay(node.index) },
                    onClick = { onTileClick(node.index) }
                )
            }
        }
    }
}

@Composable
private fun Tile(
    uri: Uri,
    modifier: Modifier = Modifier,
    isClickEnabled: Boolean,
    alignment: Alignment,
    contentScale: ContentScale,
    overlay: @Composable BoxScope.() -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.applyIf(isClickEnabled) {
            this
                .clip(shape = RectangleShape)
                .clickable(onClick = onClick)
        }
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alignment = alignment,
            contentScale = contentScale,
        )

        overlay()
    }
}

@Composable
internal fun TileGridLayoutDraft(
    tiles: Map<ThumbnailId, Thumbnail>,
    order: List<ThumbnailConfiguration>,
    tree: MutableTileGridLayoutTree,
    modifier: Modifier = Modifier,
    isLayoutFlipped: Boolean,
    spacing: Dp
) {
    LaunchedEffect(tiles, order) {
        var size = 0
        for (config in order) {
            require(tiles.containsKey(config.id))
            size++
        }
        require(size == tiles.size)
    }

    val constraintSet = tree.toConstraintSet(
        flipVertical = isLayoutFlipped,
        flipHorizontal = isLayoutFlipped,
        spacing = spacing
    )

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
    ) {
        DraftTileTree(
            tiles = tiles,
            order = order,
            tree = tree.root
        )
    }
}

@Composable
private fun DraftTileTree(
    tiles: Map<ThumbnailId, Thumbnail>,
    order: List<ThumbnailConfiguration>,
    tree: Node.Internal
) {
    tree.nodes.forEach { node ->
        when (node) {
            is Node.Internal -> DraftTileTree(
                tiles = tiles,
                order = order,
                tree = node
            )

            is Node.Leaf -> {
                val config = order[node.index]
                AsyncImage(
                    model = tiles[config.id],
                    contentDescription = null,
                    modifier = Modifier
                        .layoutId(node.reference.id)
                        .background(Color.Gray),
                    alignment = config.alignment,
                    contentScale = config.contentScale
                )
            }
        }
    }
}

@Composable
internal fun TileGridLayoutPreview(
    tree: MutableTileGridLayoutTree,
    modifier: Modifier = Modifier,
    colors: TileGridColors = TileGridColors.Default,
    padding: Dp,
    spacing: Dp
) {
    val constraintSet = tree.toPreviewConstraintSet(spacing = spacing)
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
            .background(colors.line)
            .padding(padding)
    ) {
        PreviewTileTree(
            tree = tree.root,
            colors = colors
        )
    }
}

@Composable
private fun PreviewTileTree(
    tree: Node.Internal,
    colors: TileGridColors
) {
    tree.nodes.forEach { node ->
        when (node) {
            is Node.Internal -> PreviewTileTree(
                tree = node,
                colors = colors
            )

            is Node.Leaf -> Box(
                modifier = Modifier
                    .layoutId(node.reference.id)
                    .background(colors.tile)
            )
        }
    }
}

@Composable
internal fun TileGridLayoutEditor(
    tree: MutableTileGridLayoutTree,
    order: MutableTileGridOrder,
    modifier: Modifier = Modifier,
    isLayoutFlipped: Boolean,
    containerBounds: Rect,
    spacing: Dp,
    minimumLineTouchSize: Dp = Theme.dimensions.tile.minimumLineTouchSize
) {
    val constraintSet = tree.toEditorConstraintSet(
        flipVertical = isLayoutFlipped,
        flipHorizontal = isLayoutFlipped,
        spacing = spacing,
        minimumLineTouchSize = minimumLineTouchSize,
    )
    val thumbnailDrag = remember { mutableStateOf<ThumbnailDrag?>(null) }

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
    ) {
        val tileBounds = remember(tree.layout) { mutableStateMapOf<Node.Leaf, Rect>() }

        EditorTileTree(
            order = order,
            tree = tree.root,
            isLayoutFlipped = isLayoutFlipped,
            containerBounds = containerBounds,
            thumbnailDrag = thumbnailDrag,
            tileBounds = tileBounds
        )
    }
}

@Composable
private fun EditorTileTree(
    order: MutableTileGridOrder,
    tree: Node.Internal,
    isLayoutFlipped: Boolean,
    containerBounds: Rect,
    thumbnailDrag: MutableState<ThumbnailDrag?>,
    tileBounds: MutableMap<Node.Leaf, Rect>
) {
    tree.nodes.forEach { node ->
        when (node) {
            is Node.Internal -> {
                EditorTileTree(
                    order = order,
                    tree = node,
                    isLayoutFlipped = isLayoutFlipped,
                    containerBounds = containerBounds,
                    thumbnailDrag = thumbnailDrag,
                    tileBounds = tileBounds
                )
            }

            is Node.Leaf -> {
                val config by rememberUpdatedState(order[node.index])
                key(config.id.value) {
                    val id = thumbnailDrag.value?.let { drag ->
                        if (node.id == drag.swapNode?.id) drag.id else config.id
                    } ?: config.id
                    EditorTile(
                        uri = id.uri,
                        modifier = Modifier.layoutId(node.reference.id),
                        bounds = tileBounds.getOrPut(node) { Rect.Zero },
                        drag = thumbnailDrag.value?.let { drag ->
                            if (drag.id == config.id) drag else null
                        },
                        alignment = config.alignment,
                        contentScale = config.contentScale
                    ) { bounds ->
                        tileBounds[node] = bounds
                    }
                }

                key(node.gestureReference.id) {
                    EditorGestureArea(
                        modifier = Modifier.layoutId(node.gestureReference.id),
                        onDrag = { dragAmount ->
                            config.alignment.onPan(dragAmount)
                        },
                        onLongPressDown = { offset ->
                            thumbnailDrag.value = ThumbnailDrag(
                                id = config.id,
                                index = node.index,
                                position = mutableStateOf(offset + tileBounds.getValue(node).topLeft)
                            )
                        },
                        onLongPressUp = {
                            thumbnailDrag.value?.let { drag ->
                                val swapNode = drag.swapNode
                                if (swapNode != null && node.id != swapNode.id) {
                                    order.swap(drag.index, swapNode.index)
                                }
                            }
                            thumbnailDrag.value = null
                        },
                        onLongPressDrag = { dragAmount ->
                            thumbnailDrag.value?.let { drag ->
                                val newPosition = drag.position.value + dragAmount
                                drag.position.value = newPosition
                                val boundedPosition = newPosition.coerceIn(containerBounds)
                                val swapNode = tileBounds.firstNotNullOfOrNull { (node, bounds) ->
                                    if (bounds.contains(boundedPosition)) node else null
                                }
                                drag.swapNode = swapNode
                            }
                        },
                        onZoom = { scale ->
                            config.contentScale.onZoom(scale)
                        }
                    )
                }

                node.lineReference?.let { lineReference ->
                    key(lineReference.id) {
                        EditorLine(
                            orientation = node.orientation,
                            modifier = Modifier.layoutId(lineReference.id),
                            bounds = containerBounds
                        ) { percent ->
                            node.updatePositionBy(
                                percent = percent,
                                flipVertical = isLayoutFlipped,
                                flipHorizontal = isLayoutFlipped
                            )
                        }
                    }
                }
            }
        }
    }

    tree.lineReference?.let { lineReference ->
        key(lineReference.id) {
            EditorLine(
                orientation = when (tree.orientation) {
                    Orientation.Vertical -> Orientation.Horizontal
                    Orientation.Horizontal -> Orientation.Vertical
                },
                modifier = Modifier.layoutId(lineReference.id),
                bounds = containerBounds
            ) { percent ->
                tree.updatePositionBy(
                    percent = percent,
                    flipVertical = isLayoutFlipped,
                    flipHorizontal = isLayoutFlipped
                )
            }
        }
    }
}

@Composable
private fun EditorTile(
    uri: Uri,
    modifier: Modifier = Modifier,
    bounds: Rect,
    drag: ThumbnailDrag?,
    alignment: MutableTileAlignment,
    contentScale: MutableTileContentScale,
    onBoundsSet: (Rect) -> Unit
) {
    val onBoundsSetState = rememberUpdatedState(newValue = onBoundsSet)
    Box(
        modifier = modifier
            .applyIfNotNull(drag) { zIndex(1f) }
            .onGloballyPositioned { coordinates ->
                onBoundsSetState.value.invoke(
                    Rect(
                        offset = coordinates.positionInParent(),
                        size = coordinates.size.toSize()
                    )
                )
            }
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .applyIfNotNull(drag) { drag ->
                    absoluteOffset {
                        (drag.position.value - bounds.center).round()
                    }
                },
            alignment = alignment,
            contentScale = contentScale,
            alpha = if (drag == null) 1f else Theme.colors.secondaryAlpha
        )
    }
}

@Composable
private fun EditorGestureArea(
    modifier: Modifier = Modifier,
    onDrag: (Offset) -> Unit,
    onLongPressDown: (Offset) -> Unit,
    onLongPressUp: () -> Unit,
    onLongPressDrag: (Offset) -> Unit,
    onZoom: (Float) -> Unit
) {
    val onDragState = rememberUpdatedState(newValue = onDrag)
    val onLongPressDownState = rememberUpdatedState(newValue = onLongPressDown)
    val onLongPressUpState = rememberUpdatedState(newValue = onLongPressUp)
    val onLongPressDragState = rememberUpdatedState(newValue = onLongPressDrag)
    val onZoomState = rememberUpdatedState(newValue = onZoom)

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    onDragState.value.invoke(dragAmount)
                }
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { dragAmount ->
                        onLongPressDownState.value.invoke(dragAmount)
                    },
                    onDragEnd = { onLongPressUpState.value.invoke() },
                ) { _, dragAmount ->
                    onLongPressDragState.value.invoke(dragAmount)
                }
            }
            .transformable(
                state = rememberTransformableState { zoomChange, _, _ ->
                    // TODO: Zoom from centroid
                    // TODO: Zoom set max?
                    onZoomState.value.invoke(zoomChange)
                }
            )
    )
}

@Composable
private fun EditorLine(
    orientation: Orientation,
    modifier: Modifier = Modifier,
    bounds: Rect,
    onDrag: (Float) -> Unit
) {
    val orientationState = rememberUpdatedState(newValue = orientation)
    val boundsState = rememberUpdatedState(bounds)
    val onDragState = rememberUpdatedState(newValue = onDrag)

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    val percent = when (orientationState.value) {
                        Orientation.Horizontal -> dragAmount.x / boundsState.value.width
                        Orientation.Vertical -> dragAmount.y / boundsState.value.height
                    }
                    onDragState.value.invoke(percent)
                }
            }
    )
}

private data class ThumbnailDrag(
    val id: ThumbnailId,
    val index: Int,
    val position: MutableState<Offset>
) {

    var swapNode by mutableStateOf<Node.Leaf?>(null)
}
