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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ConstraintSetScope

internal fun MutableTileGridLayoutTree.toConstraintSet(
    flipVertical: Boolean,
    flipHorizontal: Boolean,
    spacing: Dp
): ConstraintSet = ConstraintSet {
    root.constrain(
        scope = this,
        bounds = AnchorBounds(scope = this),
        flipVertical = flipVertical,
        flipHorizontal = flipHorizontal,
        lineMargin = spacing / 2
    )
}

@Suppress("NOTHING_TO_INLINE")
private inline fun Node.constrain(
    scope: ConstraintSetScope,
    bounds: AnchorBounds,
    flipVertical: Boolean,
    flipHorizontal: Boolean,
    lineMargin: Dp
) {
    when (this) {
        is Node.Internal -> constrain(
            scope = scope,
            bounds = bounds,
            flipVertical = flipVertical,
            flipHorizontal = flipHorizontal,
            lineMargin = lineMargin
        )
        is Node.Leaf -> constrain(
            scope = scope,
            bounds = bounds,
            lineMargin = lineMargin
        )
    }
}

private fun Node.Internal.constrain(
    scope: ConstraintSetScope,
    bounds: AnchorBounds,
    flipVertical: Boolean,
    flipHorizontal: Boolean,
    lineMargin: Dp
) {
    val flipVerticalActual = if (canFlipVertical) flipVertical else false
    val flipHorizontalActual = if (canFlipHorizontal) flipHorizontal else false
    scope.apply {
        when (orientation) {
            Orientation.Vertical -> {
                var anchorCache = if (!flipVerticalActual) bounds.top else bounds.bottom
                nodes.forEachIndexed { index, node ->
                    val childBounds = bounds.copy(
                        top = if (!flipVerticalActual) {
                            anchorCache
                        } else {
                            when (index) {
                                nodes.lastIndex -> bounds.top
                                else -> AnchorBounds.Anchor(
                                    anchor = createGuidelineFromBottom(nodes[index + 1].position.rounded)
                                )
                            }
                        },
                        bottom = if (!flipVerticalActual) {
                            when (index) {
                                nodes.lastIndex -> bounds.bottom
                                else -> AnchorBounds.Anchor(
                                    anchor = createGuidelineFromTop(nodes[index + 1].position.rounded)
                                )
                            }
                        } else {
                            anchorCache
                        }
                    )

                    node.constrain(
                        scope = scope,
                        bounds = childBounds,
                        flipVertical = flipVertical,
                        flipHorizontal = flipHorizontal,
                        lineMargin = lineMargin
                    )

                    anchorCache = if (!flipVerticalActual) childBounds.bottom else childBounds.top
                }
            }
            Orientation.Horizontal -> {
                var anchorCache = if (!flipHorizontalActual) bounds.left else bounds.right
                nodes.forEachIndexed { index, node ->
                    val childBounds = bounds.copy(
                        left = if (!flipHorizontalActual) {
                            anchorCache
                        } else {
                            when (index) {
                                nodes.lastIndex -> bounds.left
                                else -> AnchorBounds.Anchor(
                                    anchor = createGuidelineFromAbsoluteRight(nodes[index + 1].position.rounded)
                                )
                            }
                        },
                        right = if (!flipHorizontalActual) {
                            when (index) {
                                nodes.lastIndex -> bounds.right
                                else -> AnchorBounds.Anchor(
                                    anchor = createGuidelineFromAbsoluteLeft(nodes[index + 1].position.rounded)
                                )
                            }
                        } else {
                            anchorCache
                        }
                    )

                    node.constrain(
                        scope = scope,
                        bounds = childBounds,
                        flipVertical = flipVertical,
                        flipHorizontal = flipHorizontal,
                        lineMargin = lineMargin
                    )

                    anchorCache =
                        if (!flipHorizontalActual) childBounds.right else childBounds.left
                }
            }
        }
    }
}

private fun Node.Leaf.constrain(
    scope: ConstraintSetScope,
    bounds: AnchorBounds,
    lineMargin: Dp
) {
    scope.apply {
        constrainFill(reference) {
            top.linkTo(
                anchor = bounds.top.anchor,
                margin = if (bounds.top.linked) lineMargin else 0.dp
            )
            bottom.linkTo(
                anchor = bounds.bottom.anchor,
                margin = if (bounds.bottom.linked) lineMargin else 0.dp
            )
            absoluteLeft.linkTo(
                anchor = bounds.left.anchor,
                margin = if (bounds.left.linked) lineMargin else 0.dp
            )
            absoluteRight.linkTo(
                anchor = bounds.right.anchor,
                margin = if (bounds.right.linked) lineMargin else 0.dp
            )
        }
    }
}
