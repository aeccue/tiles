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

internal fun gridSixLayoutTree(
    layout: TileGrid.Layout.GridSix,
    positions: List<Float>? = null
) = when (layout) {
    TileGrid.Layout.GridSix.One -> layoutOneTree(positions)
    TileGrid.Layout.GridSix.Two -> layoutTwoTree(positions)
    TileGrid.Layout.GridSix.Three -> layoutThreeTree(positions)
    TileGrid.Layout.GridSix.Four -> layoutFourTree(positions)
    TileGrid.Layout.GridSix.Five -> layoutFiveTree(positions)
    TileGrid.Layout.GridSix.Six -> layoutSixTree(positions)
    TileGrid.Layout.GridSix.Seven -> layoutSevenTree(positions)
}

private fun layoutOneTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSix.One,
        orientation = Orientation.Vertical
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(3) ?: (1f / 3f)) {
            addLeaf(relativePosition = positions?.get(4) ?: 0f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(6) ?: (2f / 3f)) {
            addLeaf(relativePosition = positions?.get(7) ?: 0f)
            addLeaf(relativePosition = positions?.get(8) ?: 0.5f)
        }
    }

private fun layoutTwoTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSix.Two,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addLeaf(relativePosition = positions?.get(0) ?: 0f)
        addLeaf(relativePosition = positions?.get(1) ?: 0.25f)
        addChain(relativePosition = positions?.get(2) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(3) ?: 0f)
            addLeaf(relativePosition = positions?.get(4) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(5) ?: 0.75f) {
            addLeaf(relativePosition = positions?.get(6) ?: 0f)
            addLeaf(relativePosition = positions?.get(7) ?: 0.5f)
        }
    }


private fun layoutThreeTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSix.Three,
        orientation = Orientation.Horizontal
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(3) ?: (1f / 3f)) {
            addLeaf(relativePosition = positions?.get(4) ?: 0f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(6) ?: (2f / 3f)) {
            addLeaf(relativePosition = positions?.get(7) ?: 0f)
            addLeaf(relativePosition = positions?.get(8) ?: 0.5f)
        }
    }

private fun layoutFourTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSix.Four,
        orientation = Orientation.Vertical,
        canFlipHorizontal = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addChain(relativePosition = positions?.get(2) ?: 0.5f) {
                addLeaf(relativePosition = positions?.get(3) ?: 0f)
                addLeaf(relativePosition = positions?.get(4) ?: 0.5f)
            }
        }
        addChain(relativePosition = positions?.get(5) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(6) ?: 0f)
            addChain(relativePosition = positions?.get(7) ?: 0.5f) {
                addLeaf(relativePosition = positions?.get(8) ?: 0f)
                addLeaf(relativePosition = positions?.get(9) ?: 0.5f)
            }
        }
    }

private fun layoutFiveTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSix.Five,
        orientation = Orientation.Horizontal,
        canFlipHorizontal = true
    ) {
        addLeaf(relativePosition = positions?.get(0) ?: 0f)
        addChain(relativePosition = positions?.get(1) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(2) ?: 0f)
            addLeaf(relativePosition = positions?.get(3) ?: 0.2f)
            addLeaf(relativePosition = positions?.get(4) ?: 0.4f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.6f)
            addLeaf(relativePosition = positions?.get(6) ?: 0.8f)
        }
    }

private fun layoutSixTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSix.Six,
        orientation = Orientation.Vertical,
        canFlipVertical = true,
        canFlipHorizontal = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(3) ?: (2f / 3f))
        }
        addChain(relativePosition = positions?.get(4) ?: (1f / 3f)) {
            addLeaf(relativePosition = positions?.get(5) ?: 0f)
            addChain(relativePosition = positions?.get(6) ?: (2f / 3f)) {
                addLeaf(relativePosition = positions?.get(7) ?: 0f)
                addLeaf(relativePosition = positions?.get(8) ?: 0.5f)
            }
        }
    }

private fun layoutSevenTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridSix.Seven,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.5f)
        }
        addLeaf(relativePosition = positions?.get(3) ?: (1f / 3f))
        addChain(relativePosition = positions?.get(4) ?: (2f / 3f)) {
            addLeaf(relativePosition = positions?.get(5) ?: 0f)
            addLeaf(relativePosition = positions?.get(6) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(7) ?: (2f / 3f))
        }
    }
