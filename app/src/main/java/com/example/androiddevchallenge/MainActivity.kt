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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.CountdownScreen
import com.example.androiddevchallenge.ui.InsertTimeScreen
import com.example.androiddevchallenge.ui.InsertedTimeState
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }

    suspend fun startTimer(isRunning: Boolean, initialValue: Int): Int {
        return if (isRunning) {
            delay(1000)
            initialValue + 1
        } else {
            0
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val insertedTimeState = remember {
        mutableStateOf(InsertedTimeState(0, 30, false))
    }

    Surface(color = MaterialTheme.colors.background) {
        Box(Modifier.padding(8.dp)) {
            Crossfade(targetState = insertedTimeState.value.showDialog) { showDialog ->
                if (showDialog) {
                    InsertTimeScreen(insertedTimeState)
                } else {
                    CountdownScreen(insertedTimeState)
                }
            }
        }
    }
}


fun btnText(isStart: Boolean): String = if (isStart) "Pause" else "Start"

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
