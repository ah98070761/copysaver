package com.example.copysaver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipData
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

    // هذا السطر يمثل مدير قاعدة البيانات (Room Database) التي لم ننشئها بعد
    // سنفترض وجودها مؤقتاً لتمرير خطأ التجميع
    // lateinit var database: ClipEntryDatabase 

    // مستمع الحافظة
    private val clipboardListener = object : ClipboardManager.OnPrimaryClipChangedListener {
        override fun onPrimaryClipChanged() {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                val copiedText = clip.getItemAt(0).coerceToText(this@ClipboardMonitorService).toString()
                
                if (copiedText.isNotBlank()) {
                    Log.d(TAG, "New text copied: $copiedText")
                    // سنقوم بحفظ النص في قاعدة البيانات هنا
                    saveClip(copiedText)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created.")
        
        // هنا يمكن تهيئة قاعدة البيانات Room
        // database = ClipEntryDatabase.getDatabase(applicationContext) 
        
        // إعداد قناة الإشعارات
        createNotificationChannel()
        
        // تشغيل الخدمة كخدمة في المقدمة
        startForeground(NOTIFICATION_ID, buildNotification("CopySaver يعمل في الخلفية", "يراقب الحافظة..."))

        // إضافة مستمع الحافظة
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.addPrimaryClipChangedListener(clipboardListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started.")
        // يجب أن تعود START_STICKY لإعادة تشغيل الخدمة في حال توقفها
        return START_STICKY 
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed.")
        // إزالة مستمع الحافظة وإلغاء مهمة Coroutines
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.removePrimaryClipChangedListener(clipboardListener)
        job.cancel()
    }

    // الدالة المسؤولة عن حفظ النص المنسوخ (يجب إضافة منطق Room هنا)
    private fun saveClip(text: String) {
        // تشغيل عملية الحفظ في Coroutine
        scope.launch {
            // هذا مجرد نموذج، سيتطلب إضافة فصول Room الفعلية (ClipEntry, ClipEntryDao)
            Log.d(TAG, "Attempting to save: $text")
            
            /* // المنطق الفعلي للحفظ (يتطلب فصول Room)
            val newEntry = ClipEntry(content = text, timestamp = System.currentTimeMillis())
            database.clipDao().insert(newEntry)
            */

            // تحديث الإشعار لإظهار أن العملية نجحت
            val notification = buildNotification("تم حفظ نسخة جديدة", text.take(50) + "...")
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        // يجب إنشاء قناة الإشعارات على Android O أو أحدث
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CopySaver Monitoring"
            val descriptionText = "قناة إشعار دائمة لمراقبة الحافظة."
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // تسجيل القناة مع النظام
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
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // سيحدث خطأ ما لم يتم إنشاء ملف drawable
            .setContentIntent(pendingIntent)
            .setOngoing(true) // يجعل الإشعار دائمًا
            .build()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}