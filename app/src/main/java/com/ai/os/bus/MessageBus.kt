package com.ai.os.bus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class SystemEvent(val type: String, val payload: String)

class MessageBus {
    private val _events = MutableSharedFlow<SystemEvent>(replay = 1)
    val events: SharedFlow<SystemEvent> = _events.asSharedFlow()

    suspend fun publish(event: SystemEvent) {
        _events.emit(event)
    }
}
