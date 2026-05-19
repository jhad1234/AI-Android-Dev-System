package com.ai.os.core

import com.ai.os.agents.PlannerAgent
import com.ai.os.agents.ExecutorAgent
import com.ai.os.agents.AnalyzerAgent
import com.ai.os.core.MessageBus

object SystemBootstrap {

    private val planner = PlannerAgent()
    private val executor = ExecutorAgent()
    private val analyzer = AnalyzerAgent()

    fun startSystem() {
        // Kickstart AI loop
        MessageBus.send("planner", "BOOT: initialize system")
    }
}
