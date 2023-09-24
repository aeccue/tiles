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

internal fun gridEightLayoutTree(
    layout: TileGrid.Layout.GridEight,
    positions: List<Float>? = null
) = when (layout) {
    TileGrid.Layout.GridEight.One -> layoutOneTree(positions)
    TileGrid.Layout.GridEight.Two -> layoutTwoTree(positions)
    TileGrid.Layout.GridEight.Three -> layoutThreeTree(positions)
    TileGrid.Layout.GridEight.Four -> layoutFourTree(positions)
    TileGrid.Layout.GridEight.Five -> layoutFiveTree(positions)
    TileGrid.Layout.GridEight.Six -> layoutSixTree(positions)
}

private fun layoutOneTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridEight.One,
        orientation = Orientation.Vertical
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(3) ?: 0.25f) {
            addLeaf(relativePosition = positions?.get(4) ?: 0f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(6) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(7) ?: 0f)
            addLeaf(relativePosition = positions?.get(8) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(9) ?: 0.75f) {
            addLeaf(relativePosition = positions?.get(10) ?: 0f)
            addLeaf(relativePosition = positions?.get(11) ?: 0.5f)
        }
    }

private fun layoutTwoTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridEight.Two,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.25f)
            addLeaf(relativePosition = positions?.get(3) ?: 0.5f)
            addLeaf(relativePosition = positions?.get(4) ?: 0.75f)
        }
        addLeaf(relativePosition = positions?.get(5) ?: 0.25f)
        addChain(relativePosition = positions?.get(6) ?: 0.75f) {
            addLeaf(relativePosition = positions?.get(7) ?: 0f)
            addLeaf(relativePosition = positions?.get(8) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(9) ?: (2f / 3f))
        }
    }

private fun layoutThreeTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridEight.Three,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.25f)
            addLeaf(relativePosition = positions?.get(3) ?: 0.5f)
            addLeaf(relativePosition = positions?.get(4) ?: 0.75f)
        }
        addChain(relativePosition = positions?.get(5) ?: 0.2f) {
            addLeaf(relativePosition = positions?.get(6) ?: 0f)
            addLeaf(relativePosition = positions?.get(7) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(8) ?: 0.6f) {
            addLeaf(relativePosition = positions?.get(9) ?: 0f)
            addLeaf(relativePosition = positions?.get(10) ?: 0.5f)
        }

    }

private fun layoutFourTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridEight.Four,
        orientation = Orientation.Vertical
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(3) ?: (2f / 3f))
        }
        addChain(relativePosition = positions?.get(4) ?: 0.25f) {
            addLeaf(relativePosition = positions?.get(5) ?: 0f)
            addLeaf(relativePosition = positions?.get(6) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(7) ?: 0.75f) {
            addLeaf(relativePosition = positions?.get(8) ?: 0f)
            addLeaf(relativePosition = positions?.get(9) ?: (1f / 3f))
            addLeaf(relativePosition = positions?.get(10) ?: (2f / 3f))
        }
    }

private fun layoutFiveTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridEight.Five,
        orientation = Orientation.Vertical,
        canFlipVertical = true
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.25f)
            addLeaf(relativePosition = positions?.get(3) ?: 0.5f)
            addLeaf(relativePosition = positions?.get(4) ?: 0.75f)
        }
        addChain(relativePosition = positions?.get(5) ?: 0.2f) {
            addLeaf(relativePosition = positions?.get(6) ?: 0f)
            addLeaf(relativePosition = positions?.get(7) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(8) ?: 0.6f) {
            addLeaf(relativePosition = positions?.get(9) ?: 0f)
            addLeaf(relativePosition = positions?.get(10) ?: 0.5f)
        }
    }

private fun layoutSixTree(positions: List<Float>?) =
    tileGridLayoutTree(
        layout = TileGrid.Layout.GridEight.Six,
        orientation = Orientation.Horizontal
    ) {
        addChain(relativePosition = positions?.get(0) ?: 0f) {
            addLeaf(relativePosition = positions?.get(1) ?: 0f)
            addLeaf(relativePosition = positions?.get(2) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(3) ?: 0.25f) {
            addLeaf(relativePosition = positions?.get(4) ?: 0f)
            addLeaf(relativePosition = positions?.get(5) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(6) ?: 0.5f) {
            addLeaf(relativePosition = positions?.get(7) ?: 0f)
            addLeaf(relativePosition = positions?.get(8) ?: 0.5f)
        }
        addChain(relativePosition = positions?.get(9) ?: 0.75f) {
            addLeaf(relativePosition = positions?.get(10) ?: 0f)
            addLeaf(relativePosition = positions?.get(11) ?: 0.5f)
        }
    }
