package com.ai.os.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {

    private val _events = MutableSharedFlow<Any>(extraBufferCapacity = 100)
    val events = _events.asSharedFlow()

    suspend fun send(event: Any) {
        _events.emit(event)
    }
}