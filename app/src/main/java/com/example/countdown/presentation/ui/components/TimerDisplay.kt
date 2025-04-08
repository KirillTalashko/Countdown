package com.example.countdown.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countdown.common.utils.format

@Composable
fun TimerDisplay(
    seconds: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.displayLarge
) {
    val formattedTime = remember(seconds) {
        seconds.format()
    }

    AnimatedContent(
        targetState = formattedTime,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        modifier = modifier,
        label = "TimerDisplayAnimation"
    ) { time ->
        Text(
            text = time,
            style = textStyle,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(4.dp)
        )
    }
}


@Preview
@Composable
fun TimerDisplayPreview() {
    MaterialTheme {
        Column {
            TimerDisplay(seconds = 125)
            TimerDisplay(seconds = 62)
            TimerDisplay(seconds = 0)
        }
    }
}