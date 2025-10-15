package com.example.copysaver

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * فئة قاعدة بيانات Room الرئيسية للتطبيق.
 */
@Database(entities = [ClipEntry::class], version = 1, exportSchema = false)
abstract class ClipEntryDatabase : RoomDatabase() {

    abstract fun clipDao(): ClipEntryDao

    companion object {
        @Volatile
        private var INSTANCE: ClipEntryDatabase? = null

        fun getDatabase(context: Context): ClipEntryDatabase {
            // إذا كان INSTANCE غير موجود (فارغ)
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClipEntryDatabase::class.java,
                    "clip_db"
                )
                // 🛑 التعديل هنا: إضافة fallbackToDestructiveMigration لضمان عدم توقف التطبيق بسبب تغييرات في المخطط
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}