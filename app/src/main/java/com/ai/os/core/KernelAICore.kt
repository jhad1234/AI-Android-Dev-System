package com.ai.os.core

import com.ai.os.bus.MessageBus
import com.ai.os.bus.SystemEvent
import com.ai.os.agents.AgentRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KernelAICore(
    private val messageBus: MessageBus,
    private val agentRegistry: AgentRegistry
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        listenToSystemEvents()
    }

    fun executeUserPrompt(prompt: String) {
        scope.launch {
            // 1. نظام الحوكمة الذكي (AI Governor Policy Check)
            if (AIGovernor.validatePolicy(prompt)) {
                messageBus.publish(SystemEvent("USER_COMMAND_ACCEPTED", prompt))
                decomposeAndDelegate(prompt)
            } else {
                messageBus.publish(SystemEvent("GOVERNOR_VETO", "تم حظر الأمر: خرق سياسات أمان النظام أو استهلاك الموارد."))
            }
        }
    }

    private suspend fun decomposeAndDelegate(prompt: String) {
        // تفكيك الأمر وتوجيهه إلى ناقل الرسائل ليتلقاه الوكيل المختص
        if (prompt.contains("تعديل", ignoreCase = true) || prompt.contains("اكتب كود", ignoreCase = true)) {
            messageBus.publish(SystemEvent("CODER_TASK_ASSIGNED", prompt))
        } else {
            messageBus.publish(SystemEvent("SYS_ADMIN_TASK_ASSIGNED", prompt))
        }
    }

    private fun listenToSystemEvents() {
        scope.launch {
            messageBus.events.collect { event ->
                when (event.type) {
                    "CRITICAL_CRASH" -> handleSystemCrash(event.payload)
                    "TASK_COMPLETED" -> println("Kernel: تم تنفيذ المهمة بنجاح: ${event.payload}")
                }
            }
        }
    }

    private fun handleSystemCrash(errorLog: String) {
        println("Kernel Panic Triggered. Initiating Self-Healing Engine for: $errorLog")
        scope.launch {
            messageBus.publish(SystemEvent("SELF_HEALING_ACTIVATE", errorLog))
        }
    }
}

object AIGovernor {
    fun validatePolicy(prompt: String): Boolean {
        val dangerousKeywords = listOf("rm -rf", "drop table", "disable security")
        return dangerousKeywords.none { prompt.contains(it, ignoreCase = true) }
    }
}
