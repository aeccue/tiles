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

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintSetScope

internal data class Bounds(
    val verticalStart: Edge,
    val verticalEnd: Edge,
    val horizontalStart: Edge,
    val horizontalEnd: Edge,
    val hasLine: Boolean
) {

    val height: Float get() = verticalEnd.position - verticalStart.position
    val width: Float get() = horizontalEnd.position - horizontalStart.position

    data class Edge(
        val position: Float,
        var prev: Node? = null,
        var next: Node? = null
    ) {

        val positionState = mutableStateOf(position)
    }
}

internal data class AnchorBounds(
    val top: Anchor<ConstraintLayoutBaseScope.HorizontalAnchor>,
    val bottom: Anchor<ConstraintLayoutBaseScope.HorizontalAnchor>,
    val left: Anchor<ConstraintLayoutBaseScope.VerticalAnchor>,
    val right: Anchor<ConstraintLayoutBaseScope.VerticalAnchor>
) {

    constructor(scope: ConstraintSetScope) : this(
        top = Anchor(scope.createGuidelineFromTop(0f), false),
        bottom = Anchor(scope.createGuidelineFromBottom(0f), false),
        left = Anchor(scope.createGuidelineFromAbsoluteLeft(0f), false),
        right = Anchor(scope.createGuidelineFromAbsoluteRight(0f), false)
    )

    class Anchor<A>(
        val anchor: A,
        val linked: Boolean = true
    )
}

internal data class RectBounds(
    val top: Edge,
    val bottom: Edge,
    val left: Edge,
    val right: Edge
) {

    constructor(size: Size) : this(
        top = Edge(0f, false),
        bottom = Edge(size.height, false),
        left = Edge(0f, false),
        right = Edge(size.width, false)
    )

    class Edge(
        val value: Float,
        val linked: Boolean = true
    )

    val width = right.value - left.value
    val height = bottom.value - top.value
}
