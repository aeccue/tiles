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

internal fun gridSevenLayoutTree(
    layout: TileGrid.Layout.GridSeven,
    positions: List<Float>? = null
) = when (layout) {
    TileGrid.Layout.GridSeven.One -> layoutOneTree(positions)
    TileGrid.Layout.GridSeven.Two -> layoutTwoTree(positions)
    TileGrid.Layout.GridSeven.Three -> layoutThreeTree(positions)
    TileGrid.Layout.GridSeven.Four -> layoutFourTree(positions)
    TileGrid.Layout.GridSeven.Five -> layoutFiveTree(positions)
    TileGrid.Layout.GridSeven.Six -> layoutSixTree(positions)
    TileGrid.Layout.GridSeven.Seven -> layoutSevenTree(positions)
    TileGrid.Layout.GridSeven.Eight -> layoutEightTree(positions)
}

private fun layoutOneTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSeven.One,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addLeaf(relativePosition = positions?.get(0) ?: 0f)
        addChain(relativePosition = positions?.get(1) ?: 0.25f) {
            addLeaf(relativePosition = positions?.get(2) ?: 0f)
            addLeaf(relativePosition = positions?.get(3) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(4) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(5) ?: 0f)
            addLeaf(relativePosition = positions?.get(6) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(7) ?: 0.75f) {
            addLeaf(relativePosition = positions?.get(8) ?: 0f)
            addLeaf(relativePosition = positions?.get(9) ?: 0.5f)
        }
    }

private fun layoutTwoTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSeven.Two,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addLeaf(relativePosition = positions?.get(0) ?: 0f)
        addChain(relativePosition = positions?.get(1) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(2) ?: 0f)
            addLeaf(relativePosition = positions?.get(3) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(4) ?: (2f / 3f))
        }
        addChain(relativePosition = positions?.get(5) ?: 0.75f) {
            addLeaf(relativePosition = positions?.get(6) ?: 0f)
            addLeaf(relativePosition = positions?.get(7) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(8) ?: (2f / 3f))
        }
    }

private fun layoutThreeTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSeven.Three,
        orientation = Orientation.Horizontal,
        canFlipHorizontal = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(3) ?: (2f / 3f))
        }
        addChain(relativePosition = positions?.get(4) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(5) ?: 0f)
            addLeaf(relativePosition = positions?.get(6) ?: 0.25f)
            addLeaf(relativePosition = positions?.get(7) ?: 0.5f)
            addLeaf(relativePosition = positions?.get(8) ?: 0.75f)
        }
    }

private fun layoutFourTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSeven.Four,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: (1f / 6f))
            addLeaf(relativePosition = positions?.get(3) ?: (2f / 6f))
            addLeaf(relativePosition = positions?.get(4) ?: (3f / 6f))
            addLeaf(relativePosition = positions?.get(5) ?: (4f / 6f))
            addLeaf(relativePosition = positions?.get(6) ?: (5f / 6f))
        }
        addLeaf(relativePosition = positions?.get(7) ?: 0.2f)
    }

private fun layoutFiveTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSeven.Five,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(3) ?: (2f / 3f))
        }
        addChain(relativePosition = positions?.get(4) ?: (1f / 3f)) {
            addLeaf(relativePosition = positions?.get(5) ?: 0f)
            addLeaf(relativePosition = positions?.get(6) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(7) ?: (2f / 3f)) {
            addLeaf(relativePosition = positions?.get(8) ?: 0f)
            addLeaf(relativePosition = positions?.get(9) ?: 0.5f)
        }
    }

private fun layoutSixTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSeven.Six,
        orientation = Orientation.Horizontal,
        canFlipHorizontal = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.2f)
            addLeaf(relativePosition = positions?.get(3) ?: 0.4f)
            addLeaf(relativePosition = positions?.get(4) ?: 0.6f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.8f)
        }
        addChain(relativePosition = positions?.get(6) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(7) ?: 0f)
            addLeaf(relativePosition = positions?.get(8) ?: 0.5f)
        }
    }

private fun layoutSevenTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSeven.Seven,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(3) ?: 0.25f) {
            addLeaf(relativePosition = positions?.get(4) ?: 0f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.25f)
            addLeaf(relativePosition = positions?.get(6) ?: 0.5f)
            addLeaf(relativePosition = positions?.get(7) ?: 0.75f)
        }
        addLeaf(relativePosition = positions?.get(8) ?: 0.5f)
    }

private fun layoutEightTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSeven.Eight,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.2f)
            addLeaf(relativePosition = positions?.get(3) ?: 0.4f)
            addLeaf(relativePosition = positions?.get(4) ?: 0.6f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.8f)
        }
        addLeaf(relativePosition = positions?.get(6) ?: 0.2f)
        addLeaf(relativePosition = positions?.get(7) ?: 0.6f)
    }
