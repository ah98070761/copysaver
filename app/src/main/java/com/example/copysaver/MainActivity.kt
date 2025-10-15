// File: app/src/main/java/com/example/copysaver/MainActivity.kt

package com.example.copysaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.copysaver.ui.theme.CopySaverTheme // استيراد التنسيق الصحيح

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // تشغيل خدمة الخلفية فوراً
        // يتم استخدام startService في حالتنا، ويمكن استخدام startForegroundService في الإصدارات الأحدث
        startService(Intent(this, ClipboardMonitorService::class.java))
        
        setContent {
            CopySaverTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("CopySaver Is Active")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CopySaverTheme {
        Greeting("Android")
    }
}