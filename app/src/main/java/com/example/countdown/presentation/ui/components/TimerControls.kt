package com.example.countdown.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countdown.R

@Composable
fun TimerControls(
    isRunning: Boolean,
    remainingTime: Int,
    onStart: () -> Unit,
    onContinue: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            !isRunning && remainingTime == 0 -> {
                Button(
                    onClick = onStart,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(stringResource(R.string.start_button_label))
                }
            }

            isRunning -> {
                Button(
                    onClick = onStop,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(stringResource(R.string.pause_button_label))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onReset,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(stringResource(R.string.reset_button_label))
                }
            }

            !isRunning && remainingTime > 0 -> {
                Button(
                    onClick = onContinue,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(stringResource(R.string.continue_button_label))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onReset,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(stringResource(R.string.reset_button_label))
                }
            }
        }
    }
}

@Preview
@Composable
fun TimerControlsPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // When the timer is not running and there is no time (timer is not active)
            TimerControls(
                isRunning = false,
                remainingTime = 0,
                onStart = {},
                onStop = {},
                onReset = {},
                onContinue = {}
            )

            // When the timer is running (in progress)
            TimerControls(
                isRunning = true,
                remainingTime = 120,
                onStart = {},
                onStop = {},
                onReset = {},
                onContinue = {}
            )

            // When the timer is not started, but there is time remaining (paused)
            TimerControls(
                isRunning = false,
                remainingTime = 150,
                onStart = {},
                onStop = {},
                onReset = {},
                onContinue = {}
            )
        }
    }
}