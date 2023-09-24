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
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintSetScope
import androidx.constraintlayout.compose.Dimension
import com.aeccue.tiles.grid.gridEightLayoutTree
import com.aeccue.tiles.grid.gridFiveLayoutTree
import com.aeccue.tiles.grid.gridFourLayoutTree
import com.aeccue.tiles.grid.gridNineLayoutTree
import com.aeccue.tiles.grid.gridSevenLayoutTree
import com.aeccue.tiles.grid.gridSixLayoutTree
import com.aeccue.tiles.grid.gridThreeLayoutTree
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

fun tileGridLayoutTree(
    layout: TileGrid.Layout,
    positions: List<Float>? = null
) = when (layout) {
    is TileGrid.Layout.GridThree -> gridThreeLayoutTree(layout = layout, positions = positions)
    is TileGrid.Layout.GridFour -> gridFourLayoutTree(layout = layout, positions = positions)
    is TileGrid.Layout.GridFive -> gridFiveLayoutTree(layout = layout, positions = positions)
    is TileGrid.Layout.GridSix -> gridSixLayoutTree(layout = layout, positions = positions)
    is TileGrid.Layout.GridSeven -> gridSevenLayoutTree(layout = layout, positions = positions)
    is TileGrid.Layout.GridEight -> gridEightLayoutTree(layout = layout, positions = positions)
    is TileGrid.Layout.GridNine -> gridNineLayoutTree(layout = layout, positions = positions)
}

internal fun tileGridLayoutTree(
    layout: TileGrid.Layout,
    orientation: Orientation,
    canFlipVertical: Boolean = false,
    canFlipHorizontal: Boolean = false,
    treeBuilder: MutableTileGridLayoutTree.Scope.() -> Unit
): MutableTileGridLayoutTree {
    val scope = TileGridLayoutTreeScope(
        layout = layout,
        baseId = "root",
        orientation = orientation,
        canFlipVertical = canFlipVertical,
        canFlipHorizontal = canFlipHorizontal
    )

    treeBuilder.invoke(scope)

    return scope.toMutableTree()
}

// TODO: Write immutable tree as well
class MutableTileGridLayoutTree internal constructor(
    val layout: TileGrid.Layout,
    internal val root: Node.Internal
) {

    interface Scope {

        fun addChain(relativePosition: Float, chainBuilder: Scope.() -> Unit)
        fun addLeaf(relativePosition: Float)
        fun toMutableTree(): MutableTileGridLayoutTree
    }

    fun toList(): List<Float> {
        val list = mutableListOf<Float>()
        root.addToList(list)
        return list.toList()
    }

    private fun Node.Internal.addToList(list: MutableList<Float>) {
        nodes.forEach { node ->
            list.add(node.position)
            if (node is Node.Internal) node.addToList(list)
        }
    }
}


private class TileGridLayoutTreeScope(
    val layout: TileGrid.Layout,
    val baseId: Any,
    val orientation: Orientation,
    private val leafCounter: AtomicInteger = AtomicInteger(),
    private val canFlipVertical: Boolean,
    private val canFlipHorizontal: Boolean
) : MutableTileGridLayoutTree.Scope {

    private sealed class Child(
        val id: Any,
        val orientation: Orientation,
        val relativePosition: Float,
    ) {

        abstract fun toNode(
            bounds: Bounds,
            canFlipVertical: Boolean,
            canFlipHorizontal: Boolean
        ): Node

        class Chain(
            id: Any,
            orientation: Orientation,
            relativePosition: Float,
            val children: List<Child>
        ) : Child(
            id = id,
            orientation = orientation,
            relativePosition = relativePosition
        ) {

            override fun toNode(
                bounds: Bounds,
                canFlipVertical: Boolean,
                canFlipHorizontal: Boolean
            ): Node.Internal {
                val nodes = mutableListOf<Node>()

                var startEdgeCache = when (orientation) {
                    Orientation.Vertical -> bounds.verticalStart
                    Orientation.Horizontal -> bounds.horizontalStart
                }

                children.forEachIndexed { index, child ->
                    val childBounds = when (orientation) {
                        Orientation.Vertical -> bounds.copy(
                            verticalStart = startEdgeCache,
                            verticalEnd = when (index) {
                                children.lastIndex -> bounds.verticalEnd
                                else -> Bounds.Edge(
                                    position = bounds.verticalStart.position + bounds.height * children[index + 1].relativePosition
                                )
                            },
                            hasLine = index != 0
                        )
                        Orientation.Horizontal -> bounds.copy(
                            horizontalStart = startEdgeCache,
                            horizontalEnd = when (index) {
                                children.lastIndex -> bounds.horizontalEnd
                                else -> Bounds.Edge(
                                    position = bounds.horizontalStart.position + bounds.width * children[index + 1].relativePosition
                                )
                            },
                            hasLine = index != 0
                        )
                    }


                    val node = child.toNode(
                        bounds = childBounds,
                        canFlipVertical = canFlipVertical,
                        canFlipHorizontal = canFlipHorizontal
                    )
                    nodes.add(node)

                    startEdgeCache = when (orientation) {
                        Orientation.Vertical -> childBounds.verticalEnd
                        Orientation.Horizontal -> childBounds.horizontalEnd
                    }
                }

                return Node.Internal(
                    id = id,
                    orientation = orientation,
                    bounds = bounds,
                    nodes = nodes,
                    positionState = when (orientation) {
                        Orientation.Vertical -> bounds.horizontalStart.positionState
                        Orientation.Horizontal -> bounds.verticalStart.positionState
                    },
                    canFlipVertical = canFlipVertical,
                    canFlipHorizontal = canFlipHorizontal
                ).also { newNode ->
                    when (orientation) {
                        Orientation.Vertical -> {
                            bounds.horizontalStart.prev?.bounds?.let { prevBounds ->
                                if (prevBounds.horizontalStart.next == null) {
                                    prevBounds.horizontalStart.next = newNode
                                }
                            }
                            if (bounds.horizontalEnd.prev == null) {
                                bounds.horizontalEnd.prev = newNode
                            }
                        }
                        Orientation.Horizontal -> {
                            bounds.verticalStart.prev?.bounds?.let { prevBounds ->
                                if (prevBounds.verticalStart.next == null) {
                                    prevBounds.verticalStart.next = newNode
                                }
                            }
                            if (bounds.verticalEnd.prev == null) {
                                bounds.verticalEnd.prev = newNode
                            }
                        }
                    }
                }
            }
        }

        class Leaf(
            id: Any,
            orientation: Orientation,
            relativePosition: Float,
            val index: Int
        ) : Child(
            id = id,
            orientation = orientation,
            relativePosition = relativePosition
        ) {

            override fun toNode(
                bounds: Bounds,
                canFlipVertical: Boolean,
                canFlipHorizontal: Boolean
            ): Node.Leaf = Node.Leaf(
                id = id,
                orientation = orientation,
                bounds = bounds,
                index = index,
                positionState = when (orientation) {
                    Orientation.Vertical -> bounds.verticalStart.positionState
                    Orientation.Horizontal -> bounds.horizontalStart.positionState
                },
                canFlipVertical = canFlipVertical,
                canFlipHorizontal = canFlipHorizontal
            ).also { newNode ->
                when (orientation) {
                    Orientation.Vertical -> {
                        bounds.verticalStart.prev?.bounds?.let { prevBounds ->
                            prevBounds.verticalStart.next =
                                prevBounds.verticalStart.next?.let { currentNext ->
                                    if (currentNext.bounds.verticalStart.position > bounds.verticalStart.position) {
                                        newNode
                                    } else {
                                        currentNext
                                    }
                                } ?: newNode
                        }

                        bounds.verticalEnd.prev = bounds.verticalEnd.prev?.let { currentPrev ->
                            if (currentPrev.bounds.verticalStart.position < bounds.verticalStart.position) {
                                newNode
                            } else {
                                currentPrev
                            }
                        } ?: newNode
                    }
                    Orientation.Horizontal -> {
                        bounds.horizontalStart.prev?.bounds?.let { prevBounds ->
                            prevBounds.horizontalStart.next =
                                prevBounds.horizontalStart.next?.let { currentNext ->
                                    if (currentNext.bounds.horizontalStart.position > bounds.horizontalStart.position) {
                                        newNode
                                    } else {
                                        currentNext
                                    }
                                } ?: newNode
                        }

                        bounds.horizontalEnd.prev = bounds.horizontalEnd.prev?.let { currentPrev ->
                            if (currentPrev.bounds.horizontalStart.position < bounds.horizontalStart.position) {
                                newNode
                            } else {
                                currentPrev
                            }
                        } ?: newNode
                    }
                }
            }
        }
    }


    private val children = mutableListOf<Child>()

    override fun addChain(
        relativePosition: Float,
        chainBuilder: MutableTileGridLayoutTree.Scope.() -> Unit
    ) {
        val scope = TileGridLayoutTreeScope(
            layout = layout,
            baseId = "$baseId:${children.size}",
            orientation = when (orientation) {
                Orientation.Vertical -> Orientation.Horizontal
                Orientation.Horizontal -> Orientation.Vertical
            },
            leafCounter = leafCounter,
            canFlipVertical = canFlipVertical,
            canFlipHorizontal = canFlipHorizontal
        )
        chainBuilder.invoke(scope)

        val chain = Child.Chain(
            id = scope.baseId,
            orientation = scope.orientation,
            relativePosition = relativePosition,
            children = scope.children
        )

        children.add(chain)
    }

    override fun addLeaf(relativePosition: Float) {
        val leaf = Child.Leaf(
            id = "$baseId:${children.size}",
            orientation = orientation,
            relativePosition = relativePosition,
            index = leafCounter.getAndIncrement(),
        )

        children.add(leaf)
    }

    override fun toMutableTree(): MutableTileGridLayoutTree {
        val rootBounds = Bounds(
            verticalStart = Bounds.Edge(position = 0f),
            verticalEnd = Bounds.Edge(position = 1f),
            horizontalStart = Bounds.Edge(position = 0f),
            horizontalEnd = Bounds.Edge(position = 1f),
            hasLine = false
        )
        val root = Child.Chain(
            id = "root",
            orientation = orientation,
            relativePosition = 0f,
            children = children
        ).toNode(
            bounds = rootBounds,
            canFlipVertical = canFlipVertical,
            canFlipHorizontal = canFlipHorizontal
        )

        return MutableTileGridLayoutTree(
            layout = layout,
            root = root
        )
    }
}


internal inline val Float.rounded: Float
    get() = (this * 100).roundToInt() / 100f

internal fun ConstraintSetScope.constrainFill(
    ref: ConstrainedLayoutReference,
    constrainBlock: ConstrainScope.() -> Unit
) = constrain(ref) {
    width = Dimension.fillToConstraints
    height = Dimension.fillToConstraints

    constrainBlock()
}
