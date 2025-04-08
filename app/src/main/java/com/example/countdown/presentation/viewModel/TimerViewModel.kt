package com.example.countdown.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countdown.common.service.util.TimerServiceController
import com.example.countdown.common.utils.ErrorManager
import com.example.countdown.common.utils.EventDispatcher
import com.example.countdown.data.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: TimerRepository,
    private val timerServiceController: TimerServiceController,
    private val errorManager: ErrorManager,
    private val eventDispatcher: EventDispatcher
) : ViewModel() {

    private val _remainingTime = MutableStateFlow(0)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        setupErrorHandling()
        setupEventListening()
        loadSavedTime()
    }

    private fun loadSavedTime() {
        viewModelScope.launch {
            repository.getTimerState()?.let { seconds ->
                _remainingTime.value = seconds
            }
        }
    }

    fun startTimer(minutes: Int) {
        if (_isRunning.value) {
            return
        }

        val totalSeconds = minutes * 60
        timerServiceController.startTimer(totalSeconds)
        _isRunning.value = true
    }

    fun stopTimer() {
        timerServiceController.stopTimer()
        _isRunning.value = false
    }

    fun resetTimer() {
        viewModelScope.launch {
            repository.saveTimerState(0)
            _remainingTime.value = 0
            _isRunning.value = false
            timerServiceController.resetTimer()
        }
    }

    fun continueTimer() {
        if (_isRunning.value) {
            return
        }

        viewModelScope.launch {
            repository.getTimerState()?.takeIf { it > 0 }?.let { seconds ->
                timerServiceController.startTimer(seconds)
                _isRunning.value = true
            }
        }
    }

    private fun setupEventListening() {
        viewModelScope.launch {
            eventDispatcher.timerUpdates
                .distinctUntilChanged()
                .collect { seconds ->
                    _remainingTime.value = seconds
                    _isRunning.value = seconds > 0
                }
        }
    }


    private fun setupErrorHandling() {
        viewModelScope.launch {
            errorManager.errorMessage.collect { error ->
                error?.let {
                    _errorMessage.value = it
                }
            }
        }
    }

    fun postError(message: String) {
        viewModelScope.launch {
            errorManager.postError(message)
        }
    }
}