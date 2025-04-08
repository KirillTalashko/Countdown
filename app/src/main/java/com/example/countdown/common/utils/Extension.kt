package com.example.countdown.common.utils

import java.util.Locale

fun Int.format(): String {
    val minutes = this / 60
    val secs = this % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, secs)
}

fun Int.minutesDeclension(): String {
    return when {
        this % 10 == 1 && this % 100 != 11 -> "минута"
        this % 10 in 2..4 && this % 100 !in 12..14 -> "минуты"
        else -> "минут"
    }
}
