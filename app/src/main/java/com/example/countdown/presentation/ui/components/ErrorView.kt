package com.example.countdown.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorView(
    errorMessage: State<String>,
    modifier: Modifier = Modifier
) {
    val error = errorMessage.value
    AnimatedVisibility(
        visible = error.isNotEmpty(),
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 1000)
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 1000)
        ),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onError,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}