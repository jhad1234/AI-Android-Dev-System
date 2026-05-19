package com.ai.os.agents

import com.ai.os.core.MessageBus

class ExecutorAgent {

    init {
        MessageBus.subscribe("executor") { message ->
            execute(message)
        }
    }

    fun execute(plan: String): String {
        val result = when {
            plan.contains("compile", true) -> "EXEC: Building project..."
            plan.contains("test", true) -> "EXEC: Running tests..."
            plan.contains("deploy", true) -> "EXEC: Deploying application..."
            plan.contains("UI", true) -> "EXEC: Initializing UI components..."
            plan.contains("AI", true) -> "EXEC: Starting AI core systems..."
            else -> "EXEC: Processing task..."
        }

        MessageBus.send("analyzer", result)
        return result
    }
}
