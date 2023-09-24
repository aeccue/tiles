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

package com.aeccue.tiles.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun Media(
    source: Media,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    when (source) {
        is Media.Color.Solid -> Box(
            modifier = modifier.background(Color(source.value))
        )
        is Media.Image -> {
            val model = source.coilModel
            if (model == null) {
                Box(modifier = modifier.background(Color.Transparent))
            } else {
                AsyncImage(
                    model = model,
                    contentDescription = null,
                    modifier = modifier,
                    alignment = alignment,
                    contentScale = contentScale
                )
            }
        }
    }
}
