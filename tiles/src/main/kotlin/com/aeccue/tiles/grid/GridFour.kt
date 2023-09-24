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

package com.aeccue.tiles.grid

import androidx.compose.foundation.gestures.Orientation
import com.aeccue.tiles.grid.tree.tileGridLayoutTree

internal fun gridFourLayoutTree(
    layout: TileGrid.Layout.GridFour,
    positions: List<Float>? = null
) = when (layout) {
    TileGrid.Layout.GridFour.One -> layoutOneTree(positions)
    TileGrid.Layout.GridFour.Two -> layoutTwoTree(positions)
    TileGrid.Layout.GridFour.Three -> layoutThreeTree(positions)
    TileGrid.Layout.GridFour.Four -> layoutFourTree(positions)
    TileGrid.Layout.GridFour.Five -> layoutFiveTree(positions)
}

private fun layoutOneTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridFour.One,
        orientation = Orientation.Vertical
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(3) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(4) ?: 0f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.5f)
        }
    }

private fun layoutTwoTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridFour.Two,
        orientation = Orientation.Vertical
    ) {
        addLeaf(relativePosition = positions?.get(0) ?: 0f)
        addLeaf(relativePosition = positions?.get(1) ?: 0.25f)
        addLeaf(relativePosition = positions?.get(2) ?: 0.5f)
        addLeaf(relativePosition = positions?.get(3) ?: 0.75f)
    }

private fun layoutThreeTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridFour.Three,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addLeaf(relativePosition = positions?.get(0) ?: 0f)
        addLeaf(relativePosition = positions?.get(1) ?: 0.25f)
        addChain(relativePosition = positions?.get(2) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(3) ?: 0f)
            addLeaf(relativePosition = positions?.get(4) ?: 0.5f)
        }
    }

private fun layoutFourTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridFour.Four,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addLeaf(relativePosition = positions?.get(0) ?: 0f)
        addChain(relativePosition = positions?.get(1) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(2) ?: 0f)
            addLeaf(relativePosition = positions?.get(3) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(4) ?: (2f / 3f))
        }
    }

private fun layoutFiveTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridFour.Five,
        orientation = Orientation.Horizontal,
        canFlipHorizontal = true
    ) {
        addLeaf(relativePosition = positions?.get(0) ?: 0f)
        addChain(relativePosition = positions?.get(1) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(2) ?: 0f)
            addLeaf(relativePosition = positions?.get(3) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(4) ?: (2f / 3f))
        }
    }
