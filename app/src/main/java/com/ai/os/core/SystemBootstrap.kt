package com.ai.os.core

import com.ai.os.agents.PlannerAgent
import com.ai.os.agents.ExecutorAgent
import com.ai.os.agents.AnalyzerAgent

object SystemBootstrap {

    private val planner = PlannerAgent()
    private val executor = ExecutorAgent()
    private val analyzer = AnalyzerAgent()

    fun startSystem() {

        // تشغيل أولي للنظام
        MessageBus.send("planner", "BOOT: initialize system")

        // اختبار تشغيل فعلي للنظام
        MessageBus.send("executor", "BOOT: executor ready")
        MessageBus.send("analyzer", "BOOT: analyzer ready")
    }
}
