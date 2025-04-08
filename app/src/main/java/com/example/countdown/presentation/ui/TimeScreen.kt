package com.example.countdown.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.countdown.presentation.ui.components.ErrorView
import com.example.countdown.presentation.ui.components.TimeInputField
import com.example.countdown.presentation.ui.components.TimerControls
import com.example.countdown.presentation.ui.components.TimerDisplay
import com.example.countdown.presentation.ui.theme.CountdownTheme
import com.example.countdown.presentation.viewModel.TimerViewModel
import kotlinx.coroutines.delay

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {
    val remainingTime by viewModel.remainingTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    var minutesInput by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            delay(3000)
            viewModel.postError("")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        ErrorView(
            errorMessage = viewModel.errorMessage.collectAsState(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TimerDisplay(
                seconds = remainingTime,
                modifier = Modifier.padding(vertical = 32.dp)
            )
            TimeInputField(
                value = minutesInput,
                onValueChange = {
                    if (it.isEmpty() || (it.toIntOrNull() ?: 0) > 0) {
                        minutesInput = it
                    }
                },
                enabled = !isRunning && remainingTime == 0,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            TimerControls(
                isRunning = isRunning,
                remainingTime = remainingTime,
                onStart = {
                    minutesInput.toIntOrNull()?.takeIf { it > 0 }?.let { minutes ->
                        viewModel.startTimer(minutes)
                        minutesInput = ""
                    }
                },
                onStop = { viewModel.stopTimer() },
                onContinue = { viewModel.continueTimer() },
                onReset = {
                    viewModel.resetTimer()
                    minutesInput = ""
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    CountdownTheme {
        TimerScreen()
    }
}