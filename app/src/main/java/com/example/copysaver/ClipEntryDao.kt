package com.example.copysaver

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * واجهة الوصول للبيانات لجدول [ClipEntry].
 */
@Dao
interface ClipEntryDao {
    
    // إدراج إدخال جديد. في حالة التعارض، تجاهله.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: ClipEntry)
    
    // الحصول على جميع الإدخالات مرتبة حسب الطابع الزمني تنازليًا (الأحدث أولاً)
    @Query("SELECT * FROM clips ORDER BY timestamp DESC")
    fun getAllClips(): Flow<List<ClipEntry>>
    
    // حذف أقدم الإدخالات للحفاظ على حجم قاعدة البيانات
    @Query("DELETE FROM clips WHERE id NOT IN (SELECT id FROM clips ORDER BY timestamp DESC LIMIT 500)")
    suspend fun clearOldClips()
}