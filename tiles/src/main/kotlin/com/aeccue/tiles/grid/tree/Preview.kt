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

internal fun MutableTileGridLayoutTree.toPreviewConstraintSet(
    spacing: Dp
): ConstraintSet = ConstraintSet {
    root.constrain(
        scope = this,
        bounds = AnchorBounds(scope = this),
        lineMargin = spacing / 2
    )
}

@Suppress("NOTHING_TO_INLINE")
private inline fun Node.constrain(
    scope: ConstraintSetScope,
    bounds: AnchorBounds,
    lineMargin: Dp
) {
    when (this) {
        is Node.Internal -> constrain(
            scope = scope,
            bounds = bounds,
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
    lineMargin: Dp
) {
    scope.apply {
        when (orientation) {
            Orientation.Vertical -> {
                var anchorCache = bounds.top
                nodes.forEachIndexed { index, node ->
                    val childBounds = bounds.copy(
                        top = anchorCache,
                        bottom = when (index) {
                            nodes.lastIndex -> bounds.bottom
                            else -> AnchorBounds.Anchor(
                                anchor = createGuidelineFromTop(nodes[index + 1].position.rounded)
                            )
                        }
                    )

                    node.constrain(
                        scope = scope,
                        bounds = childBounds,
                        lineMargin = lineMargin
                    )

                    anchorCache = childBounds.bottom
                }
            }
            Orientation.Horizontal -> {
                var anchorCache = bounds.left
                nodes.forEachIndexed { index, node ->
                    val childBounds = bounds.copy(
                        left = anchorCache,
                        right = when (index) {
                            nodes.lastIndex -> bounds.right
                            else -> AnchorBounds.Anchor(
                                anchor = createGuidelineFromAbsoluteLeft(nodes[index + 1].position.rounded)
                            )
                        }
                    )

                    node.constrain(
                        scope = scope,
                        bounds = childBounds,
                        lineMargin = lineMargin
                    )

                    anchorCache = childBounds.right
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
