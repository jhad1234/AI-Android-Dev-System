package com.ai.os.core

import com.ai.os.AIEngine

object FullSystemLauncher {

    private val ai = AIEngine()

    fun launch() {
        MessageBus.send("planner", "SYSTEM ONLINE")

        val test = ai.process("status check")

        MessageBus.send("analyzer", "BOOT TEST: $test")
    }
}
