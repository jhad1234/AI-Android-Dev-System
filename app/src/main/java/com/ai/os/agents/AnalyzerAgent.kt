package com.ai.os.agents

import com.ai.os.core.MessageBus

class AnalyzerAgent {

    init {
        MessageBus.subscribe("analyzer") { message ->
            analyze(message)
        }
    }

    fun analyze(result: String): String {

        val feedback = when {
            result.contains("Building", true) -> "ANALYSIS: Build stage detected - OK"
            result.contains("tests", true) -> "ANALYSIS: Testing stage executed - verify results"
            result.contains("Deploying", true) -> "ANALYSIS: Deployment stage - monitor system"
            result.contains("UI", true) -> "ANALYSIS: UI initialization completed"
            result.contains("AI", true) -> "ANALYSIS: AI subsystem active"
            else -> "ANALYSIS: Task processed successfully"
        }

        MessageBus.send("planner", "FEEDBACK: $feedback")
        return feedback
    }
}
