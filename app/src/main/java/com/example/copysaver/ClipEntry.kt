package com.example.copysaver

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * يمثل إدخالاً واحدًا محفوظًا من الحافظة.
 */
@Entity(tableName = "clips")
data class ClipEntry(
    // معرّف فريد لكل إدخال
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    
    // المحتوى النصي المنسوخ
    @ColumnInfo(name = "content") val content: String,
    
    // الطابع الزمني لعملية النسخ
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)