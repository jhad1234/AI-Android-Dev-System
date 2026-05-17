package com.ai.os

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.ai.os.services.WatchdogService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    companion object {
        val uiLogs = MutableStateFlow("منصة الحوسبة الذاتية قيد التشغيل والتحضير...\n")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // تشغيل الخدمة الأمامية المستقلة وصمام الأمان فوراً عند تشغيل التطبيق
        val serviceIntent = Intent(this, WatchdogService::class.java)
        startForegroundService(serviceIntent)

        setContent {
            MaterialTheme {
                MainConsoleScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainConsoleScreen() {
    val logs by MainActivity.uiLogs.collectAsState()
    var commandInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("AI-OS Executive Console") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // شاشة عرض الحالة وسجلات النواة والوكلاء اللحظية
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth()
                    .background(Color(0xFF0A0A0A))
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = logs,
                    color = Color(0xFF00FF66),
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // حقل إدخال الأوامر المباشر للنظام
            OutlinedTextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                label = { Text("أدخل أمر التطوير أو الأتمتة المباشر...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (commandInput.isNotBlank()) {
                        scope.launch {
                            MainActivity.uiLogs.value += "User Console: $commandInput\n"
                            // هنا يتم تمرير الأمر لنواة الكيرنل المركزية
                            MainActivity.uiLogs.value += "Kernel: جاري تحليل المعطيات والتحقق عبر الـ Governor...\n"
                            commandInput = ""
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
            ) {
                Text("إرسال الأمر لنواة النظام")
            }
        }
    }
}
