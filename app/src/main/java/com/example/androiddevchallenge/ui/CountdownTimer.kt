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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.androiddevchallenge.ui.TimerState.Paused
import com.example.androiddevchallenge.ui.TimerState.Running
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class TimerState {
    Running, Paused
}

@Composable
fun CountdownTimer(totalMinutes: Int, totalSeconds: Int, onTimerClick: () -> Unit) {
    val scope = rememberCoroutineScope()
    val previousTimerJob by remember { mutableStateOf<Job?>(null) }

    val snackbarState = remember { SnackbarHostState() }
    val minutes = remember { mutableStateOf(totalMinutes) }
    val seconds = remember { mutableStateOf(totalSeconds) }

    val totalDurationSeconds = totalTimeSeconds(minutes.value, seconds.value)
    var remainingTime by remember { mutableStateOf(totalDurationSeconds) }
    var timerState by remember { mutableStateOf(Paused) }

    fun startCounter(fromLatestValue: Boolean = false) {
        val initialValue = if (fromLatestValue) remainingTime else totalDurationSeconds
        timerState = Running
        previousTimerJob?.cancel() // Cancel previous ongoing Job when restarting - no leaking.
        scope.launch {
            val startTime = withFrameMillis { it }
            if (remainingTime == 0L) {
                remainingTime = initialValue
            }
            while (remainingTime > 0 && timerState != Paused) {
                val elapsedTime = (withFrameMillis { it } - startTime) / 1000
                remainingTime = initialValue - elapsedTime
            }
        }
    }

    fun toggleTimerState() {
        if (timerState == Running) {
            timerState = Paused
        } else {
            startCounter(fromLatestValue = true)
        }
    }

    if (remainingTime == 0L) {
        scope.launch {
            snackbarState.showSnackbar("Time! ??????", duration = SnackbarDuration.Short)
        }
    }

    DoneFeedback(snackbarState = snackbarState)

    val totalTimeSeconds = totalTimeSeconds(minutes.value, seconds.value)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Countdown(totalTimeSeconds, remainingTime, onTimerClick)
            Row {
                RoundedCornersButton(
                    icon = if (remainingTime == totalTimeSeconds) {
                        Icons.Filled.PlayArrow
                    } else {
                        Icons.Filled.Replay
                    },
                    onClick = {
                        startCounter()
                    }
                )

                RoundedCornersButton(
                    icon = if (timerState == Running) Icons.Filled.PauseCircle else Icons.Outlined.PlayCircle,
                    enabled = remainingTime != totalTimeSeconds && remainingTime != 0L,
                    onClick = {
                        toggleTimerState()
                    }
                )
            }
        }
    }
}

private fun totalTimeSeconds(minutes: Int, seconds: Int): Long = minutes * 60L + seconds
