package com.ai.os.agents

import com.ai.os.core.MessageBus

class PlannerAgent {

    // 🧠 Memory (experience store)
    private val history = mutableListOf<String>()

    // 🧠 Adaptive weights (decision intelligence layer)
    private val weights = mapOf(
        "build" to 1.2,
        "compile" to 1.3,
        "ui" to 1.1,
        "app" to 1.0,
        "ai" to 1.4,
        "model" to 1.4,
        "test" to 1.2,
        "deploy" to 1.3
    )

    init {
        MessageBus.subscribe("planner") { message ->
            plan(message)
        }
    }

    fun plan(task: String): String {

        history.add(task)

        val decision = evaluate(task)

        val finalPlan = "PLAN[${decision.confidence}%]: ${decision.plan}"

        MessageBus.send("executor", finalPlan)

        return finalPlan
    }

    // 🧠 TRUE Decision Engine V4 (rule + memory + adaptive scoring)
    private fun evaluate(task: String): Decision {

        val t = task.lowercase()

        var scoreBuild = 1.0
        var scoreAI = 1.0
        var scoreUI = 1.0
        var scoreGeneral = 1.0

        // weighted semantic scoring
        for ((key, weight) in weights) {
            if (key in t) {
                when (key) {
                    "build", "compile", "deploy" -> scoreBuild += weight
                    "ai", "model" -> scoreAI += weight
                    "ui", "app" -> scoreUI += weight
                    else -> scoreGeneral += weight
                }
            }
        }

        // temporal memory reinforcement (recency bias)
        val recentMatch = history.takeLast(5).count { it.contains(t.take(5), true) }
        val memoryBoost = recentMatch * 0.3
        scoreGeneral += memoryBoost

        // stability normalization
        val scores = mapOf(
            "BUILD" to scoreBuild,
            "AI" to scoreAI,
            "UI" to scoreUI,
            "GENERAL" to scoreGeneral
        )

        val best = scores.maxByOrNull { it.value }!!

        val plan = when (best.key) {
            "BUILD" -> "compile → test → package → deploy"
            "AI" -> "initialize reasoning core → load agents → optimize inference → refine model"
            "UI" -> "construct UI → bind state → render → optimize experience"
            else -> "analyze → execute → verify → optimize pipeline"
        }

        val total = scores.values.sum()
        val confidence = ((best.value / total) * 100).toInt().coerceIn(1, 100)

        return Decision(plan, confidence)
    }

    data class Decision(
        val plan: String,
        val confidence: Int
    )
}
