package com.ai.os.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ai.os.bus.MessageBus
import com.ai.os.bus.SystemEvent
import com.ai.os.agents.AgentRegistry
import com.ai.os.core.KernelAICore
import com.ai.os.loader.DynamicDexLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class WatchdogService : Service() {

    private val messageBus = MessageBus()
    private lateinit var agentRegistry: AgentRegistry
    private lateinit var kernelCore: KernelAICore
    private lateinit var dexLoader: DynamicDexLoader
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        startNotificationChannel()

        agentRegistry = AgentRegistry(messageBus)
        kernelCore = KernelAICore(messageBus, agentRegistry)
        dexLoader = DynamicDexLoader(this)

        agentRegistry.initializeAgents()
        monitorRuntimeStability()
    }

    private fun monitorRuntimeStability() {
        scope.launch {
            messageBus.events.collect { event ->
                if (event.type == "PATCH_VERIFIED_SUCCESS") {
                    try {
                        // محاولة حقن وتفعيل التحديث الجديد في البيئة الحية
                        dexLoader.injectDynamicFeature(event.payload, "com.ai.os.dynamic.NewFeature", "execute")
                    } catch (t: Throwable) {
                        // في حال حدوث إنهيار (Crash Loop Protection) يتم التراجع فوراً لضمان الاستقرار
                        messageBus.publish(SystemEvent("CRITICAL_CRASH", "فشل التحديث الساخن: ${t.localizedMessage}"))
                        executeRollback()
                    }
                }
            }
        }
    }

    private fun executeRollback() {
        println("Watchdog: تم رصد عدم استقرار! إلغاء الحقن الأخير واستعادة الحالة المستقرة (Rollback)...")
        val brokenPatch = File(filesDir, "dynamic_patch.dex")
        if (brokenPatch.exists()) {
            brokenPatch.delete() // حذف الرقعة البرمجية المسببة للعطل فوراً لحماية الهاتف
        }
    }

    private fun startNotificationChannel() {
        val channelId = "agentic_os_watchdog"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Agentic OS Watchdog", NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Autonomous Agentic OS")
            .setContentText("صمام الأمان والـ Watchdog يراقب استقرار النواة والوكلاء...")
            .build()

        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
}
