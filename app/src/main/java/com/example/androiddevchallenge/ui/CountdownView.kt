/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R

@Composable
fun CountdownScreen(insertedTimeState: MutableState<InsertedTimeState>) {
    Card(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.hint)
        )
    }

    CountdownTimer(
        insertedTimeState.value.minutes,
        insertedTimeState.value.seconds
    ) { insertedTimeState.value = insertedTimeState.value.copy(showDialog = true) }
}
