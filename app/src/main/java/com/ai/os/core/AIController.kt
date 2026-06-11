package com.ai.os.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Central brain of the system
 * Responsible for routing events and coordinating agents
 */
class AIController(
    private val scope: CoroutineScope
) {

    init {
        observeEvents()
    }

    private fun observeEvents() {
        scope.launch(Dispatchers.Default) {
            EventBus.events.collect { event ->
                when (event) {
                    is AIEvent.TaskCreated -> handleTask(event)
                    is AIEvent.AgentMessage -> routeMessage(event)
                    is AIEvent.SystemError -> handleError(event)
                    is AIEvent.TaskCompleted -> handleCompletion(event)
                }
            }
        }
    }

    private fun handleTask(event: AIEvent.TaskCreated) {
        println("[AIController] Task received: ${event.id}")
    }

    private fun routeMessage(event: AIEvent.AgentMessage) {
        println("[AIController] Routing message from ${event.from} to ${event.to}")
    }

    private fun handleError(event: AIEvent.SystemError) {
        println("[AIController] Error from ${event.source}: ${event.error}")
    }

    private fun handleCompletion(event: AIEvent.TaskCompleted) {
        println("[AIController] Task completed: ${event.id}")
    }
}