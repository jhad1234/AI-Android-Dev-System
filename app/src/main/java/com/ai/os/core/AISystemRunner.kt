package com.ai.os.core

import com.ai.os.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object AISystemRunner {

    private val scope = MainScope()

    fun init() {
        MessageBus.subscribe("planner") { msg -> push("PLANNER: $msg") }
        MessageBus.subscribe("executor") { msg -> push("EXECUTOR: $msg") }
        MessageBus.subscribe("analyzer") { msg -> push("ANALYZER: $msg") }
    }

    private fun push(text: String) {
        scope.launch {
            val current = MainActivity.uiLogs.value
            MainActivity.uiLogs.value = current + text + "\n"
        }
    }
}