package com.ai.os.agents

import com.ai.os.core.MessageBus

class ExecutorAgent {

    private var lastPlan: String = ""

    init {
        MessageBus.subscribe("executor") { message ->
            execute(message)
        }
    }

    fun execute(plan: String): String {

        lastPlan = plan

        val actions = plan.lowercase()
            .replace("plan", "")
            .replace("[", "")
            .replace("]", "")
            .split("→", ",", ":")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val results = mutableListOf<String>()

        for (action in actions) {
            val result = runAction(action)
            results.add(result)

            // live feedback
            MessageBus.send("analyzer", "EXEC_STEP: $action -> $result")
        }

        val summary = results.joinToString("|")

        MessageBus.send("analyzer", "EXECUTION_SUMMARY: $summary")

        return summary
    }

    private fun runAction(action: String): String {
        return when {
            "compile" in action -> "build_ok"
            "test" in action -> "tests_ok"
            "package" in action -> "packaged"
            "deploy" in action -> "deployed"
            "ui" in action -> "ui_ready"
            "ai" in action -> "ai_core_started"
            "analyze" in action -> "analysis_done"
            else -> "done"
        }
    }
}
