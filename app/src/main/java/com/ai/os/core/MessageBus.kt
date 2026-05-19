package com.ai.os.core

object MessageBus {

    private val subscribers = mutableMapOf<String, (String) -> Unit>()

    fun subscribe(channel: String, handler: (String) -> Unit) {
        subscribers[channel] = handler
    }

    fun send(channel: String, message: String) {
        subscribers[channel]?.invoke(message)
    }

    fun broadcast(message: String) {
        subscribers.values.forEach { it.invoke(message) }
    }
}
