package com.ai.os.agents

import com.ai.os.core.MessageBus

class PlannerAgent {

    init {
        MessageBus.subscribe("planner") { message ->
            plan(message)
        }
    }

    fun plan(task: String): String {

        val decision = evaluate(task)

        val finalPlan = "PLAN[${decision.confidence}]: ${decision.plan}"

        MessageBus.send("executor", finalPlan)

        return finalPlan
    }

    // 🧠 Decision Engine Core (V2)
    private fun evaluate(task: String): Decision {

        val t = task.lowercase()

        var scoreBuild = 0
        var scoreAI = 0
        var scoreUI = 0
        var scoreGeneral = 1

        if ("build" in t || "compile" in t) scoreBuild += 3
        if ("app" in t || "ui" in t) scoreUI += 2
        if ("ai" in t || "model" in t) scoreAI += 3
        if ("test" in t) scoreGeneral += 1
        if ("deploy" in t) scoreBuild += 2

        val best = listOf(
            "BUILD" to scoreBuild,
            "AI" to scoreAI,
            "UI" to scoreUI,
            "GENERAL" to scoreGeneral
        ).maxByOrNull { it.second }!!

        val plan = when (best.first) {
            "BUILD" -> "compile → test → package → deploy"
            "AI" -> "initialize AI core → load agents → optimize reasoning"
            "UI" -> "build interface → bind state → render output"
            else -> "analyze → execute → verify → optimize"
        }

        val confidence = best.second * 25

        return Decision(plan, confidence)
    }

    data class Decision(
        val plan: String,
        val confidence: Int
    )
}
