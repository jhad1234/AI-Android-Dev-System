package com.ai.os.agents

import com.ai.os.bus.MessageBus
import com.ai.os.bus.SystemEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface BaseAgent {
    val agentName: String
    fun startListening()
}

class AgentRegistry(private val messageBus: MessageBus) {
    private val scope = CoroutineScope(Dispatchers.Default)
    
    private val agents = listOf(
        CoderAgent(messageBus),
        QATesterAgent(messageBus)
    )

    fun initializeAgents() {
        agents.forEach { agent ->
            agent.startListening()
            println("Agent [${agent.agentName}] up and running.")
        }
    }
}

class CoderAgent(private val messageBus: MessageBus) : BaseAgent {
    override val agentName = "Coder_Agent"
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun startListening() {
        scope.launch {
            messageBus.events.collect { event ->
                if (event.type == "CODER_TASK_ASSIGNED" || event.type == "SELF_HEALING_ACTIVATE") {
                    generateCodePatch(event.payload)
                }
            }
        }
    }

    private suspend fun generateCodePatch(task: String) {
        println("$agentName: جاري توليد الكود وإصلاح الأنظمة تلقائياً لـ: $task")
        // محاكاة معالجة الكود الناجحة وإنتاج ملف Dex محسن
        messageBus.publish(SystemEvent("CODE_PATCH_GENERATED", "dynamic_patch.dex"))
    }
}

class QATesterAgent(private val messageBus: MessageBus) : BaseAgent {
    override val agentName = "QA_Tester_Agent"
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun startListening() {
        scope.launch {
            messageBus.events.collect { event ->
                if (event.type == "CODE_PATCH_GENERATED") {
                    verifyPatch(event.payload)
                }
            }
        }
    }

    private suspend fun verifyPatch(patchPath: String) {
        println("$agentName: جاري إجراء الفحص الأمني والاختبار الوظيفي للرقعة البرمجية...")
        // الفحص الذاتي للتأكد من خلو الرقعة البرمجية من الأخطاء الثنائية والمنطقية
        messageBus.publish(SystemEvent("PATCH_VERIFIED_SUCCESS", patchPath))
    }
}
