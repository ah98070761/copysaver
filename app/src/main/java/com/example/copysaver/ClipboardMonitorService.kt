package com.example.copysaver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * خدمة Android تعمل في المقدمة لمراقبة الحافظة وحفظ المحتوى المنسوخ.
 */
class ClipboardMonitorService : Service() {

    private val TAG = "ClipMonitorService"
    private val CHANNEL_ID = "CopySaver_Channel"
    private val NOTIFICATION_ID = 101

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var database: ClipEntryDatabase

    // مستمع الحافظة
    private val clipboardListener = object : ClipboardManager.OnPrimaryClipChangedListener {
        override fun onPrimaryClipChanged() {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                // استخدام coerceToText للتأكد من التعامل مع أنواع النسخ المختلفة
                val copiedText = clip.getItemAt(0).coerceToText(this@ClipboardMonitorService).toString()
                
                if (copiedText.isNotBlank()) {
                    Log.d(TAG, "New text copied: $copiedText")
                    saveClip(copiedText)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created.")
        
        // تهيئة قاعدة البيانات Room
        database = ClipEntryDatabase.getDatabase(applicationContext) 
        
        createNotificationChannel()
        
        // تشغيل الخدمة كخدمة في المقدمة
        startForeground(NOTIFICATION_ID, buildNotification("CopySaver يعمل في الخلفية", "يراقب الحافظة..."))

        // إضافة مستمع الحافظة
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.addPrimaryClipChangedListener(clipboardListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started.")
        return START_STICKY 
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed.")
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.removePrimaryClipChangedListener(clipboardListener)
        job.cancel()
    }

    // الدالة المسؤولة عن حفظ النص المنسوخ
    private fun saveClip(text: String) {
        scope.launch {
            val newEntry = ClipEntry(content = text, timestamp = System.currentTimeMillis())
            database.clipDao().insert(newEntry)
            
            // للحفاظ على حجم قاعدة البيانات (حذف أقدم 500)
            database.clipDao().clearOldClips() 

            // تحديث الإشعار
            val notification = buildNotification("تم حفظ نسخة جديدة", text.take(50) + "...")
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CopySaver Monitoring"
            val descriptionText = "قناة إشعار دائمة لمراقبة الحافظة."
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun buildNotification(title: String, content: String): NotificationCompat.Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT // استخدام FLAG_UPDATE_CURRENT للتحديث
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            // 🛑 تم تغيير هذا السطر ليشير إلى المورد الجديد
            .setSmallIcon(R.drawable.ic_notification_icon) 
            .setContentIntent(pendingIntent)
            .setOngoing(true) 
            .setStyle(NotificationCompat.BigTextStyle().bigText(content)) // لعرض النص الكامل
            .build()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}