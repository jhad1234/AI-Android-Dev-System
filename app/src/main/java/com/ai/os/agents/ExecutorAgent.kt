package com.ai.os.agents

import com.ai.os.core.MessageBus

class ExecutorAgent {

    init {
        MessageBus.subscribe("executor") { message ->
            execute(message)
        }
    }

    fun execute(plan: String): String {

        val actions = plan.lowercase()
            .replace("plan", "")
            .replace("[", "")
            .replace("]", "")
            .split("→", ",", ":")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val results = actions.map { action ->
            when {
                "compile" in action -> "build_ok"
                "test" in action -> "tests_ok"
                "package" in action -> "packaged"
                "deploy" in action -> "deployed"
                "ui" in action -> "ui_ready"
                "analyze" in action -> "analysis_done"
                else -> "done"
            }
        }

        val summary = results.joinToString("|")

        MessageBus.send("analyzer", summary)

        return summary
    }
}
