package com.example.copysaver

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.copysaver.ui.theme.CopySaverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // بدء خدمة مراقبة الحافظة في الخلفية
        startClipboardService()

        setContent {
            CopySaverTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = { Text("CopySaver - حافظ النسخ") })
                }) { innerPadding ->
                    // هنا يمكن بناء واجهة المستخدم الرئيسية لعرض النسخ المحفوظة
                    Greeting(name = "مرحبًا بعودتك!", modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    private fun startClipboardService() {
        val serviceIntent = Intent(this, ClipboardMonitorService::class.java)
        // بدء الخدمة كخدمة في المقدمة (Foreground Service)
        ContextCompat.startForegroundService(this, serviceIntent)
        Toast.makeText(this, "بدأ مراقبة الحافظة في الخلفية", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // يمكنك هنا بناء واجهة المستخدم الفعلية لعرض السجلات
    Text(
        text = "مرحباً $name، التطبيق يعمل في الخلفية لمراقبة ما تنسخه.",
        modifier = modifier
    )
}