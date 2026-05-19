package com.ai.os.agents

import com.ai.os.core.MessageBus

class PlannerAgent {

    init {
        MessageBus.subscribe("planner") { message ->
            plan(message)
        }
    }

    fun plan(task: String): String {
        val plan = when {
            task.contains("build", true) -> "PLAN: compile -> test -> deploy"
            task.contains("app", true) -> "PLAN: initialize UI -> setup logic -> run"
            task.contains("ai", true) -> "PLAN: initialize AI core -> load agents -> activate bus"
            else -> "PLAN: analyze -> execute -> verify"
        }

        MessageBus.send("executor", plan)
        return plan
    }
}
